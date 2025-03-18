package com.example.f_food.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.dao.FoodWithOrder;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.dao.ReviewDAO;
import com.example.f_food.entity.Order;
import com.example.f_food.entity.Review;
import com.example.f_food.R;
import com.example.f_food.repository.OrderRepository;
import com.example.f_food.screen.features_customer.OrderHistoryDetail;
import com.example.f_food.screen.features_customer.ReviewAndRating;
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
        int food_id = 0;
        OrderRepository orderRepository = new OrderRepository(context);
        List<FoodWithOrder> foodWithOrderList = orderRepository.getFoodNamesByOrderId(order.getOrderId());
        List<FoodWithOrder> image = orderRepository.getImageByOrderId(order.getOrderId());
        for (FoodWithOrder p: foodWithOrderList
             ) {
            if(p.order_id == order.getOrderId()) {
                food_id = p.food_id;
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
        holder.status.setText("Trạng thái: " + order.getOrderStatus());

        if ("Delivered".equals(order.getOrderStatus())) {
            holder.btnReview.setVisibility(View.VISIBLE);
        } else {
            holder.btnReview.setVisibility(View.GONE);
        }


        // Xử lý sự kiện click nút đánh giá (nếu cần)
        int finalFood_id = food_id;
        holder.btnReview.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewAndRating.class);
            intent.putExtra("order_id", order.getOrderId());
            intent.putExtra("food_id", finalFood_id);
            intent.putExtra("food_name", holder.tvFoodName.getText().toString()); // Truyền tên món ăn
            intent.putExtra("food_image", image.get(0).image_url);
            intent.putExtra("restaurant_id", order.getRestaurantId());
            intent.putExtra("user_id", order.getUserId());
            // Kiểm tra xem người dùng đã đánh giá món ăn này chưa
            ReviewDAO reviewDAO = RestaurantRoomDatabase.getInstance(context).reviewDAO();
            Review existingReview = reviewDAO.getReviewByRestaurantAndFoodName(order.getRestaurantId(), holder.tvFoodName.getText().toString());

            if (existingReview != null) {
                // Nếu đã có đánh giá, hiển thị AlertDialog
                new AlertDialog.Builder(context)
                        .setTitle("Đã đánh giá")
                        .setMessage("Bạn đã đánh giá sản phẩm này rồi. Bạn không thể đánh giá lại.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                // Nếu chưa có đánh giá, mở màn hình ReviewAndRating
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderHistoryDetail.class);
            intent.putExtra("food_name", holder.tvFoodName.getText().toString()); // Truyền tên món ăn
            intent.putExtra("food_image", image.get(0).image_url); // Truyền hình ảnh món ăn
            intent.putExtra("food_id", finalFood_id); // Truyền food_id
            intent.putExtra("restaurant_id", order.getRestaurantId()); // Truyền restaurant_id
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView  totalPrice, paymentMethod, tvFoodName, status;
        ImageView ivFood;
        Button btnReview;

        public OrderViewHolder(View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.foodName);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            ivFood = itemView.findViewById(R.id.ivFood);
            status = itemView.findViewById(R.id.status);
            btnReview = itemView.findViewById(R.id.btnReview);
        }
    }
}
