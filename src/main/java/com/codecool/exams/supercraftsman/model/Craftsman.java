package com.codecool.exams.supercraftsman.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Craftsman {
    private long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    private String phoneNumber;

    public Craftsman() {
    }

    public Craftsman(long id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
