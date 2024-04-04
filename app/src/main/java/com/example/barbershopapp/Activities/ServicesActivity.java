package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.barbershopapp.Adapters.AppointmentsAdapter;
import com.example.barbershopapp.Adapters.ServicesAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Service;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity {

    private ArrayList<Service> serviceArrayList;
    RecyclerView servicesRV;
    ServicesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        servicesRV = findViewById(R.id.servicesRV);
        servicesRV.setItemAnimator(new DefaultItemAnimator());

        serviceArrayList = new ArrayList<Service>();

        FirebaseManager.getInstance().fetchServicesList(ServicesActivity.this, serviceArrayList, new FirebaseManager.onServicesFetchedListener() {
            @Override
            public void onServicesFetched(ArrayList<Service> services) {
                adapter = new ServicesAdapter(services);
                servicesRV.setAdapter(adapter);
            }
        });
    }
}