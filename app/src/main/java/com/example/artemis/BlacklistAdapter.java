package com.example.artemis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BlacklistAdapter extends RecyclerView.Adapter<BlacklistAdapter.BlacklistViewHolder> {
    ArrayList<String> favourites;
    Context context;

    public BlacklistAdapter(Context context, ArrayList<String> s1) {
        this.context = context;
        favourites = s1;
    }

    @NonNull
    @Override
    public BlacklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent,false);
        return new BlacklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlacklistViewHolder holder, final int position) {
        holder.myText.setText(favourites.get(position));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
            }
        });
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String[] item = {"Delete"};
                builder.setTitle("Select an action");
                builder.setItems(item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        BlackListDatabaseHelper dbHelper = new BlackListDatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String deleted = favourites.get(position);
                        db.delete("bl_table", "Title = ?", new String[] {deleted});
                        db.close();
                        favourites.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class BlacklistViewHolder extends RecyclerView.ViewHolder {
        TextView myText;
        ConstraintLayout constraintLayout;

        public BlacklistViewHolder(@NonNull View itemView) {
            super(itemView);
            myText = itemView.findViewById(R.id.myTextview);
            constraintLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
