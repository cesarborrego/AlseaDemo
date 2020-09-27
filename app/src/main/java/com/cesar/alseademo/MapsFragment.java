package com.cesar.alseademo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cesar.alseademo.model.DatosSismo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsFragment extends AppCompatActivity implements OnMapReadyCallback {
    private DatosSismo datosSismo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        setTitle("Ubicación del sismo");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadExtras();
    }

    private void loadExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            datosSismo = intent.getParcelableExtra("sismo");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        loadMarkers(datosSismo, googleMap);
    }

    private void loadMarkers(DatosSismo datosSismo, GoogleMap googleMap) {
        LatLng sydney = new LatLng(datosSismo.getLat(), datosSismo.getLon());
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(datosSismo.getTitulo()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}