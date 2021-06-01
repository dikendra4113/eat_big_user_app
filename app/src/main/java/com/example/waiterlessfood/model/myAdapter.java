package com.example.waiterlessfood.model;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.waiterlessfood.R;

import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

import static com.example.waiterlessfood.UserActivity.loadingBar;
import static com.example.waiterlessfood.UserActivity.phoneNumber;
import static com.example.waiterlessfood.UserActivity.seatNo;


public class myAdapter extends FirebaseRecyclerAdapter<model,myAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {

        holder.productName.setText(model.getPname());
        holder.product_description.setText(model.getDescription());
        holder.user_productPrice.setText(model.getPrice());
        holder.productCatagory.setText(model.getCatagary());
        holder.pid = model.getPid();
        holder.seatNo = seatNo;

        Glide.with(holder.productImage.getContext()).load(model.getImage()).into(holder.productImage);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail,parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName,product_description,user_productPrice,productCatagory;
//        Button addToCart;
        ImageView addQuantity,minusQuantity;
        TextView productQuantity;
        public  String pid,seatNo;
        private String saveCurrentDate,saveCurrentTime;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = (ImageView)itemView.findViewById(R.id.productImage);
            productName = (TextView)itemView.findViewById(R.id.productName);

            product_description = (TextView)itemView.findViewById(R.id.product_description);
            user_productPrice = (TextView)itemView.findViewById(R.id.user_productPrice);
//            addToCart = (Button) itemView.findViewById(R.id.addToCart);
            addQuantity = (ImageView)itemView.findViewById(R.id.addQuantity);
            minusQuantity = (ImageView)itemView.findViewById(R.id.minusQuantity);
            productQuantity = (TextView)itemView.findViewById(R.id.productQuantity);

            addQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(productQuantity.getText().toString());
                    quantity++;
                    if(quantity > 0){

                        productQuantity.setText(""+quantity);
                        updateCart();
                    }

                }
            });

            minusQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(productQuantity.getText().toString());
                    if(quantity>0) {
                        quantity--;
                        productQuantity.setText(""+quantity);
                        updateCart();
                    }
                    if(quantity ==0){
                        removeFromCart();
                    }
                }
            });
//            addToCart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   // storeProductInfo();
//                    loadingBar.setTitle("Add to Cart");
//                    loadingBar.setMessage("Please Wait...");
//                    loadingBar.setCanceledOnTouchOutside(false);
//                    loadingBar.show();
//                    Calendar calendar = Calendar.getInstance();
//                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
//                    saveCurrentDate = currentDate.format(calendar.getTime());
//
//                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//                    saveCurrentTime = currentTime.format(calendar.getTime());
//                    final String randomKeyGen = saveCurrentDate+saveCurrentTime;
//
//                    final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("CartList");
//                    final HashMap<String,Object> cartMap = new HashMap<>();
//
//                    cartMap.put("table",seatNo);
//                    cartMap.put("date",saveCurrentDate);
//                    cartMap.put("pid",pid);
//                    cartMap.put("time",saveCurrentTime);
//                    cartMap.put("quantity",productQuantity.getText().toString());
//                    cartMap.put("pname",productName.getText().toString());
//                    cartMap.put("description",product_description.getText().toString());
//                    cartMap.put("price",user_productPrice.getText().toString());
//                    cartMap.put("catagary",productCatagory.getText().toString());
//                    final UserDb userDb = new UserDb();
//                    productRef.child("User View").child(phoneNumber).child("Products").child(pid)
//                            .updateChildren(cartMap)
//                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()){
//                                        productRef.child("Admin View").child(phoneNumber).child("Products").child(pid)
//                                                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()){
//                                                    loadingBar.dismiss();
//
//                                                }
//                                            }
//                                        });
//
//                                    }
//                                }
//                            });
//                }
//            });



        }

        private void removeFromCart() {
            final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("CartList");
            cartRef.child("User View").child(phoneNumber).child("Products").child(pid)
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.i("Remove from Cart","Succefully!!!");
                    }

                }
            });
        }

        private void updateCart() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
            saveCurrentDate = currentDate.format(calendar.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calendar.getTime());
            final String randomKeyGen = saveCurrentDate+saveCurrentTime;

            final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("CartList");
            final HashMap<String,Object> cartMap = new HashMap<>();

            cartMap.put("table",seatNo);
            cartMap.put("date",saveCurrentDate);
            cartMap.put("pid",pid);
            cartMap.put("time",saveCurrentTime);
            cartMap.put("quantity",productQuantity.getText().toString());
            cartMap.put("pname",productName.getText().toString());
            cartMap.put("description",product_description.getText().toString());
            cartMap.put("price",user_productPrice.getText().toString());
            cartMap.put("catagary",productCatagory.getText().toString());
            //final UserDb userDb = new UserDb();
            productRef.child("User View").child(phoneNumber).child("Products").child(pid)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                productRef.child("Admin View").child(phoneNumber).child("Products").child(pid)
                                        .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.i("Add to Cart","Succefully");

                                        }
                                    }
                                });

                            }
                        }
                    });


        }

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
