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

    private EditText blUrl1;
    private EditText blUrl2;
    private EditText blUrl3;
    private EditText blUrl4;
    private EditText blUrl5;
    private EditText blUrl6;
    private EditText blUrl7;
    private EditText blUrl8;
    private EditText blUrl9;
    private EditText blUrl10;
    private ArrayList<EditText> blackList;
    BlackListDatabaseHelper blackListDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        blackListDatabaseHelper = new BlackListDatabaseHelper(this);
        blUrl1 = findViewById(R.id.editText2);
        blUrl2 = findViewById(R.id.editText3);
        blUrl3 = findViewById(R.id.editText4);
        blUrl4 = findViewById(R.id.editText5);
        blUrl5 = findViewById(R.id.editText6);
        blUrl6 = findViewById(R.id.editText7);
        blUrl7 = findViewById(R.id.editText8);
        blUrl8 = findViewById(R.id.editText9);
        blUrl9 = findViewById(R.id.editText10);
        blUrl10 = findViewById(R.id.editText11);
        blackList = new ArrayList<>();
        blackList.add(blUrl1);
        blackList.add(blUrl2);
        blackList.add(blUrl3);
        blackList.add(blUrl4);
        blackList.add(blUrl5);
        blackList.add(blUrl6);
        blackList.add(blUrl7);
        blackList.add(blUrl8);
        blackList.add(blUrl9);
        blackList.add(blUrl10);
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
