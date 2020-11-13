package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "jour")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jour")
    private Long idDay;

    @Column(name = "jour")
    private String day;

    @OneToMany(mappedBy = "day")
    private Set<TimeTable> timeTables;

    public Day(Long idDay, String day) {
        this.idDay = idDay;
        this.day = day;
    }

    public Day() {

    }

    public Long getIdDay() {
        return idDay;
    }

    public void setIdDay(Long idDay) {
        this.idDay = idDay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Set<TimeTable> getTimes() {
        return timeTables;
    }

    public void setTimes(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
