package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
