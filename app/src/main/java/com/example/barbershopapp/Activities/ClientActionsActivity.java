package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.barbershopapp.R;

public class ClientActionsActivity extends AppCompatActivity {

    TextView headline;
    Button backToMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_actions);

        headline = findViewById(R.id.clientActionsHeadline);
        backToMainBtn = findViewById(R.id.backToMainBtn);

        String fragmentName = getIntent().getStringExtra("Fragment to display");
        Fragment fragment;
        try {
            assert fragmentName != null;
            fragment = (Fragment) Class.forName(fragmentName).newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, fragment).commit();

            switch (fragmentName) {
                case "com.example.barbershopapp.Fragments.ContactFragment":
                    headline.setText(R.string.contact);
                    break;
                case "com.example.barbershopapp.Fragments.ClientServicesFragment":
                    headline.setText(R.string.our_services);
                    break;
                case "com.example.barbershopapp.Fragments.AppointmentsListFragment":
                    headline.setTextSize(34);
                    headline.setText(R.string.upcoming_appointments);
                    break;
                default:
                    headline.setText(R.string.default_title);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        backToMainBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ClientActionsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}