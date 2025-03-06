package com.example.f_food.Screen.order_processing;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.Adapter.PendingOrderAdapter;
import com.example.f_food.Entity.Order;
import com.example.f_food.Entity.Restaurant;
import com.example.f_food.R;
import com.example.f_food.Repository.OrderRepository;
import com.example.f_food.Repository.RestaurantRepository;

import java.util.List;

public class PendingOrder extends AppCompatActivity {
    private RecyclerView rvPendingOrders;
    private PendingOrderAdapter adapter;
    private OrderRepository orderRepository;
    private RestaurantRepository restaurantRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);

        rvPendingOrders = findViewById(R.id.rvPendingOrders);
        rvPendingOrders.setLayoutManager(new LinearLayoutManager(this));

        orderRepository = new OrderRepository(this);
        restaurantRepository = new RestaurantRepository(this);
        List<Order> orders = orderRepository.getAllOrders();
//        List<Restaurant> restaurants = restaurantRepository.getAllRestaurants();

        adapter = new PendingOrderAdapter(this, orders, order ->
                Toast.makeText(PendingOrder.this, "Order ID: " + order.getOrderId(), Toast.LENGTH_SHORT).show()
        );



        rvPendingOrders.setAdapter(adapter);
    }
}
