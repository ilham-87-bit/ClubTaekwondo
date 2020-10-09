package com.clubtaekwondo.club.model;

import javax.persistence.*;

@Entity
@Table(name = "role_utilisateur")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_role")
    private Long idUserRole;

    @Column(name = "role")
    private String role;

    public UserRole( String role) {

        this.role = role;
    }

    public UserRole() {

    }

    public Long getIdUserRole() {
        return idUserRole;
    }

    public void setIdUserRole(Long idUserRole) {
        this.idUserRole = idUserRole;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
