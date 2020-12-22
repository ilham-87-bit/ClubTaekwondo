package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Student;

import java.util.List;

public interface StudentService {

    Student save(Student student);

    void delete(Student student);

    List<Student> getAllStudent();

    Student findById(Long id);
}
