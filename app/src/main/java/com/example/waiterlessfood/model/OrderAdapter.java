package com.example.waiterlessfood.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterlessfood.OrderViewActivity;
import com.example.waiterlessfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel,OrderAdapter.myViewHolder> {

    private final OrderViewActivity orderViewActivity;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param orderViewActivity
     * @param options
     */
    public OrderAdapter(OrderViewActivity orderViewActivity, @NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
        this.orderViewActivity = orderViewActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderAdapter.myViewHolder holder, int position, @NonNull OrderModel model) {

        holder.order_seat.setText("Seat No.: "+model.getSeatNo());
        holder.order_price.setText("Rs.: "+model.getPrice());
//        holder.order_des.setText("Des: "+model.getDescription());
        holder.order_pname.setText(model.getPname()+" x "+model.getQuantity());

        holder.restaurant_name.setText(model.getRestaurant_name());
        holder.order_date.setText("Date: "+model.getDate());
//        holder.order_quantity.setText("Qantity: "+model.getQuantity());
        if(model.getStatus().equals("Done")){
            holder.statusImg.setImageResource(R.drawable.ic_done_24);
            holder.delivery_status.setText("Delivery Done");
        }
        if(model.getStatus().equals("false")){
            holder.statusImg.setImageResource(R.drawable.ic_wait);
            holder.delivery_status.setText("Delivery: wait");
        }
//        if(model.getPaid().equals("Pending")){
//            holder.order_payment.setTextColor(R.drawable.seat_alloted);
//        }
//        holder.order_payment.setText("Payment Status:"+model.getPaid());
    }

    @NonNull
    @Override
    public OrderAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_item_view,parent,false);
        return new OrderAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView order_pname,restaurant_name,order_price,order_seat,order_date,order_quantity,order_payment,delivery_status;
        ImageView statusImg;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurant_name = (TextView)itemView.findViewById(R.id.restaurant_name_tv);
            order_pname = (TextView)itemView.findViewById(R.id.order_item_tv);
//            order_des = (TextView)itemView.findViewById(R.id.delivery_status_tv);
            order_price = (TextView)itemView.findViewById(R.id.total_amount_tv);
            order_seat = (TextView)itemView.findViewById(R.id.order_no_tv);
            order_date = (TextView)itemView.findViewById(R.id.order_date_tv);
//            order_payment = (TextView)itemView.findViewById(R.id.order_payment);
            delivery_status = (TextView)itemView.findViewById(R.id.delivery_status_tv);
            statusImg = itemView.findViewById(R.id.done_ic);

        }
    }
}
