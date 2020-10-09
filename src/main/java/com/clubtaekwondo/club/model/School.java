package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ecole")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ecole")
    private Long idSchool;

    @Column(name = "nome_ecole")
    private String schoolName;

    @OneToOne
    @JoinColumn(name = "id_adresse")
    private Address address;

    @OneToMany(mappedBy = "school")
    private Set<CategoryBySchool> categoryBySchools;

    @OneToMany(mappedBy = "s")
    private Set<TimeTable> timeTables;

    public School(Long idSchool, String schoolName, Address address) {
        this.idSchool = idSchool;
        this.schoolName = schoolName;
        this.address = address;
    }

    public School() {

    }

    public Long getIdSchool() {
        return idSchool;
    }

    public void setIdSchool(Long idSchool) {
        this.idSchool = idSchool;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public Set<TimeTable> getTimes() {
        return timeTables;
    }

    public void setTimes(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
