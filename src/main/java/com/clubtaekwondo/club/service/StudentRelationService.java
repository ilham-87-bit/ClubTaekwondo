package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.StudentRelation;

import java.util.List;

public interface StudentRelationService {

    StudentRelation save(StudentRelation studentRelation);

    void delete(StudentRelation studentRelation);

    List<StudentRelation> getAllStudentRelation();

    StudentRelation findById(Long id);
}
