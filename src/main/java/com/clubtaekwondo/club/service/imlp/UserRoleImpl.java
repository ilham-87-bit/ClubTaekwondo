package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.UserRole;
import com.clubtaekwondo.club.repository.UserRoleRepository;
import com.clubtaekwondo.club.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserRole> getAllUserRole() {

        return userRoleRepository.findAll();
    }

    @Override
    public UserRole findByRole(Role role) {
        return userRoleRepository.findByRole(role.getAlea());
    }
}
