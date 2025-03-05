package com.example.f_food.Adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
    private List<Restaurant> restaurantList;
    private OnItemClickListener listener;

    // Constructor
    public RestaurantListAdapter(List<Restaurant> restaurantList, OnItemClickListener listener) {
        this.restaurantList = restaurantList;
        this.listener = listener;
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant, listener);
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    // ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtRestaurantName, txtAddress, txtPhone, txtStatus;
        private ImageView imgRestaurant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRestaurantName = itemView.findViewById(R.id.txtRestaurantName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            imgRestaurant = itemView.findViewById(R.id.imgRestaurant);
        }

        public void bind(final Restaurant restaurant, final OnItemClickListener listener) {
            txtRestaurantName.setText(restaurant.getName());
            txtAddress.setText(restaurant.getAddress());
            txtPhone.setText(restaurant.getPhone());
            txtStatus.setText(restaurant.getStatus());
            //imgRestaurant.setImageResource(restaurant.getImage());

            // Xử lý sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(restaurant);
                }
            });
        }
    }
}