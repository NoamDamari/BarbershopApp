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

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.R;

public class LoginFragment extends Fragment {

    private EditText emailET;
    private EditText passwordET;
    private Button loginBtn;
    private Button regBtn;
    private Button managerLoginBtn;

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
        loginBtn = view.findViewById(R.id.loginBtn);
        regBtn = view.findViewById(R.id.regBtn);
        managerLoginBtn = view.findViewById(R.id.loginAsManagerBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    FirebaseManager.getInstance().login(email , password , getContext());
                }
            }
        });

        // Navigation to RegisterFragment
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        // Navigation to ManagerLoginFragment
        managerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_managerLoginFragment);
            }
        });

        return  view;
    }
}