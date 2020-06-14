package com.example.radiant.Classes;

public class Appointment {

    private String Service;
    private String Description;
    private String Date;
    private String FullName;
    private String Email;
    private String Time;
    private String Payment;

    public Appointment()
    {
        //empty constructor needed
    }

    public Appointment(String service, String description, String fullName, String email, String date, String time, String payment) {
        this.Date = date;
        this.Description = description;
        this.Email = email;
        this.Service = service;
        this.Payment = payment;
        this.Time = time;
        this.FullName = fullName;
    }

    public String getService() {
        return Service;
    }

    public String getDescription() {
        return Description;
    }

    public String getDate() {
        return Date;
    }

    public String getFullName() {
        return FullName;
    }

    public String getEmail() {
        return Email;
    }

    public String getTime() {
        return Time;
    }

    public String getPayment() {
        return Payment;
    }
}
