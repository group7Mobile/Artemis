package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomePage extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button set;
    private Button get;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        pref = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
        editor = pref.edit();
        set = findViewById(R.id.button3);
        get = findViewById(R.id.button3);
        set.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                setHomepage();
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                getHomepage();
            }
        });
    }

    /*
     * Returns the url of the homepage. Returns an empty string if no homepage set.
     */
    public String getHomepage() {
        String url = "";
        pref.getString("Homepage", url);
        return url;
    }

    /*
     * Sets the url of the homepage.
     */
    public void setHomepage() {
        input = findViewById(R.id.editText);
        EditText url = input;
        set = findViewById(R.id.button3);
        editor.putString("Homepage", url.toString());
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(HomePage.this, Settings.class);
        startActivity(goMainPage);
    }
}