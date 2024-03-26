package com.example.barbershopapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbershopapp.Adapters.HoursAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SetAppointmentActivity extends AppCompatActivity {
    private CalendarView calender;
    public TextView textView;
    private TextView selectedDateTV;
    private RecyclerView hoursRV;
    public TextView selectedHourTV;
    private ArrayList<String> availableHours;
    private Spinner servicesSpinner;
    private Button setAppointmentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment);

        calender = findViewById(R.id.calendarView);
        textView = findViewById(R.id.textView10);
        selectedDateTV = findViewById(R.id.selectedDateTV);
        hoursRV = findViewById(R.id.hoursRecyclerView);
        selectedHourTV = findViewById(R.id.selectedHourTV);
        servicesSpinner = findViewById(R.id.servicesSpinner);
        setAppointmentBtn = findViewById(R.id.setAppointmentBtn);

        availableHours = new ArrayList<String>();
        String[] services = getResources().getStringArray(R.array.service_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        servicesSpinner.setAdapter(adapter);

        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                String selectedDate = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
                selectedDateTV.setText(selectedDate);
                textView.setText("Avilable Hours on " + selectedDate);

                availableHours.clear();
                FirebaseManager.getInstance().getAvailableHours(selectedDate, availableHours, SetAppointmentActivity.this, new FirebaseManager.onAvailableHoursFetchedListener() {
                    @Override
                    public void onAvailableHoursFetched(ArrayList<String> availableHours) {
                        HoursAdapter adapter = new HoursAdapter(availableHours);
                        hoursRV.setAdapter(adapter);
                    }
                });
            }
        });

        setAppointmentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String date = selectedDateTV.getText().toString();
                String hour = selectedHourTV.getText().toString();
                String service = servicesSpinner.getSelectedItem().toString();

                String username = getIntent().getStringExtra("username");
                String email = getIntent().getStringExtra("email");
                String phone = getIntent().getStringExtra("phone");
                String password = getIntent().getStringExtra("password");

                Client client = new Client(username , email ,password , phone);

                if(isFutureDate(date + " " + hour)) {
                    Appointment appointment = new Appointment(date , hour , service , client);
                    FirebaseManager.getInstance().setAppointment(appointment ,SetAppointmentActivity.this);
                }
                else {
                    Toast.makeText(SetAppointmentActivity.this ,"Appointment Set FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isFutureDate(String date) {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.getDefault());
            Date selectedDate = dateFormat.parse(date);

            Date currentDate = new Date();

            if (selectedDate.after(currentDate)) {
                return true;
            } else {
                Toast.makeText(SetAppointmentActivity.this ,"Select a future date and time", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


}