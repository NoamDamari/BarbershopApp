package com.example.barbershopapp.Models;

public class Client extends User {

    private String phone;

    public Client(String username, String email, String password) {
        super(username, email, password);
    }

    public Client(String username, String email, String password, String phone) {
        super(username, email, password);
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
