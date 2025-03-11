package com.example.f_food.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.DAO.FoodWithOrder;
import com.example.f_food.DAO.OrderDAO;
import com.example.f_food.Entity.Food;
import com.example.f_food.Entity.Order;
import com.example.f_food.Entity.User;
import com.example.f_food.R;
import com.example.f_food.Repository.FoodRepository;
import com.example.f_food.Repository.OrderRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;

    public OrderHistoryAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;

    }


    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        OrderRepository orderRepository = new OrderRepository(context);
        List<FoodWithOrder> foodWithOrderList = orderRepository.getFoodNamesByOrderId(order.getOrderId());
        List<FoodWithOrder> image = orderRepository.getImageByOrderId(order.getOrderId());
        for (FoodWithOrder p: foodWithOrderList
             ) {
            if(p.order_id == order.getOrderId()) {
                holder.tvFoodName.setText("Tên món: " + p.food_name);
            }

        }
        for (FoodWithOrder p: image
        ) {
            if(p.image_url != null && !p.image_url.isEmpty()) {
                Picasso.get()
                        .load(p.image_url)
                        .resize(500, 500)
                        .centerCrop()
                        .into(holder.ivFood);
            }

        }
        // Update as needed// Update as needed
        holder.totalPrice.setText("Tổng Tiền: " + order.getTotalPrice());
        holder.paymentMethod.setText("Thanh toán: " + order.getPaymentMethod());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, customerName, phoneNumber, totalPrice, paymentMethod, tvFoodName;
        ImageView ivFood;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            customerName = itemView.findViewById(R.id.customerName);
            tvFoodName = itemView.findViewById(R.id.foodName);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            ivFood = itemView.findViewById(R.id.ivFood);
        }
    }
}
