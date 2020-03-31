package com.example.artemis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class PopUpBlocking extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText txtInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_blocking);
        ListView listView = (ListView) findViewById(R.id.listView2);
        String[] items = {"1","2","3"};
        arrayList = new ArrayList<>(Arrays.asList(items));
        //constructor of adapter to store input item separately in list_item and put them in list_view
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.txtitem,arrayList);
        listView.setAdapter(adapter);
        Button btnAdd = (Button) findViewById(R.id.button23);
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

        Button btnPop = (Button) findViewById(R.id.button25);
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
        finish();
    }
}
