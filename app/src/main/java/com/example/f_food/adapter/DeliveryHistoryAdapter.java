package com.example.f_food.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.Order;
import com.example.f_food.R;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.screen.order_processing.DeliveryDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeliveryHistoryAdapter extends RecyclerView.Adapter<DeliveryHistoryAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;

    private final RestaurantRepository restaurantRepository;
    public DeliveryHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.restaurantRepository = new RestaurantRepository(context);
    }

    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged(); // Cập nhật danh sách sau khi thay đổi
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_delivery_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("ID: " + order.getOrderId());
        String formattedDate = formatDateTime(order.getCreatedAt());
        holder.tvDate.setText(formattedDate);

//        // Kiểm tra trạng thái đơn hàng và đặt icon tương ứng
        String status = order.getOrderStatus().trim().toLowerCase();
        if (status.equals("delivered")) {
            holder.imgStatus.setImageResource(R.drawable.icsuccess);
        } else if (status.equals("cancelled")) {
            holder.imgStatus.setImageResource(R.drawable.icfail);
        } else {
            holder.imgStatus.setImageResource(R.drawable.iccheck); // Icon mặc định nếu không khớp
        }

        Restaurant restaurant = restaurantRepository.getRestaurantById(order.getRestaurantId());
        String restaurantAddress = (restaurant != null) ? restaurant.getAddress() : "Unknown Address";

        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeliveryDetails.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("deliveryAddress", "vcvcvcv");
            intent.putExtra("deliveryTime", formattedDate);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate;
        ImageView imgStatus;
        Button btnDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_date);
            imgStatus = itemView.findViewById(R.id.img_status);
            btnDetails = itemView.findViewById(R.id.btn_details);
        }
    }
    private String formatDateTime(String createdAt) {
        try {
            // Định dạng ban đầu của `createdAt` (ví dụ: "2025-03-10 14:30:15")
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Định dạng hiển thị mới (Chỉ ngày/tháng + giờ/phút)
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

            Date date = inputFormat.parse(createdAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return createdAt; // Nếu lỗi, hiển thị chuỗi gốc
        }
    }

}
