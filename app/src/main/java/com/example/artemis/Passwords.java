package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Passwords extends AppCompatActivity {

    EditText editText, editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button11);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1 = editText.getText().toString();
                String text2 = editText2.getText().toString();

                if (text1.equals("") || text2.equals("")) {
                    // No password entered
                    Toast.makeText(Passwords.this, "No Password Entered", Toast.LENGTH_SHORT).show();
                } else {
                    if (text1.equals(text2)){
                        // Saves the password
                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password", text1);
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), Settings.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Passwords don't match
                        Toast.makeText(Passwords.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(this, MainActivity.class);
        startActivity(goMainPage);
    }
}
