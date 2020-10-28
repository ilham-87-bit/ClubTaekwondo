package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "ville")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ville")
    private Long id;

    @Column(name = "nome_ville")
    private String cityName;

    @Column(name = "code_postal")
    private String postalCode;

    public City(Long id, String cityName, String postalCode) {
        this.id = id;
        this.cityName = cityName;
        this.postalCode = postalCode;
    }

    public City() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long idCity) {
        this.id = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
