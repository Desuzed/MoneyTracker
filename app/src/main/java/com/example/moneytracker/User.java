package com.example.moneytracker;

public class User {
    private String name;
    private String e_mail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public User(String name, String e_mail) {
        this.name = name;
        this.e_mail = e_mail;
    }
}
