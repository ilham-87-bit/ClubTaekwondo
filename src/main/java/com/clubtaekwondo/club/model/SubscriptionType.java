package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "type_abonnement")
public class SubscriptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_abonnement")
    private Long idType;
    @Column(name = "nbr_heur")
    private Integer nbrHours;

    private String description;

    @OneToMany(mappedBy = "type")
    private Set<Tariff> tariffs;

    @OneToMany(mappedBy = "subscriptionType")
    private Set<TimeTable> timeTables;

    public SubscriptionType(Long idType, Integer nbrHours, String description, Set<Tariff> tariffs, Set<TimeTable> timeTables) {
        this.idType = idType;
        this.nbrHours = nbrHours;
        this.description = description;
        this.tariffs = tariffs;
        this.timeTables = timeTables;
    }

    public SubscriptionType() {

    }

    public Long getIdType() {
        return idType;
    }

    public void setIdType(Long idSubscriptionType) {
        this.idType = idSubscriptionType;
    }

    public Integer getNbrHours() {
        return nbrHours;
    }

    public void setNbrHours(Integer nbrHours) {
        this.nbrHours = nbrHours;
    }

    public Set<Tariff> getTariffs() {
        return tariffs;
    }

    public void setTariffs(Set<Tariff> tariffs) {
        this.tariffs = tariffs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TimeTable> getTimeTables() {
        return timeTables;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        this.timeTables = timeTables;
    }
}
