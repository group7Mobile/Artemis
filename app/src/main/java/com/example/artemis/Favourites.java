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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class Favourites extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText txtInput;
    private SharedPreferences fav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ListView listView = (ListView) findViewById(R.id.listView2);
        String[] items = {"1","2","3"};
        arrayList = new ArrayList<>(Arrays.asList(items));
        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        Button btnAdd = (Button) findViewById(R.id.button21);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInput = (EditText) findViewById(R.id.editText3);
                String newItem = txtInput.getText().toString();
                //every time add an item, add it in the top of stack by adding it to the 0 index of the arrayList
                arrayList.add(0,newItem);
                adapter.notifyDataSetChanged();
            }
        });

        Button btnPop = (Button) findViewById(R.id.button24);
        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()==0){
                    return;
                }

                arrayList.remove(0);
                adapter.notifyDataSetChanged();
            }
        });

    }
    public void mainPage(View v) {
        Intent goMainPage = new Intent(this, Settings.class);
        startActivity(goMainPage);
    }

    /*
     * 	Adds a favourite to the list. To save to sharedPreferences, must convert arraylist to gson:
     */
    public void addToFavourites(String title, String url, boolean homepage) {
        Site favId = new Site(title, url);
        SharedPreferences fav = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
        //Retrieve ArrayList from preferences:
        Gson gson = new Gson();
        //Check if blacklist has already been created:
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
    public void removeFavourite(Site favId) {
        try {
            SharedPreferences fav = getApplicationContext().getSharedPreferences("GlobalPrefs", 0);
            //Retrieve ArrayList from preferences:
            Gson gson = new Gson();
            String json = fav.getString("Favourites", "");
            ArrayList<Site> list = gson.fromJson(json, ArrayList.class);

            list.remove(favId);
            //Add ArrayLit back to preferences:
            Gson gsonAdd = new Gson();
            String jsonAdd = gson.toJson(list);
            SharedPreferences.Editor editor = fav.edit();
            editor.putString("Favourites", json);
            editor.commit();
        } catch (Exception e) {
            System.out.println("Site not in blacklist");
        }
    }
}