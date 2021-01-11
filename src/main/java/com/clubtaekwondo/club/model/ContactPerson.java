package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "personne_contact")
public class ContactPerson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personne_contact")
    private Long idPerson;

    @Column(name = "nom_personne")
    private String personName;

    @Column(name = "prenom_personne")
    private String personFirstName;

    @Column(name = "gsm")
    private Integer gsm;

    @Column(name = "email")
    private String email;


    public ContactPerson(Long idPerson, String personName, String personFirstName, Integer gsm, String email) {
        this.idPerson = idPerson;
        this.personName = personName;
        this.personFirstName = personFirstName;
        this.gsm = gsm;
        this.email = email;
    }

    public ContactPerson() {

    }


    public Long getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Long idPerson) {
        this.idPerson = idPerson;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public Integer getGsm() {
        return gsm;
    }

    public void setGsm(Integer gsm) {
        this.gsm = gsm;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
