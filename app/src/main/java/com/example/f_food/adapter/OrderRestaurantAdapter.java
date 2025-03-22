package com.example.f_food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.dao.FoodWithOrder;
import com.example.f_food.entity.Order;

import java.util.List;
import java.util.Map;

public class OrderRestaurantAdapter extends RecyclerView.Adapter<OrderRestaurantAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onAccept(Order order);

        void onDecline(Order order);
    }

    public OrderRestaurantAdapter(List<Order> orderList, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderId.setText("Đơn hàng #" + order.getOrderId());

        holder.rvFoodList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
            FoodWithOrderAdapter foodAdapter = new FoodWithOrderAdapter(order.getFoodWithOrderList());
        holder.rvFoodList.setAdapter(foodAdapter);

        switch (order.getOrderStatus()) {
            case "Pending":
                holder.orderActionLayout.setVisibility(View.VISIBLE);
                holder.txtOrderStatus.setVisibility(View.GONE);
                break;
            case "Cancelled":
                holder.orderActionLayout.setVisibility(View.GONE);
                holder.txtOrderStatus.setVisibility(View.VISIBLE);
                holder.txtOrderStatus.setText("Đã từ chối");
                break;
            case "Delivered":
                holder.orderActionLayout.setVisibility(View.GONE);
                holder.txtOrderStatus.setVisibility(View.VISIBLE);
                holder.txtOrderStatus.setText("Đang vận chuyển");
                break;
            default:
                holder.orderActionLayout.setVisibility(View.GONE);
                holder.txtOrderStatus.setVisibility(View.GONE);
                break;
        }

        holder.btnAccept.setOnClickListener(v -> listener.onAccept(order));
        holder.btnDecline.setOnClickListener(v -> listener.onDecline(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderId, txtOrderStatus;
        RecyclerView rvFoodList;
        Button btnAccept, btnDecline;
        LinearLayout orderActionLayout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            rvFoodList = itemView.findViewById(R.id.rvFoodList);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            orderActionLayout = itemView.findViewById(R.id.orderActionLayout);
        }
    }
}