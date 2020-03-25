package com.example.mapmanager;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_maps_fr_map);
        mapFragment.getMapAsync(this);

        Button btnClearMap = findViewById(R.id.activity_maps_btn_clear_map);
        btnClearMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.getUiSettings().setZoomControlsEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
                public void onMapClick(LatLng latLng) {
                map.addMarker(new MarkerOptions().position(latLng).title("New Location"));
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
        map.setOnInfoWindowClickListener(this);

        // Add a marker in Saltillo and move the camera
        LatLng saltillo = new LatLng(25.4232101, -101.0053024);
        map.addMarker(new MarkerOptions().position(saltillo).title("Saltillo Coahuila, Mexico"));
        map.moveCamera(CameraUpdateFactory.newLatLng(saltillo));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info!", Toast.LENGTH_SHORT).show();
    }
}
