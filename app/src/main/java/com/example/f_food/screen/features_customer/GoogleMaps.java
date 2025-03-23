package com.example.f_food.screen.features_customer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.f_food.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMaps extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_google_maps);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Tọa độ của 2G7G+M2 Thạch Thất, Hanoi, Vietnam
        LatLng thachThat = new LatLng(21.0031, 105.5321);
        LatLng thachThat1 = new LatLng(21.0031, 105.5321);

        // Thêm marker và di chuyển camera đến vị trí
        mMap.addMarker(new MarkerOptions().position(thachThat).title("Thạch Thất, Hà Nội"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thachThat, 15));
    }
}
