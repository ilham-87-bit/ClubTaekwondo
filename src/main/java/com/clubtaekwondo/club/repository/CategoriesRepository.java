package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    @Override
    List<Categories> findAll();
}
