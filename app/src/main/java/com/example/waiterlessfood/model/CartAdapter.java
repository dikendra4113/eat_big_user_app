package com.example.waiterlessfood.model;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.waiterlessfood.ProductCartDetailActivity;
import com.example.waiterlessfood.R;

import com.example.waiterlessfood.UserActivity;
import com.example.waiterlessfood.prevelent.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;

import io.paperdb.Paper;

import static com.example.waiterlessfood.UserActivity.loadingBar;
import static com.example.waiterlessfood.UserActivity.phoneNumber;


public class CartAdapter extends FirebaseRecyclerAdapter<cartModel,CartAdapter.myViewHolder> {
    private static ProductCartDetailActivity productCartDetailActivity;
    public static int overall_price=0;
    private final TextView total_price;
    private final Button payButton;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param payButton
     * @param total_price
     * @param productCartDetailActivity
     * @param options
     */
    public CartAdapter(Button payButton, TextView total_price, ProductCartDetailActivity productCartDetailActivity, @NonNull FirebaseRecyclerOptions<cartModel> options) {
        super(options);
        this.productCartDetailActivity = productCartDetailActivity;
        this.total_price = total_price;
        this.payButton = payButton;

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull cartModel cartModel) {

        holder.productName.setText(cartModel.getPname());
        holder.pid = cartModel.getPid();
        holder.user_productPrice.setText("Rs.:"+Integer.parseInt(cartModel.getPrice()) * Integer.parseInt(cartModel.getQuantity()));
        holder.productQuantity.setText(cartModel.getQuantity()+"*"+cartModel.getPrice());
        //String temp = Paper.book().read(Prevelents.flag);

        if (!holder.pid.isEmpty()){
            overall_price = overall_price + Integer.parseInt(cartModel.getPrice()) * Integer.parseInt(cartModel.getQuantity());

            cartModel.setTotal(""+overall_price);
            Log.i("price",cartModel.getTotal()+ "===="+ overall_price);
            total_price.setText("Total Price:"+overall_price);


        }else if(holder.pid.isEmpty()) {
            overall_price = 0;
        }




    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_view_layout,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        //public int overall_price;
        TextView productName,user_productPrice;
        Button editButton,removeButton;
        TextView productQuantity;
        public String pid;




        public myViewHolder(@NonNull final View itemView) {
            super(itemView);

            productName = (TextView)itemView.findViewById(R.id.cart_productName);

            user_productPrice = (TextView)itemView.findViewById(R.id.cart_productPrice);

            productQuantity = (TextView)itemView.findViewById(R.id.cart_productQuantity);
            editButton = (Button)itemView.findViewById(R.id.cart_EditBtn);
            removeButton = (Button)itemView.findViewById(R.id.cart_Remove);


            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(productCartDetailActivity,UserActivity.class);
                    productCartDetailActivity.startActivity(intent);
                    productCartDetailActivity.finish();



                }
            });
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingBar.setTitle("Remove to Cart");
                    loadingBar.setMessage("Please Wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("CartList");
                    cartRef.child("User View").child(phoneNumber).child("Products").child(pid)
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(productCartDetailActivity, "Item Removed", Toast.LENGTH_LONG).show();
                                productCartDetailActivity.recreate();
                            }

                        }
                    });
                }
            });


        }

    }



}
