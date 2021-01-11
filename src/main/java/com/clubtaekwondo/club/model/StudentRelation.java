package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "avoir")
public class StudentRelation {

    @EmbeddedId
    private StudentRelationPK studentRelationPK;

    @Column(name = "lien_parenté")
    private String relationship;


    public StudentRelation(StudentRelationPK studentRelationPK, String relationship) {
        this.studentRelationPK = studentRelationPK;
        this.relationship = relationship;
    }

    public StudentRelation() {

    }

    public StudentRelation(Long student, Long contactPerson) {
        this.studentRelationPK = new StudentRelationPK(student, contactPerson);

    }

    public StudentRelationPK getStudentRelationPK() {
        return studentRelationPK;
    }

    public void setStudentRelationPK(StudentRelationPK studentRelationPK) {
        this.studentRelationPK = studentRelationPK;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
