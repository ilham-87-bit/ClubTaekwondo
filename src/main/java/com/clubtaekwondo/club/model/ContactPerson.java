package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "personne_contact")
public class ContactPerson {

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

    @OneToMany(mappedBy = "contactPerson")
    private Set<StudentRelation> persons;

    public ContactPerson(Long idPerson, String personName, String personFirstName, Integer gsm, String email, Set<StudentRelation> persons) {
        this.idPerson = idPerson;
        this.personName = personName;
        this.personFirstName = personFirstName;
        this.gsm = gsm;
        this.email = email;
        this.persons = persons;
    }

    public ContactPerson() {

    }

    public ContactPerson(Set<StudentRelation> persons) {
        this.persons = persons;
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

    public Set<StudentRelation> getPersons() {

        return persons;
    }

    public void setPersons(Set<StudentRelation> persons) {
        this.persons = persons;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
