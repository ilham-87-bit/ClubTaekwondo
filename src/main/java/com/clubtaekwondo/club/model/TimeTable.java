package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.sql.Time;

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
    @ManyToOne
    @JoinColumn(name = "id_entraineur")
    private Coach co;

    @Column(name = "heur_debut")
    private Integer startTime;

    @Column(name = "heur_fin")
    private Integer endTime;

    public TimeTable(Long idTime, Categories c, School s, Day day, Coach co, Integer startTime, Integer endTime) {
        this.idTime = idTime;
        this.c = c;
        this.s = s;
        this.day = day;
        this.co = co;
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

    public Coach getCo() {
        return co;
    }

    public void setCo(Coach co) {
        this.co = co;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }
}
