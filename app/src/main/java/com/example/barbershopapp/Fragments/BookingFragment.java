package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershopapp.Activities.SetAppointmentActivity;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingFragment extends Fragment {

    TextView selectedDateTV;
    TextView selectedTimeTV;
    TextView selectedServiceTV;
    Button setAppointmentBtn;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_booking, container, false);
        selectedDateTV = view.findViewById(R.id.textView6);
        selectedTimeTV = view.findViewById(R.id.textView7);
        selectedServiceTV = view.findViewById(R.id.textView8);
        setAppointmentBtn = view.findViewById(R.id.setAppointmentBtn);

        Bundle bundle = new Bundle();
        bundle = getArguments();
        assert bundle != null;
        selectedDateTV.setText(bundle.getString("selected date"));
        selectedTimeTV.setText(bundle.getString("selected hour"));
        selectedServiceTV.setText(bundle.getString("selected service"));

        setAppointmentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String date = selectedDateTV.getText().toString();
                String hour = selectedTimeTV.getText().toString();
                String service = selectedServiceTV.getText().toString();

                if (getActivity() != null) {

                    String username = getActivity().getIntent().getStringExtra("username");
                    String email = getActivity().getIntent().getStringExtra("email");
                    String phone = getActivity().getIntent().getStringExtra("phone");
                    String password = getActivity().getIntent().getStringExtra("password");
                    Client client = new Client(username , email ,password , phone);

                    boolean isFuture = FirebaseManager.getInstance().isFutureTime(date + " " + hour);
                    if(isFuture) {
                        Appointment appointment = new Appointment(date , hour , service , client);
                        FirebaseManager.getInstance().setAppointment(appointment , getContext());
                    }
                    else {
                        Toast.makeText(getContext() ,"Appointment Set FAILED", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }
}