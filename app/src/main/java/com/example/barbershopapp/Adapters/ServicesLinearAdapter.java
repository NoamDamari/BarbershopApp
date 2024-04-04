package com.example.barbershopapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.Fragments.CalenderFragment;
import com.example.barbershopapp.Fragments.ServicesFragment;
import com.example.barbershopapp.Models.Service;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class ServicesLinearAdapter extends RecyclerView.Adapter<ServicesLinearAdapter.ViewHolder>{

    ArrayList<Service> services;

    public ServicesLinearAdapter(ArrayList<Service> services) {
        this.services = services;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTV;
        TextView servicePriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTV = itemView.findViewById(R.id.textView19);
            servicePriceTV = itemView.findViewById(R.id.textView20);
        }
    }

    @NonNull
    @Override
    public ServicesLinearAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_linear_layout, parent, false);
        return new ServicesLinearAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesLinearAdapter.ViewHolder holder, int position) {
        holder.serviceNameTV.setText(services.get(position).getServiceName());
        holder.servicePriceTV.setText(services.get(position).getServicePrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Service selectedService = services.get(holder.getAdapterPosition());

                // Get the NavHostFragment
                NavHostFragment navHostFragment = (NavHostFragment) ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView2);

                // Get the current fragment within the NavHostFragment
                Fragment currentFragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();

                if (currentFragment instanceof ServicesFragment) {
                    ServicesFragment servicesFragment = (ServicesFragment) currentFragment;
                    ((ServicesFragment) currentFragment).updateSelectedService(selectedService);
                }
                //((SetAppointmentActivity) holder.itemView.getContext()).selectedHourTV.setText(selectedHour);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
