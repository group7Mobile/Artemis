package com.example.artemis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class HPDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "HPDatabaseHelper";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "hp_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "Url";

    public HPDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL1 + " INTEGER PRIMARY KEY, " +
                COL2 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String lk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, lk);

        Log.d(DB_NAME, "addData: Adding " + lk + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public String getHomePage() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Url FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> strings= new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("Url"));
                strings.add(data);

                // do what ever you want here
            }while(cursor.moveToNext());
        }
        cursor.close();
        return strings.get(0);
    }

    public int rePopulate(String lk) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, lk);

        String query = "delete FROM " + TABLE_NAME;
        long del = db.rawQuery(query, null).getCount();
        long repl = db.insert(TABLE_NAME, null, contentValues);
        if (del > 0) {
            res++;
        }
        if (repl > 0) {
            res++;
        }
        return res;
    }

    public void del(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "Url = ?", new String[] {url} );
        db.close();
    }
}