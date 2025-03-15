package com.example.f_food.Screen.features_customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Adapter.CartAdapter;
import com.example.f_food.DAO.CartManager;
import com.example.f_food.Entity.CartItem;
import com.example.f_food.R;

import java.util.ArrayList;
import java.util.List;

public class activity_cart extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private Button btnBuy;
    private TextView txtSubtotal;
    private TextView txtDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activityCart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View
        recyclerCart = findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        txtDiscount = findViewById(R.id.txtDiscount);
        txtDiscount.setText("5%");

        txtSubtotal = findViewById(R.id.txtSubtotal);
        btnBuy = findViewById(R.id.btnBuyNow);

        // Lấy danh sách sản phẩm trong giỏ hàng
        var cartItems = CartManager.getInstance().getCartItems();

        // Tạo Adapter và truyền callback để cập nhật tổng giá tiền khi thay đổi
        cartAdapter = new CartAdapter(this, cartItems, this::updateTotalPrice);

        recyclerCart.setAdapter(cartAdapter);

        // Hiển thị tổng tiền ban đầu
        updateTotalPrice();

        // Xử lý sự kiện nút "Mua ngay"
        btnBuy.setOnClickListener(v -> proceedToCheckout());
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {  // Chỉ tính sản phẩm được chọn
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        txtSubtotal.setText("Total: " + totalPrice + " VNĐ");
    }

    private void proceedToCheckout() {
        ArrayList<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        if (selectedItems.isEmpty()) {
            showAlert("Lưu ý", "Bạn chưa chọn mua sản phẩm nào!");
            return;
        }

        Intent intent = new Intent(activity_cart.this, activity_checkout.class);
        intent.putParcelableArrayListExtra("selectedItems", selectedItems);
        startActivity(intent);
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
