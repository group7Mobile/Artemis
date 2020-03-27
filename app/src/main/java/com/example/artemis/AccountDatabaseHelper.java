package com.example.artemis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "AccountDatabaseHelper";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Account_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "FirstName";
    public static final String COL3 = "LastName";
    public static final String COL4 = "Email";
    public static final String COL5 = "Password";

    public AccountDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
