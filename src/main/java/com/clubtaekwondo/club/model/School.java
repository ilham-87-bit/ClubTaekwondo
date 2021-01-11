package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ecole")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ecole")
    private Long idSchool;

    @Column(name = "nom_ecole")
    private String name;

    @OneToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @ManyToMany()
    @JoinTable(name = "offrir",
            joinColumns = {@JoinColumn(name = "id_ecole")},
            inverseJoinColumns = {@JoinColumn(name = "id_categorie")})
    private List<Categories> categoriesList;

    @OneToMany(mappedBy = "s")
    private Set<TimeTable> timeTables;

    @Transient
    private String fullUrlImg;

    public School(Long idSchool, String name, Address address, List<Categories> categoriesList, Set<TimeTable> timeTables) {
        this.idSchool = idSchool;
        this.name = name;
        this.address = address;
        this.categoriesList = categoriesList;
        this.timeTables = timeTables;
    }

    public School() {

    }

    public Long getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(Long idSchool) {
        this.idSchool = idSchool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public String getFullUrlImg() {
        return fullUrlImg;
    }

    public void setFullUrlImg(String fullUrlImg) {
        this.fullUrlImg = fullUrlImg;
    }

    public List<Categories> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }
}
