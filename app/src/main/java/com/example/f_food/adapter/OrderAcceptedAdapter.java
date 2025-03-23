package com.example.f_food.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.R;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Restaurant;
import com.example.f_food.repository.RestaurantRepository;
import com.example.f_food.screen.order_processing.DeliveryStatusUpdate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAcceptedAdapter extends RecyclerView.Adapter<OrderAcceptedAdapter.ViewHolder> {
    private final List<Order> orderList;
    private final Context context;
    private final RestaurantRepository restaurantRepository;

    private final String userEmail, userPassword, userName, userPhone;
    public OrderAcceptedAdapter(Context context, List<Order> orderList,
                                String userEmail, String userPassword,
                                String userName, String userPhone) {
        this.context = context;
        this.orderList = orderList;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhone = userPhone;
        this.restaurantRepository = new RestaurantRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_accepted, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);


        holder.tvOrderId.setText("ID: " + order.getOrderId());
        String formattedDate = formatDateTime(order.getCreatedAt());
        holder.tvDate.setText(formattedDate);
        holder.imgStatus.setImageResource(R.drawable.ic_deli);

        Restaurant restaurant = restaurantRepository.getRestaurantById(order.getRestaurantId());
        String restaurantAddress = (restaurant != null) ? restaurant.getAddress() : "Unknown Address";

        holder.btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeliveryStatusUpdate.class);
            intent.putExtra("orderId", order.getOrderId());
            intent.putExtra("restaurantAddress", restaurantAddress);
            intent.putExtra("deliveryAddress", "cxcxcxc");
            intent.putExtra("deliveryTime", formattedDate);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate;
        ImageView imgTruck, imgCalendar, imgStatus;
        Button btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_date);
            imgTruck = itemView.findViewById(R.id.img_truck);
            imgCalendar = itemView.findViewById(R.id.img_calendar);
            imgStatus = itemView.findViewById(R.id.img_status);
            btnUpdate = itemView.findViewById(R.id.btn_update);
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
