package com.example.artemis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.print.PrinterId;

import java.util.ArrayList;

public class BlackListDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BlackListDatabaseHelper";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "bl_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Title";
    private static final String COL3 = "Url";

    public BlackListDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT);";
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

    public ArrayList<String> retrieveTitlesFromDatabase() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectString = "SELECT * FROM bl_table";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex("Title")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public ArrayList<String> retrieveLinksFromDatabase() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectString = "SELECT Url FROM bl_table";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex("Url")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arrayList;
    }

    public String retrieveLinkByTitle(String s) {
        String link = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectString = "SELECT * FROM bl_table WHERE Title = ?";
        Cursor cursor = db.rawQuery(selectString, new String[] {s});
        if (cursor.moveToFirst()) {
            link = cursor.getString(cursor.getColumnIndex("Url"));
        }
        cursor.close();
        db.close();
        return link;
    }
}
