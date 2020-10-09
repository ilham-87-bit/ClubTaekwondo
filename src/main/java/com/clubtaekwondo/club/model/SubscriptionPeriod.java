package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dure_abonnement")
public class SubscriptionPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_dure_abonnement")
    private Long id;

    @Column(name = "durée")
    private String period;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "period")
    private Set<Tariff> tariffs;

    public SubscriptionPeriod(Long id, String period, String description, Set<Tariff> tariffs) {
        this.id = id;
        this.period = period;
        this.description = description;
        this.tariffs = tariffs;
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

    public Set<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(Set<Tariff> tariffs) {
        this.tariffs = tariffs;
    }
}
