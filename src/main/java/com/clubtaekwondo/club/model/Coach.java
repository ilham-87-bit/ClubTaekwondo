package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "entraineur")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_entraineur")
    private  Long idCoach;

    @Column(name = "nom_entraineur")
    private  String nameCoach;

    @Column(name = "prenom_entraineur")
    private String firstNameCoach;

    @OneToOne
    @JoinColumn(name = "id_ecole")
    private School school;

    @OneToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @OneToMany(mappedBy = "coach")
    private Set<CategoryByCoach> categoryByCoaches;

    @OneToMany(mappedBy = "co")
    private Set<TimeTable> timeTables;

    public Coach(Long idCoach, String nameCoach, String firstNameCoach, School school, Address address) {
        this.idCoach = idCoach;
        this.nameCoach = nameCoach;
        this.firstNameCoach = firstNameCoach;
        this.school = school;
        this.address = address;
    }

    public Coach() {

    }

    public Long getIdCoach() {
        return idCoach;
    }

    public void setIdCoach(Long idCoach) {
        this.idCoach = idCoach;
    }

    public String getNameCoach() {
        return nameCoach;
    }

    public void setNameCoach(String nameCoach) {
        this.nameCoach = nameCoach;
    }

    public String getFirstNameCoach() {
        return firstNameCoach;
    }

    public void setFirstNameCoach(String firstNameCoach) {
        this.firstNameCoach = firstNameCoach;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<CategoryByCoach> getCategoryByCoaches() {
        return categoryByCoaches;
    }

    public void setCategoryByCoaches(Set<CategoryByCoach> categoryByCoaches) {
        this.categoryByCoaches = categoryByCoaches;
    }

    public Set<TimeTable> getTimes() {
        return timeTables;
    }

    public void setTimes(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
