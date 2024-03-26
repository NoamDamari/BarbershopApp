package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.R;

public class ManagerLoginActivity extends AppCompatActivity {

    private EditText managerNameET;
    private EditText managerEmailET;
    private EditText managerPasswordET;
    private EditText managerIdET;
    private Button managerLoginBtn;
    private Button managerBackToLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        managerNameET = findViewById(R.id.managerNameInput);
        managerEmailET = findViewById(R.id.managerEmailInput);
        managerPasswordET = findViewById(R.id.managerPasswordInput);
        managerIdET = findViewById(R.id.managerIdInput);

        managerLoginBtn = findViewById(R.id.managerLoginBtn);
        managerBackToLoginBtn = findViewById(R.id.managerBackToLoginBtn);

        managerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = managerNameET.getText().toString();
                String email = managerEmailET.getText().toString();
                String password = managerPasswordET.getText().toString();
                String managerId = managerIdET.getText().toString();

                FirebaseManager.getInstance().managerLogin(email , password , managerId , ManagerLoginActivity.this);
            }
        });
        managerBackToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}