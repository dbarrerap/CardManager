package org.dbarrera.cardmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Dav3 on 5/24/13.
 */
public class Data extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cardmanager.db";
    public static final String ID_COL = BaseColumns._ID;
    public static final String TABLE_NAME = "cards";
    public static final String NAME_COL = "name";
    public static final String CARD_NUMBER_COL = "card_number";
    public static final String CARD_CCV_COL = "card_ccv";
    public static final String CARD_TYPE_COL = "card_type";
    public static final String CARD_TYPE_POS_COL = "card_type_pos";
    public static final String CARD_INTL_COL = "card_intl";
    public static final String CARD_DEBIT_COL = "card_debit";
    public static final String CARD_BANK_COL = "card_bank";
    public static final String CARD_BANK_POS_COL = "card_bank_pos";
    public static final String CARD_VALIDTHRU_MONTH_COL = "card_validthru_month";
    public static final String CARD_VALIDTHRU_YEAR_COL = "card_validthru_year";
    public static final String CARD_IMAGE_COL = "card_image";
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_COL + " TEXT NOT NULL, " +
            CARD_NUMBER_COL + " TEXT NOT NULL, " +
            CARD_CCV_COL + " TEXT NOT NULL, " +
            CARD_TYPE_COL + " TEXT, " +
            CARD_TYPE_POS_COL + " INTEGER, " +
            CARD_INTL_COL + " TEXT, " +
            CARD_DEBIT_COL + " TEXT, " +
            CARD_BANK_COL + " TEXT, " +
            CARD_BANK_POS_COL + " INTEGER, " +
            CARD_VALIDTHRU_MONTH_COL + " TEXT, " +
            CARD_VALIDTHRU_YEAR_COL + " TEXT, " +
            CARD_IMAGE_COL + " TEXT);";

    public Data(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            Log.w(Data.class.getName(), "Actualizando base de datos de version " + oldVersion + " a la version " + newVersion + ".");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " RENAME TO " + TABLE_NAME + "v" + oldVersion);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
