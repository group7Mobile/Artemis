package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {
    private Button set;
    private EditText input;
    HPDatabaseHelper hpDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        set = findViewById(R.id.button3);
        input = findViewById(R.id.editText);
        hpDatabaseHelper = new HPDatabaseHelper(this);
        set.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                addToHPDB(input.getText().toString());
                Toast.makeText(getApplicationContext(), "Homepage is successfully set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
     * Returns the url of the homepage. Returns an empty string if no homepage set.
     */
    public String getHomepage() {
        String url = "";
        return url;
    }

    /*
     * Sets the url of the homepage.
     */
    public void setHomepage() {
        EditText url = input;
        hpDatabaseHelper.rePopulate(url.getText().toString());
    }

    public void mainPage(View v) {
        finish();
    }

    public void addToHPDB(String s) {
        SQLiteDatabase db = hpDatabaseHelper.getWritableDatabase();
        hpDatabaseHelper.clearTable(db);
        ContentValues values = new ContentValues();
        values.put("Url", s);
        db.insert("hp_table", null, values);
        db.close();
    }

    public String retreiveFromHPDB() {
        SQLiteDatabase db = hpDatabaseHelper.getReadableDatabase();
        String select = "SELECT * FROM hp_table;";
        String returnHP = "";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                returnHP = cursor.getString(cursor.getColumnIndex("Url"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnHP;
    }
}