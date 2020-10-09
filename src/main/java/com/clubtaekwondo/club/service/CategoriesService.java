package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Categories;

import java.util.List;

public interface CategoriesService {

    Categories saveCategories(Categories categories);

    void deleteCategories(Categories categories);

    List<Categories> getAllCategory();

    Categories findById(Long id);

    void deleteAllCategories(List<Categories> categoriesList);
}
