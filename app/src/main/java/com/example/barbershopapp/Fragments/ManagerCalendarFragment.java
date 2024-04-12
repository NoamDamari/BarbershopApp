package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.barbershopapp.R;



public class ManagerCalendarFragment extends Fragment {

    Button toBlockBtn;
    Button toUnblockBtn;

    public ManagerCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_calendar, container, false);

        toBlockBtn = view.findViewById(R.id.toBlockBtn);
        toUnblockBtn = view.findViewById(R.id.toUnblockBtn);

        toBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_managerCalendarFragment_to_blockingTimeFragment);
            }
        });

        toUnblockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_managerCalendarFragment_to_unblockingTimeFragment);
            }
        });

        return view;
    }
}