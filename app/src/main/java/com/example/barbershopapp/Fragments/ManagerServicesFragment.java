package com.example.barbershopapp.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.barbershopapp.Adapters.ServicesAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Service;
import com.example.barbershopapp.R;
import java.util.ArrayList;


public class ManagerServicesFragment extends Fragment {

    RecyclerView servicesRV;
    Button addServiceBtn;
    Button deleteServiceBtn;
    ServicesAdapter adapter;

    public ManagerServicesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_manager_services, container, false);

        addServiceBtn = view.findViewById(R.id.addServiceBtn);
        deleteServiceBtn = view.findViewById(R.id.deleteServiceBtn);
        servicesRV = view.findViewById(R.id.managerServicesRV);
        servicesRV.setItemAnimator(new DefaultItemAnimator());
        ArrayList<Service> servicesList = new ArrayList<>();

        FirebaseManager.getInstance().fetchServicesList(getContext(), servicesList, new FirebaseManager.onServicesFetchedListener() {
            @Override
            public void onServicesFetched(ArrayList<Service> services) {
                adapter = new ServicesAdapter(services);
                servicesRV.setAdapter(adapter);
            }
        });

        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddServiceDialog();
            }
        });

        deleteServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteServiceDialog();
            }
        });
        return view;
    }

    private void showAddServiceDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Service");

        // Set up the input
        EditText serviceNameInput = new EditText(getContext());
        serviceNameInput.setHint("Service Name");
        EditText servicePriceInput = new EditText(getContext());
        servicePriceInput.setHint("Service Price");

        // Set the input view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(48, 0, 48, 0);
        serviceNameInput.setLayoutParams(layoutParams);
        servicePriceInput.setLayoutParams(layoutParams);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(serviceNameInput);
        layout.addView(servicePriceInput);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String serviceName = serviceNameInput.getText().toString().trim();
                String servicePrice = servicePriceInput.getText().toString().trim();
                FirebaseManager.getInstance().addService(getContext() ,serviceName, servicePrice);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Show the dialog
        builder.show();
    }

    private void showDeleteServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete Service");

        // Set up the input
        EditText serviceNameInput = new EditText(getContext());
        serviceNameInput.setHint("Service Name");

        // Set the input view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(48, 0, 48, 0);
        serviceNameInput.setLayoutParams(layoutParams);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(serviceNameInput);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String serviceName = serviceNameInput.getText().toString().trim();
                FirebaseManager.getInstance().deleteService(getContext() , serviceName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Show the dialog
        builder.show();
    }
}
