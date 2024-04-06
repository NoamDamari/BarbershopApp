package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.barbershopapp.R;

public class ManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        String fragmentName = getIntent().getStringExtra("Fragment to display");
        Fragment fragment;
        try {
            fragment = (Fragment) Class.forName(fragmentName).newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}