package com.example.artemis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDatabaseHelper extends SQLiteOpenHelper {
    public AccountDatabaseHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key, password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    //Inserting into database
    public boolean insert(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.insert("user", null, contentValues);
        if(ins==-1) return false;
        else return true;
    }

    //Check if email exists
    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("Select * From user Where email =?", new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true;
    }

    //Check the email and password
    public boolean emailpassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From user Where email =? And password =?", new String[]{email, password});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public String getEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM user;";
        String retS = "";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                retS =  cursor.getString(cursor.getColumnIndex("email"));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return retS;
    }
}

