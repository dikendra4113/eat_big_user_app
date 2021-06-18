package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.waiterlessfood.model.UserDb;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class MainProfileActivity extends AppCompatActivity {
    private TextView mTvUserName,mTvMyOrder,mTvSave,mTvOffer,mTvLogout;
    private ImageView userImg,editProfile;
    private Uri imageUri;
    private StorageReference storageProfilePrictureRef;
    private StorageTask uploadTask;
    private String pn,myUrl;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;
    private String checker= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        ImageView myInfo = findViewById(R.id.my_info_bg);
        userImg = findViewById(R.id.profile_pic_iv);
        editProfile = findViewById(R.id.profile_pic_edit);
        mTvUserName = findViewById(R.id.user_name_tv);
        mTvMyOrder = findViewById(R.id.my_order_tx);
        mTvOffer = findViewById(R.id.my_offer_tv);
        mTvLogout = findViewById(R.id.logout_tv);
        mTvSave = findViewById(R.id.save);
        UserDb userDb = new UserDb();
        Paper.init(this);
        userRef = FirebaseDatabase.getInstance().getReference();
        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        pn = Paper.book().read(Prevelents.phoneKey);
        mTvUserName.setText(userDb.getFullname());
        myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });
        mTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                Paper.book().destroy();
                finish();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(MainProfileActivity.this);
            }
        });
        mTvMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrderViewActivity.class));
            }
        });
        mTvOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OfferActivity.class));
            }
        });
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else {
                    storeProductInfo();
                    progressDialog = new ProgressDialog(MainProfileActivity.this);
                    progressDialog.setTitle("Update Profile");
                    progressDialog.setMessage("Please wait, while we are updating your account information");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }

            }
        });
        userInfoDisplay();
    }

    private void userInfoDisplay()
    {
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(pn);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {


                    String name = dataSnapshot.child("fullname").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Paper.book().write(Prevelents.imageUrl,image);
                        Glide.with(userImg.getContext()).load(image).placeholder(R.drawable.account_circle_ic).into(userImg);
                    }
                    mTvUserName.setText(name);




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            userImg.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MainProfileActivity.this, MainProfileActivity.class));
            finish();
        }
    }

    private void storeProductInfo(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate+saveCurrentTime;

        final StorageReference filepath = storageProfilePrictureRef.child(pn);

        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(MainProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw (task.getException());
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    myUrl = task.getResult().toString();
                    progressDialog.dismiss();
                    saveProductIntoDatabase();
                }
            });

        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 ){
            if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                saveProductIntoDatabase();
            }
        }
    }

    private void saveProductIntoDatabase() {
        final HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("image",myUrl);


        userRef.child("Users").child(pn).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(MainProfileActivity.this, "Profile is updated", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(MainProfileActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}