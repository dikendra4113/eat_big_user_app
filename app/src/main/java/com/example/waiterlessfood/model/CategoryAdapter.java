package com.example.waiterlessfood.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.waiterlessfood.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder>  {

    private final Context context;
   int data = 6;


    public CategoryAdapter(Context context, int data) {
        this.context = context;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.cell_cat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return data;
    }



    static class Holder extends RecyclerView.ViewHolder {


        private final TextView mTvName;
        private final TextView mIvProfile;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mTvName = itemView.findViewById(R.id.food_title);
            mIvProfile = itemView.findViewById(R.id.food_ic);
        }
    }


}
