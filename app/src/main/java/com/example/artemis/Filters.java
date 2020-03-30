package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class Filters extends AppCompatActivity {
    private RecyclerAdapter recyclerAdapter;
    private EditText txtInput;
    private SharedPreferences fav;
    private FilterWordsDBhelper filterWords;
    public ArrayList<String> arrayList;
    String wordId= null;
    public boolean status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        filterWords = new FilterWordsDBhelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        arrayList = new ArrayList<>();
        arrayList = getFilterWords();
        recyclerAdapter = new RecyclerAdapter(this, arrayList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //constructor of adapter to store input item separately in list_item and put them in list_view
        Button btnAdd = (Button) findViewById(R.id.button2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput = (EditText) findViewById(R.id.editText18);
                String newItem = txtInput.getText().toString();

                //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
                addTofilterDB(newItem);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

    }

    public void mainPage(View v) {
        finish();
    }
    public ArrayList<String> getArrayList(){
        return arrayList;
    }
    public boolean getStatus(){
        return status;
    }
    public void addTofilterDB(String word) {
        SQLiteDatabase db = filterWords.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("words", word);
        db.insert("filter_table", null, values);
        db.close();
        arrayList.add(word);
    }


    public ArrayList<String> getFilterWords() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = filterWords.getReadableDatabase();
        String selectString = "SELECT * FROM filter_table";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex("words")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arrayList;
    }

}
