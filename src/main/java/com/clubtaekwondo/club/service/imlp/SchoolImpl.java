package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.School;
import com.clubtaekwondo.club.repository.SchoolRepository;
import com.clubtaekwondo.club.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public School save(School school) {
        return schoolRepository.save(school);
    }

    @Override
    public void delete(School school) {
        schoolRepository.delete(school);
    }

    @Override
    public List<School> getAllSchool() {
        return schoolRepository.findAll();
    }

    @Override
    public School findById(Long id) {
        return schoolRepository.getOne(id);
    }
}
