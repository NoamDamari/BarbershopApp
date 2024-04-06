package com.example.barbershopapp.Adapters;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.Appointment;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>{

    ArrayList<Appointment> appointmentsList;
    boolean isManager;

    public AppointmentsAdapter(ArrayList<Appointment> appointmentsList , boolean isManager) {
        this.appointmentsList = appointmentsList;
        this.isManager = isManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView dateTV;
        TextView timeTV;
        TextView serviceTypeTV;
        TextView clientNameTV;
        Button cancelBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            clientNameTV = itemView.findViewById(R.id.clientNameTV2);
            serviceTypeTV = itemView.findViewById(R.id.serviceTypeTV);
            cancelBtn = itemView.findViewById(R.id.cButton);
        }
    }

    @NonNull
    @Override
    public AppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_layout, parent, false);
        return new AppointmentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapter.ViewHolder holder, int position) {

        Appointment appointment = appointmentsList.get(holder.getAdapterPosition());
        holder.dateTV.setText(appointmentsList.get(position).getDate());
        holder.timeTV.setText(appointmentsList.get(position).getTime());
        holder.serviceTypeTV.setText(appointmentsList.get(position).getServiceType());
        holder.clientNameTV.setText(appointmentsList.get(position).getClient().getUsername());

        if (isManager) {
            holder.clientNameTV.setVisibility(View.VISIBLE);
        } else {
            holder.clientNameTV.setVisibility(View.GONE);
        }

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Appointment Cancel");
                builder.setMessage("Are you sure you want to cancel this appointment?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            Appointment appointmentToCancel = appointmentsList.get(holder.getAdapterPosition());
                            appointmentsList.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);

                            FirebaseManager.getInstance().cancelAppointment(appointmentToCancel);
                        }
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

}
