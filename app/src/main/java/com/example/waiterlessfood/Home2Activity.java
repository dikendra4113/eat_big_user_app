package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.waiterlessfood.model.PopularBrandActAdapter;
import com.example.waiterlessfood.prevelent.Prevelents;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

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
        mRcPopularBrnd = findViewById(R.id.popular_brands_recycler);
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
                    case R.id.menu_profile:
                        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(i);
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
                                ,HomeActivity.class).putExtra("phone",phone));
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.scan:

                        scanBarcode();
                        overridePendingTransition( 0, 0 );
                        return true;
                    case R.id.cart:
                        startActivity( new Intent(getApplicationContext()
                                ,ProductCartDetailActivity.class).putExtra("phone",phone));
                        overridePendingTransition( 0, 0 );
                        return true;

                    case R.id.account:
//                        startActivity( new Intent(getApplicationContext()
//                                ,Account.class));
                        overridePendingTransition( 0, 0 );
                        return true;
                }
                return false;
            }
        } );

        loadIntialData();

    }

    private void loadIntialData() {
        setupBrand();
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
                        if(snapshot.child("Seats").child(result.getContents()).exists()){
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
}