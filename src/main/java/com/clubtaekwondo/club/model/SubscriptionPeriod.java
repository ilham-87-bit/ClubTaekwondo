package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dure_abonnement")
public class SubscriptionPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dure_abonnement")
    private Long id;

    @Column(name = "durée")
    private String period;

    @Column(name = "nbr_mois")
    private Integer nbrMonth;

    @Column(name = "description")
    private String description;


    public SubscriptionPeriod(Long id, String period, Integer nbrMonth, String description) {
        this.id = id;
        this.period = period;
        this.nbrMonth = nbrMonth;
        this.description = description;
    }

    public SubscriptionPeriod() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNbrMonth() {
        return nbrMonth;
    }

    public void setNbrMonth(Integer nbrMonth) {
        this.nbrMonth = nbrMonth;
    }
}
