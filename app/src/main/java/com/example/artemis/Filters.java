package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;

public class Filters extends AppCompatActivity {
    private FilterAdapter filterAdapter;
    private EditText txtInput;
    private SharedPreferences fav;
    private FilterWordsDBhelper filterWords;
    private Switch aSwitch;
    private boolean isUpdated;
    public ArrayList<String> arrayList;
    String wordId= null;
    public boolean status;
    SwitchDatabaseHelper switchDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        filterWords = new FilterWordsDBhelper(this);
        isUpdated = false;
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        switchDatabaseHelper = new SwitchDatabaseHelper(this);
        aSwitch = findViewById(R.id.switch1);
        arrayList = new ArrayList<>();
        arrayList = getFilterWords();
        filterAdapter = new FilterAdapter(this, arrayList);
        recyclerView.setAdapter(filterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //constructor of adapter to store input item separately in list_item and put them in list_view
        Button btnAdd = (Button) findViewById(R.id.button2);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput = (EditText) findViewById(R.id.editText18);
                String newItem = txtInput.getText().toString();
                //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
                addTofilterDB(newItem.toLowerCase());
                filterAdapter.notifyDataSetChanged();
            }
        });
        getFromSwitchDB();
    }

    public void mainPage(View v) {
        if (isUpdated) {
            Intent goToMainPage = new Intent(Filters.this, MainActivity.class);
            goToMainPage.putExtra(Intent.EXTRA_TEXT, "set");
            goToMainPage.putExtra(Intent.EXTRA_TITLE, "set");
            startActivity(goToMainPage);
            pushSwitchDB();
        } else {
            finish();
        }
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

    public void pushSwitchDB() {
        String i = "";
        if (aSwitch.isChecked()) {
            i = "t";
        } else {
            i = "f";
        }
        SQLiteDatabase db = switchDatabaseHelper.getWritableDatabase();
        switchDatabaseHelper.clearTable(db);
        ContentValues values = new ContentValues();
        values.put("sw", i);
        db.insert("swt_table", null, values);
        db.close();
    }

    public void getFromSwitchDB() {
        String i = "";
        SQLiteDatabase db = switchDatabaseHelper.getReadableDatabase();
        String select = "SELECT * FROM swt_table";
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                i = cursor.getString(cursor.getColumnIndex("sw"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (i.equals("f")) {
            aSwitch.setChecked(false);
        } else {
            if (i.equals("t")) {
                aSwitch.setChecked(true);
            }
        }
    }

    public void ifUpdated(View v) {
        isUpdated = true;
        System.out.println("clicked");
    }

}
