package com.example.f_food.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.f_food.repository.RestaurantRepository;

import java.util.ArrayList;
import java.util.List;

public class UnpaidOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrdersDisplayAdapter adapter;
    private RestaurantRoomDatabase db;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = RestaurantRoomDatabase.getInstance(getContext());
        loadUnpaidOrders();

        return view;
    }
    private void loadUnpaidOrders() {
        RestaurantRepository restaurantRepository = new RestaurantRepository(requireContext());
        int uid = getLoggedInUserId();
        int rid = restaurantRepository.getRestaurantByUserId(uid).getRestaurantId();

        List<Order> allOrders = db.orderDAO().getOrdersByRestaurantId(rid);
        List<Order> unpaidOrders = new ArrayList<>();

        for (Order order : allOrders) {
            String status = order.getOrderStatus();
            if (!status.equals("Delivered") && !status.equals("Cancelled") && !status.equals("Pending")) {
                unpaidOrders.add(order);
            }
        }

        adapter = new OrdersDisplayAdapter(requireContext(), unpaidOrders);
        recyclerView.setAdapter(adapter);
    }

    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return preferences.getInt("userId", -1);
    }

}
