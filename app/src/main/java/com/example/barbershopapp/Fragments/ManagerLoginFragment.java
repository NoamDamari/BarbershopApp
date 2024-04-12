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

public class ManagerLoginFragment extends Fragment {

    private EditText managerNameET;
    private EditText managerEmailET;
    private EditText managerPasswordET;
    private EditText managerIdET;

    public ManagerLoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_manager_login, container, false);
        managerNameET = view.findViewById(R.id.managerNameInput);
        managerEmailET = view.findViewById(R.id.managerEmailInput);
        managerPasswordET = view.findViewById(R.id.managerPasswordInput);
        managerIdET = view.findViewById(R.id.managerIdInput);

        Button managerLoginBtn = view.findViewById(R.id.managerLoginBtn);
        Button managerBackToLoginBtn = view.findViewById(R.id.managerBackToLoginBtn);

        managerLoginBtn.setOnClickListener(v -> {
            String name = managerNameET.getText().toString();
            String email = managerEmailET.getText().toString();
            String password = managerPasswordET.getText().toString();
            String managerId = managerIdET.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || managerId.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
            else{
                FirebaseManager.getInstance().managerLogin(email , password , managerId , getContext());
            }
        });

        // Navigation back to Login Fragment
        managerBackToLoginBtn.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_managerLoginFragment_to_loginFragment));
        return  view;
    }
}