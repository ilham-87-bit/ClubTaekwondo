package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.User;

public interface UserService {

    User save(User user, Role role);

    User save(User user);

    User findByLogin(String login);
}
