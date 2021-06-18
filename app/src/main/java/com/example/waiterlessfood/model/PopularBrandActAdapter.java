package com.example.waiterlessfood.model;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.waiterlessfood.R;



public class PopularBrandActAdapter extends RecyclerView.Adapter<PopularBrandActAdapter.Holder> {

   int data;

    public PopularBrandActAdapter(int data) {
        this.data =data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.popular_brands_item_view,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(position == 1){
            holder.brandImg.setImageResource(R.drawable.pizzahut);
        }else if(position == 2){
            holder.brandImg.setImageResource(R.drawable.dominos);
        }else if(position == 3){
            holder.brandImg.setImageResource(R.drawable.grill);
        }else if(position == 4){
            holder.brandImg.setImageResource(R.drawable.mcdonald);
        }

    }


    @Override
    public int getItemCount() {
        return data;
    }

    class Holder extends RecyclerView.ViewHolder {
            ImageView brandImg;

        public Holder(@NonNull View itemView) {
            super(itemView);
            brandImg = itemView.findViewById(R.id.brandImg);

        }
    }
}
