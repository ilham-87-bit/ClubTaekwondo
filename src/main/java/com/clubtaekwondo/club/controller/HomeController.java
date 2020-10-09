package com.clubtaekwondo.club.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/index")
    public String home() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        return ("index");
    }

    @GetMapping(value = "/admin/indexAdmin")
    public String homeAdmin() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        return ("indexAdmin");
    }


//    @GetMapping(value = "/login")
//    public String login()
//    {
//        return ("login");
//    }
//
//    @Autowired
//    private UserRepository userRepository ;
//    @Autowired
//    private UserRoleRepository userRoleRepository ;
//
//    @Autowired
//    private PasswordEncoder encoder ;
//
//
//    @GetMapping(value = "/addAdmin")
//    public String addAdmin()
//    {
//        User user = new User();
//        user.setEmail("admin@takewando.com");
//        user.setLogin("admin");
//        user.setPassword(encoder.encode("admin"));
//        UserRole role_admin = userRoleRepository.findByRole("ROLE_ADMIN");
//        if (role_admin == null){
//            role_admin = new UserRole("ROLE_ADMIN");
//            role_admin = userRoleRepository.save(role_admin);
//
//        }
//        user.setUserRole(role_admin);
//        if( userRepository.findByLogin(user.getLogin()) ==  null){
//            userRepository.save(user);
//        }
//        return ("index");
//    }
}
