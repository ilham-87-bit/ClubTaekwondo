package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "offrir")
public class CategoryBySchool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offrir")
    private Long idCategoryBySchool;

    @ManyToOne
    @JoinColumn(name = "id_categori")
    private Categories cat;
    @ManyToOne
    @JoinColumn(name = "id_ecole")
    private School school;

    public CategoryBySchool(Long idCategoryBySchool, Categories cat, School school) {
        this.idCategoryBySchool = idCategoryBySchool;
        this.cat = cat;
        this.school = school;
    }

    public CategoryBySchool(Categories cat, School school) {
        this.cat = cat;
        this.school = school;
    }

    public CategoryBySchool() {

    }

    public Long getIdCategoryBySchool() {
        return idCategoryBySchool;
    }

    public void setIdCategoryBySchool(Long idCategoryBySchool) {
        this.idCategoryBySchool = idCategoryBySchool;
    }

    public Categories getCat() {
        return cat;
    }

    public void setCat(Categories cat) {
        this.cat = cat;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
