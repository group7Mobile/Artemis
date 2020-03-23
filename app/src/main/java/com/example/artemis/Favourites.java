package com.example.artemis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
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
    String pageId =null;

    //private SharedPreferences fav;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        dbHelper = new FavDatabaseHelper(this);
        urlInput = (EditText) findViewById(R.id.editText3);
        titleInput = (EditText) findViewById(R.id.editTextTitle);
        listView =  findViewById(R.id.listView2);
        arrayList = new ArrayList<>();
        retrieveFromDatabase();


        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        Button btnAdd = (Button) findViewById(R.id.button21);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = titleInput.getText().toString();
                String newItem2 = urlInput.getText().toString();
                addFavourite(newItem, newItem2);
            }
        });
        Button btnPop = (Button) findViewById(R.id.button24);
        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFavourite();
            }
        });
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_favourites,R.id.textView,arrayList);
        Bundle getIntent = getIntent().getExtras();
        if (getIntent != null && getIntent.getInt(Intent.EXTRA_TEXT) != 1 && getIntent.getInt(Intent.EXTRA_TEXT) != 2) {
            String title = getIntent.getString(Intent.EXTRA_TITLE);
            String link = getIntent.getString(Intent.EXTRA_PROCESS_TEXT);
            addFavourite(title, link);
            Intent intent = new Intent(Favourites.this, MainActivity.class);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, link);
            startActivity(intent);
        }
    }

    public void mainPage(View v) {
        Bundle getterFav = getIntent().getExtras();
        if (getterFav != null) {
            int res = getterFav.getInt(Intent.EXTRA_TEXT);
            if (res == 1) {
                Intent goMainPage = new Intent(this, MainActivity.class);
                startActivity(goMainPage);
            } else {
                if (res == 2) {
                    Intent goMainPage1 = new Intent(this, Settings.class);
                    startActivity(goMainPage1);
                }
            }
        }
    }

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
    /*private View.OnClickListener onDelete=new View.OnClickListener() {
        public void onClick(View v) {
            //if we haven't clicked a note yet, do nothing
            if (pageId==null) {
                return;
            }
            else{
                //we have have clicked a note, then delete it!
                removeFavourite();
                //set noteId back to null, so the next note will be added as new
                pageId=null;

            }

            urlInput.setText("");
            titleInput.setText("");
        }
    };*/

    private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent,
                                View view, int position,
                                long id)
        {
            //get the id of the item in the list clicked on
            //we need this so we can update the changes later
            pageId =String.valueOf(id);
            //query the database for that ID
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c=db.rawQuery("SELECT ID, Title, Url FROM favs_table WHERE ID=?", null);



        }
    };

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

    public String getUrl(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectString = "SELECT * FROM favs_table where Title = " + title;
        Cursor cursor = db.rawQuery(selectString, null);
        return cursor.getString(cursor.getColumnIndex("Url"));
    }

    /*
     * Removes a favourite from the list.
     */
    public void removeFromFavourites(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("favs_table", "Title = ?", new String[] {id});
        db.close();
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
