package com.cesar.alseademo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.cesar.alseademo.adapter.DatosSismoAdapter;
import com.cesar.alseademo.interfaces.AdapterSelected;
import com.cesar.alseademo.model.DatosSismo;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements AdapterSelected {
    private RecyclerView recyclerView;
    private DatosSismoAdapter datosSismoAdapter;

    private ArrayList<DatosSismo> datosSismoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Resultados");
        loadExtras();
        setupRecycler();
    }

    private void loadExtras() {
        Intent intent = getIntent();
        if(intent!=null) {
            datosSismoArrayList = intent.getParcelableArrayListExtra("datosSismos");
        }
    }

    private void setupRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(datosSismoArrayList!=null) {
            datosSismoAdapter = new DatosSismoAdapter(datosSismoArrayList, getApplicationContext());
            datosSismoAdapter.adapterSelected = this;
            recyclerView.setAdapter(datosSismoAdapter);
        }

    }

    @Override
    public void selectedItem(int position) {
        callMapsFragment(position);
    }

    private void callMapsFragment(int position){
        Intent intent = new Intent(getApplicationContext(), MapsFragment.class);
        intent.putExtra("sismo", datosSismoArrayList.get(position));
        startActivity(intent);
    }
}