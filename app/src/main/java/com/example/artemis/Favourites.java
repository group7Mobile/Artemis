package com.example.artemis;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Favourites extends AppCompatActivity {
    private FavDatabaseHelper dbHelper;
    private EditText urlInput;
    private EditText titleInput;
    private ArrayList<String> arrayList;
    private RecyclerAdapter recyclerAdapter;
    String pageId =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        dbHelper = new FavDatabaseHelper(this);

        urlInput = findViewById(R.id.editText3);
        titleInput = findViewById(R.id.editTextTitle);

        RecyclerView recyclerView = findViewById(R.id.recyclerView11);

        arrayList = new ArrayList<>();
        arrayList = dbHelper.retrieveTitlesFromDatabase();

        recyclerAdapter = new RecyclerAdapter(this, arrayList);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnAdd = findViewById(R.id.button21);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = titleInput.getText().toString();
                String newItem2 = urlInput.getText().toString();
                addFavourite(newItem, newItem2);
            }
        });
        Bundle getIntent = getIntent().getExtras();
        if (getIntent != null && getIntent.getInt(Intent.EXTRA_TEXT) != 1 && getIntent.getInt(Intent.EXTRA_TEXT) != 2) {
            String title = getIntent.getString(Intent.EXTRA_TITLE);
            String link = getIntent.getString(Intent.EXTRA_PROCESS_TEXT);
            addFavourite(title, link);
            finish();
        }
    }

/*    @Override
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, Settings.class);
        context.startActivity(intent);
    }*/

    public void addFavourite(String ttl, String link) {
        //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
        if (ttl.equals("")) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Please enter a title", duration);
            toast.show();
        } else if (link.equals("")) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Please enter a URL", duration);
            toast.show();
        } else {
            arrayList.add(ttl);
            addToDatabase(ttl, link);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    /*
     * 	Adds a favourite to the list. To save to sharedPreferences, must convert arraylist to gson:
     */
    public void addToDatabase(String title, String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Url", url);
        db.insert("favs_table", null, values);
        db.close();
    }

    public void removeFavourite(String favourite) {
        if(arrayList.size()==0){
            return;
        } else if (!arrayList.contains(favourite)) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Not in favourites. Please enter a valid title", duration);
            toast.show();
        } else {
            arrayList.remove(favourite);
            recyclerAdapter.notifyDataSetChanged();
            removeFromDatabase(favourite);
        }
    }

    public void removeFromDatabase(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favs_table", "Title = ?", new String[] {id});
        db.close();
    }

    public String getUrl(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectString = "SELECT * FROM favs_table where Title = " + title;
        Cursor cursor = db.rawQuery(selectString, null);
        return cursor.getString(cursor.getColumnIndex("Url"));
    }

    public void mainPage(View v) {
        /**
         Bundle getterFav = getIntent().getExtras();
         if (getterFav != null) {
         int res = getterFav.getInt(Intent.EXTRA_TEXT);
         if (res == 1) {
         finish();
         } else {
         if (res == 2) {
         Intent goMainPage1 = new Intent(this, Settings.class);
         startActivity(goMainPage1);
         }
         }
         }
         **/
        finish();
    }
}

class PageAdapter extends CursorAdapter {


    public PageAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}