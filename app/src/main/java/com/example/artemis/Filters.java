package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Filters extends AppCompatActivity {
    public  ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText txtInput;
    private SharedPreferences fav;
    FilterWordsDBhelper filterWords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        ListView listView = (ListView) findViewById(R.id.listview3);
        String[] items = {"1","2","3"};
        arrayList = new ArrayList<String>(Arrays.asList(items));
        retrieveFromFilterDB();
        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        Button btnAdd = (Button) findViewById(R.id.button2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput = (EditText) findViewById(R.id.editText18);
                String newItem = txtInput.getText().toString();
                //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
                arrayList.add(0,newItem);
                addTofilterDB(newItem);
                adapter.notifyDataSetChanged();
            }
        });

        Button btnPop = (Button) findViewById(R.id.button32);
        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()==0){
                    return;
                }

                arrayList.remove(0);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void mainPage(View v) {
        finish();
    }
    public ArrayList<String> getArrayList(){
        return arrayList;
    }

    public void addTofilterDB(String word) {
        SQLiteDatabase db = filterWords.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < arrayList.size(); i++) {
            String current = arrayList.get(i);
            if (!current.equals("")) {
                values.put("words", current);
                db.insert("filter_table", null, values);
            }
        }
        db.close();
    }

    public void retrieveFromFilterDB() {
        SQLiteDatabase db = filterWords.getReadableDatabase();
        String selectString = "SELECT * FROM filter_table";
        Cursor cursor = db.rawQuery(selectString, null);
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex("words")));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


}
