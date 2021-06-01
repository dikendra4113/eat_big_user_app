package com.example.waiterlessfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.waiterlessfood.model.OrderAdapter;
import com.example.waiterlessfood.model.OrderModel;
import com.example.waiterlessfood.model.SeatAdapter;
import com.example.waiterlessfood.model.SeatModel;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class SeatActivity extends AppCompatActivity {

    public SeatAdapter adapter;
    public RecyclerView seat_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        seat_recycler = findViewById(R.id.seat_RecyclerView);
        seat_recycler.setLayoutManager(new  GridLayoutManager(this, 4));
        FirebaseRecyclerOptions<SeatModel> options =
                new FirebaseRecyclerOptions.Builder<SeatModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Seats"), SeatModel.class)
                        .build();


        adapter =new SeatAdapter(options);
        seat_recycler.setAdapter(adapter);
    }

    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();


    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}