package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_categorie")
    private Long id;

    @Column(name = "nom_categorie")
    private String categoryName;

    @Column(name = "description_categorie")
    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Tariff> tariffs;

    @OneToMany(mappedBy = "categories")
    private Set<CategoryByCoach> categoryByCoaches;

    @OneToMany(mappedBy = "cat")
    private Set<CategoryBySchool> categoryBySchools;

    @OneToMany(mappedBy = "c")
    private Set<TimeTable> timeTables;


    public Categories(Long id, String categoryName, Set<Tariff> tariffs, Set<CategoryByCoach> categoryByCoaches, Set<CategoryBySchool> categoryBySchools, Set<TimeTable> timeTables, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.tariffs = tariffs;
        this.categoryByCoaches = categoryByCoaches;
        this.categoryBySchools = categoryBySchools;
        this.timeTables = timeTables;
        this.description = description;
    }

    public Categories() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long idCategories) {
        this.id = idCategories;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public Set<CategoryByCoach> getCategoryByCoaches() {
        return categoryByCoaches;
    }

    public void setCategoryByCoaches(Set<CategoryByCoach> categoryByCoaches) {
        this.categoryByCoaches = categoryByCoaches;
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

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }

    public Set<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(Set<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
