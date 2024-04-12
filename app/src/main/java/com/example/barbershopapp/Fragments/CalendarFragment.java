package com.example.barbershopapp.Fragments;

import android.annotation.SuppressLint;
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
    CalendarView calendar;
    RecyclerView availableHoursRV;
    TextView textView;
    TextView selectedDateTV;
    TextView selectedHourTV;
    Button nextBtn;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        calendar = view.findViewById(R.id.calendarView2);
        availableHoursRV = view.findViewById(R.id.hoursRecyclerView2);
        textView = view.findViewById(R.id.textView30);
        selectedDateTV = view.findViewById(R.id.selectedDateTV2);
        selectedHourTV = view.findViewById(R.id.selectedHourTV2);
        nextBtn = view.findViewById(R.id.nextBtn);

        calendar.setMinDate(System.currentTimeMillis() - 1000);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        availableHoursRV.setLayoutManager(layoutManager);

        ArrayList<String> availableHours = new ArrayList<>();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        selectedDateTV.setText(currentDate);
        textView.setText(getString(R.string.available_hours_for_today) + currentDate);

        // Display available times for current date
        FirebaseManager.getInstance().fetchAvailableHours(currentDate, availableHours, getContext(), availableHours1 -> {

            HoursAdapter adapter = new HoursAdapter(availableHours1);
            availableHoursRV.setAdapter(adapter);
        });

        // Display available times for any selected date
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, dayOfMonth);

            String selectedDate = dateFormat.format(selectedCalendar.getTime());
            selectedDateTV.setText(selectedDate);
            //textView.setText("Available Hours on " + selectedDate);

            availableHours.clear();
                FirebaseManager.getInstance().fetchAvailableHours(selectedDate, availableHours, getContext(), new FirebaseManager.onAvailableHoursFetchedListener() {
                    @Override
                    public void onAvailableHoursFetched(ArrayList<String> availableHours) {
                        if (availableHours.isEmpty())
                        {
                            textView.setText("No available appointment slots for this date");
                        }
                        else {
                            textView.setText("Available Hours on " + selectedDate);
                        }
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
                Navigation.findNavController(view).navigate(R.id.action_calendarFragment_to_servicesFragment , bundle);
            }
        });
        return view;
    }

    public void updateSelectedHour(String selectedHour) {
        selectedHourTV.setText(selectedHour);
    }
}

