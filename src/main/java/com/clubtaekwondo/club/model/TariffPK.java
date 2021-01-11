package com.clubtaekwondo.club.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class TariffPK implements Serializable {


    private Long idCategory;

    private Long idType;

    private Long idPeriod;

    public TariffPK(Long idCategory, Long idType, Long idPeriod) {
        this.idCategory = idCategory;
        this.idType = idType;
        this.idPeriod = idPeriod;
    }

    public TariffPK() {
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public Long getIdType() {
        return idType;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
    }

    public Long getIdPeriod() {
        return idPeriod;
    }

    public void setIdPeriod(Long idPeriod) {
        this.idPeriod = idPeriod;
    }
}
