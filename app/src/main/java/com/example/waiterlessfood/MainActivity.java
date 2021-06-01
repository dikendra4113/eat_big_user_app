package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waiterlessfood.model.UserDb;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.example.waiterlessfood.prevelent.ResetPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private ImageView loginButton;
    private TextView registerTextView,forgetPassword;
    private EditText loginMobileNumber,loginPassword;
    private ProgressDialog loadingBar;
    private CheckBox rememberMe;
    static FirebaseAuth mAuth;


    private String currentDb = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.signinbtn);
        registerTextView = findViewById(R.id.signup_tv);
        loginMobileNumber = findViewById(R.id.phone_no_et);
        loginPassword = findViewById(R.id.editTextPassword);
        loadingBar = new ProgressDialog(this);
        rememberMe = findViewById(R.id.remember_me);
        forgetPassword = findViewById(R.id.forgetpassword);


        mAuth = FirebaseAuth.getInstance();

        Paper.init(this);

       // AdminButton = findViewById(R.id.AdminButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }

        });
//        adminTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginButton.setText("Login Admin");
//                adminTextView.setVisibility(View.INVISIBLE);
//                notAdminTextView.setVisibility(View.VISIBLE);
//                registerTextView.setVisibility(View.INVISIBLE);
//                currentDb = "Admins";
//
//            }
//        });
//        notAdminTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginButton.setText("Login");
//                adminTextView.setVisibility(View.VISIBLE);
//                registerTextView.setVisibility(View.VISIBLE);
//                notAdminTextView.setVisibility(View.INVISIBLE);
//                currentDb = "Users";
//            }
//        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });


        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }

        });

        String phoneKey = Paper.book().read(Prevelents.phoneKey);
        String passwordKey = Paper.book().read(Prevelents.passwordKey);
        if(phoneKey !="" && passwordKey != ""){
            if(!TextUtils.isEmpty(phoneKey) && !TextUtils.isEmpty(passwordKey)){
                pleaseWait();
                allowAccessToAccount(phoneKey,passwordKey);
            }
        }
    }

    private void loginUser() {

        String phoneNumber = loginMobileNumber.getText().toString();
        String password = loginPassword.getText().toString();

        if(phoneNumber.isEmpty()){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
        }else {
            pleaseWait();
            allowAccessToAccount(phoneNumber,password);
        }

    }

    private void pleaseWait() {
        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void allowAccessToAccount(final String phoneNumber, final String password) {

        if(rememberMe.isChecked()){
            Paper.book().write(Prevelents.phoneKey,phoneNumber);
            Paper.book().write(Prevelents.passwordKey,password);

        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(currentDb).child(phoneNumber).exists() ){
                    //Allow login to User
                    UserDb userData = snapshot.child(currentDb).child(phoneNumber).getValue(UserDb.class);
                    if(userData.getPhone().equals(phoneNumber) && userData.getPassword().equals(password)){
                         if(currentDb.equals("Users")){
                            Toast.makeText(MainActivity.this, "Logged in Succesfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Paper.book().write(Prevelents.phone,phoneNumber);
                            Intent intent = new Intent(MainActivity.this,Home2Activity.class);
                            intent.putExtra("phone",phoneNumber);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Please Enter Correct Password...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }else {
                    Toast.makeText(MainActivity.this, "This "+phoneNumber+" is Not Exist You need to Create an Account", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}