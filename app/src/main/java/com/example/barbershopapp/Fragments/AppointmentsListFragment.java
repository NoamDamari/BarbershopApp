package com.example.barbershopapp.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.barbershopapp.Adapters.AppointmentsAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.R;
import java.util.ArrayList;



public class AppointmentsListFragment extends Fragment {

    RecyclerView appointmentsRV;
    LinearLayoutManager layoutManager;
    AppointmentsAdapter adapter;

    public AppointmentsListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_appointments_list, container, false);

        appointmentsRV = view.findViewById(R.id.appointmentsListRV);
        layoutManager = new LinearLayoutManager(getContext());
        appointmentsRV.setLayoutManager(layoutManager);
        appointmentsRV.setItemAnimator(new DefaultItemAnimator());
        ArrayList<Appointment> clientAppointments = new ArrayList<>();

        FirebaseManager.getInstance().fetchAppointments(getContext(), clientAppointments, false , appointmentsList -> {
            //Collections.reverse(appointmentsList);
            adapter = new AppointmentsAdapter(appointmentsList ,false);
            appointmentsRV.setAdapter(adapter);
        });

        return view;
    }
}