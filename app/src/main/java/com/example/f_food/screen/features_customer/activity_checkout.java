package com.example.f_food.screen.features_customer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.CheckoutAdapter;
import com.example.f_food.entity.CartItem;
import com.example.f_food.R;
import java.util.ArrayList;

public class activity_checkout extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckoutAdapter checkoutAdapter;
    private TextView txtTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_checkout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        hideSystemUI(); // Ẩn ngay khi khởi chạy

        // Đảm bảo hệ thống vẫn ẩn khi người dùng chạm vào màn hình
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                visibility -> hideSystemUI()
        );
        recyclerView = findViewById(R.id.recyclerCheckout);
        txtTotalPrice = findViewById(R.id.totalPrice);

        // Lấy danh sách sản phẩm từ Intent
        ArrayList<CartItem> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");

        if (selectedItems != null) {
            checkoutAdapter = new CheckoutAdapter(selectedItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(checkoutAdapter);

            // Tính tổng tiền
            double totalPrice = 0;
            for (CartItem item : selectedItems) {
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
            txtTotalPrice.setText(String.format("Total: Rp %.2f", totalPrice));
        }
    }
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}
