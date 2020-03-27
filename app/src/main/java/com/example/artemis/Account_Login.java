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

    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    Button btnsign;
    EditText txt_email, txt_pass;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__login);
        openHelper = new AccountDatabaseHelper(this);
        db = openHelper.getWritableDatabase();
        btnsign = (Button)findViewById(R.id.button30);
        txt_email =  (EditText) findViewById(R.id.editText9);
        txt_pass = (EditText) findViewById(R.id.editText10);
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String pass = txt_pass.getText().toString();
                cursor = db.rawQuery("SELECT * FROM " + AccountDatabaseHelper.TABLE_NAME + " WHERE " + AccountDatabaseHelper.COL4 + " =? AND " + AccountDatabaseHelper.COL5 + " =? ", new String[] {email, pass});
                if(cursor!=null){
                    if(cursor.getCount()>0){
                        cursor.moveToNext();
                        Toast.makeText(getApplicationContext(), "Signed In", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void goToAccountPage(View v) {
        Intent goAccountPage = new Intent(Account_Login.this, AccountPage.class);
        startActivity(goAccountPage);
    }
}
