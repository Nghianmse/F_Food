package com.example.f_food.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.Restaurant;
import com.example.f_food.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantManagementListAdapter extends RecyclerView.Adapter<RestaurantManagementListAdapter.ViewHolder> {
    private List<Restaurant> restaurantList;
    private Context context;

    private ViewHolder.OnStatusChangeListener statusChangeListener;
    public RestaurantManagementListAdapter(List<Restaurant> restaurantList, ViewHolder.OnStatusChangeListener statusChangeListener) {
        this.restaurantList = restaurantList;
        this.statusChangeListener = statusChangeListener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText, addressText, phoneText, statusText;
        private Button changestatus;
        private ImageView imageres;


        public interface OnStatusChangeListener {
            void onStatusChange(int position);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            imageres=itemView.findViewById(R.id.image_restaurant);
            nameText = itemView.findViewById(R.id.txtrestaurant_Management_Name);
            addressText = itemView.findViewById(R.id.txtrestaurant_Management_Address);
            phoneText = itemView.findViewById(R.id.txtrestaurant_Management_Phone);
            statusText = itemView.findViewById(R.id.txtrestaurant_Management_Status);
            changestatus=itemView.findViewById(R.id.btnChangeStatus_Restaurant);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant_manager, parent, false);
        return new ViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        holder.nameText.setText("Name: " + restaurant.getName());
        holder.addressText.setText("Address: " + restaurant.getAddress());
        holder.phoneText.setText("Phone: " + restaurant.getPhone());
        holder.statusText.setText("Status: " + restaurant.getStatus());

        // Load image from the path or URL using Picasso
        Picasso.get()
                .load(restaurant.getImage())  // Assuming restaurant.getImage() contains the image path or URL

                .error(R.drawable.isushi)  // Optional: Add an error image if loading fails
                .into(holder.imageres);  // Assuming you have an ImageView in your ViewHolder

        holder.changestatus.setOnClickListener(v -> {
            if (statusChangeListener != null) {
                statusChangeListener.onStatusChange(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

}
