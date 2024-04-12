package com.example.barbershopapp.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.barbershopapp.Adapters.BlockedTimesAdapter;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.BlockedTime;
import com.example.barbershopapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class UnblockingTimeFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Button startDateBtn;
    Button endDateBtn;
    Button unblockDatesBtn;
    TextView textView;
    RecyclerView blockedHoursRV;
    LinearLayoutManager layoutManager;
    BlockedTimesAdapter adapter;

    boolean selectingStartDate = false;

    public UnblockingTimeFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_unblocking_time, container, false);

        startDateBtn = view.findViewById(R.id.startDateToUnblockBtn);
        endDateBtn = view.findViewById(R.id.endDateToUnblockBtn);
        unblockDatesBtn = view.findViewById(R.id.unblockDateBtn);
        textView = view.findViewById(R.id.textView32);
        blockedHoursRV = view.findViewById(R.id.blockedHoursRV);
        layoutManager = new LinearLayoutManager(getContext());
        blockedHoursRV.setLayoutManager(layoutManager);
        blockedHoursRV.setItemAnimator(new DefaultItemAnimator());

        Intent intent = requireActivity().getIntent();

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

        unblockDatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = startDateBtn.getText().toString();
                String endDate = endDateBtn.getText().toString();
                FirebaseManager.getInstance().unblockDatesRange(getContext() , startDate , endDate);
            }
        });

        return view;
    }

    private void setCurrentDateTime() {
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String currentDateStr = dateFormat.format(currentDate);
        // Set formatted date and time
        startDateBtn.setText(currentDateStr);
        endDateBtn.setText(currentDateStr);
        ArrayList<BlockedTime> currentDateBlockedTimes = new ArrayList<>();
        FirebaseManager.getInstance().fetchBlockedTimes(getContext(), currentDateStr, currentDateStr, currentDateBlockedTimes, new FirebaseManager.onBlockedTimesFetchedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onBlockedTimesFetched(ArrayList<BlockedTime> blockedTimeArrayList) {
                if (blockedTimeArrayList.isEmpty())
                {
                    String str = "No blocked times to display\n all day is available";
                    textView.setText(str);
                } else {
                    String str = "Blocked times for today:";
                    textView.setText(str);
                    //textView.setVisibility(View.GONE);
                    adapter = new BlockedTimesAdapter(blockedTimeArrayList);
                    blockedHoursRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
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
            String startDate = startDateBtn.getText().toString();
            String endDate = endDateBtn.getText().toString();
            onEndDateSet(startDate ,endDate);

        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    private void onEndDateSet(String startDate , String endDate){
        ArrayList<BlockedTime> blockedTimes = new ArrayList<>();
        FirebaseManager.getInstance().fetchBlockedTimes(getContext(), startDate, endDate, blockedTimes, new FirebaseManager.onBlockedTimesFetchedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onBlockedTimesFetched(ArrayList<BlockedTime> blockedTimeArrayList) {
                if (blockedTimeArrayList.isEmpty())
                {
                    String str = "No blocked times to display\n all range is available";
                    textView.setText(str);
                } else {
                    String str = "Blocked times in range:\n" + startDate + " - " + endDate;
                    textView.setText(str);
                    adapter = new BlockedTimesAdapter(blockedTimeArrayList);
                    blockedHoursRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}