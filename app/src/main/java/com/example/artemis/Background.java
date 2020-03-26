package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    public void changeA(View view) {
        SharedPreferences sharedPref = getSharedPreferences("bg", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bg", R.color.colorBG1);
        editor.commit();
        showToast();
    }

    public void changeB(View view) {
        SharedPreferences sharedPref = getSharedPreferences("bg", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bg", R.color.colorBG2);
        editor.commit();
        showToast();
    }

    public void changeC(View view) {
        SharedPreferences sharedPref = getSharedPreferences("bg", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bg", R.color.colorBG3);
        editor.commit();
        showToast();
    }

    public void changeD(View view) {
        SharedPreferences sharedPref = getSharedPreferences("bg", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bg", R.color.colorBG4);
        editor.commit();
        showToast();
    }

    public void changeE(View view) {
        SharedPreferences sharedPref = getSharedPreferences("bg", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bg", R.color.colorBG5);
        editor.commit();
        showToast();
    }

    public void changeF(View view) {
        SharedPreferences sharedPref = getSharedPreferences("bg", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("bg", R.color.colorBG6);
        editor.commit();
        showToast();
    }

    public void showToast() {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, "Background colour changed", duration);
        toast.show();
    }
}
