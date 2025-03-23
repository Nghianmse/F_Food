package com.example.f_food.screen.features_customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.f_food.R;
import com.example.f_food.screen.authentication_authorization.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ManageAddress extends AppCompatActivity {
    private EditText etAddress, etDetailAddress;
    private Button btnComplete, btnCurrentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyAR7MKPVaGnaIGxUDT5HE4CZ50AXiad_o0"; // Thay thế bằng API Key của bạn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Kiểm tra nếu người dùng chưa đăng nhập
        if (!isUserLoggedIn()) {
            showAlertDialog("Bạn chưa đăng nhập, bạn vui lòng đăng nhập để thao tác.");
            return;
        }
        setContentView(R.layout.activity_manage_address);

        etAddress = findViewById(R.id.etAddress);
        etDetailAddress = findViewById(R.id.etDetailAddress);
        btnComplete = findViewById(R.id.btnComplete);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        btnCurrentLocation.setOnClickListener(v -> requestNewLocation());
        btnComplete.setOnClickListener(v -> saveAddress());
        ImageButton homeIcon = findViewById(R.id.homeIcon);
        homeIcon.setOnClickListener(v -> {
            // Start HomeStart Activity when the home icon is clicked
            Intent intent = new Intent(ManageAddress.this, HomeStart.class);
            startActivity(intent);
            finish(); // Optionally finish the current activity if you don't want to return to it
        });
    }

    // Kiểm tra quyền truy cập vị trí
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Yêu cầu quyền truy cập vị trí
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Lấy vị trí hiện tại
    @SuppressLint("MissingPermission")
    private void requestNewLocation() {
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Không có kết nối Internet", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        fetchAddressFromLatLng(latitude, longitude);
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi lấy vị trí: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Sử dụng Google Maps Geocoding API để lấy địa chỉ từ tọa độ
    private void fetchAddressFromLatLng(double latitude, double longitude) {
        new Thread(() -> {
            try {
                String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                        latitude + "," + longitude + "&key=" + GOOGLE_MAPS_API_KEY;

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");

                if (results.length() > 0) {
                    String address = results.getJSONObject(0).getString("formatted_address");
                    runOnUiThread(() -> etAddress.setText(address));
                } else {
                    runOnUiThread(() -> Toast.makeText(ManageAddress.this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ManageAddress.this, "Lỗi khi truy vấn địa chỉ", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    // Kiểm tra kết nối Internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Lưu địa chỉ
    private void saveAddress() {
        String address = etAddress.getText().toString();
        String detailAddress = etDetailAddress.getText().toString();

        if (address.isEmpty() || detailAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Lưu địa chỉ thành công!", Toast.LENGTH_SHORT).show();
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1); // Sử dụng PreferenceManager thay vì getSharedPreferences
        return userId != -1;
    }

    // Hiển thị hộp thoại thông báo và chuyển sang màn hình đăng nhập
    private void showAlertDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(ManageAddress.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestNewLocation();
            } else {
                Toast.makeText(this, "Bạn chưa cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
