package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barbershopapp.Adapters.ClientsAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class ClientsFragment extends Fragment {

    RecyclerView clientsRV;
    LinearLayoutManager layoutManager;
    ClientsAdapter adapter;

    public ClientsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        clientsRV = view.findViewById(R.id.clientsRV);
        layoutManager = new LinearLayoutManager(getContext());
        clientsRV.setLayoutManager(layoutManager);
        clientsRV.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Client> clients = new ArrayList<>();

        FirebaseManager.getInstance().fetchClientsList(getContext(), clients, new FirebaseManager.onClientsListFetchedListener() {
            @Override
            public void onClientsListFetched(ArrayList<Client> clientsList) {
                adapter = new ClientsAdapter(clientsList);
                clientsRV.setAdapter(adapter);
            }
        });

        return view;
    }
}