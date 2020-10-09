package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "eleve")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_eleve")
    private Long idStudent;

    @Column(name = "nom_eleve")
    private String name;

    @Column(name = "prenom_eleve")
    private String firstName;

    @Column(name = "date_naissance")
    private Date birthDay;

    @Column(name = "gsm")
    private Integer gsm;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @OneToMany(mappedBy = "student")
    private Set<StudentRelation> person;

    public Student(Long idStudent, String name, String firstName, Date birthDay, Integer gsm, String email, Address address) {
        this.idStudent = idStudent;
        this.name = name;
        this.firstName = firstName;
        this.birthDay = birthDay;
        this.gsm = gsm;
        this.email = email;
        this.address = address;
    }

    public Student() {

    }

    public Student(Set<StudentRelation> person) {
        this.person = person;
    }

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }

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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<StudentRelation> getPerson() {
        return person;
    }

    public void setPerson(Set<StudentRelation> person) {
        this.person = person;
    }
}
