package com.clubtaekwondo.club.service;

import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.UserRole;

import java.util.List;

public interface UserRoleService {

    List<UserRole> getAllUserRole();

    UserRole findByRole(Role role);
}
