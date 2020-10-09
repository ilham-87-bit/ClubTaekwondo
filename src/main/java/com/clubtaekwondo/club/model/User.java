package com.clubtaekwondo.club.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "utilisateur")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_utilisateur")
    private Long id;

    @Column(name = "nome")
    private String lastName;

    @Column(name = "prenom")
    private String firstName;

    @Column(name = "mot_de_passe")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "id_role_utilisateur")
    private UserRole userRole;

    public User(Long id, String lastName, String firstName, String password, String email, UserRole userRole) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long idUser) {
        this.id = idUser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getUserRole().getRole());
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return  email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
