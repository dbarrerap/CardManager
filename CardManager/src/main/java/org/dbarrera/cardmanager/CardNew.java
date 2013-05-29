package org.dbarrera.cardmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dav3 on 5/23/13.
 */
public class CardNew extends Activity implements View.OnClickListener {

    Spinner card_type_spin, card_bank_spin;
    DatePicker valid_thru;
    EditText txt_card_name, txt_card_number, txt_card_ccv;
    ImageView picPreview;
    ToggleButton toggle_intl, toggle_debit;
    Button save;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int GALLERY_PIC_REQUEST = 1338;
    private ArrayList<cardDetailsPOJO> dataToSave;
    String picPath = "null";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_new);

        /*
		 * Bug: Al hacer cambio de orientacion, el layout se destruye y se vuelve a crear,
		 *  por ende creando todos los objetos desde cero.
		 * Workaround: Fijar la orientacion de este layout via setRequestedOrientation()
		 * Observacion: Recomiendan getLastNonConfigurationInstance(), pero el SDK dice que es deprecated.
		 * Documentacion: This method was deprecated in API level 13.
		 *  Use the new Fragment API setRetainInstance(boolean) instead; this is also available
		 *  on older platforms through the Android compatibility package.
		 */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupWidgets();
    }

    private void setupWidgets() {

        txt_card_name = (EditText)findViewById(R.id.txt_card_name);
        txt_card_number = (EditText)findViewById(R.id.txt_card_number);
        txt_card_ccv = (EditText)findViewById(R.id.txt_card_ccv);
        toggle_intl = (ToggleButton)findViewById(R.id.tgl_intl);
        toggle_debit = (ToggleButton)findViewById(R.id.tgl_debit);
        valid_thru = (DatePicker)findViewById(R.id.datePicker);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
            ocultarElementoDatePicker(valid_thru, "mDaySpinner");
        } else {
            ocultarElementoDatePicker(valid_thru, "mDayPicker");
        }
        save = (Button)findViewById(R.id.button_save);
        save.setOnClickListener(this);

        dataToSave = new ArrayList<cardDetailsPOJO>();

        /*
         * Bug: La IDE de Android Studio aun no permite referenciar android:entries con @array/<name>
         * Workaround: Llenar los Spinner en Tiempo de Ejecucion mediante un ArrayAdapter
         */
        card_type_spin = (Spinner)findViewById(R.id.spinner);
        card_bank_spin = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> card_type_adapter = ArrayAdapter.createFromResource(this, R.array.strArrCardType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> card_bank_adapter = ArrayAdapter.createFromResource(this, R.array.strArrCardBank, android.R.layout.simple_spinner_item);
        card_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        card_bank_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        card_type_spin.setAdapter(card_type_adapter);
        card_bank_spin.setAdapter(card_bank_adapter);
    }

    /*
	 * Metodo: ocultarElementoDatePicker()
	 * Parametros: datePicker, element (mDayPicker, mMonthPicker, mYearPicker || mDaySpinner, mMonthSpinner, mYearSpinner)
	 * Objetivo: Ocultar un elemento del view DatePicker
	 * Observacion: elemento depende de la version de API (<=2.3.3 usa Picker, 3.0+ usa Spinner)
	 */
    private void ocultarElementoDatePicker(DatePicker datePicker, String element) {
        try {
            Field f = DatePicker.class.getDeclaredField(element);
            f.setAccessible(true);
            View fInst = (View)f.get(datePicker);
            fInst.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_new, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getAlphabeticShortcut() == 'f'){
            //Inicia la camara via Intent
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        } else if (item.getAlphabeticShortcut() == 'g') {
            //Inicia la galeria de fotos via Intent
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryIntent, GALLERY_PIC_REQUEST);
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == CAMERA_PIC_REQUEST){
                //Extraer datos arrojados por el Intent y enviar al ImageView
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                picPreview = (ImageView)findViewById(R.id.imageView);
                picPreview.setImageBitmap(thumbnail);

                //Almacenamiento de imagen
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                File dir = new File(Environment.getExternalStorageDirectory(),"cardManager");
                if (!dir.exists()){
                    dir.mkdir();
                }
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                String fileName = "cm_" + timestamp + ".jpg";
                File newFile = new File(dir,fileName);
                try {
                    newFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    fos.write(baos.toByteArray());
                    fos.close();
                    baos.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(), newFile.getAbsolutePath(), newFile.getName(), "Foto");
                    picPath = dir + fileName;
                } catch (Exception e) {
                    Log.e(CardNew.class.getName(), e.getMessage().toString());
                    e.printStackTrace();
                }

                //Notificar cambio en galeria
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED), Uri.parse("file://" + Environment.getExternalStorageDirectory()).toString());

            }
            if (requestCode == GALLERY_PIC_REQUEST){
                //Extraer datos arrojados por el Intent y parsearla para enviarla al ImageView
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                cursor.close();
                picPath = picturePath;

                picPreview = (ImageView)findViewById(R.id.imageView);
                picPreview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.button_save){
            if(allFieldFilled()){
                //Almacenar los datos en un objeto alterno para ser pasados al metodo insertData()
                cardDetailsPOJO cdpojo = new cardDetailsPOJO();
                cdpojo.setName(txt_card_name.getText().toString());
                cdpojo.setCardNumber(txt_card_number.getText().toString());
                cdpojo.setCardCCV(txt_card_ccv.getText().toString());
                cdpojo.setCardType(card_type_spin.getSelectedItem().toString());
                cdpojo.setCardTypePos(card_type_spin.getSelectedItemPosition());
                if (toggle_intl.isChecked()){
                    cdpojo.setIntl("1");
                } else {
                    cdpojo.setIntl("0");
                }
                if (toggle_debit.isChecked()){
                    cdpojo.setDebit("1");
                } else {
                    cdpojo.setDebit("0");
                }
                cdpojo.setValidThruMonth(String.valueOf(valid_thru.getMonth() + 1));
                cdpojo.setValidThruYear(String.valueOf(valid_thru.getYear()));
                cdpojo.setBank(card_bank_spin.getSelectedItem().toString());
                cdpojo.setBankPos(card_bank_spin.getSelectedItemPosition());
                cdpojo.setImage(picPath);
                dataToSave.add(cdpojo);
                //Toast.makeText(getApplicationContext(), "" + toggle_debit.getText().toString(), Toast.LENGTH_SHORT).show();
                insertData(cdpojo);
            } else {
                Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    Metodo: insertData()
    Parametros: cdpojo,
    Objetivo: insertar datos almacenados temporalmente en un objeto cardDetailsPOJO.java a la base de datos
     cardManager.db segun las caracteristicas en Data.java
     */
    private void insertData(cardDetailsPOJO cdpojo) {
        long affectedRow = 0;
        // .open() BEGIN
        Data db = new Data(this);
        SQLiteDatabase dbData = db.getWritableDatabase();
        // .open() END
        ContentValues cv = new ContentValues();
        cv.put(Data.NAME_COL,cdpojo.getName());
        cv.put(Data.CARD_NUMBER_COL, cdpojo.getCardNumber());
        cv.put(Data.CARD_CCV_COL, cdpojo.getCardCCV());
        cv.put(Data.CARD_TYPE_COL, cdpojo.getCardType());
        cv.put(Data.CARD_TYPE_POS_COL, cdpojo.getCardTypePos());
        cv.put(Data.CARD_INTL_COL, cdpojo.getIntl());
        cv.put(Data.CARD_DEBIT_COL, cdpojo.getDebit());
        cv.put(Data.CARD_VALIDTHRU_MONTH_COL, cdpojo.getValidThruMonth());
        cv.put(Data.CARD_VALIDTHRU_YEAR_COL, cdpojo.getValidThruYear());
        cv.put(Data.CARD_BANK_COL, cdpojo.getBank());
        cv.put(Data.CARD_BANK_POS_COL, cdpojo.getBankPos());
        cv.put(Data.CARD_IMAGE_COL,cdpojo.getImage());
        try {
            affectedRow = dbData.insert(Data.TABLE_NAME, null, cv);
        } catch (Exception e) {
            //De haber un problema al ingresar, mostrar al usuario.
            Log.e(CardNew.class.getName(),"Error ingresando datos en la base: " + e.getMessage().toString());
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Hubo un error al ingresar datos.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage(e.getMessage().toString())
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog d = b.create();
            d.show();
        } finally {
            db.close();
            //Toast.makeText(getApplicationContext(), "Registro #" + affectedRow + " guardado con exito.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage("Registro #" + affectedRow + " guardado con exito.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog d = b.create();
            d.show();
        }
    }

    private boolean allFieldFilled() {
        if (txt_card_name.getText().toString().isEmpty() || txt_card_number.getText().toString().isEmpty() || txt_card_ccv.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}