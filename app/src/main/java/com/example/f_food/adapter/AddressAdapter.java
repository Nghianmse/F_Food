package com.example.f_food.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.dao.AddressWithUser;
import com.example.f_food.entity.Address;
import com.example.f_food.screen.features_customer.ManageAddress;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<AddressWithUser> addressWithUserList;
    private Context context;

    public AddressAdapter(Context context, List<AddressWithUser> addressWithUserList) {
        this.context = context;
        this.addressWithUserList = addressWithUserList;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        AddressWithUser addressWithUser = addressWithUserList.get(position);

        // Set the data to the TextViews
        holder.tvUsername.setText("Tên người dùng: " + addressWithUser.username);  // User's username
        holder.tvAddress.setText("Địa chỉ: " +addressWithUser.address);  // Address
        holder.tvDetailAddress.setText("Tên đường, Tòa nhà, Số nhà: " + addressWithUser.detailAddress);  // Detail address

        // Check if it is the default address
        if (addressWithUser.isDefault) {
            holder.tvIsDefault.setText("Địa chỉ mặc định");
            holder.tvIsDefault.setVisibility(View.VISIBLE); // Make it visible
        } else {
            holder.tvIsDefault.setVisibility(View.GONE); // Hide it if it's not default
        }

        holder.tvAddressType.setText("Loại địa chỉ: " + addressWithUser.addressType);  // Address type


    }

    @Override
    public int getItemCount() {
        return addressWithUserList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvAddress, tvDetailAddress, tvIsDefault, tvAddressType;
        Button btnAddAddress;

        public AddressViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDetailAddress = itemView.findViewById(R.id.tvDetailAddress);
            tvIsDefault = itemView.findViewById(R.id.tvIsDefault);
            tvAddressType = itemView.findViewById(R.id.tvAddressType);
            btnAddAddress = itemView.findViewById(R.id.btnAddAddress);
        }
    }
}


