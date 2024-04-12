package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershopapp.Adapters.ServicesLinearAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Service;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class ServicesFragment extends Fragment {

    TextView selectedServiceTV;
    RecyclerView servicesRV;
    Button nextBtn;
    Button backBtn;
    LinearLayoutManager layoutManager;
    ServicesLinearAdapter adapter;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        selectedServiceTV = view.findViewById(R.id.selectedServiceTV);
        nextBtn = view.findViewById(R.id.button4);
        backBtn = view.findViewById(R.id.button3);
        layoutManager = new LinearLayoutManager(getContext());
        servicesRV = view.findViewById(R.id.servicesLinearRV);
        servicesRV.setLayoutManager(layoutManager);
        servicesRV.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Service> serviceArrayList = new ArrayList<>();

        FirebaseManager.getInstance().fetchServicesList(getContext(), serviceArrayList, new FirebaseManager.onServicesFetchedListener() {
            @Override
            public void onServicesFetched(ArrayList<Service> services) {
                adapter = new ServicesLinearAdapter(services);
                servicesRV.setAdapter(adapter);
            }
        });

        backBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_servicesFragment_to_calenderFragment));

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedService = selectedServiceTV.getText().toString();

                if(selectedService.equals("Service Type")) {
                    Toast.makeText(getContext(), "Select Service Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle = getArguments();
                assert bundle != null;
                bundle.putString("selected service" , selectedService);
                Navigation.findNavController(view).navigate(R.id.action_servicesFragment_to_bookingFragment , bundle);
            }
        });
        return view;
    }

    public void updateSelectedService(Service selectedService2) {
        selectedServiceTV.setText(selectedService2.getServiceName());
    }
}