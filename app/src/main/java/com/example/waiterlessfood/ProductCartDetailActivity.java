package com.example.waiterlessfood;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waiterlessfood.model.CartAdapter;
import com.example.waiterlessfood.model.cartModel;
import com.example.waiterlessfood.model.model;
import com.example.waiterlessfood.model.myAdapter;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

import static com.example.waiterlessfood.UserActivity.phoneNumber;
import static com.example.waiterlessfood.UserActivity.seatNo;
import static com.example.waiterlessfood.prevelent.Prevelents.*;

public class ProductCartDetailActivity extends AppCompatActivity {

    RecyclerView cart_recyclerView;
    public  CartAdapter adapter;
    TextView total_price;
    public String upiId = "dikendraptn4113@oksbi";
    private String name = "Food App";
    String TAG ="main";
    final int UPI_PAYMENT = 0;
    String amount ;
    static String phone;
    String saveCurrentDate,saveCurrentTime,randomKey;
    private  String paymentStatus;
    Dialog dialog;
    ProgressDialog progressDialog;
    private String seat;
    private String restaurant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cart_recyclerView = findViewById(R.id.item_quantity_recycler);
        Button payButton = findViewById(R.id.payButton);
        total_price = findViewById(R.id.totalamount_tv);
        //coupon section
        ImageView couponApplyBtn = findViewById(R.id.apply_coupon_btn);
        TextView couponEditText = findViewById(R.id.apply_coupon_et);
        TextView payamount = findViewById(R.id.payamount_tv);
        TextView discount_amount_tv = findViewById(R.id.discount_amount_tv);
        ImageView discount_msg_bg = findViewById(R.id.discount_msg_bg);
        TextView discount_msg_tv = findViewById(R.id.discount_msg_tv);
        TextView cart_restaurant_tv = findViewById(R.id.cart_restaurant_tv);

        cart_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialog = new Dialog(this);
        Paper.init(this);
        progressDialog = new ProgressDialog(this);
        seat = Paper.book().read(previous_seat);
       phone = Paper.book().read(phoneKey);
       restaurant_name = Paper.book().read(current_hotel);
        cart_restaurant_tv.setText(restaurant_name);
        final FirebaseRecyclerOptions<cartModel> options =
                new FirebaseRecyclerOptions.Builder<cartModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(phone).child("Products"), cartModel.class)
                        .build();


        adapter =new CartAdapter(payButton,total_price,ProductCartDetailActivity.this,options);
        cart_recyclerView.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        randomKey = saveCurrentDate+saveCurrentTime;
        discount_amount_tv.setText("0");

        total_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                payamount.setText(total_price.getText().toString());
            }
        });


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.payment_dialogue_box);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView closebtn = dialog.findViewById(R.id.close_btn);
                Button cashbtn =  dialog.findViewById(R.id.cash_button);
                Button onlinebtn =  dialog.findViewById(R.id.online_button);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                cashbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        paymentStatus = "false";
                        moveRecord();


                    }
                });
                onlinebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        paymentStatus = "Done";
                        payUsingUpi(name, upiId, amount);

                    }
                });

                dialog.show();




            }
        });


        //++++Coupon Apply

        couponApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgeessBar();
                if(couponEditText.getText().toString().isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(ProductCartDetailActivity.this, "Please Enter Valid Coupon Code", Toast.LENGTH_SHORT).show();
                }else {
                        showProgeessBar();
                    if(couponEditText.getText().toString().equalsIgnoreCase("TRYNEW")){
                        int pay_amt = Integer.parseInt(total_price.getText().toString()) - 75;
                        payamount.setText(pay_amt+"");
                        discount_amount_tv.setText("-75");
                        discount_msg_bg.setVisibility(View.VISIBLE);
                        discount_msg_tv.setText("You have saved ???75 on the bill");
                        discount_msg_tv.setVisibility(View.VISIBLE);
                        amount = ""+pay_amt;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(ProductCartDetailActivity.this, "Coupon Applied Successfully", Toast.LENGTH_SHORT).show();

                            }
                        }, 2000);
                    }else if(couponEditText.getText().toString().equalsIgnoreCase("GET50")){
                        int pay_amt = Integer.parseInt(total_price.getText().toString()) - 50;
                        payamount.setText(pay_amt+"");
                        amount = ""+pay_amt;
                        discount_amount_tv.setText("-50");
                        discount_msg_bg.setVisibility(View.VISIBLE);
                        discount_msg_tv.setText("You have saved ???50 on the bill");
                        discount_msg_tv.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(ProductCartDetailActivity.this, "Coupon Applied Successfully", Toast.LENGTH_SHORT).show();

                            }
                        }, 2000);
                    }else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(ProductCartDetailActivity.this, "Please Enter Valid Coupon", Toast.LENGTH_LONG).show();

                            }
                        }, 2000);
                    }
                }
            }
        });

    }

    private void showProgeessBar() {

        progressDialog.setTitle("Verifying Coupon");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void moveRecord() {
        final DatabaseReference toPath = FirebaseDatabase.getInstance().getReference().child("Order").child("Users View").child(phone);
        final DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(phone).child("Products");
        final DatabaseReference adminPath = FirebaseDatabase.getInstance().getReference().child("Order").child("Admins View");
        final DatabaseReference seatPath = FirebaseDatabase.getInstance().getReference().child("Seats").child(restaurant_name).child(seat);
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    final HashMap<String,Object> cartData = new HashMap<>();
                    cartData.put("pname",data.child("pname").getValue().toString());
                    cartData.put("date",data.child("date").getValue().toString());
                    cartData.put("time",data.child("time").getValue().toString());
                    cartData.put("quantity",data.child("quantity").getValue().toString());
                    cartData.put("price",data.child("price").getValue().toString());
//                    cartData.put("catagary",data.child("catagary").getValue().toString());
                    cartData.put("pid",data.child("pid").getValue().toString());
                    cartData.put("seatNo",seatNo);
                    cartData.put("description",data.child("description").getValue().toString());
                    cartData.put("paid",paymentStatus);
                    cartData.put("user_contact",phone);
                    cartData.put("restaurant_name",restaurant_name);
                    cartData.put("status",paymentStatus);
                    final String pid = data.child("pid").getValue().toString();
                    Log.i("Pname",cartData.toString());
                    toPath.child(pid).updateChildren(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                adminPath.child(pid).updateChildren(cartData);
                                HashMap<String,Object> seatData = new HashMap<>();
                                seatData.put("availability","booked");
                                seatPath.updateChildren(seatData);
                            }
                        }
                    });

                }
                fromPath.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProductCartDetailActivity.this, "Order Completed!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductCartDetailActivity.this,UserActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

//                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isComplete()) {
//                            HashMap<String,Object> paymentStatus = new HashMap<>();
//                            paymentStatus.put("payment","Done");
//                            //toPath.child(phone).child(fromPath.child("Products").getKey())
//                            Log.i("Payment",""+dataSnapshot.child("Products").getValue()+"+++"+dataSnapshot.getValue());
//                            fromPath.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(ProductCartDetailActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                        } else {
//                            Toast.makeText(ProductCartDetailActivity.this, "Order Not Placed ", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

    void payUsingUpi(  String name,String upiId,  String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", "dikendraptn4113@oksbi")
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", "Pay to my app")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(ProductCartDetailActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(ProductCartDetailActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(ProductCartDetailActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);


                moveRecord();

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(ProductCartDetailActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(ProductCartDetailActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(ProductCartDetailActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }




    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();
        adapter.overall_price = 0;

    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }

}