package com.example.f_food.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.R;
import com.example.f_food.Entity.Food;
import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
    private List<Food> foodList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Food food);
    }

    public FoodListAdapter(List<Food> foodList, OnItemClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.bind(food, listener);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName, productDescription, productPrice, productStockStatus;
        private ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productStockStatus = itemView.findViewById(R.id.productStockStatus);
            productImage = itemView.findViewById(R.id.productImage);
        }

        public void bind(final Food food, final OnItemClickListener listener) {
            productName.setText(food.getName());
            productDescription.setText(food.getDescription());
            productPrice.setText("$" + food.getPrice());
            productStockStatus.setText(food.getStockStatus());
            //productImage.setImageResource(food.getImageUrl());

            itemView.setOnClickListener(v -> listener.onItemClick(food));
        }
    }
}
