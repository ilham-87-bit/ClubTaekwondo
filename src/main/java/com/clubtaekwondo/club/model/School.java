package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ecole")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ecole")
    private Long idSchool;

    @Column(name = "nome_ecole")
    private String name;

    @OneToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @OneToMany(mappedBy = "school")
    private Set<CategoryBySchool> categoryBySchools;

    @OneToMany(mappedBy = "s")
    private Set<TimeTable> timeTables;

    @Transient
    private String fullUrlImg;

    public School(Long idSchool, String name, Address address, Set<CategoryBySchool> categoryBySchools, Set<TimeTable> timeTables) {
        this.idSchool = idSchool;
        this.name = name;
        this.address = address;
        this.categoryBySchools = categoryBySchools;
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

    public Set<CategoryBySchool> getCategoryBySchools() {
        return categoryBySchools;
    }

    public void setCategoryBySchools(Set<CategoryBySchool> categoryBySchools) {
        this.categoryBySchools = categoryBySchools;
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
}
