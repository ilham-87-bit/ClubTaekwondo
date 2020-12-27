package com.clubtaekwondo.club.model;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "coach")
    private Set<CategoryByCoach> categoryByCoaches;

    @OneToMany(mappedBy = "co")
    private Set<TimeTable> timeTables;

    public Coach(Long id, String lastName, String firstName, String password, String email, UserRole userRole, School school, Address address, Set<CategoryByCoach> categoryByCoaches, Set<TimeTable> timeTables) {
        super(id, lastName, firstName, password, email, userRole);
        this.school = school;
        this.address = address;
        this.categoryByCoaches = categoryByCoaches;
        this.timeTables = timeTables;
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

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
