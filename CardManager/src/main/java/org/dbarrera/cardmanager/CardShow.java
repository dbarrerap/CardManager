package org.dbarrera.cardmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
 * Created by Dav3 on 5/25/13.
 */
public class CardShow extends Activity implements AdapterView.OnItemClickListener {

    //TextView tv_card_name, tv_card_number;
    Data db;
    ListView myLV;
    private SQLiteDatabase dbRead;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_show);
        //tv_card_name = (TextView)findViewById(R.id.txt_show_name);
        //tv_card_number = (TextView)findViewById(R.id.txt_show_number);

        //readDB();
        setupWidget();
        populateLV();

    }

    private void setupWidget() {
        myLV = (ListView)findViewById(R.id.listView);
        myLV.setOnItemClickListener(this);
    }

    private void populateLV() {
        db = new Data(this);
        dbRead = db.getReadableDatabase();
        try {
            Cursor c = dbRead.query(Data.TABLE_NAME, null, null, null, null, null, null);
            startManagingCursor(c);
            String[] fromField = new String[]{Data.NAME_COL, Data.CARD_NUMBER_COL};
            int[] toView = new int[]{R.id.list_item_card_name, R.id.list_item_card_number};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, c, fromField, toView);

            myLV.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Cursor c = dbRead.query(Data.TABLE_NAME,null,Data.ID_COL + "=" + id, null, null, null, null);
        if (c.moveToFirst()){
            String debit, intl;
            if (c.getString(c.getColumnIndex(Data.CARD_DEBIT_COL)).equals("1")) {
                debit = "Si";
            } else {
                debit = "No";
            }

            if (c.getString(c.getColumnIndex(Data.CARD_INTL_COL)).equals("1")) {
                intl = "Si";
            } else {
                intl = "No";
            }

            String message = "Banco: " + c.getString(c.getColumnIndex(Data.CARD_BANK_COL)) +"\n"
                    + "Numero: " + c.getString(c.getColumnIndex(Data.CARD_NUMBER_COL)) + "\n"
                    + "Tipo: " + c.getString(c.getColumnIndex(Data.CARD_TYPE_COL)) + "\n"
                    + "Expira: " + c.getString(c.getColumnIndex(Data.CARD_VALIDTHRU_MONTH_COL)) + "/"
                    + c.getString(c.getColumnIndex(Data.CARD_VALIDTHRU_YEAR_COL)) + "\n"
                    + "Debito: " + debit + "\n" + "Internacional: " + intl;
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog d = b.create();
            d.show();
        }
        c.close();
    }

    /*private void readDB() {
        db = new Data(this);
        dbRead = db.getReadableDatabase();

        String result_name = "";
        String result_number = "";
        String[] columns = new String[]{ Data.NAME_COL, Data.CARD_NUMBER_COL };
        try {
            Cursor c = dbRead.query(Data.TABLE_NAME,columns,null,null,null,null,null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                result_name += c.getString(c.getColumnIndex(Data.NAME_COL)) + "\n";
                result_number += c.getString(c.getColumnIndex(Data.CARD_NUMBER_COL)) + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            tv_card_name.setText(result_name);
            tv_card_number.setText(result_number);
        }
    }*/
}