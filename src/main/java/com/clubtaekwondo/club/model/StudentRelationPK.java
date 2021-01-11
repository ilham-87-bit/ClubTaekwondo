package com.clubtaekwondo.club.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StudentRelationPK implements Serializable {

    private Long IdStudent;

    private Long idContactPerson;


    public StudentRelationPK(Long idStudent, Long idContactPerson) {
        IdStudent = idStudent;
        this.idContactPerson = idContactPerson;
    }

    public StudentRelationPK() {

    }

    public Long getIdStudent() {
        return IdStudent;
    }

    public void setIdStudent(Long idStudent) {
        IdStudent = idStudent;
    }

    public Long getIdContactPerson() {
        return idContactPerson;
    }

    public void setIdContactPerson(Long idContactPerson) {
        this.idContactPerson = idContactPerson;
    }
}
