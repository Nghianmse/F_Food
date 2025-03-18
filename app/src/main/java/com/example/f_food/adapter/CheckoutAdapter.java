package com.example.f_food.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.entity.CartItem;
import com.example.f_food.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private List<CartItem> cartItems;

    public CheckoutAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.txtProductName.setText(item.getProduct().getName());
        holder.txtProductPrice.setText(String.format("%.2f x %d", item.getProduct().getPrice(), item.getQuantity()));
        Picasso.get().load(item.getProduct().getImageUrl()).into(holder.imgProduct);
        holder.btnGroup.setVisibility(View.GONE);
        holder.remove.setVisibility(View.GONE);
        holder.Checkbox.setVisibility(View.GONE);
        holder.layoutItemCart.setBackgroundColor(Color.WHITE);

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice;
        LinearLayout btnGroup, layoutItemCart;
        ImageView imgProduct;
        ImageView remove;
        CheckBox Checkbox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnGroup = itemView.findViewById(R.id.btnGroup);
            remove = itemView.findViewById(R.id.btnRemove);
            Checkbox = itemView.findViewById(R.id.chkSelectItem);
            layoutItemCart = itemView.findViewById(R.id.layoutItemCart);
        }
    }
}
