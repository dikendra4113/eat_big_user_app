package com.example.waiterlessfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.waiterlessfood.model.OfferAdapter;
import com.example.waiterlessfood.model.OfferModel;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class OfferActivity extends AppCompatActivity {

    RecyclerView odder_recycler;
    public OfferAdapter adapter;
    public String saveCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        odder_recycler = findViewById(R.id.offer_RecyclerView);
        odder_recycler.setLayoutManager(new LinearLayoutManager(this));
        String phone = Paper.book().read(Prevelents.phone);
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        FirebaseRecyclerOptions<OfferModel> options =
                new FirebaseRecyclerOptions.Builder<OfferModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Offer"), OfferModel.class)
                        .build();


        adapter =new OfferAdapter(OfferActivity.this,options);
        odder_recycler.setAdapter(adapter);
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