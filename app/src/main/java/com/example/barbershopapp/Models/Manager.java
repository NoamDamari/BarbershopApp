package com.example.barbershopapp.Models;

public class Manager extends User {
    String managerId;
    public Manager(String username, String email, String password) {
        super(username, email, password);
    }

    public Manager(String username, String email, String password, String managerId) {
        super(username, email, password);
        this.managerId = managerId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
