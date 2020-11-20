package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.CategoryByCoach;

import java.util.List;

public interface CategoryByCoachService {

    CategoryByCoach save(CategoryByCoach categoryByCoach);

    CategoryByCoach getById(Long id);

    void delete(CategoryByCoach categoryByCoach);

    List<CategoryByCoach> getAllCategoryByCoach();
}
