package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;

public class Favourites extends AppCompatActivity {
    private ArrayList<String> arrayList;

    private ArrayAdapter<String> adapter;
    private EditText urlInput;
    private EditText titleInput;
    private FavDatabaseHelper dbHelper;
    private ListView listView;
    private ImageButton deleteButton;
    //private SharedPreferences fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        dbHelper = new FavDatabaseHelper(this);
        urlInput = (EditText) findViewById(R.id.editText3);
        titleInput = (EditText) findViewById(R.id.editTextTitle);
        listView = findViewById(R.id.listView2);
        arrayList = new ArrayList<>();
        retrieveFromDatabase();

        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);;
        Button btnAdd = (Button) findViewById(R.id.button21);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavourite();
            }
        });
        Button btnPop = (Button) findViewById(R.id.button24);
        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavourite();
            }
        });
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_favourites,R.id.textView,arrayList);
    }

    public void mainPage(View v) {
        Intent goMainPage = new Intent(this, Settings.class);
        startActivity(goMainPage);
    }

    public void addFavourite() {
        String newItem = titleInput.getText().toString();
        String newItem2 = urlInput.getText().toString();
        //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
        if (titleInput.getText().toString().equals("")) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Please enter a title", duration);
            toast.show();
        } else if (urlInput.getText().toString().equals("")) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Please enter a URL", duration);
            toast.show();
        } else {
            arrayList.add(newItem);
            addToDatabase(newItem, newItem2);
            adapter.notifyDataSetChanged();
        }
    }

    public void removeFavourite() {
        if(arrayList.size()==0){
            return;
        } else if (!arrayList.contains(titleInput.getText().toString())) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Not in favourites. Please enter a valid title", duration);
            toast.show();
        } else {
            arrayList.remove(titleInput.getText().toString());
            adapter.notifyDataSetChanged();
            removeFromFavourites(titleInput.getText().toString());
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

    public void retrieveFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectString = "SELECT * FROM favs_table";
        Cursor cursor = db.rawQuery(selectString, null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getString(cursor.getColumnIndex("Title")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    /*
     * Removes a favourite from the list.
     */
    public void removeFromFavourites(String title) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favs_table", "Title = ?", new String[] {title});
        db.close();
    }
}