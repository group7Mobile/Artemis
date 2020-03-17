package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePageWidgets extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_widgets);
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(HomePageWidgets.this, Settings.class);
        startActivity(goMainPage);
    }
}
