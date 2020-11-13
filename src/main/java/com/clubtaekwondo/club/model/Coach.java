package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "entraineur")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entraineur")
    private Long idCoach;

    @Column(name = "nom_entraineur")
    private String nameCoach;

    @Column(name = "prenom_entraineur")
    private String firstNameCoach;

    private String email;

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

    public Coach(Long idCoach, String nameCoach, String firstNameCoach, String email, School school, Address address, Set<CategoryByCoach> categoryByCoaches, Set<TimeTable> timeTables) {
        this.idCoach = idCoach;
        this.nameCoach = nameCoach;
        this.firstNameCoach = firstNameCoach;
        this.email = email;
        this.school = school;
        this.address = address;
        this.categoryByCoaches = categoryByCoaches;
        this.timeTables = timeTables;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
