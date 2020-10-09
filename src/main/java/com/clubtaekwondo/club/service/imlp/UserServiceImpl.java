package com.clubtaekwondo.club.service.imlp;

import com.clubtaekwondo.club.model.Role;
import com.clubtaekwondo.club.model.User;
import com.clubtaekwondo.club.model.UserRole;
import com.clubtaekwondo.club.repository.UserRepository;
import com.clubtaekwondo.club.repository.UserRoleRepository;
import com.clubtaekwondo.club.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    @Override
    public User save(User user, Role role) {
        UserRole role_admin = userRoleRepository.findByRole(role.getAlea());
        if (role_admin == null) {
            role_admin = new UserRole(role.getAlea());
            role_admin = userRoleRepository.save(role_admin);

        }
        user.setUserRole(role_admin);
        // verfier que le login est unique
        if (role == Role.ADMIN && userRepository.getUserCountWithRole(role_admin) > 0) {
            throw new IllegalArgumentException("admin existe déja");
        }
        if (userRepository.findByEmail(user.getEmail()) == null) {
           user = userRepository.save(user);

        }
        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByEmail(login);
    }
}
