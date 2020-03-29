package com.example.artemis;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    ArrayList<String> favourites;
    Context context;

    public RecyclerAdapter(Context context, ArrayList<String> s1) {
        this.context = context;
        favourites = s1;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row, parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        holder.myText.setText(favourites.get(position));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("favourites", favourites.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView myText;
        ConstraintLayout constraintLayout;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            myText = itemView.findViewById(R.id.myTextview);
            constraintLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
