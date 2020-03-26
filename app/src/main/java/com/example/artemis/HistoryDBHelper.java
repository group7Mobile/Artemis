package com.example.artemis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistoryDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "HistoryDBHelper";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "history_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "URL";

    public HistoryDBHelper(Context context) {
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


    public boolean addData(String url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, url);
        Log.d(DB_NAME,"ADDING"+url+"to"+TABLE_NAME);
        long result=db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }
    }



    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getById(String ID) {
        String[] args={ID};
        SQLiteDatabase db = this.getWritableDatabase();
        return(db.rawQuery("SELECT ID,URL  FROM history_table WHERE ID=?",
                args));
    }

    public void del(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "Url = ?", new String[] {url} );
        db.close();
    }
}
