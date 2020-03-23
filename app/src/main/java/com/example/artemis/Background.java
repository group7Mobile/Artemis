package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Background extends AppCompatActivity {
    private Button a;
    private Button b;
    private Button c;
    private Button d;
    private Button e;
    private Button f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        a = findViewById(R.id.button14);
        b = findViewById(R.id.button18);
        c = findViewById(R.id.button21);
        d = findViewById(R.id.button23);
        e = findViewById(R.id.button24);
        f = findViewById(R.id.button25);
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(Background.this, Settings.class);
        startActivity(goMainPage);
    }
}
