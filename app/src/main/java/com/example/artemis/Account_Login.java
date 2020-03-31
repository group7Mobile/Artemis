package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Account_Login extends AppCompatActivity {

    EditText e1, e2;
    Button b1;
    AccountDatabaseHelper db;
    AccountEmailHolderDatabaseHelper accountEmailHolderDatabaseHelper;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__login);
        db = new AccountDatabaseHelper(this);
        e1 = (EditText)findViewById(R.id.editText9);
        e2 = (EditText)findViewById(R.id.editText10);
        b1 = (Button) findViewById(R.id.button30);
        accountEmailHolderDatabaseHelper = new AccountEmailHolderDatabaseHelper(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                boolean checkemailpassword = db.emailpassword(email, password);
                if(checkemailpassword==true) {
                    pushEmail();
                    Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToAccountPage(View v) {
        finish();
    }

    public void pushEmail() {
        SQLiteDatabase db1 = accountEmailHolderDatabaseHelper.getWritableDatabase();
        accountEmailHolderDatabaseHelper.clearTable(db1);
        ContentValues values = new ContentValues();
        values.put("email", db.getEmail());
        db1.insert("email_table", null,  values);
        db.close();
    }
}
