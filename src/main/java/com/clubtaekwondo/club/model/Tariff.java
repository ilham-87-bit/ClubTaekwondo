package com.clubtaekwondo.club.model;


import javax.persistence.*;

@Entity
@Table(name = "tarif")
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif")
    private Long idTarif;

    @ManyToOne
    @JoinColumn(name="id_categorie")
   private Categories category ;
    @ManyToOne
    @JoinColumn(name="id_type_abonnement")
   private SubscriptionType type ;
    @ManyToOne
    @JoinColumn(name="id_dure_abonnement")
   private SubscriptionPeriod period ;

    private float prix ;

    public Tariff(Long idTarif, Categories category, SubscriptionType type, SubscriptionPeriod period, float prix) {
        this.idTarif = idTarif;
        this.category = category;
        this.type = type;
        this.period = period;
        this.prix = prix;
    }

    public Tariff() {
    }

    public Long getIdTarif() {
        return idTarif;
    }

    public void setIdTarif(Long idTarif) {
        this.idTarif = idTarif;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public SubscriptionPeriod getPeriod() {
        return period;
    }

    public void setPeriod(SubscriptionPeriod period) {
        this.period = period;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}
