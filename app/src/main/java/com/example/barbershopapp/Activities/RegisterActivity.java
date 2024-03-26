package com.example.barbershopapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText phoneNumberET;
    private Button registerBtn;
    private Button backToLoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseManager firebaseManager = new FirebaseManager();
        usernameET = findViewById(R.id.regUsernameInput);
        emailET = findViewById(R.id.regEmailInput);
        passwordET = findViewById(R.id.regPasswordInput);
        phoneNumberET = findViewById(R.id.regPhoneInput);

        registerBtn = findViewById(R.id.registerBtn);
        backToLoginBtn = findViewById(R.id.backToLoginBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String phone = phoneNumberET.getText().toString();

                Client client = new Client(username , email , password , phone);

                FirebaseManager.getInstance().register(client , RegisterActivity.this);
            }
        });

        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}