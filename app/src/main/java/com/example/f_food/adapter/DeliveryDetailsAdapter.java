package com.example.f_food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.entity.Food;
import com.example.f_food.R;
import com.example.f_food.entity.OrderDetail;
import com.example.f_food.repository.FoodRepository;

import java.util.List;
public class DeliveryDetailsAdapter extends RecyclerView.Adapter<FoodAcceptShippingAdapter.FoodViewHolder> {
    private List<OrderDetail> orderDetailList;
    private FoodRepository foodRepository;

    public DeliveryDetailsAdapter(Context context, List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
        this.foodRepository = new FoodRepository(context); // Khởi tạo để tra cứu tên món ăn
    }

    @NonNull
    @Override
    public FoodAcceptShippingAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_details, parent, false);
        return new FoodAcceptShippingAdapter.FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAcceptShippingAdapter.FoodViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);

        // Lấy thông tin món ăn từ foodId
        Food food = foodRepository.getFoodById(orderDetail.getFoodId());

        if (food != null) {
            holder.foodName.setText(food.getName());
            holder.foodPrice.setText(String.format(": %,.2f $", food.getPrice()) + " x " + orderDetail.getQuantity());
        } else {
            holder.foodName.setText("Món ăn không tồn tại");
            holder.foodPrice.setText("N/A");
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailList != null ? orderDetailList.size() : 0;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
        }
    }

}
