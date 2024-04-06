package com.example.barbershopapp.Models;

public class Client extends User {
    private String phone;
    private String uid;

    public Client(String username, String email, String password , String phone , String uid) {
        super(username, email, password);
        this.phone = phone;
        this.uid = uid;
    }

    public Client() {
        super();
        this.phone = "phone";
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
