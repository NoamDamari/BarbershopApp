package com.example.barbershopapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.Fragments.CalendarFragment;
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

                // Get the NavHostFragment
                NavHostFragment navHostFragment = (NavHostFragment) ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView2);

                // Get the current fragment within the NavHostFragment
                Fragment currentFragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();

                if (currentFragment instanceof CalendarFragment) {
                    CalendarFragment calendarFragment = (CalendarFragment) currentFragment;
                    ((CalendarFragment) currentFragment).updateSelectedHour(selectedHour);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }
}
