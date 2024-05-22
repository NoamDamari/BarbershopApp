package com.example.barbershopapp.FireBaseUtils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.barbershopapp.Models.Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataFetcher {

    private static DataFetcher instance;
    private final FirebaseAuth mAuth;
    private final DatabaseReference mDatabase;

    public DataFetcher() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static synchronized DataFetcher getInstance() {
        if (instance == null) {
            instance = new DataFetcher();
        }
        return instance;
    }

    // Fetching data of the currently logged-in user(client)
    public void fetchCurrentUserDetails(DataFetcher.onUserDetailsFetchedListener listener) {

        String uid = mAuth.getUid();
        mDatabase.child("Clients").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Client client = snapshot.getValue(Client.class);
                            listener.onUserDetailsFetched(client);
                        } else {
                            listener.onUserDetailsFetched(null);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onUserDetailsFetched(null);
                    }
                });
    }

    // Fetching available hours for appointments booking
    public void fetchAvailableHours(String date, ArrayList<String> availableHours, Context context, DataFetcher.onAvailableHoursFetchedListener listener) {
        DatabaseReference appointmentsRef = mDatabase.child("Appointments").child(date);
        DatabaseReference unavailableTimesRef = mDatabase.child("Unavailable dates").child(date);
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot appointmentSnapshot) {
                boolean appointmentsExist = appointmentSnapshot.exists();

                unavailableTimesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot unavailableSnapshot) {
                        // Check if there are appointments for the given date
                        if (!appointmentsExist) {
                            if (unavailableSnapshot.hasChild("Blocked by")) {
                                // If all day is blocked
                                listener.onAvailableHoursFetched(availableHours);
                            } else {
                                // If there are no appointments, check unavailable times
                                for (int hour = 10; hour <= 21; hour++) {
                                    String hourString = String.valueOf(hour) + ":00";
                                    if (!unavailableSnapshot.hasChild(hourString)) {
                                        availableHours.add(hourString);
                                    }
                                }
                            }
                        } else {
                            // If there are appointments, check unavailable times for each hour
                            for (int hour = 10; hour <= 21; hour++) {
                                String hourString = String.valueOf(hour) + ":00";
                                if (!appointmentSnapshot.hasChild(hourString) && !unavailableSnapshot.hasChild(hourString)) {
                                    availableHours.add(hourString);
                                }
                            }
                        }
                        // Notify listener with available hours
                        listener.onAvailableHoursFetched(availableHours);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onAvailableHoursFetched(null);
                        Toast.makeText(context, "Database access failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onAvailableHoursFetched(null);
                Toast.makeText(context, "Database access failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface onAvailableHoursFetchedListener {
        void onAvailableHoursFetched(ArrayList<String> availableHours);
    }

    public interface onUserDetailsFetchedListener {
        void onUserDetailsFetched(Client client);
    }
}
