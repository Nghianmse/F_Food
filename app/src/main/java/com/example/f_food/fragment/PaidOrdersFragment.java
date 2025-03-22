package com.example.f_food.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.f_food.R;
import com.example.f_food.adapter.OrdersDisplayAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Order;
import java.util.ArrayList;
import java.util.List;

public class PaidOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrdersDisplayAdapter adapter;
    private RestaurantRoomDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = RestaurantRoomDatabase.getInstance(getContext());
        loadPaidOrders();

        return view;
    }

    private void loadPaidOrders() {
        List<Order> allOrders = db.orderDAO().getAllOrders();
        List<Order> paidOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if ("Delivered".equals(order.getOrderStatus())) {
                paidOrders.add(order);
            }
        }

        adapter = new OrdersDisplayAdapter(getContext(), paidOrders);
        recyclerView.setAdapter(adapter);
    }
}
