package com.example.f_food.Screen.features_customer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Adapter.CartAdapter;
import com.example.f_food.DAO.CartManager;
import com.example.f_food.R;

public class activity_cart extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
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
        recyclerCart = findViewById(R.id.recyclerCart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter = new CartAdapter(this, CartManager.getInstance().getCartItems());
        recyclerCart.setAdapter(cartAdapter);
    }
}