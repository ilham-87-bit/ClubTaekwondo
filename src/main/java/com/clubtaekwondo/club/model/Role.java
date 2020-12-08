package com.clubtaekwondo.club.model;

public enum Role {

    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
//    ,COACH("ROLE_COACH")

    private final String alea;

    Role(String alea) {
        this.alea = alea;
    }

    public String getAlea() {
        return alea;
    }
}
