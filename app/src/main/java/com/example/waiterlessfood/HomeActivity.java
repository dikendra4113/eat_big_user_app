package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    CardView scan_cardView,order_cardView,seats_cardView,party_cardView;
    public static String phone;

    String image,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        final View inflatedView = getLayoutInflater().inflate(R.layout.nav_header, null);

        nav = findViewById(R.id.nav_menu);
        final TextView nav_username = inflatedView.findViewById(R.id.nav_username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        final Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("fullname").exists())
                    {
                        name = dataSnapshot.child("fullname").getValue().toString();
                       nav_username.setText("Welcome \n"+name);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_home:
                        Intent intent = new Intent(getApplicationContext(),OrderViewActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_profile:
                        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_setting:
                        Intent intent2 = new Intent(getApplicationContext(),OrderViewActivity.class);
                        startActivity(intent2);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_logout:

                        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Paper.book().destroy();
                        finish();
                        break;

                }
                return false;
            }
        });



        //++++++++++++++++++++++++++++++++++++++CardViews++++++++++++++++++++++++++++++++++++++++++++++++++++
        scan_cardView = findViewById(R.id.scan_cardView);

        order_cardView = findViewById(R.id.order_cardView);
        seats_cardView = findViewById(R.id.seats_cardView);

        party_cardView = findViewById(R.id.party_cardView);

        scan_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(HomeActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();
            }
        });

        order_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),OrderViewActivity.class);
                startActivity(intent1);
            }
        });
        seats_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),SeatActivity.class);
                startActivity(intent1);
            }
        });
        party_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),PartiesActivity.class);
                startActivity(intent2);
            }
        });
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
                            startActivity(intent);

                        }

                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this, "Invailid Scan....", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}