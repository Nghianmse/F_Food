package com.example.f_food.Screen.features_customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.f_food.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ManageAddress extends AppCompatActivity {
    private EditText etAddress, etDetailAddress;
    private Button btnComplete, btnCurrentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_address);

        etAddress = findViewById(R.id.etAddress);
        etDetailAddress = findViewById(R.id.etDetailAddress);
        btnComplete = findViewById(R.id.btnComplete);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnCurrentLocation.setOnClickListener(v -> requestNewLocation());

        btnComplete.setOnClickListener(v -> saveAddress());
    }

    // ✅ Yêu cầu quyền truy cập vị trí
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // ✅ Yêu cầu người dùng cấp quyền nếu chưa có
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    // ✅ Lấy vị trí chính xác nhất, không lấy cache cũ
    @SuppressLint("MissingPermission")
    private void requestNewLocation() {
        if (!checkPermissions()) {
            requestPermissions();
            return;
        }

        locationRequest = new LocationRequest.Builder(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(ManageAddress.this, "Không thể lấy vị trí!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        convertLatLonToAddress(latitude, longitude);
                        fusedLocationClient.removeLocationUpdates(locationCallback); // Ngừng cập nhật sau khi lấy xong vị trí
                    }
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    // ✅ Chuyển tọa độ thành địa chỉ chi tiết
    private void convertLatLonToAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                etAddress.setText(addressText);
            } else {
                Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi truy vấn địa chỉ", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAddress() {
        String address = etAddress.getText().toString();
        String detailAddress = etDetailAddress.getText().toString();

        if (address.isEmpty() || detailAddress.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Lưu địa chỉ thành công!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestNewLocation();
            } else {
                Toast.makeText(this, "Bạn chưa cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
