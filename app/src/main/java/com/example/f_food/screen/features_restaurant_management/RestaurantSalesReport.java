package com.example.f_food.screen.features_restaurant_management;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.f_food.R;
import com.example.f_food.adapter.ViewPagerAdapter;
import com.example.f_food.dao.RestaurantRoomDatabase;
import com.example.f_food.entity.Order;
import com.example.f_food.repository.RestaurantRepository;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RestaurantSalesReport extends AppCompatActivity {

    private TextView tvPaid, tvUnpaid, tvMonthlyTotal, tvAllTimeTotal;
    private RestaurantRoomDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_sales_report);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurantSalesReportActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvPaid = findViewById(R.id.tvPaid);
        tvUnpaid = findViewById(R.id.tvUnpaid);
        tvMonthlyTotal = findViewById(R.id.tvMonthlyTotal);
        tvAllTimeTotal = findViewById(R.id.tvAllTimeTotal);

        db = RestaurantRoomDatabase.getInstance(this);
        loadReport();

        setupTabs();
    }

    private void loadReport() {
        RestaurantRepository restaurantRepository = new RestaurantRepository(this);
        int uid = getLoggedInUserId();
        int rid = restaurantRepository.getRestaurantByUserId(uid).getRestaurantId();

        List<Order> allOrders = db.orderDAO().getOrdersByRestaurantId(rid);

        double totalPaid = 0;
        double totalUnpaid = 0;
        double totalPaidThisMonth = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = cal.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String strMonthStart = sdf.format(monthStart);
        String strToday = sdf.format(new Date());

        for (Order order : allOrders) {
            switch (order.getOrderStatus()) {
                case "Delivered":
                    totalPaid += order.getTotalPrice();
                    if (order.getCreatedAt().compareTo(strMonthStart) >= 0 && order.getCreatedAt().compareTo(strToday) <= 0) {
                        totalPaidThisMonth += order.getTotalPrice();
                    }
                    break;
                case "Cancelled":
                    // Bỏ qua
                    break;
                case "Pending":
                    // Bỏ qua
                    break;
                default:
                    totalUnpaid += order.getTotalPrice();
                    break;
            }
        }

        tvPaid.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", totalPaid));
        tvUnpaid.setText(String.format(Locale.getDefault(), "%,.0f VNĐ", totalUnpaid));
        tvMonthlyTotal.setText(String.format("Tháng này (%s - %s): %,.0f VNĐ", strMonthStart, strToday, totalPaidThisMonth));
        tvAllTimeTotal.setText(String.format("Tổng cộng: %,.0f VNĐ", totalPaid));
    }

    private void setupTabs() {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) tab.setText("Chưa thanh toán");
            else tab.setText("Đã thanh toán");
        }).attach();
    }
    private int getLoggedInUserId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("userId", -1); // Trả về -1 nếu không tìm thấy userId
    }
}