package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.StudentRelation;
import com.clubtaekwondo.club.model.StudentRelationPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRelationRepository extends JpaRepository<StudentRelation, StudentRelationPK> {
}
