package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Shortcuts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcuts);
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(Shortcuts.this, Settings.class);
        startActivity(goMainPage);
    }
}
