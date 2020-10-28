package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.School;

import java.util.List;

public interface SchoolService {

    School save(School school);

    void delete(School school);

    List<School> getAllSchool();

    School findById(Long id);
}
