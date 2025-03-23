package com.example.f_food.screen.features_customer;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.PolylineOptions;

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

        // Nhận intent
        Intent intent = getIntent();
        double originLat = intent.getDoubleExtra("origin_lat", 0.0);
        double originLng = intent.getDoubleExtra("origin_lng", 0.0);
        double destLat = intent.getDoubleExtra("dest_lat", 0.0);
        double destLng = intent.getDoubleExtra("dest_lng", 0.0);

        LatLng origin = new LatLng(originLat, originLng);
        LatLng destination = new LatLng(destLat, destLng);

        // Marker
        mMap.addMarker(new MarkerOptions().position(origin).title("Nhà hàng"));
        mMap.addMarker(new MarkerOptions().position(destination).title("Nơi giao hàng"));

        // Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 15));

        // Polyline (vẽ đường nối)
        mMap.addPolyline(new PolylineOptions()
                .add(origin, destination)
                .width(8)
                .color(Color.BLUE)
                .geodesic(true));
    }


}
