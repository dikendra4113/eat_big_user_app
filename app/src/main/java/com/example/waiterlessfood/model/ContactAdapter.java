package com.example.waiterlessfood.model;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.waiterlessfood.PartiesActivity;
import com.example.waiterlessfood.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> implements Filterable {

    private final PartiesActivity context;
    private final ArrayList<Contact> contactArrayList;
    private final ArrayList<Contact> backup;

    private ContactSelectListener listener;

//    public void setListener(ContactSelectListener listener) {
//        context.listener = listener;
//    }

    public ContactAdapter(PartiesActivity context, ArrayList<Contact> contactArrayList) {
        this.context= context;
        this.contactArrayList = contactArrayList;
        this.backup = new ArrayList<>(contactArrayList);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.cell_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Contact model = contactArrayList.get(position);

        if (model != null) {
            holder.mTvName.setText(model.restaurant_owner);
            holder.mTvMobile.setText(model.restaurant_phone);

            holder.mIvProfile.setText(String.valueOf(model.restaurant_owner.toUpperCase().charAt(0)));
            holder.mBtninvite.setOnClickListener(view -> {
                String phoneNum = model.restaurant_phone;
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:"+phoneNum));    //context is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                }else {     //have got permission
                    try{
                        context.startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(context,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return contactArrayList != null && contactArrayList.size() > 0 ? contactArrayList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<Contact> filteredData = new ArrayList<>();
            if (keyword.toString().isEmpty()) {
                filteredData.addAll(backup);
            } else {
                for (Contact obj : backup) {
                    if (obj.restaurant_owner.toLowerCase().contains(keyword.toString().toLowerCase())) {
                        filteredData.add(obj);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            contactArrayList.clear();
            contactArrayList.addAll((ArrayList<Contact>) filterResults.values);
            notifyDataSetChanged();
        }

    };

    static class Holder extends RecyclerView.ViewHolder {

        private final TextView mTvMobile;
        private final TextView mTvName;
        private final Button mBtninvite;
        private final TextView mIvProfile;

        public Holder(@NonNull View itemView) {
            super(itemView);

            mTvMobile = itemView.findViewById(R.id.contact_phone);
            mTvName = itemView.findViewById(R.id.contact_name);
            mBtninvite = itemView.findViewById(R.id.inviteBtn);
            mIvProfile = itemView.findViewById(R.id.firstLater);
        }
    }


    public interface ContactSelectListener {
        void onContactSelected(Contact contact);
    }
}
