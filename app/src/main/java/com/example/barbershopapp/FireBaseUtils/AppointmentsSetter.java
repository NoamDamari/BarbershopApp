package com.example.barbershopapp.FireBaseUtils;

import android.content.Context;
import android.widget.Toast;

import com.example.barbershopapp.Models.Appointment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppointmentsSetter {
    private static AppointmentsSetter instance;
    private final DatabaseReference mDatabase;

    public AppointmentsSetter() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized AppointmentsSetter getInstance() {
        if (instance == null) {
            instance = new AppointmentsSetter();
        }
        return instance;
    }

    // Set an Appointment at the barbershop
    public void setAppointment(Appointment appointment, Context context) {
        String uid = appointment.getClient().getUid();

        // Set appointment on client record
        mDatabase.child("Clients").child(uid).child("Appointments").
                child(appointment.getDate()).child(appointment.getTime()).setValue(appointment);

        // Set appointment on Appointments collection
        mDatabase.child("Appointments").child(appointment.getDate()).child(appointment.getTime()).
                setValue(appointment);

        Toast.makeText(context, "Appointment Set SUCCESS", Toast.LENGTH_SHORT).show();
    }
}
