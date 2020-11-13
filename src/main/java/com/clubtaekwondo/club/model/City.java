package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "ville")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ville")
    private Long idCity;

    @Column(name = "nome_ville")
    private String cityName;

    @Column(name = "code_postal")
    private String postalCode;

    public City(Long idCity, String cityName, String postalCode) {
        this.idCity = idCity;
        this.cityName = cityName;
        this.postalCode = postalCode;
    }

    public City() {

    }

    public Long getIdCity() {
        return idCity;
    }

    public void setIdCity(Long idCity) {
        this.idCity = idCity;
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
