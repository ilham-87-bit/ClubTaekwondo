package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "donner")
public class CategoryByCoach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donne")
    private Long idCategoryByCoach;

    @ManyToOne
    @JoinColumn(name = "id_categories")
    private Categories categories;
    @ManyToOne
    @JoinColumn(name = "id_entraineur")
    private Coach coach;

    public CategoryByCoach(Long idCategoryByCoach, Categories categories, Coach coach) {
        this.idCategoryByCoach = idCategoryByCoach;
        this.categories = categories;
        this.coach = coach;
    }

    public CategoryByCoach(Categories categories, Coach coach) {
        this.categories = categories;
        this.coach = coach;
    }

    public CategoryByCoach() {

    }

    public Long getIdCategoryByCoach() {
        return idCategoryByCoach;
    }

    public void setIdCategoryByCoach(Long idCategoryByCoach) {
        this.idCategoryByCoach = idCategoryByCoach;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}
