package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.User;

import java.util.List;

public interface UserService {

    User save(User user, Role role);

    User save(User user);

    User findByLogin(String login);

    List<User> getAllUser();
}
