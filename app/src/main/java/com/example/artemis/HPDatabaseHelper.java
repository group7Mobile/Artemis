package com.example.artemis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public Cursor getHomePage() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Url FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public int rePopulate(String lk) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, 1);
        contentValues.put(COL2, lk);
        long del = db.delete(TABLE_NAME, COL1 + "=" + 1, null);
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