package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPassword extends AppCompatActivity {

    EditText editText;
    Button button;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        // Load the password
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");
        editText = (EditText) findViewById(R.id.editText19);
        button = (Button) findViewById(R.id.button31);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();

                if (text.equals(password)) {
                    Intent intent = new Intent(getApplicationContext(), Settings.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EnterPassword.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void mainPage(View v) {
        finish();
    }

    public void goToFavPage(View v) {
        Intent goFavPage = new Intent(EnterPassword.this, Favourites.class);
        goFavPage.putExtra(Intent.EXTRA_TEXT, 1);
        startActivity(goFavPage);
    }
}
