package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie")
    private Long idCategory;

    @Column(name = "categorie")
    private String categoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "age")
    private Integer age;


    @OneToMany(mappedBy = "c")
    private Set<TimeTable> timeTables;


    public Categories(Long idCategory, String categoryName, String description, Integer age, Set<TimeTable> timeTables) {
        this.idCategory = idCategory;
        this.categoryName = categoryName;
        this.description = description;
        this.age = age;
        this.timeTables = timeTables;
    }

    public Categories() {

    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategories) {
        this.idCategory = idCategories;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
