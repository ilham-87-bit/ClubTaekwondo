package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.CategoryBySchool;
import com.clubtaekwondo.club.repository.CategoryBySchoolRepository;
import com.clubtaekwondo.club.service.CategoryBySchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryBySchoolImpl implements CategoryBySchoolService {

    @Autowired
    private CategoryBySchoolRepository categoryBySchoolRepository;

    @Override
    public CategoryBySchool save(CategoryBySchool categoryBySchool) {
        return categoryBySchoolRepository.save(categoryBySchool);
    }

    @Override
    public void delete(CategoryBySchool categoryBySchool) {
        categoryBySchoolRepository.delete(categoryBySchool);
    }

    @Override
    public CategoryBySchool getById(Long id) {
        return categoryBySchoolRepository.getOne(id);
    }

    @Override
    public List<CategoryBySchool> getAllCategoryBySchool() {
        return categoryBySchoolRepository.findAll();
    }
}
