package com.example.f_food.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.f_food.R;
import com.example.f_food.dao.FoodWithOrder;

import java.util.List;

public class FoodWithOrderAdapter extends RecyclerView.Adapter<FoodWithOrderAdapter.FoodViewHolder> {
    private List<FoodWithOrder> foodList;

    public FoodWithOrderAdapter(List<FoodWithOrder> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodWithOrder food = foodList.get(position);
        holder.txtFoodName.setText(food.food_name);
        Glide.with(holder.itemView.getContext()).load(food.image_url).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView txtFoodName;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.foodImage);
            txtFoodName = itemView.findViewById(R.id.txtFoodName);
        }
    }
}



