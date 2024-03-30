package com.example.barbershopapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.Models.Service;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder>{

    ArrayList<Service> services;

    public ServicesAdapter(ArrayList<Service> services) {
        this.services = services;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTV;
        TextView servicePriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTV = itemView.findViewById(R.id.serviceNameTV);
            servicePriceTV = itemView.findViewById(R.id.servicePriceTV);
        }
    }

    @NonNull
    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_layout, parent, false);
        return new ServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesAdapter.ViewHolder holder, int position) {
        Service service = services.get(holder.getAdapterPosition());
        holder.serviceNameTV.setText(services.get(position).getServiceName());
        holder.servicePriceTV.setText(services.get(position).getServicePrice());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

}
