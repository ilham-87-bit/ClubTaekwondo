package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "adresse")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adresse")
    private Long id;

    @Column(name = "rue")
    private String street;

    @Column(name = "numero")
    private String number;


    @OneToOne
    @JoinColumn(name = "id_ville")
    private City city;

    public Address(Long id, String street, String number, City city) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.city = city;
    }

    public Address() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long idAddress) {
        this.id = idAddress;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
