package com.example.myapplication;


import static com.example.myapplication.DataBaseContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db"; // название бд
    private static final int VERSION = 1; // версия базы данных



    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ UserEntry.TABLE_NAME_1 +" ("+UserEntry.T1_ATR1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+UserEntry.T1_ATR2+" TEXT, "+UserEntry.T1_ATR3+" TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ DataEntry.TABLE_NAME_2 +" ("+DataEntry.T2_ATR1+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +DataEntry.T2_ATR2+" TEXT, "+DataEntry.T2_ATR3+" TEXT,"+DataEntry.T2_ATR4+" TEXT,"+DataEntry.T2_ATR5+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
       /* db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + DataEntry.TABLE_NAME_2);
                onCreate(db);*/
    }

}
