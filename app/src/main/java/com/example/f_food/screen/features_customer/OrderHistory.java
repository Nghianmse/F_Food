package com.example.f_food.screen.features_customer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.f_food.adapter.OrderHistoryAdapter;
import com.example.f_food.entity.Order;
import com.example.f_food.R;
import com.example.f_food.dao.RestaurantRoomDatabase;
import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.orderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch orders with status "Delivered" or "Cancelled"
        orderList = RestaurantRoomDatabase.getInstance(this).orderDAO().getDeliveredOrCancelledOrders();
        orderAdapter = new OrderHistoryAdapter(orderList, this);
        recyclerView.setAdapter(orderAdapter);
    }
}
