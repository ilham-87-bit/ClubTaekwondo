package com.clubtaekwondo.club.controller.user;

import com.clubtaekwondo.club.model.Address;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class StudentDTO {

    private String name;
    private String firstName;
    private String birthDay;
    private String gsm;
    private String email;
    private String nationalRegistry;
    private Address address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalRegistry() {
        return nationalRegistry;
    }

    public void setNationalRegistry(String nationalRegistry) {
        this.nationalRegistry = nationalRegistry;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
