package com.example.macbookretina.eventnow;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by MacbookRetina on 4/11/17.
 */

public class GoogleMapRender extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    double lattitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lattitude = getIntent().getExtras().getDouble("lattitude");
        longitude = getIntent().getExtras().getDouble("longitude");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        map = googleMap;
        setUpMap();
        setMarker(lattitude,longitude);
    }

    public void setUpMap(){

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    void setMarker(double lat, double lng)
    {
        LatLng location = new LatLng(lat,lng);
        map.addMarker(new MarkerOptions().position(location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,13.5f));
    }

}
