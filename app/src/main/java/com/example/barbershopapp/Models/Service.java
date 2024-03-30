package com.example.barbershopapp.Models;

public class Service {
    String ServiceName;
    String ServicePrice;

    public Service(String serviceName, String servicePrice) {
        ServiceName = serviceName;
        ServicePrice = servicePrice;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }
}
