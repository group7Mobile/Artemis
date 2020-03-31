package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.gson.Gson;
import java.util.ArrayList;

public class TimeTable extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button add;
    private EditText input1;
    private EditText input2;
    private EditText input3;
    private EditText input4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        pref = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
        editor = pref.edit();
        add = findViewById(R.id.button26);
        add.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                addTimetableEntry();
            }
        });
    }

    private void addTimetableEntry() {
        input1 = findViewById(R.id.editText12);
        input2 = findViewById(R.id.editText15);
        input3 = findViewById(R.id.editText16);
        input4 = findViewById(R.id.editText17);
        EditText name = input1;
        EditText date = input2;
        EditText startTime = input3;
        EditText duration = input4;
        TimetableEntry entry = new TimetableEntry(name.toString(), date.toString(), startTime.toString(), Integer.parseInt(duration.toString()));

        SharedPreferences time = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
        //Retrieve ArrayList from preferences:
        Gson gson = new Gson();
        //Check if timetable has already been created:
        ArrayList<TimetableEntry> list;
        if (time.contains("Timetable")) {
            String json = time.getString("Timetable", "");
            list = gson.fromJson(json, ArrayList.class);
        } else { //If not, create the list:
            list = new ArrayList<TimetableEntry>();
        }
        list.add(entry);
        //Add ArrayList back to preferences:
        Gson gsonAdd = new Gson();
        String jsonAdd = gson.toJson(list);
        SharedPreferences.Editor editor = time.edit();
        editor.putString("Timetable", jsonAdd);
        editor.commit();
    }

    public void mainPage(View v) {
        finish();
    }
}
