package com.example.waiterlessfood.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterlessfood.OrderViewActivity;
import com.example.waiterlessfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SeatAdapter extends FirebaseRecyclerAdapter<SeatModel, SeatAdapter.myViewHolder> {

        public String status;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *

     * @param options
     */
    public SeatAdapter( @NonNull FirebaseRecyclerOptions<SeatModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull SeatAdapter.myViewHolder holder, int position, @NonNull SeatModel model) {
       status= model.getAvailability();
        if(status.equals("available")){
            holder.seatAvail.setBackgroundColor(R.drawable.seatcolor);
        }

        holder.seatAvail.setText(model.getTableNo());


    }

    @NonNull
    @Override
    public SeatAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new SeatAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        Button seatAvail;
        TextView tableNum;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            seatAvail = (Button)itemView.findViewById(R.id.seat);


        }
    }
}
