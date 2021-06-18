package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.waiterlessfood.model.OfferAdapter;
import com.example.waiterlessfood.model.PopularAdapter;
import com.example.waiterlessfood.model.model;
import com.example.waiterlessfood.model.PopularBrandActAdapter;
import com.example.waiterlessfood.model.myAdapter;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import io.paperdb.Paper;

public class Home2Activity extends AppCompatActivity {
    public static String phone;
    private RecyclerView mRcPopularBrnd;
    private RecyclerView mRcPopular;
    private DrawerLayout mDrawerLayout;
    private PopularAdapter adapter;
    private Dialog dialog;
    String restuarantSelected ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        dialog = new Dialog(this);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        NavigationView mNavigation = findViewById(R.id.nav_menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.menu_ic);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId( R.id.home );
        final Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        mRcPopular = findViewById(R.id.popular_recycler);
        mRcPopular.setLayoutManager(new LinearLayoutManager(this));
        mRcPopularBrnd = findViewById(R.id.popular_brands_recycler);
        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Popular"), model.class)
                        .build();

        model model = new model();
//        Log.i("Modessada",model.image);
         adapter =new PopularAdapter(Home2Activity.this,options);
        mRcPopular.setAdapter(adapter);
        Paper.init(this);
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        Intent intent = new Intent(getApplicationContext(),OrderViewActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_live_seat:
                        Intent i = new Intent(getApplicationContext(),SeatActivity.class);
                        startActivity(i);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_call:
                        startActivity(new Intent(getApplicationContext(),PartiesActivity.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_profile:
                        startActivity( new Intent(getApplicationContext(),MainProfileActivity.class));
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_setting:
                        Intent intent2 = new Intent(getApplicationContext(),OrderViewActivity.class);
                        startActivity(intent2);

                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_logout:

                        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent1);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        Paper.book().destroy();
                        finish();
                        break;

                }
                return false;
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener( new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.favorite:
                        startActivity( new Intent(getApplicationContext()
                                ,OrderViewActivity.class).putExtra("phone",phone));
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.scan:
                        if(restuarantSelected == null){
                            showRestaurantDailog();
                        }else{
                            scanBarcode();
                        }


                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.cart:
                        startActivity( new Intent(getApplicationContext()
                                ,ProductCartDetailActivity.class).putExtra("phone",phone));
                        overridePendingTransition( 0, 0 );
                        return true;

                    case R.id.account:
                        startActivity( new Intent(getApplicationContext()
                                ,MainProfileActivity.class));
                        overridePendingTransition( 0, 0 );
                        return true;
                }
                return false;
            }
        } );

        loadIntialData();

    }

    private void showRestaurantDailog() {
        dialog.setContentView(R.layout.hotel_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView closebtn = dialog.findViewById(R.id.close_btn);
        Button cancle_option =  dialog.findViewById(R.id.cancle_option);
        Button confirm_btn =  dialog.findViewById(R.id.confirm_button);
        Spinner spinner = dialog.findViewById(R.id.spinnerHotel);
        ArrayAdapter option = ArrayAdapter.createFromResource(this, R.array.hotels_option, R.layout.spinner_item_start);
        option.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(option);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                restuarantSelected = item.toString();
                Paper.book().write(Prevelents.current_hotel,restuarantSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                restuarantSelected = null;

            }
        });
        cancle_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                restuarantSelected = null;

            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcode();
                dialog.dismiss();


            }
        });

        dialog.show();
    }

    private void loadIntialData() {
        setupPopular();
        setupBrand();
    }

    private void setupPopular() {
    }

    private void setupBrand() {
        PopularBrandActAdapter brandAdapter = new PopularBrandActAdapter(5);
        mRcPopularBrnd.setAdapter(brandAdapter);
    }


    private void scanBarcode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(Home2Activity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scanning");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null && result.getContents() !=null){
            DatabaseReference seatRef;
            seatRef = FirebaseDatabase.getInstance().getReference();
            seatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if(snapshot.child("Seats").child(restuarantSelected).child(result.getContents()).exists()){
                            Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                            intent.putExtra("seat",result.getContents());
                            intent.putExtra("phone",phone);
                            Paper.book().write(Prevelents.previous_seat,result.getContents());
                            startActivity(intent);

                        }

                    }catch (Exception e){
                        Toast.makeText(Home2Activity.this, "Invailid Scan....", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Home2Activity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
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