package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountPage extends AppCompatActivity {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button reg, sign;
    EditText _typename, _tame, txt_email, txt_pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        openHelper = new AccountDatabaseHelper(this);
        reg = (Button) findViewById(R.id.button9);
        sign = (Button) findViewById(R.id.button35);
        _typename = (EditText) findViewById(R.id.editText5);
        _tame = (EditText) findViewById(R.id.editText6);
        txt_email = (EditText) findViewById(R.id.editText7);
        txt_pass = (EditText) findViewById(R.id.editText8);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = openHelper.getWritableDatabase();
                String fname = _typename.getText().toString();
                String lname = _tame.getText().toString();
                String email = txt_email.getText().toString();
                String pass = txt_pass.getText().toString();
                addData(fname, lname, email, pass);
                Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_LONG).show();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPage.this, Account_Login.class);
                startActivity(intent);
            }
        });
    }

    public void addData(String fname, String lname, String email, String pass) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AccountDatabaseHelper.COL2, fname);
        contentValues.put(AccountDatabaseHelper.COL3, lname);
        contentValues.put(AccountDatabaseHelper.COL4, email);
        contentValues.put(AccountDatabaseHelper.COL5, pass);
        long id = db.insert(AccountDatabaseHelper.TABLE_NAME, null, contentValues);

    }

    public void mainPage(View v) {
        finish();
    }
}
