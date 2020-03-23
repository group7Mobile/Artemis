package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class BlackList extends AppCompatActivity {
    private EditText blUrl;

    private ArrayList<EditText> blackList;
    BlackListDatabaseHelper blackListDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        blackListDatabaseHelper = new BlackListDatabaseHelper(this);
        blUrl = findViewById(R.id.editText31);
        blackList = new ArrayList<>();
        blackList.add(blUrl);
        retrieveFromBLDB();
    }

    public void mainPage(View v) {
        add(v);
        Intent goMainPage = new Intent(BlackList.this, Settings.class);
        startActivity(goMainPage);
    }

    public void addToBLDB() {
        SQLiteDatabase db = blackListDatabaseHelper.getWritableDatabase();
        blackListDatabaseHelper.clearTable(db);
        ContentValues values = new ContentValues();

        for (int i = 0; i < blackList.size(); i++) {
            String current = blackList.get(i).getText().toString();
            if (!current.equals("")) {
                values.put("URL", current);
                db.insert("bl_table", null, values);
            }
        }
        db.close();
    }

    public void retrieveFromBLDB() {
        SQLiteDatabase db = blackListDatabaseHelper.getReadableDatabase();
        String selectString = "SELECT * FROM bl_table";
        Cursor cursor = db.rawQuery(selectString, null);
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                blackList.get(i).setText(cursor.getString(cursor.getColumnIndex("URL")));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void add(View v) {
        addToBLDB();
    }
}
