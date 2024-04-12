package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.barbershopapp.R;

public class ManagementActivity extends AppCompatActivity {

    TextView headline;
    Button backToMainBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        headline = findViewById(R.id.managementHeadline);
        backToMainBtn = findViewById(R.id.backToManagerMainBtn);

        String managerId = getIntent().getStringExtra("manager id");
        String fragmentName = getIntent().getStringExtra("Fragment to display");
        Fragment fragment;
        try {
            assert fragmentName != null;
            fragment = (Fragment) Class.forName(fragmentName).newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView5, fragment).commit();

            switch (fragmentName) {
                case "com.example.barbershopapp.Fragments.ManagerCalenderFragment":
                    headline.setTextSize(34);
                    headline.setText("Manage your calendar");
                    break;
                case "com.example.barbershopapp.Fragments.ClientsFragment":
                    headline.setText("Clients List");
                    break;
                case "com.example.barbershopapp.Fragments.ManagerServicesFragment":
                    headline.setTextSize(34);
                    headline.setText("Barbershop Services");
                    break;
                case "com.example.barbershopapp.Fragments.ScheduleFragment":
                    headline.setTextSize(34);
                    headline.setText("Barbershop Schedule");
                    break;
                default:
                    headline.setText("Default Title");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, ManagerMainActivity.class);
                intent.putExtra("manager id" , managerId);
                startActivity(intent);
            }
        });

    }

}