package com.example.f_food.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.Food;
import com.example.f_food.R;

import java.util.List;

public class FoodAcceptShippingAdapter extends RecyclerView.Adapter<FoodAcceptShippingAdapter.FoodViewHolder> {
    private List<Food> foodList;

    public FoodAcceptShippingAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_accept_shipping, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food foodItem = foodList.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodPrice.setText(String.format(": %,.2f $", foodItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
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
