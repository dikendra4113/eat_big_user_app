package com.example.waiterlessfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waiterlessfood.model.CartAdapter;
import com.example.waiterlessfood.model.OrderAdapter;
import com.example.waiterlessfood.model.OrderModel;
import com.example.waiterlessfood.model.cartModel;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

import static com.example.waiterlessfood.UserActivity.seatNo;

public class OrderViewActivity extends AppCompatActivity {

    RecyclerView order_recyclerView;
    public OrderAdapter adapter;
    public String saveCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        order_recyclerView = findViewById(R.id.order_RecyclerView);
        order_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String phone = Paper.book().read(Prevelents.phone);
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Order").child("Users View").child(phone), OrderModel.class)
                        .build();


        adapter =new OrderAdapter(OrderViewActivity.this,options);
        order_recyclerView.setAdapter(adapter);
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