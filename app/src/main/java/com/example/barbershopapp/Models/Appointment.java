package com.example.barbershopapp.Models;

public class Appointment {
    private String date;
    private String time;
    private String serviceType;
    private Client client;

    public Appointment(String date, String time, String serviceType, Client client) {
        this.date = date;
        this.time = time;
        this.serviceType = serviceType;
        this.client = client;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
