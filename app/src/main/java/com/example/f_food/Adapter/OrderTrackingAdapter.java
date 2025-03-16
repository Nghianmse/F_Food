package com.example.f_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.DAO.FoodWithOrder;
import com.example.f_food.Repository.OrderRepository;
import com.example.f_food.Entity.Order;
import com.example.f_food.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;

    public OrderTrackingAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_tracking_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        int food_id = 0;
        OrderRepository orderRepository = new OrderRepository(context);
        List<FoodWithOrder> foodWithOrderList = orderRepository.getFoodNamesByOrderId(order.getOrderId());
        List<FoodWithOrder> image = orderRepository.getImageByOrderId(order.getOrderId());

        // Get food name and ID
        for (FoodWithOrder p : foodWithOrderList) {
            if (p.order_id == order.getOrderId()) {
                food_id = p.food_id;
                holder.foodName.setText("Tên món: " + p.food_name);
            }
        }

        // Set image for food if it exists
        for (FoodWithOrder p : image) {
            if (p.image_url != null && !p.image_url.isEmpty()) {
                Picasso.get()
                        .load(p.image_url)
                        .resize(500, 500)
                        .centerCrop()
                        .into(holder.ivFood);
            }
        }

        // Set order details in the view
        holder.totalPrice.setText("Tổng Tiền: " + order.getTotalPrice());
        holder.status.setText("Trạng thái đơn hàng: " + order.getOrderStatus());
        holder.paymentMethod.setText("Thanh toán: " + order.getPaymentMethod());
        // Check the order status and set the appropriate text
        if ("Preparing".equals(order.getOrderStatus())) {
            holder.findingShipperText.setVisibility(View.VISIBLE);
        } else {
            holder.findingShipperText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView totalPrice, paymentMethod, foodName, status, findingShipperText  ;
        ImageView ivFood;

        public OrderViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            ivFood = itemView.findViewById(R.id.orderImage);
            status = itemView.findViewById(R.id.orderStatus);
            findingShipperText = itemView.findViewById(R.id.findingShipperText);
        }
    }
}
