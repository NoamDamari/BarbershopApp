package com.example.barbershopapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.Activities.SetAppointmentActivity;
import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {
    ArrayList<String> hours;
    public HoursAdapter(ArrayList<String> hours) {
        this.hours = hours;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hourTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.hourTV = itemView.findViewById(R.id.hourTV);
        }

    }

    @NonNull
    @Override
    public HoursAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hour_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoursAdapter.ViewHolder holder, int position) {
        String hour = hours.get(position);
        holder.hourTV.setText(hour);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedHour = hours.get(holder.getAdapterPosition());
                ((SetAppointmentActivity) holder.itemView.getContext()).selectedHourTV.setText(selectedHour);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }
}
