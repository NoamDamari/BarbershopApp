package com.example.barbershopapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershopapp.Adapters.HoursAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarFragment extends Fragment {

    CalendarView calender;
    RecyclerView availableHoursRV;
    TextView textView;
    TextView selectedDateTV;
    TextView selectedHourTV;
    Button nextBtn;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        calender = view.findViewById(R.id.calendarView2);
        availableHoursRV = view.findViewById(R.id.hoursRecyclerView2);
        textView = view.findViewById(R.id.textView30);
        selectedDateTV = view.findViewById(R.id.selectedDateTV2);
        selectedHourTV = view.findViewById(R.id.selectedHourTV2);
        nextBtn = view.findViewById(R.id.nextBtn);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        availableHoursRV.setLayoutManager(layoutManager);

        ArrayList<String> availableHours = new ArrayList<String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        selectedDateTV.setText(currentDate);
        textView.setText("Available hours for today " + currentDate);

        // Display available times for current date
        FirebaseManager.getInstance().fetchAvailableHours(currentDate, availableHours, getContext(), new FirebaseManager.onAvailableHoursFetchedListener() {
            @Override
            public void onAvailableHoursFetched(ArrayList<String> availableHours) {
                HoursAdapter adapter = new HoursAdapter(availableHours);
                availableHoursRV.setAdapter(adapter);
            }
        });

        // Display available times for any selected date
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                String selectedDate = dateFormat.format(selectedCalendar.getTime());
                selectedDateTV.setText(selectedDate);
                textView.setText("Avilable Hours on " + selectedDate);

                availableHours.clear();
                FirebaseManager.getInstance().fetchAvailableHours(selectedDate, availableHours, getContext(), new FirebaseManager.onAvailableHoursFetchedListener() {
                    @Override
                    public void onAvailableHoursFetched(ArrayList<String> availableHours) {
                        HoursAdapter adapter = new HoursAdapter(availableHours);
                        availableHoursRV.setAdapter(adapter);
                    }
                });
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = selectedDateTV.getText().toString();
                String selectedHour = selectedHourTV.getText().toString();
                if (selectedDate.equals("Date") || selectedHour.equals("Time")) {
                    Toast.makeText(getContext(), "Select date and time", Toast.LENGTH_SHORT).show();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("selected date" , selectedDate);
                bundle.putString("selected hour" , selectedHour);
                Navigation.findNavController(view).navigate(R.id.action_calenderFragment_to_servicesFragment , bundle);
            }
        });
        return view;
    }

    public void updateSelectedHour(String selectedHour) {
        selectedHourTV.setText(selectedHour);
    }

}

