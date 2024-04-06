package com.example.barbershopapp.Adapters;

import android.content.DialogInterface;
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
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ViewHolder>{

    ArrayList<Client> clientsList;

    public ClientsAdapter(ArrayList<Client> clientArrayList) {
        this.clientsList = clientArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView clientNameTV;
        TextView clientEmailTV;
        TextView clientPhoneTV;
        Button deleteClientBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clientNameTV = itemView.findViewById(R.id.clientNameTV);
            clientEmailTV = itemView.findViewById(R.id.clientEmailTV);
            clientPhoneTV = itemView.findViewById(R.id.clientPhoneTV);
            deleteClientBtn = itemView.findViewById(R.id.deleteClientBtn);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_details_layout, parent, false);
        return new ClientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Client client = clientsList.get(holder.getAdapterPosition());
        holder.clientNameTV.setText(clientsList.get(position).getUsername());
        holder.clientEmailTV.setText(clientsList.get(position).getEmail());
        holder.clientPhoneTV.setText(clientsList.get(position).getPhone());

        holder.deleteClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Delete Client Account");
                builder.setMessage("Are you sure you want to delete this client account?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            Client clientToDelete = clientsList.get(holder.getAdapterPosition());
                            clientsList.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            FirebaseManager.getInstance().deleteClientAccount(clientToDelete);
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
        return clientsList.size();
    }
}
