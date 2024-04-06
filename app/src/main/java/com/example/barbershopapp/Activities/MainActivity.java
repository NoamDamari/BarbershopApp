package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

public class MainActivity extends AppCompatActivity {

    Client currentUser;
    Appointment closestAppointment;
    private TextView usernameTV;
    private ImageView signOutBtn;
    private LinearLayout nextAppointmentLayout;
    private TextView cardHeadlineTV;
    private TextView nextDateTV;
    private TextView nextTimeTV;
    private TextView nextServiceTV;
    private CardView toCalenderCV;
    private CardView toAppointmentsListCV;
    private CardView toContactCV;
    private CardView toServicesCV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTV = findViewById(R.id.usernameTV);
        signOutBtn = findViewById(R.id.signOutBtn);
        toCalenderCV = findViewById(R.id.toCalenderCV);
        toAppointmentsListCV = findViewById(R.id.toAppointmentsListCV);
        toContactCV = findViewById(R.id.toContactCV);
        toServicesCV = findViewById(R.id.toServicesCV);
        nextAppointmentLayout = findViewById(R.id.nextAppointmentLayout);
        nextDateTV = findViewById(R.id.nextDateTV);
        nextTimeTV = findViewById(R.id.nextTimeTV);
        nextServiceTV = findViewById(R.id.nextServiceTV);
        cardHeadlineTV = findViewById(R.id.cardHeadlineTV);

        FirebaseManager.getInstance().fetchCurrentUserDetails(new FirebaseManager.onUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(Client client) {
                currentUser = client;
                String name = currentUser.getUsername();
                usernameTV.setText(name);
            }
        });

        FirebaseManager.getInstance().fetchClosestAppointmentForClient(new FirebaseManager.onClosestAppointmentDetailsFetchedListener() {
            @Override
            public void onClosestAppointmentDetailsFetched(Appointment appointment) {
                closestAppointment = appointment;
                if(closestAppointment != null) {
                    String date = closestAppointment.getDate();
                    String time = closestAppointment.getTime();
                    String serviceType = closestAppointment.getServiceType();

                    nextAppointmentLayout.setVisibility(View.VISIBLE);

                    cardHeadlineTV.setText("Next Appointment:");
                    nextDateTV.setText(date);
                    nextTimeTV.setText(time);
                    nextServiceTV.setText(serviceType);
                }
                else {
                    return;
                }
            }
        });

        toCalenderCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SetAppointmentActivity.class);
                intent.putExtra("username" , currentUser.getUsername());
                intent.putExtra("email" , currentUser.getEmail());
                intent.putExtra("password" , currentUser.getPassword());
                intent.putExtra("phone" , currentUser.getPhone());
                intent.putExtra("uid" , currentUser.getUid());

                startActivity(intent);
            }
        });

        toAppointmentsListCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppointmentsListActivity.class);
                intent.putExtra("username" , currentUser.getUsername());
                intent.putExtra("email" , currentUser.getEmail());
                intent.putExtra("password" , currentUser.getPassword());
                intent.putExtra("phone" , currentUser.getPhone());
                intent.putExtra("uid" , currentUser.getUid());
                startActivity(intent);
            }
        });

        toContactCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                intent.putExtra("username" , currentUser.getUsername());
                intent.putExtra("email" , currentUser.getEmail());
                intent.putExtra("password" , currentUser.getPassword());
                intent.putExtra("phone" , currentUser.getPhone());
                intent.putExtra("uid" , currentUser.getUid());
                startActivity(intent);
            }
        });

        toServicesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ServicesActivity.class);
                intent.putExtra("username" , currentUser.getUsername());
                intent.putExtra("email" , currentUser.getEmail());
                intent.putExtra("password" , currentUser.getPassword());
                intent.putExtra("phone" , currentUser.getPhone());
                intent.putExtra("uid" , currentUser.getUid());
                startActivity(intent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager.getInstance().signOut(MainActivity.this);
            }
        });
    }
}