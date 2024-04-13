package com.example.barbershopapp.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BlockingTimeFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button blockBtn;
    Button startDateBtn;
    Button endDateBtn;
    Button startTimeBtn;
    Button endTimeBtn;
    boolean selectingStartDate = false;
    boolean selectingStartTime = false;

    public BlockingTimeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_blocking_time, container, false);
        blockBtn = view.findViewById(R.id.setAvailabilityBtn);
        startDateBtn = view.findViewById(R.id.startDateBtn);
        endDateBtn = view.findViewById(R.id.endDateBtn);
        startTimeBtn = view.findViewById(R.id.startTimeBtn);
        endTimeBtn = view.findViewById(R.id.endTimeBtn);

        Intent intent = requireActivity().getIntent();
        String managerId = intent.getStringExtra("manager id");

        // Set current date and time
        setCurrentDateTime();

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingStartDate = true;
                showDatePickerDialog(true);
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingStartDate = false;
                showDatePickerDialog(false);
            }
        });

        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingStartTime = true;
                showTimePickerDialog(true);
            }
        });

        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectingStartTime = false;
                showTimePickerDialog(false);
            }
        });

        blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = startDateBtn.getText().toString();
                String endDate = endDateBtn.getText().toString();
                String startTime = startTimeBtn.getText().toString();
                String endTime = endTimeBtn.getText().toString();

                FirebaseManager.getInstance().BlockTimeRange(getContext() , managerId , startDate , endDate,  startTime , endTime);
            }
        });

        return view;
    }

    private void setCurrentDateTime() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();

        // Check if minutes are already 00, if not, round up to the next hour
        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes != 0) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);
        }

        Date currentDate = calendar.getTime();

        // Format date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Set formatted date and time
        startDateBtn.setText(dateFormat.format(currentDate));
        endDateBtn.setText(dateFormat.format(currentDate));
        startTimeBtn.setText(timeFormat.format(currentDate));
        endTimeBtn.setText(timeFormat.format(currentDate));
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Prevent selection of past dates

        if (!isStartDate) {
            long minEndDate = startDateBtn.getTag() != null ? (long) startDateBtn.getTag() : System.currentTimeMillis() - 1000;
            datePickerDialog.getDatePicker().setMinDate(minEndDate);
        }
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);

        if (selectingStartDate) {
            startDateBtn.setText(formatDate(selectedDate.getTime()));
            startDateBtn.setTag(selectedDate.getTimeInMillis());
        } else {
            endDateBtn.setText(formatDate(selectedDate.getTime()));
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = 0;

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedTime.set(Calendar.MINUTE, 0);

                if (isStartTime) {
                    startTimeBtn.setText(formatTime(selectedTime.getTime()));
                    startTimeBtn.setTag(selectedTime.getTimeInMillis());
                } else {
                    endTimeBtn.setText(formatTime(selectedTime.getTime()));
                    endTimeBtn.setTag(selectedTime.getTimeInMillis());
                }
            }
        }, hourOfDay, minute, true); // 24-hour time format
        timePickerDialog.show();
    }

    private String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(date);
    }
}
