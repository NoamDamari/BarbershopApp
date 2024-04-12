package com.example.barbershopapp.Models;

public class BlockedTime {
    String date;
    String Hour;

    public BlockedTime(String date, String hour) {
        this.date = date;
        Hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }
}
