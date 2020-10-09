package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.ContactPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {
}
