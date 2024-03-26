package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

public class MainActivity extends AppCompatActivity {

    Client currentUser;
    private TextView usernameET;
    private ImageButton returnBtn;
    private CardView toCalenderCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = findViewById(R.id.usernameTV);
        returnBtn = findViewById(R.id.returnButton);
        toCalenderCV = findViewById(R.id.toCalenderCV);
        FirebaseManager.getInstance().getCurrentUserDetails(new FirebaseManager.onUserDetailsFetchedListener() {
            @Override
            public void onUserDetailsFetched(Client client) {
                currentUser = client;
                String name = currentUser.getUsername();
                usernameET.setText(name);
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