package com.example.f_food.screen.features_customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.CartAdapter;
import com.example.f_food.dao.CartManager;
import com.example.f_food.entity.CartItem;
import com.example.f_food.R;

import java.util.ArrayList;

public class activity_cart extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private Button btnBuy;
    double discount, totalPrice;
    private TextView txtSubtotal, txtTotal;
    private TextView txtDiscount;

    int rid;

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
        hideSystemUI(); // Ẩn ngay khi khởi chạy

        // Đảm bảo hệ thống vẫn ẩn khi người dùng chạm vào màn hình
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                visibility -> hideSystemUI()
        );
        // Ánh xạ View
        recyclerCart = findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        txtDiscount = findViewById(R.id.txtDiscount);
        txtDiscount.setText("Giảm giá : 5%");

        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTotal = findViewById(R.id.txtTotal);
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
        totalPrice = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            if (item.isSelected()) {  // Chỉ tính sản phẩm được chọn
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
        }
        discount =  totalPrice * 0.05;
        txtSubtotal.setText("Total: " + totalPrice + " VNĐ");
        txtTotal.setText("Total: " + (totalPrice - discount));
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

        // Kiểm tra nếu có nhiều hơn 1 nhà hàng
        int firstRestaurantId = selectedItems.get(0).getProduct().getRestaurantId();
        for (CartItem item : selectedItems) {
            if (item.getProduct().getRestaurantId() != firstRestaurantId) {
                showAlert("Lưu ý", "Bạn chỉ có thể đặt món từ một nhà hàng trong một đơn hàng!");
                return;
            }
            rid = item.getProduct().getRestaurantId();
        }



        // Nếu hợp lệ, chuyển sang màn hình thanh toán
        Intent intent = new Intent(activity_cart.this, activity_checkout.class);
        intent.putParcelableArrayListExtra("selectedItems", selectedItems);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("discount", discount);
        intent.putExtra("rId", rid);
        startActivity(intent);
    }


    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

}
