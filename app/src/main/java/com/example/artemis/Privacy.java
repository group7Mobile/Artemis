package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

public class Privacy extends AppCompatActivity {
    private RecyclerAdapter recyclerAdapter;
    private HistoryDBHelper dbHelper;
    private ArrayList<String> arrayList;
    String historyId= null;
    private SharedPreferences fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        dbHelper = new HistoryDBHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerView3);
        arrayList = new ArrayList<>();
        arrayList=getBrowserHist();
        //constructor of adapter to store input item separately in list_item and put them in list_view
        recyclerAdapter = new RecyclerAdapter(this, arrayList);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnShow = (Button) findViewById(R.id.button18);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList=getBrowserHist();
                recyclerAdapter.notifyDataSetChanged();
            }
        });
        Button btnClear = (Button) findViewById(R.id.button14);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                recyclerAdapter.notifyDataSetChanged();
                dbHelper.del();

            }
        });


        //constructor of adapter to store input item separately in list_item and put them in list_view

    }

    public void mainPage(View v) {
        finish();
    }
    public ArrayList<String> getBrowserHist()  {
        ArrayList<String> arrayList = new ArrayList<>();
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
            return arrayList;
        }

}
