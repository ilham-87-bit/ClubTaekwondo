package com.clubtaekwondo.club.model;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;


@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private  String token  ;

    @ManyToOne
    @JoinColumn(name =  "id_utilisateur")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
