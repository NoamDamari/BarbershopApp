package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barbershopapp.FireBaseUtils.Registration;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;


public class RegisterFragment extends Fragment {

    private EditText usernameET;
    private EditText emailET;
    private EditText passwordET;
    private EditText phoneNumberET;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        usernameET = view.findViewById(R.id.regUsernameInput);
        emailET = view.findViewById(R.id.regEmailInput);
        passwordET = view.findViewById(R.id.regPasswordInput);
        phoneNumberET = view.findViewById(R.id.regPhoneInput);
        Button registerBtn = view.findViewById(R.id.registerBtn);
        Button backToLoginBtn = view.findViewById(R.id.backToLoginBtn);

        registerBtn.setOnClickListener(v -> {

            String username = usernameET.getText().toString();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();
            String phone = phoneNumberET.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()){
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
            else {
                // Register the client and store details in the database
                Client client = new Client(username , email , password , phone);
                Registration.getInstance().register(client , getContext());
            }
        });

        // Navigation back to Login Fragment
        backToLoginBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment));

        return view;
    }
}