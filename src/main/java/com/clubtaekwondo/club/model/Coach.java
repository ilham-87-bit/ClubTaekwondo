package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "entraineur")
public class Coach extends User {

    @OneToOne
    @JoinColumn(name = "id_ecole")
    private School school;

    @OneToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "donner",
            joinColumns = {@JoinColumn(name = "id_entraineur")},
            inverseJoinColumns = {@JoinColumn(name = "id_categorie")})
    private List<Categories> categoriesList;

    public Coach(Long id, String lastName, String firstName, String password, String email, UserRole userRole, School school, Address address, List<Categories> categoriesList) {
        super(id, lastName, firstName, password, email, userRole);
        this.school = school;
        this.address = address;
        this.categoriesList = categoriesList;
    }

    public Coach() {

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

    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }
}
