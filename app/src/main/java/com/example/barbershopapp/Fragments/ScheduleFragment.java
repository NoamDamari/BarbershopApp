package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barbershopapp.Activities.AppointmentsListActivity;
import com.example.barbershopapp.Adapters.AppointmentsAdapter;
import com.example.barbershopapp.Adapters.ClientsAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.R;

import java.util.ArrayList;
import java.util.Collections;


public class ScheduleFragment extends Fragment {

    RecyclerView appointmentsScheduleRV;
    LinearLayoutManager layoutManager;
    AppointmentsAdapter adapter;

    public ScheduleFragment() {
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
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        appointmentsScheduleRV = view.findViewById(R.id.scheduleRV);
        layoutManager = new LinearLayoutManager(getContext());
        appointmentsScheduleRV.setLayoutManager(layoutManager);
        appointmentsScheduleRV.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Appointment> appointments = new ArrayList<>();

        FirebaseManager.getInstance().fetchAppointments(getContext() , appointments , true , new FirebaseManager.onAppointmentsFetchedListener(){
            @Override
            public void onAppointmentsFetched(ArrayList<Appointment> appointments) {
                adapter = new AppointmentsAdapter(appointments ,true);
                appointmentsScheduleRV.setAdapter(adapter);
            }
        });
        return view;
    }
}