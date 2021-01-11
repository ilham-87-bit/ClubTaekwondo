package com.clubtaekwondo.club.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tarif")
public class Tariff implements Serializable {

    @EmbeddedId
    private TariffPK tariffPK;

    private float prix;

    public Tariff(TariffPK tariffPK, float prix) {
        this.tariffPK = tariffPK;
        this.prix = prix;
    }

    public Tariff() {
    }

    public Tariff(Long idCategory, Long idPeriod, Long idType) {
        this.tariffPK = new TariffPK(idCategory, idPeriod, idType);
    }

    public TariffPK getTariffPK() {
        return tariffPK;
    }

    public void setTariffPK(TariffPK tariffPK) {
        this.tariffPK = tariffPK;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }


    @Override
    public String toString() {
        return "model.Tarif[ id=" + this.getTariffPK() + " ]";
    }
}
