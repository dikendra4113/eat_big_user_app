package com.example.waiterlessfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waiterlessfood.model.Contact;
import com.example.waiterlessfood.model.ContactAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PartiesActivity extends AppCompatActivity {
    private ImageView mIvSearch;
    private CharSequence mSearch = "";
    private ArrayList<Contact> contactArrayList= new ArrayList();
    ContactAdapter contactAdapter;
    RecyclerView mRcContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parties);
        mIvSearch = findViewById(R.id.search_icon);
        EditText mEtSearch = findViewById(R.id.search_et);


         mRcContacts = findViewById(R.id.contact_recycler);
        mRcContacts.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Admins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                VaccineModal vaccineModal = new VaccineModal();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    Contact model = new Contact();
                    model = dsp.getValue(Contact.class);
                    contactArrayList.add(model);
                }
                if(contactArrayList.size()>0){
                    setupAdapter();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mIvSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contactAdapter.getFilter().filter(charSequence);
                mSearch = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mSearch.length() == 0) {
                    mIvSearch.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setupAdapter() {
         contactAdapter = new ContactAdapter(PartiesActivity.this, contactArrayList);
//        contactAdapter.setListener(this);
        mRcContacts.setAdapter(contactAdapter);
    }



    public void onInviteScreenBackClicked(View view) {
        onBackPressed();
    }


}