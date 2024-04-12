package com.example.barbershopapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershopapp.FirebaseManager;
import com.example.barbershopapp.Models.BlockedTime;
import com.example.barbershopapp.Models.Client;
import com.example.barbershopapp.R;

import java.util.ArrayList;

public class BlockedTimesAdapter extends RecyclerView.Adapter<BlockedTimesAdapter.ViewHolder>{

    ArrayList<BlockedTime> blockedTimesList;

    public BlockedTimesAdapter(ArrayList<BlockedTime> blockedTimesList) {
        this.blockedTimesList = blockedTimesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView blockedDateTV;
        TextView blockedHourTV;
        Button unblockBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            blockedDateTV = itemView.findViewById(R.id.blockedDateTV);
            blockedHourTV = itemView.findViewById(R.id.blockedHourTV);
            unblockBtn = itemView.findViewById(R.id.unblockBtn);
        }
    }

    @NonNull
    @Override
    public BlockedTimesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_time_layout, parent, false);
        return new BlockedTimesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedTimesAdapter.ViewHolder holder, int position) {

        holder.blockedDateTV.setText(blockedTimesList.get(position).getDate());
        holder.blockedHourTV.setText(blockedTimesList.get(position).getHour());

        holder.unblockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    BlockedTime blockedTime = blockedTimesList.get(holder.getAdapterPosition());
                    blockedTimesList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    FirebaseManager.getInstance().unblockSingleTimeSlot(blockedTime);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return blockedTimesList.size();
    }


}
