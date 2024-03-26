package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton returnBtn;
    private LinearLayout nextAppointmentLayout;
    private TextView cardHeadlineTV;
    private TextView nextDateTV;
    private TextView nextTimeTV;
    private TextView nextServiceTV;
    private CardView toCalenderCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTV = findViewById(R.id.usernameTV);
        returnBtn = findViewById(R.id.returnButton);
        toCalenderCV = findViewById(R.id.toCalenderCV);
        nextAppointmentLayout = findViewById(R.id.nextAppointmentLayout);
        nextDateTV = findViewById(R.id.nextDateTV);
        nextTimeTV = findViewById(R.id.nextTimeTV);
        nextServiceTV = findViewById(R.id.nextServiceTV);
        cardHeadlineTV = findViewById(R.id.cardHeadlineTV);

        FirebaseManager.getInstance().getCurrentUserDetails(new FirebaseManager.onUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(Client client) {
                currentUser = client;
                String name = currentUser.getUsername();
                usernameTV.setText(name);
            }
        });

        FirebaseManager.getInstance().fetchClosestAppointment(new FirebaseManager.onClosestAppointmentDetailsFetchedListener() {
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

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
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
                startActivity(intent);
            }
        });
    }
}