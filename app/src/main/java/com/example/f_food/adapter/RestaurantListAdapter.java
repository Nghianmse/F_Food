package com.example.f_food.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.Restaurant;
import com.example.f_food.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {
    private List<Restaurant> restaurantList;
    private OnItemClickListener listener;

    private static Context context;

    // Constructor
    public RestaurantListAdapter(List<Restaurant> restaurantList, OnItemClickListener listener, Context context) {
        this.restaurantList = restaurantList;
        this.listener = listener;
        this.context = context;
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(int id);
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

    private static void showAlert(String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
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
            if(restaurant.getImage() != null && !restaurant.getImage().isEmpty()) {
                if (restaurant.getImage().startsWith("content://")) {
                    imgRestaurant.setImageURI(Uri.parse(restaurant.getImage()));
                } else {
                    Picasso.get()
                            .load(restaurant.getImage())
                            .resize(500, 500)
                            .centerCrop()
                            .into(imgRestaurant);
                }
            }

            if (restaurant.getStatus().equals("Open"))  txtStatus.setTextColor(Color.GREEN);
            else txtStatus.setTextColor(Color.RED);

            // Xử lý sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (restaurant.getStatus().equals("Open")) {
                        listener.onItemClick(restaurant.getRestaurantId());
                    }
                    else {
                        showAlert("Chú ý", "Nhà hàng đã đóng cửa!");
                    }
                }
            });
        }
    }
}