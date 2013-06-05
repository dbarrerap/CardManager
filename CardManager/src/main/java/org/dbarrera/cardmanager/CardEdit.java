package org.dbarrera.cardmanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dav3 on 5/26/13.
 * Class: CardEdit
 * Manages: activity_card_edit.xml
 */
public class CardEdit extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Data db;
    Spinner item_select_spinner, u_card_type_spinner, u_card_bank_spinner;
    Button update_button, delete_button;
    DatePicker u_dp;
    EditText u_card_number, u_card_ccv;
    ImageView u_picPreview;
    ToggleButton u_toggle_intl, u_toggle_debit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);
        setupWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        item_select_spinner.setOnItemSelectedListener(this);
    }

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

    private void setupWidgets() {
        //Spinner de Tipo de Tarjeta
        u_card_type_spinner = (Spinner)findViewById(R.id.u_card_type_spinner);
        //Spinner del Banco
        u_card_bank_spinner = (Spinner)findViewById(R.id.u_card_bank_spinner);
        //Cargar StringArray en Spinners antes de ser referenciados
        ArrayAdapter<CharSequence> card_type_adapter = ArrayAdapter.createFromResource(this, R.array.strArrCardType, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> card_bank_adapter = ArrayAdapter.createFromResource(this, R.array.strArrCardBank, android.R.layout.simple_spinner_item);
        card_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        card_bank_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        u_card_type_spinner.setAdapter(card_type_adapter);
        u_card_bank_spinner.setAdapter(card_bank_adapter);
        //Spinner de nombres
        item_select_spinner = (Spinner)findViewById(R.id.u_spinner);
        loadDBintoSpinner();
        //Boton de eliminar
        delete_button = (Button)findViewById(R.id.u_delete);
        delete_button.setOnClickListener(this);
        //TextView de Numero y CCV
        u_card_number = (EditText)findViewById(R.id.u_card_number);
        u_card_ccv = (EditText)findViewById(R.id.u_card_ccv);
        //Los Toggle de Intl y Debito
        u_toggle_intl = (ToggleButton)findViewById(R.id.u_tgl_intl);
        u_toggle_debit = (ToggleButton)findViewById(R.id.u_tgl_debit);
        //DatePicker
        u_dp = (DatePicker)findViewById(R.id.u_datePicker);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
            ocultarElementoDatePicker(u_dp, "mDaySpinner");
        } else {
            ocultarElementoDatePicker(u_dp, "mDayPicker");
        }
        //Imagen
        u_picPreview = (ImageView)findViewById(R.id.u_imageView);
        //Boton de Actualizar
        update_button = (Button)findViewById(R.id.button_update);
        update_button.setOnClickListener(this);
    }

    private void loadDBintoSpinner() {
        List<String> lables = getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        item_select_spinner.setAdapter(dataAdapter);
    }

    private List<String> getAllLabels() {

        db = new Data(this);
        SQLiteDatabase dbData = db.getReadableDatabase();
        Cursor cursor = null;
        if (dbData != null) {
            cursor = dbData.query(Data.TABLE_NAME, null, null, null, null, null, null);
        }
        List<String> labels = new ArrayList<String>();

        assert cursor != null;
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(cursor.getColumnIndex(Data.NAME_COL)));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return labels;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.u_delete:
                //Delete from database where card_name = Data.NAME_COL (Should use Data.ID_COL instead of Data.NAME_COL, but that means changing the way to present data.
            case R.id.button_update:
                //Update with new data from View
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        db = new Data(this);
        SQLiteDatabase dbData = db.getReadableDatabase();

        Cursor c = null;
        if (dbData != null) {
            c = dbData.query(Data.TABLE_NAME,null,Data.NAME_COL + " = '" + adapterView.getSelectedItem().toString() + "'",null,null,null,null);
        }

        assert c != null;
        if (c.moveToFirst()){
            u_card_number.setText(c.getString(c.getColumnIndex(Data.CARD_NUMBER_COL)));
            u_card_ccv.setText(c.getString(c.getColumnIndex(Data.CARD_CCV_COL)));
            //u_card_type_spinner.setSelection(c.getInt(c.getColumnIndex(Data.CARD_TYPE_POS_COL)));
            if (c.getString(c.getColumnIndex(Data.CARD_INTL_COL)) == "1"){
                u_toggle_intl.setChecked(true);
            } else {
                u_toggle_intl.setChecked(false);
            }
            if (c.getString(c.getColumnIndex(Data.CARD_DEBIT_COL)) == "1"){
                u_toggle_debit.setChecked(true);
            } else {
                u_toggle_debit.setChecked(false);
            }
            //u_card_bank_spinner.setSelection(c.getInt(c.getColumnIndex(Data.CARD_BANK_POS_COL)));
            //No se puede setear fecha?
            /*if  (c.getString(c.getColumnIndex(Data.CARD_IMAGE_COL)) != "null"){
                u_picPreview.setImageURI(Uri.parse("file://" + c.getString(c.getColumnIndex(Data.CARD_IMAGE_COL))));
            }*/
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}