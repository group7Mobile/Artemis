package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Account_Login extends AppCompatActivity {

    EditText e1, e2;
    Button b1;
    AccountDatabaseHelper db;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__login);
        db = new AccountDatabaseHelper(this);
        e1 = (EditText)findViewById(R.id.editText9);
        e2 = (EditText)findViewById(R.id.editText10);
        b1 = (Button) findViewById(R.id.button30);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                boolean checkemailpassword = db.emailpassword(email, password);
                if(checkemailpassword==true)
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Wrong email or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToAccountPage(View v) {
        Intent goAccountPage = new Intent(Account_Login.this, AccountPage.class);
        startActivity(goAccountPage);
    }
}
