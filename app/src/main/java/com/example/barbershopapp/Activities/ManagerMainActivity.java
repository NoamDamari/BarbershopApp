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
import com.example.barbershopapp.Fragments.ClientsFragment;
import com.example.barbershopapp.Fragments.ScheduleFragment;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Manager;
import com.example.barbershopapp.R;

public class ManagerMainActivity extends AppCompatActivity {

    private Manager currentManager;
    private Appointment nextAppointment;
    private TextView usernameTV;
    private ImageView signOutBtn;
    private LinearLayout nextAppointmentLayout;
    private TextView cardHeadlineTV;
    private TextView nextDateTV;
    private TextView nextTimeTV;
    private TextView nextServiceTV;
    private TextView nextClientTV;
    private CardView toCalendarCV;
    private CardView toAppointmentsListCV;
    private CardView toClientsCV;
    private CardView toServicesCV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main);

        usernameTV = findViewById(R.id.mUsernameTV);
        signOutBtn = findViewById(R.id.mSignOutBtn);
        toCalendarCV = findViewById(R.id.mToCalendarCV);
        toAppointmentsListCV = findViewById(R.id.mToAppointmentsListCV);
        toClientsCV = findViewById(R.id.mToClientsCV);
        toServicesCV = findViewById(R.id.mToServicesCV);
        nextAppointmentLayout = findViewById(R.id.mNextAppointmentLayout);
        nextDateTV = findViewById(R.id.mNextDateTV);
        nextTimeTV = findViewById(R.id.mNextTimeTV);
        nextServiceTV = findViewById(R.id.mNextServiceTV);
        nextClientTV = findViewById(R.id.mNextClientNameTV);
        cardHeadlineTV = findViewById(R.id.mCardHeadlineTV);

        Intent intent = getIntent();
        String managerId = null;
        if (intent != null) {
            managerId = intent.getStringExtra("manager id");
        }

        FirebaseManager.getInstance().fetchManagerDetails( managerId , new FirebaseManager.onManagerDetailsFetchedListener() {
            @Override
            public void onManagerDetailsFetched(Manager manager) {
                currentManager = manager;
                String name = currentManager.getUsername();
                usernameTV.setText(name);
            }
        });

        FirebaseManager.getInstance().fetchNextAppointment(new FirebaseManager.onNextAppointmentFetchedListener() {

            @Override
            public void onNextAppointmentsFetched(Appointment appointment) {
                nextAppointment = appointment;
                if(nextAppointment != null) {
                    String date = nextAppointment.getDate();
                    String time = nextAppointment.getTime();
                    String serviceType = nextAppointment.getServiceType();
                    String clientName = nextAppointment.getClient().getUsername();

                    nextAppointmentLayout.setVisibility(View.VISIBLE);

                    cardHeadlineTV.setText("Next Appointment:");
                    nextDateTV.setText(date);
                    nextTimeTV.setText(time);
                    nextServiceTV.setText(serviceType);
                    nextClientTV.setText(clientName);
                }
                else {
                    return;
                }
            }
        });

        toCalendarCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerMainActivity.this, SetAppointmentActivity.class);
                intent.putExtra("username" , currentManager.getUsername());
                intent.putExtra("email" , currentManager.getEmail());
                intent.putExtra("password" , currentManager.getPassword());
                startActivity(intent);
            }
        });

        toAppointmentsListCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity(ScheduleFragment.class);
            }
        });

        toClientsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity(ClientsFragment.class);
            }
        });

        toServicesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerMainActivity.this, ServicesActivity.class);
                intent.putExtra("username" , currentManager.getUsername());
                intent.putExtra("email" , currentManager.getEmail());
                intent.putExtra("password" , currentManager.getPassword());
                startActivity(intent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseManager.getInstance().signOut(ManagerMainActivity.this);
            }
        });
    }

    private void openNextActivity(Class fragmentClass) {
        Intent intent = new Intent(this, ManagementActivity.class);
        intent.putExtra("username" , currentManager.getUsername());
        intent.putExtra("email" , currentManager.getEmail());
        intent.putExtra("password" , currentManager.getPassword());
        intent.putExtra("Fragment to display", fragmentClass.getName());
        startActivity(intent);
    }
}