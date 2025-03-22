package com.example.f_food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.R;
import com.example.f_food.entity.Order;
import java.util.List;

public class OrdersRestaurantAdapter extends RecyclerView.Adapter<OrdersRestaurantAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onAcceptClick(Order order);
        void onRejectClick(Order order);
        void onItemClick(Order order); // Thêm hàm xử lý click toàn item
    }

    public OrdersRestaurantAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText(String.format("Order ID: %s", order.getOrderId()));
        holder.tvPrice.setText(String.format("Price: %.2f VNĐ", order.getTotalPrice()));

        switch (order.getOrderStatus()) {
            case "Pending":
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.tvStatus.setVisibility(View.GONE);
                holder.btnAccept.setOnClickListener(v -> listener.onAcceptClick(order));
                holder.btnReject.setOnClickListener(v -> listener.onRejectClick(order));
                break;
            case "Cancelled":
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("❌ Đã hủy");
                break;
            case "Delivering":
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("Đang vận chuyển");
                break;
            case "Delivered":
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("✅ Đã hoàn thành");
                break;
            case "Preparing":
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.tvStatus.setText("⏰ Đơn hàng chờ được vận chuyển");
                break;
            default:
                holder.btnAccept.setVisibility(View.GONE);
                holder.btnReject.setVisibility(View.GONE);
                holder.tvStatus.setVisibility(View.GONE);
                break;
        }

        // Gán sự kiện click vào toàn bộ layout item
        holder.orderDetails.setOnClickListener(v -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvPrice, tvStatus;
        Button btnAccept, btnReject;
        LinearLayout orderDetails;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
            orderDetails = itemView.findViewById(R.id.orderDetails);
        }
    }
}