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

import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.screen.order_processing.AcceptShippingOrder;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {
    private final List<Order> orderList;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final Context context;
    private final OnOrderClickListener listener; // Lưu listener

    // Interface xử lý sự kiện khi nhấn vào một đơn hàng
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    // Constructor nhận Context, danh sách Order và listener
    public PendingOrderAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
        this.restaurantRepository = new RestaurantRepository(context);
        this.orderRepository = new OrderRepository(context);
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

        // Kiểm tra order có hợp lệ không
        if (!(order instanceof Order)) {
            return;
        }

        // Lấy thông tin nhà hàng từ database dựa trên restaurantId
        Restaurant restaurant = restaurantRepository.getRestaurantById(order.getRestaurantId());
        String restaurantAddress = (restaurant != null) ? restaurant.getAddress() : "Unknown Address";

        // Hiển thị dữ liệu lên ViewHolder
        holder.tvRestaurantAddress.setText(restaurantAddress);
        holder.tvDeliveryAddress.setText("User Address"); // Dữ liệu thực tế từ DB

        // Khi nhấn nút Details -> Gọi listener và chuyển sang AcceptShippingOrder
        holder.btnDetails.setOnClickListener(v -> {
            listener.onOrderClick(order);

            Intent intent = new Intent(context, AcceptShippingOrder.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("deliveryAddress", "User's Address"); // Lấy từ DB nếu có
            intent.putExtra("deliveryTime", order.getCreatedAt());
            intent.putExtra("foodOrder", "Food details here"); // Lấy từ DB nếu có
            intent.putExtra("cost", order.getTotalPrice());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRestaurantAddress, tvDeliveryAddress;
        Button btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRestaurantAddress = itemView.findViewById(R.id.tvRestaurantAddress);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
