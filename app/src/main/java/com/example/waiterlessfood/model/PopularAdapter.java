package com.example.waiterlessfood.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.waiterlessfood.Home2Activity;
import com.example.waiterlessfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.waiterlessfood.UserActivity.phoneNumber;
import static com.example.waiterlessfood.UserActivity.seatNo;


public class PopularAdapter extends FirebaseRecyclerAdapter<model, PopularAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param home2Activity
     * @param options
     */
    public PopularAdapter(Home2Activity home2Activity, @NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
        Log.i("Option",options+"");
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {
        Log.i("MSG",model.getCatagary());
        holder.productName.setText(model.getPname());
        holder.product_description.setText(model.getDescription());
        holder.user_productPrice.setText("Rs."+model.getPrice()+"/-");

        Glide.with(holder.productImage.getContext()).load(model.getImage()).into(holder.productImage);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_item_view,parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName,product_description,user_productPrice;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = (ImageView)itemView.findViewById(R.id.productImage);
            productName = (TextView)itemView.findViewById(R.id.item_name_popolar);

            product_description = (TextView)itemView.findViewById(R.id.weight_popular);
            user_productPrice = (TextView)itemView.findViewById(R.id.product_price);

        }




    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
