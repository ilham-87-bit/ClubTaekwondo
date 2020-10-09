package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.repository.CategoriesRepository;
import com.clubtaekwondo.club.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;


    @Override
    public Categories saveCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }



    @Override
    public void deleteCategories(Categories categories) {
        categoriesRepository.delete(categories);
    }

    @Override
    public List<Categories> getAllCategory() {
        return categoriesRepository.findAll();
    }

    @Override
    public Categories findById(Long id) {

        return categoriesRepository.getOne(id);
    }

    @Override
    public void deleteAllCategories(List<Categories> categoriesList) {
        for(Categories category : categoriesList){
            categoriesRepository.delete(category);
        }
    }
}
