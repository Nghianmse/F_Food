package com.example.f_food.Screen.order_processing;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;
import android.widget.Button;

import com.example.f_food.DAO.OrderDetailDAO;
import com.example.f_food.Entity.Food;
import com.example.f_food.R;
import com.example.f_food.Adapter.FoodAcceptShippingAdapter;
import com.example.f_food.Repository.FoodRepository;
import com.example.f_food.Repository.OrderRepository;
import com.example.f_food.Repository.OrderDetailRepository;

import java.util.List;

public class AcceptShippingOrder extends AppCompatActivity {
    private TextView tvOrderId, tvRestaurantAddress, tvDeliveryAddress, tvDeliveryTime, tvCost;
    private RecyclerView foodRecyclerView;
    private Button acceptButton;
    private FoodAcceptShippingAdapter foodAdapter;

    private OrderRepository orderRepository;
    private FoodRepository foodRepository;
    private OrderDetailRepository orderDetailRepository;
    private OrderDetailDAO orderDetailDAO;

    private List<Food> foodList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_shipping_order);
        orderRepository = new OrderRepository(this);
        orderDetailRepository = new OrderDetailRepository(this);
        foodRepository = new FoodRepository(this);

        // Ánh xạ các thành phần giao diện
        tvOrderId = findViewById(R.id.orderId);
        tvRestaurantAddress = findViewById(R.id.restaurantAddress);
        tvDeliveryAddress = findViewById(R.id.deliveryAddress);
        tvDeliveryTime = findViewById(R.id.deliveryTime);
        tvCost = findViewById(R.id.foodCost);
        foodRecyclerView = findViewById(R.id.foodListAcceptShipping);
        acceptButton = findViewById(R.id.acceptButton);

        // Cấu hình RecyclerView
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ Intent
        int orderId = getIntent().getIntExtra("orderId", -1);
        String restaurantAddress = getIntent().getStringExtra("restaurantAddress");
        String deliveryAddress = getIntent().getStringExtra("deliveryAddress");
        String deliveryTime = getIntent().getStringExtra("deliveryTime");
        double cost = getIntent().getDoubleExtra("cost", 0.0);

        // Hiển thị dữ liệu đơn hàng
        tvOrderId.setText("Order ID: " + orderId);
        tvRestaurantAddress.setText("Restaurant: " + restaurantAddress);
        tvDeliveryAddress.setText("Delivery Address: " + deliveryAddress);
        tvDeliveryTime.setText("Delivery Time: " + deliveryTime);
        tvCost.setText("Total Cost: $" + cost);


        // Lấy danh sách món ăn từ Order ID
        foodList  = orderRepository.getFoodListByOrderId(orderId);

        if (foodList != null && !foodList.isEmpty()) {
            foodAdapter = new FoodAcceptShippingAdapter(foodList);
            foodRecyclerView.setAdapter(foodAdapter);
        } else {
            Log.d("AcceptShippingOrder", "Không có món ăn nào trong đơn hàng.");
        }
        acceptButton.setOnClickListener(v -> showConfirmationDialog(orderId));
    }
    // Hiển thị hộp thoại xác nhận
    // Hiển thị hộp thoại xác nhận
    private void showConfirmationDialog(int orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đơn hàng");
        builder.setMessage("Bạn có chắc chắn muốn chấp nhận đơn hàng #" + orderId + " không?");
        builder.setPositiveButton("Chấp nhận", (dialog, which) -> {
            // Gọi phương thức cập nhật trạng thái đơn hàng
            orderRepository.updateOrderStatus(orderId, "Delivering");
            Toast.makeText(AcceptShippingOrder.this, "Đơn hàng #" + orderId + " đã chuyển sang trạng thái 'public abstract  AddressDAO addressDAO();'!", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
