package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BlackList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(BlackList.this, Settings.class);
        startActivity(goMainPage);
    }
}
