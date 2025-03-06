package com.example.f_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.Order;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.Repository.RestaurantRepository;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private OnOrderClickListener listener;
    private RestaurantRepository restaurantRepository;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    // Constructor nhận Context để khởi tạo database
    public PendingOrderAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
        this.restaurantRepository = new RestaurantRepository(context); // Khởi tạo repository với context
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Lấy thông tin nhà hàng từ database dựa trên restaurantId
        Restaurant restaurant = restaurantRepository.getRestaurantById(order.getRestaurantId());

        if (restaurant != null) {
            holder.tvRestaurantAddress.setText(restaurant.getAddress()); // Hiển thị địa chỉ nhà hàng
        } else {
            holder.tvRestaurantAddress.setText("Unknown Address"); // Nếu không tìm thấy
        }

        holder.tvDeliveryAddress.setText("User Address"); // Bạn có thể cập nhật nếu có dữ liệu

        holder.btnDetails.setOnClickListener(v -> {
            listener.onOrderClick(order);
            Toast.makeText(v.getContext(), "Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDeliveryIcon;
        TextView tvRestaurantAddress, tvArrow, tvDeliveryAddress;
        Button btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDeliveryIcon = itemView.findViewById(R.id.imgDeliveryIcon);
            tvRestaurantAddress = itemView.findViewById(R.id.tvRestaurantAddress);
            tvArrow = itemView.findViewById(R.id.tvArrow);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
