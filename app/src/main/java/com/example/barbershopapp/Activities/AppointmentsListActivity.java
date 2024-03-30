package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.barbershopapp.Adapters.AppointmentsAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AppointmentsListActivity extends AppCompatActivity {

    private ArrayList<Appointment> clientAppointments;
    RecyclerView appointmentsRV;
    LinearLayoutManager layoutManager;
    AppointmentsAdapter adapter;
    Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_list);

        appointmentsRV = findViewById(R.id.appointmentsRV);
        layoutManager = new LinearLayoutManager(this);
        appointmentsRV.setLayoutManager(layoutManager);
        appointmentsRV.setItemAnimator(new DefaultItemAnimator());
        clientAppointments = new ArrayList<Appointment>();
        returnBtn = findViewById(R.id.returnBtn2);


        FirebaseManager.getInstance().fetchClientAppointments(AppointmentsListActivity.this, clientAppointments, new FirebaseManager.onClientAppointmentsFetchedListener() {
            @Override
            public void onClientAppointmentsFetched(ArrayList<Appointment> appointmentsList) {
                Collections.reverse(appointmentsList);
                adapter = new AppointmentsAdapter(appointmentsList);
                appointmentsRV.setAdapter(adapter);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentsListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}