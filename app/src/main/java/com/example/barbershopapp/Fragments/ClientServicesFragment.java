package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barbershopapp.Adapters.ServicesAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Service;
import com.example.barbershopapp.R;

import java.util.ArrayList;


public class ClientServicesFragment extends Fragment {

    RecyclerView servicesRV;
    ServicesAdapter adapter;

    public ClientServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_services, container, false);

        servicesRV = view.findViewById(R.id.newServicesRV);
        servicesRV.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Service> servicesArrayList = new ArrayList<>();

        FirebaseManager.getInstance().fetchServicesList(getContext(), servicesArrayList, services -> {
            adapter = new ServicesAdapter(services);
            servicesRV.setAdapter(adapter);
        });

        return view;
    }
}