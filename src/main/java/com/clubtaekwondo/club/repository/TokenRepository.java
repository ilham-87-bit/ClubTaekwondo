package com.clubtaekwondo.club.repository;

import com.clubtaekwondo.club.model.Categories;
import com.clubtaekwondo.club.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {


}
