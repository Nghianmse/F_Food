package com.example.f_food.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Entity.CartItem;
import com.example.f_food.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    private CartUpdateListener cartUpdateListener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartUpdateListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartUpdateListener = listener;
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

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.txtProductPrice.setText(formatter.format(item.getProduct().getPrice()) + " VNĐ");

        if (item.getProduct().getImageUrl() != null && !item.getProduct().getImageUrl().isEmpty()) {
            Picasso.get()
                    .load(item.getProduct().getImageUrl())
                    .resize(50, 50)
                    .centerCrop()
                    .into(holder.imgProduct);
        }
        holder.chkSelectItem.setOnCheckedChangeListener(null); // Ngăn chặn gọi lại khi cập nhật ViewHolder
        holder.chkSelectItem.setChecked(item.isSelected());

// Xử lý sự kiện CheckBox khi người dùng chọn/bỏ chọn sản phẩm
        holder.chkSelectItem.setOnClickListener(v -> {
            item.setSelected(holder.chkSelectItem.isChecked()); // Lưu trạng thái chọn của sản phẩm đó
            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }
        });


        // Cập nhật số lượng từ cartItems
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // Xử lý sự kiện nút giảm
        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();  // Lấy số lượng từ CartItem
            if (currentQuantity > 1) {
                item.setQuantity(currentQuantity - 1);  // Cập nhật số lượng trong CartItem
                cartItems.set(position, item);  // Cập nhật lại dữ liệu trong danh sách
                notifyItemChanged(position);  // Cập nhật lại RecyclerView
                if (cartUpdateListener != null) {
                    cartUpdateListener.onCartUpdated();
                }
            }
        });

        // Xử lý sự kiện nút tăng
        holder.btnIncrease.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();  // Lấy số lượng từ CartItem
            item.setQuantity(currentQuantity + 1);  // Cập nhật số lượng trong CartItem
            cartItems.set(position, item);  // Cập nhật lại dữ liệu trong danh sách
            notifyItemChanged(position);  // Cập nhật lại RecyclerView
            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }
        });

        // Xử lý sự kiện nút xóa
        holder.btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);  // Xóa sản phẩm khỏi danh sách
            notifyItemRemoved(position);  // Cập nhật lại RecyclerView
            notifyItemRangeChanged(position, cartItems.size());  // Cập nhật lại vị trí các item
            if (cartUpdateListener != null) {
                cartUpdateListener.onCartUpdated();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName, txtProductPrice, txtQuantity;
        ImageView imgProduct, btnRemove;
        MaterialButton btnDecrease, btnIncrease;
        CheckBox chkSelectItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            chkSelectItem = itemView.findViewById(R.id.chkSelectItem);
        }
    }
}
