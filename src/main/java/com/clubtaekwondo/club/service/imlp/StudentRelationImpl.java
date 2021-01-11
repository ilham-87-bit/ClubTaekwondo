package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.StudentRelation;
import com.clubtaekwondo.club.repository.StudentRelationRepository;
import com.clubtaekwondo.club.service.StudentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentRelationImpl implements StudentRelationService {

    @Autowired
    private StudentRelationRepository studentRelationRepository;


    @Override
    public StudentRelation save(StudentRelation studentRelation) {
        return studentRelationRepository.save(studentRelation);
    }

    @Override
    public void delete(StudentRelation studentRelation) {
        studentRelationRepository.delete(studentRelation);
    }

    @Override
    public List<StudentRelation> getAllStudentRelation() {
        return studentRelationRepository.findAll();
    }

}
