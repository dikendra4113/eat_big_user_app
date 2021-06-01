package com.example.waiterlessfood;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waiterlessfood.prevelent.ECValidattion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.example.waiterlessfood.MainActivity.mAuth;

public class RegistrationActivity extends AppCompatActivity {
    ImageView registerButton;
    EditText fullnameEditText,passwordEditText,mobileNumber,emailEditText;
    String otpId;
    private ProgressDialog loadingBar;
    DatabaseReference RootRef;


    public void signInWithPhone(){
        String phoneNumber = mobileNumber.getText().toString();
       String fullName = fullnameEditText.getText().toString();
       String password = passwordEditText.getText().toString();
       String emailId = emailEditText.getText().toString();

       if(phoneNumber.isEmpty()){
           Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
       }
       else if(fullName.isEmpty()){
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
       }else if(password.isEmpty()){
           Toast.makeText(this, "Please Enter please enter password", Toast.LENGTH_SHORT).show();
       }else if(emailId.isEmpty()) {
           Toast.makeText(this, "Please Enter valid Email id", Toast.LENGTH_SHORT).show();

       }else{
           ECValidattion.PassValidator v1 = new ECValidattion.PassValidator();
           String err = v1.validate(password);
           if(err!=null) {
               Toast.makeText(this, ""+err, Toast.LENGTH_SHORT).show();

           }else{

               loadingBar.setTitle("Authenticate Mobile Number");
               loadingBar.setMessage("Please Wait...");
               loadingBar.setCanceledOnTouchOutside(false);
               loadingBar.show();

               validateUser(phoneNumber,fullName,password,emailId);


               // OnVerificationStateChangedCallbacks
           }
       }

    }




    private void validateUser(final String phoneNumber, final String fullName, final String password, final String emailId) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Users").child(phoneNumber).exists()){
                    //Store new User to DataBAse
                    phoneAuthentication(phoneNumber,fullName,emailId,password);
                  //  saveUserInDataBase(phoneNumber,fullName,emailId,password);


                }else{
                    Toast.makeText(RegistrationActivity.this, "This "+phoneNumber+"is already Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please Try Again Using another Phone Number", Toast.LENGTH_SHORT).show();

                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void phoneAuthentication(final String phoneNumber, final String fullName, final String emailId, final String password) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                RegistrationActivity.this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential,phoneNumber, fullName, emailId, password);
                        saveUserInDataBase(phoneNumber, fullName, emailId, password);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(RegistrationActivity.this, "Error:"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                    @Override
                    public void onCodeSent( String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpId = s;
                        super.onCodeSent(s, forceResendingToken);
                        Dialog dialog = new Dialog(RegistrationActivity.this);
                        dialog.setContentView(R.layout.varify_popup);
                        final EditText otp = dialog.findViewById(R.id.otpEditTExt);
                        Button varifyButton = dialog.findViewById(R.id.otpButton);
                        varifyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String varificationCode = otp.getText().toString();
                                if(varificationCode.isEmpty())return;
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,varificationCode);
                                signInWithPhoneAuthCredential(credential,phoneNumber,fullName,emailId,password);
                            }
                        });
                        dialog.show();
                    }
                });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String phoneNumber, final String fullName, final String emailId, final String password) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInDataBase(phoneNumber, fullName, emailId, password);
                            startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                            finish();
                            // ...
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserInDataBase(String phoneNumber, String fullName, String emailId, String password){


        HashMap<String,Object> userData = new HashMap<>();
        userData.put("phone",phoneNumber);
        userData.put("fullname",fullName);
        userData.put("email",emailId);
        userData.put("password",password);
        RootRef.child("Users").child(phoneNumber).updateChildren(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Congratulation You have Succefully Create an Account", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }
                });

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerButton = findViewById(R.id.signup_btn);
        fullnameEditText = findViewById(R.id.fullname);
        passwordEditText = findViewById(R.id.editTextPassword);
        mobileNumber = findViewById(R.id.phone_no);
        emailEditText= findViewById(R.id.email_id);
        loadingBar = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithPhone();
            }
        });

    }
}