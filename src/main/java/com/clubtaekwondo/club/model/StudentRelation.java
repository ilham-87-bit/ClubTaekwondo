package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "avoir")
public class StudentRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_avoir")
    private Long idStudentRelation;

    @ManyToOne
    @JoinColumn(name = "id_eleve")
    private Student student;
    @ManyToOne
    @JoinColumn(name = "id_person_contact")
    private ContactPerson contactPerson;

    public StudentRelation(Student student, ContactPerson contactPerson) {
        this.student = student;
        this.contactPerson = contactPerson;
    }

    public StudentRelation(Long idStudentRelation, Student student, ContactPerson contactPerson) {
        this.idStudentRelation = idStudentRelation;
        this.student = student;
        this.contactPerson = contactPerson;
    }

    public StudentRelation() {

    }

    public Long getIdStudentRelation() {
        return idStudentRelation;
    }

    public void setIdStudentRelation(Long idStudentRelation) {
        this.idStudentRelation = idStudentRelation;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }
}
