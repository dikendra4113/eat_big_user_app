package com.example.waiterlessfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PartiesActivity extends AppCompatActivity {
    TextView helpline;
    Button callbtn;
    public void onCall(View view) {
        String phoneNum = helpline.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
        callIntent.setData(Uri.parse("tel:"+phoneNum));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"your Activity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parties);
        helpline =findViewById(R.id.pnNum);
        callbtn = findViewById(R.id.call_btn);
    }
}