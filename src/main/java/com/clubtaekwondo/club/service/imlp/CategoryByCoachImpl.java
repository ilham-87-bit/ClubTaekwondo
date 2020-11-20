package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.CategoryByCoach;
import com.clubtaekwondo.club.repository.CategoryByCoachRepository;
import com.clubtaekwondo.club.service.CategoryByCoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryByCoachImpl implements CategoryByCoachService {

    @Autowired
    private CategoryByCoachRepository categoryByCoachRepository;

    @Override
    public CategoryByCoach save(CategoryByCoach categoryByCoach) {
        return categoryByCoachRepository.save(categoryByCoach);
    }

    @Override
    public CategoryByCoach getById(Long id) {
        return categoryByCoachRepository.getOne(id);
    }

    @Override
    public void delete(CategoryByCoach categoryByCoach) {
        categoryByCoachRepository.delete(categoryByCoach);
    }

    @Override
    public List<CategoryByCoach> getAllCategoryByCoach() {
        return categoryByCoachRepository.findAll();
    }
}
