package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView,menu_profile;
    private EditText fullNameEditText, userPhoneEditText, addressEditText,profile_email;
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";
    public String pn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        View inflatedView = getLayoutInflater().inflate(R.layout.nav_header,null);

//        profileImageView = (CircleImageView) findViewById(R.id.profile_image);
       // menu_profile = inflatedView.findViewById(R.id.header_image);
        fullNameEditText = (EditText) findViewById(R.id.profile_fullname);
        userPhoneEditText = (EditText) findViewById(R.id.profile_mobile);
        addressEditText = (EditText) findViewById(R.id.profile_address);
        profile_email = findViewById(R.id.profile_email_change);
//        profileChangeTextBtn = (TextView) findViewById(R.id.change_profile_image);
        closeTextBtn = (TextView) findViewById(R.id.cancel_btn);
        saveTextButton = (TextView) findViewById(R.id.update_btn);
        pn = Paper.book().read(Prevelents.phone);

        userInfoDisplay(fullNameEditText, userPhoneEditText,profile_email, addressEditText);


        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });


        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                   // updateOnlyUserInfo();
                }
            }
        });


//        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                checker = "clicked";
//
//                CropImage.activity(imageUri)
//                        .setAspectRatio(1, 1)
//                        .start(ProfileActivity.this);
//            }
//        });
    }



    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap. put("fullname", fullNameEditText.getText().toString());
        userMap. put("address", addressEditText.getText().toString());
        userMap. put("phone", userPhoneEditText.getText().toString());
        userMap. put("email", profile_email.getText().toString());
        ref.child(pn).updateChildren(userMap);

        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        Toast.makeText(ProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            finish();
        }
    }




    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is address.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Name is mandatory.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(profile_email.getText().toString()))
        {
            Toast.makeText(this, "Email is mandatory.", Toast.LENGTH_SHORT).show();
        }
//        else if(checker.equals("clicked"))
//        {
//            uploadImage();
//        }
    }



//    private void uploadImage()
//    {
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Update Profile");
//        progressDialog.setMessage("Please wait, while we are updating your account information");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//        if (imageUri != null)
//        {
//            final StorageReference fileRef = storageProfilePrictureRef
//                    .child(pn + ".jpg");
//
//            uploadTask = fileRef.putFile(imageUri);
//
//            uploadTask.continueWithTask(new Continuation() {
//                @Override
//                public Object then(@NonNull Task task) throws Exception
//                {
//                    if (!task.isSuccessful())
//                    {
//                        throw task.getException();
//                    }
//
//                    return fileRef.getDownloadUrl();
//                }
//            })
//                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task)
//                        {
//                            if (task.isSuccessful())
//                            {
//                                Uri downloadUrl = task.getResult();
//                                myUrl = downloadUrl.toString();
//
//                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                                HashMap<String, Object> userMap = new HashMap<>();
//                                userMap. put("fullname", fullNameEditText.getText().toString());
//                                userMap. put("address", addressEditText.getText().toString());
//                                userMap. put("phone", userPhoneEditText.getText().toString());
//                                userMap. put("image", myUrl);
//                                ref.child(pn).updateChildren(userMap);
//
//                                progressDialog.dismiss();
//
//                                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
//                                Toast.makeText(ProfileActivity.this, "Profile Info update successfully.", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                            else
//                            {
//                                progressDialog.dismiss();
//                                Toast.makeText(ProfileActivity.this, "Error.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//        }
//        else
//        {
//            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
//        }
//    }


    private void userInfoDisplay(final EditText fullNameEditText, final EditText userPhoneEditText, final EditText profile_email, final EditText addressEditText)
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
                        if(dataSnapshot.child("address").exists()){
                            String address = dataSnapshot.child("address").getValue().toString();
                            addressEditText.setText(address);
                        }


                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        profile_email.setText(email);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }}