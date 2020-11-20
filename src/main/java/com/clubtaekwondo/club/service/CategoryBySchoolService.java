package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.CategoryBySchool;

import java.util.List;

public interface CategoryBySchoolService {

    CategoryBySchool save(CategoryBySchool categoryBySchool);

    void delete(CategoryBySchool categoryBySchool);

    CategoryBySchool getById(Long id);

    List<CategoryBySchool> getAllCategoryBySchool();
}
