package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;

public class AlertSettings extends AppCompatActivity {

    private Switch swt;
    AlertSettingsDatabaseHelper alertSettingsDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_settings);
        swt = findViewById(R.id.switch4);
        alertSettingsDatabaseHelper = new AlertSettingsDatabaseHelper(this);
        swt.setChecked(getFromAlertDB());
    }

    public void mainPage(View v) {
        ifChecked();
        finish();
    }

    public void ifChecked() {
        int i;
        if (swt.isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        SQLiteDatabase db = alertSettingsDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        alertSettingsDatabaseHelper.clearTable(db);
        values.put("isChecked", i);
        db.insert("alert_table", null, values);
        db.close();
    }

    public boolean getFromAlertDB() {
        SQLiteDatabase db = alertSettingsDatabaseHelper.getReadableDatabase();
        String select = "SELECT * FROM alert_table;";
        int i = 0;
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                i = cursor.getInt(cursor.getColumnIndex("isChecked"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }
}
