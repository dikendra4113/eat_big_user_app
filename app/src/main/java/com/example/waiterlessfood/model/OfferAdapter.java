package com.example.waiterlessfood.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterlessfood.OfferActivity;
import com.example.waiterlessfood.OrderViewActivity;
import com.example.waiterlessfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OfferAdapter extends FirebaseRecyclerAdapter<OfferModel, OfferAdapter.myViewHolder> {

    private final OfferActivity orderViewActivity;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param orderViewActivity
     * @param options
     */
    public OfferAdapter(OfferActivity orderViewActivity, @NonNull FirebaseRecyclerOptions<OfferModel> options) {
        super(options);
        this.orderViewActivity = orderViewActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull OfferAdapter.myViewHolder holder, int position, @NonNull OfferModel model) {

      holder.offer.setText(model.getOffer_name());
      holder.code.setText(model.getOffer_code());
    }

    @NonNull
    @Override
    public OfferAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item_view,parent,false);
        return new OfferAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView offer,code;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            code = itemView.findViewById(R.id.coupon_code);
            offer = itemView.findViewById(R.id.offer_name);
        }
    }
}
