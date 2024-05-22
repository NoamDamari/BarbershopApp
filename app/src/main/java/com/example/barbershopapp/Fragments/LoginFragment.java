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

import com.example.barbershopapp.FireBaseUtils.Login;
import com.example.barbershopapp.R;

public class LoginFragment extends Fragment {

    private EditText emailET;
    private EditText passwordET;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailET = view.findViewById(R.id.loginEmailInput);
        passwordET = view.findViewById(R.id.loginPasswordInput);
        Button loginBtn = view.findViewById(R.id.loginBtn);
        Button regBtn = view.findViewById(R.id.regBtn);
        Button managerLoginBtn = view.findViewById(R.id.loginAsManagerBtn);

        loginBtn.setOnClickListener(v -> {

            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
            else {
                Login.getInstance().login(email , password , getContext());
            }
        });

        // Navigation to Register Fragment
        regBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment));

        // Navigation to Manager Login Fragment
        managerLoginBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_managerLoginFragment));

        return  view;
    }
}