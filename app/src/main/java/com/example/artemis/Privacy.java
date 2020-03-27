package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;

public class Privacy extends AppCompatActivity {

    private HistoryDBHelper dbHelper;
    private ArrayList<String> arrayList;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        dbHelper = new HistoryDBHelper(this);

        listView = findViewById(R.id.listviewH);
        arrayList = new ArrayList<>();

        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.txtitem, arrayList);
        listView.setAdapter(adapter);
        Button btnShow = (Button) findViewById(R.id.button18);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBrowserHist();
                adapter.notifyDataSetChanged();
            }
        });
        Button btnClear = (Button) findViewById(R.id.button14);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                adapter.notifyDataSetChanged();
            }
        });


        //constructor of adapter to store input item separately in list_item and put them in list_view

    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(this, Settings.class);
        startActivity(goMainPage);
    }
    public void getBrowserHist()  {

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String selectString = "SELECT * FROM history_table";
            Cursor cursor = db.rawQuery(selectString, null);
            if (cursor.moveToFirst()) {
                do {
                    arrayList.add(cursor.getString(cursor.getColumnIndex("URL")));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

}
