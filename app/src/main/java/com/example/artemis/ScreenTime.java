package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class ScreenTime extends AppCompatActivity {

    RadioButton rb1, rb2, rb3, rb4, rb5;
    boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_time);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.textView40);
        textViewDate.setText(currentDate);

        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rb3 = (RadioButton) findViewById(R.id.rb3);
        rb4 = (RadioButton) findViewById(R.id.rb4);
        rb5 = (RadioButton) findViewById(R.id.rb5);

        rb1.setChecked(false);
        rb2.setChecked(false);
        rb3.setChecked(false);
        rb4.setChecked(false);
        rb5.setChecked(false);

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 15 Minutes
                setScreenTimeout(900);

                if (success) {
                    rb1.setChecked(true);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    Toast.makeText(ScreenTime.this, "Screen off set to 15 minutes", Toast.LENGTH_SHORT).show();
                    success = false;
                } else {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                }
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 30 Minutes
                setScreenTimeout(1800);

                if (success) {
                    rb1.setChecked(false);
                    rb2.setChecked(true);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    Toast.makeText(ScreenTime.this, "Screen off set to 30 minutes", Toast.LENGTH_SHORT).show();
                    success = false;
                } else {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                }
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 Hour
                setScreenTimeout(3600);

                if (success) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(true);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                    Toast.makeText(ScreenTime.this, "Screen off set to 1 hour", Toast.LENGTH_SHORT).show();
                    success = false;
                } else {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                }
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2 hours
                setScreenTimeout(7200);

                if (success) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(true);
                    rb5.setChecked(false);
                    Toast.makeText(ScreenTime.this, "Screen off set to 2 hours", Toast.LENGTH_SHORT).show();
                    success = false;
                } else {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                }
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3 hours
                setScreenTimeout(10800);

                if (success) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(true);
                    Toast.makeText(ScreenTime.this, "Screen off set to 3 hours", Toast.LENGTH_SHORT).show();
                    success = false;
                } else {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);
                    rb5.setChecked(false);
                }
            }
        });
    }

    private void setScreenTimeout(int seconds){
        boolean value = false;
        Context context = getApplicationContext();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            value = android.provider.Settings.System.canWrite(context);
            if (value) {
                android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, seconds);
                success = true;
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else {
            android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, seconds);
            success = true;
        }
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(ScreenTime.this, Settings.class);
        startActivity(goMainPage);
    }
}
