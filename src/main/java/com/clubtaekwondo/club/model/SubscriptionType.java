package com.clubtaekwondo.club.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "type_abonnement")
public class SubscriptionType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_type_abonnement")
    private Long id;
    @Column(name = "nbr_heur")
    private Integer nbrHours;

    private String description;

    @OneToMany(mappedBy = "type")
    private Set<Tariff> tariffs;

    public SubscriptionType(Long id, Integer nbrHours, String description, Set<Tariff> tariffs) {
        this.id = id;
        this.nbrHours = nbrHours;
        this.description = description;
        this.tariffs = tariffs;
    }

    public SubscriptionType() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long idSubscriptionType) {
        this.id = idSubscriptionType;
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
}
