package com.example.artemis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlertSettingsDatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "AlertSettingsDatabasehandler";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "alert_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "isChecked";

    public AlertSettingsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " INTEGER);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void clearTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

