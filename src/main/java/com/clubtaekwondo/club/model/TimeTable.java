package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "horaire")
public class TimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_heur")
    private Long idTime;

    @ManyToOne
    @JoinColumn(name = "id_categori")
    private Categories c;
    @ManyToOne
    @JoinColumn(name = "id_ecole")
    private School s;
    @ManyToOne
    @JoinColumn(name = "id_jour")
    private Day day;

    @ManyToMany()
    @JoinTable(name = "encadrer",
            joinColumns = {@JoinColumn(name = "id_horaire")},
            inverseJoinColumns = {@JoinColumn(name = "id_entraineur")})
    private List<Coach> coachList;

    @ManyToMany()
    @JoinTable(name = "concerne",
            joinColumns = {@JoinColumn(name = "id_horaire")},
            inverseJoinColumns = {@JoinColumn(name = "id_type_abonnement")})
    private List<SubscriptionType> subscriptionTypeList;

    @Column(name = "heur_debut")
    private String startTime;

    @Column(name = "heur_fin")
    private String endTime;

    public TimeTable(Long idTime, Categories c, School s, Day day, List<Coach> coachList, List<SubscriptionType> subscriptionTypeList, String startTime, String endTime) {
        this.idTime = idTime;
        this.c = c;
        this.s = s;
        this.day = day;
        this.coachList = coachList;
        this.subscriptionTypeList = subscriptionTypeList;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeTable() {

    }

    public Long getIdTime() {
        return idTime;
    }

    public void setIdTime(Long idTime) {
        this.idTime = idTime;
    }

    public Categories getC() {
        return c;
    }

    public void setC(Categories c) {
        this.c = c;
    }

    public School getS() {
        return s;
    }

    public void setS(School s) {
        this.s = s;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<SubscriptionType> getSubscriptionTypeList() {
        return subscriptionTypeList;
    }

    public void setSubscriptionTypeList(List<SubscriptionType> subscriptionTypeList) {
        this.subscriptionTypeList = subscriptionTypeList;
    }

    public List<Coach> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<Coach> coachList) {
        this.coachList = coachList;
    }
}
