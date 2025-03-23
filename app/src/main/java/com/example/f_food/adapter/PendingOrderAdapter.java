package com.example.f_food.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.repository.AddressRepository;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.screen.order_processing.AcceptShippingOrder;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private final RestaurantRepository restaurantRepository;

    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final Context context;
    private final OnOrderClickListener listener;
    private final String userEmail, userPassword, userName, userPhone;
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public PendingOrderAdapter(Context context, List<Order> orderList, OnOrderClickListener listener,
                                String userEmail, String userPassword, String userName, String userPhone) {
        this.context = context;
        this.orderList = new ArrayList<>(orderList);
        this.listener = listener;
        this.restaurantRepository = new RestaurantRepository(context);
        this.addressRepository  = new AddressRepository(context);
        this.orderRepository = new OrderRepository(context);
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhone = userPhone;
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

        Restaurant restaurant = restaurantRepository.getRestaurantById(order.getRestaurantId());
        String restaurantAddress = (restaurant != null) ? restaurant.getAddress() : "Unknown Address";

        String deliveryAddress = addressRepository.getAddressByUserId(order.getUserId());

//        holder.tvRestaurantAddress.setText(restaurantAddress);
//        holder.tvDeliveryAddress.setText(deliveryAddress);
        holder.tvFullAddress.setText(restaurantAddress + " → " + deliveryAddress);

        holder.btnDetails.setOnClickListener(v -> {
            listener.onOrderClick(order);
            Intent intent = new Intent(context, AcceptShippingOrder.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("deliveryAddress", deliveryAddress);
            intent.putExtra("deliveryTime", order.getCreatedAt());
            intent.putExtra("foodOrder", "Food details here");
            intent.putExtra("cost", order.getTotalPrice());

            intent.putExtra("email", userEmail);
            intent.putExtra("password", userPassword);
            intent.putExtra("userName", userName);
            intent.putExtra("userPhone", userPhone);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateList(List<Order> newList) {
        orderList.clear();
        orderList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullAddress;
        Button btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullAddress= itemView.findViewById(R.id.tvFullAddress);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
