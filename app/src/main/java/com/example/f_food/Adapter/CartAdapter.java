package com.example.f_food.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.CartItem;
import com.example.f_food.Entity.Food;
import com.example.f_food.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private DecimalFormat formatter = new DecimalFormat("#,###"); // Thêm dòng này
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.txtProductName.setText(item.getProduct().getName());
        holder.txtProductPrice.setText(formatter.format(item.getProduct().getPrice()) + " VNĐ");
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyDataSetChanged();
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyDataSetChanged();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtQuantity;
        Button btnIncrease, btnDecrease;
        ImageView btnRemove;
        ImageView imgProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
        public void bind(final Food food, final FoodListAdapter.OnItemClickListener listener) {
            txtProductName.setText(food.getName());
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            txtProductPrice.setText(formatter.format(food.getPrice()) + " VNĐ");
            if(food.getImageUrl() != null && !food.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(food.getImageUrl())
                        .resize(500, 500)
                        .centerCrop()
                        .into(imgProduct);
            }
            itemView.setOnClickListener(v -> listener.onItemClick(food.getFoodId()));
        }
    }
}
