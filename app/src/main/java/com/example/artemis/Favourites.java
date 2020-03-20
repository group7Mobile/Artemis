package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;

public class Favourites extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText urlInput;
    private EditText titleInput;
    private SharedPreferences fav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        urlInput = (EditText) findViewById(R.id.editText3);
        titleInput = (EditText) findViewById(R.id.editTextTitle);
        ListView listView = (ListView) findViewById(R.id.listView2);
        arrayList = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            String item = arrayList.get(i);
            if (item.equals(urlInput.getText().toString())) {
                arrayList.add(item);
                adapter.notifyDataSetChanged();
            }
        }

        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
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
        String newItem = urlInput.getText().toString();
        String newItem2 = titleInput.getText().toString();
        //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
        arrayList.add(newItem2);
        addToFavourites(newItem, newItem2, false);
        adapter.notifyDataSetChanged();
    }

    public void removeFavourite() {
        if(arrayList.size()==0){
            return;
        }
        boolean deleted = false;
        for (int i = 0; i < arrayList.size(); i++) {
            String item = arrayList.get(i);
            if (item.equals(titleInput.getText().toString())) {
                arrayList.remove(i);
                adapter.notifyDataSetChanged();
                deleted = true;
                removeFromFavourites(urlInput.getText().toString());
            }
        }
        if (deleted = false) {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Site not in favourites", duration);
            toast.show();
        }
    }

    /*
     * 	Adds a favourite to the list. To save to sharedPreferences, must convert arraylist to gson:
     */
    public void addToFavourites(String title, String url, boolean homepage) {
        Site favId = new Site(title, url);
        SharedPreferences fav = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
        //Retrieve ArrayList from preferences:
        Gson gson = new Gson();
        //Check if favourites has already been created:
        ArrayList<Site> list;
        if (fav.contains("Favourites")) {
            String json = fav.getString("Favourites", "");
            list = gson.fromJson(json, ArrayList.class);
        } else { //If not, create the list:
            list = new ArrayList<Site>();
        }
        list.add(favId);
        //Add ArrayLit back to preferences:
        Gson gsonAdd = new Gson();
        String jsonAdd = gson.toJson(list);
        SharedPreferences.Editor editor = fav.edit();
        editor.putString("Favourites", jsonAdd);
        if (homepage) {
            editor.putString("HomepageURL", "url");
        }
        editor.commit();
    }

    /*
     * Removes a favourite from the list.
     */
    public void removeFromFavourites(String url) {
        try {
            SharedPreferences fav = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
            //Retrieve ArrayList from preferences:
            Gson gson = new Gson();
            String json = fav.getString("Favourites", "");
            ArrayList<Site> list = gson.fromJson(json, ArrayList.class);
            for (int i = 0; i < list.size(); i++) {
                Site item = list.get(i);
                if (item.getUrl().equals(url)) {
                    list.remove(i);
                }
            }
            //Add ArrayList back to preferences:
            Gson gsonAdd = new Gson();
            String jsonAdd = gson.toJson(list);
            SharedPreferences.Editor editor = fav.edit();
            editor.putString("Favourites", json);
            editor.commit();
        } catch (Exception e) {
            System.out.println("Site not in Favourites");
        }
    }
}