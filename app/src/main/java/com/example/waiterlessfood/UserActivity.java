package com.example.waiterlessfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.waiterlessfood.model.ViewPagerAdapter;
import com.example.waiterlessfood.model.myAdapter;
import com.example.waiterlessfood.prevelent.FragmentAll;
import com.example.waiterlessfood.prevelent.FragmentBeverages;
import com.example.waiterlessfood.prevelent.FragmentMainCourse;
import com.example.waiterlessfood.prevelent.FragmentPastas;
import com.example.waiterlessfood.prevelent.FragmentPizzas;
import com.example.waiterlessfood.prevelent.FragmentSandwiches;
import com.example.waiterlessfood.prevelent.FragmentStarter;
import com.google.android.material.tabs.TabLayout;

public class UserActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    myAdapter adapter;
    static public String seatNo ;       ////// BY Defualt seat =1 for Testing Purpuse
    static public String phoneNumber;
    static public ProgressDialog loadingBar;
    int flag = 0;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;
    Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        checkoutBtn = findViewById(R.id.checkOut);
        loadingBar = new ProgressDialog(this);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
       // pagerAdapter.addFragment(new FragmentAll(),"All");
        pagerAdapter.addFragment(new FragmentStarter(),"starters");
        pagerAdapter.addFragment(new FragmentMainCourse(),"meals");
        pagerAdapter.addFragment(new FragmentSandwiches(),"sandwiches");
        pagerAdapter.addFragment(new FragmentPizzas(),"pizzas");
        pagerAdapter.addFragment(new FragmentPastas(),"pastas");
        pagerAdapter.addFragment(new FragmentBeverages(),"beverages");

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phone");
        seatNo = intent.getStringExtra("seat");

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProductCartDetailActivity.class);
                startActivity(intent);
            }
        });

   }


}