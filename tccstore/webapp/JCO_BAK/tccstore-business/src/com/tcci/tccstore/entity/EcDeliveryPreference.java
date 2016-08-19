/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_DELIVERY_PREFERENCE")
@NamedQueries({
    @NamedQuery(name = "EcDeliveryPreference.findAll", query = "SELECT e FROM EcDeliveryPreference e"),
    @NamedQuery(name = "EcDeliveryPreference.findByMember", 
            query = "SELECT e.ecDeliveryPlace FROM EcDeliveryPreference e"
                    + " WHERE e.ecMember=:ecMember"
                    + " AND e.ecDeliveryPlace.active=TRUE"
                    + " AND e.ecPlant.active=TRUE"
                    + " ORDER BY e.ecDeliveryPlace.name, e.ecPlant.code"),
    @NamedQuery(name = "EcDeliveryPreference.findByMember2", 
            query = "SELECT e FROM EcDeliveryPreference e"
                    + " WHERE e.ecMember=:ecMember"
                    + " AND e.ecDeliveryPlace.active=TRUE"
                    + " AND e.ecPlant.active=TRUE"
                    + " ORDER BY e.ecDeliveryPlace.name, e.ecPlant.code")
})
public class EcDeliveryPreference implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcDeliveryPreferencePK ecDeliveryPreferencePK;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcMember ecMember;
    @JoinColumn(name = "DELIVERY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcDeliveryPlace ecDeliveryPlace;
    @JoinColumn(name = "PLANT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcPlant ecPlant;

    public EcDeliveryPreference() {
    }

    public EcDeliveryPreference(EcDeliveryPreferencePK ecDeliveryPreferencePK) {
        this.ecDeliveryPreferencePK = ecDeliveryPreferencePK;
    }

    public EcDeliveryPreference(long memberId, long deliveryId, long plantId) {
        this.ecDeliveryPreferencePK = new EcDeliveryPreferencePK(memberId, deliveryId, plantId);
    }

    public EcDeliveryPreferencePK getEcDeliveryPreferencePK() {
        return ecDeliveryPreferencePK;
    }

    public void setEcDeliveryPreferencePK(EcDeliveryPreferencePK ecDeliveryPreferencePK) {
        this.ecDeliveryPreferencePK = ecDeliveryPreferencePK;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public EcDeliveryPlace getEcDeliveryPlace() {
        return ecDeliveryPlace;
    }

    public void setEcDeliveryPlace(EcDeliveryPlace ecDeliveryPlace) {
        this.ecDeliveryPlace = ecDeliveryPlace;
    }

    public EcPlant getEcPlant() {
        return ecPlant;
    }

    public void setEcPlant(EcPlant ecPlant) {
        this.ecPlant = ecPlant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ecDeliveryPreferencePK != null ? ecDeliveryPreferencePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcDeliveryPreference)) {
            return false;
        }
        EcDeliveryPreference other = (EcDeliveryPreference) object;
        if ((this.ecDeliveryPreferencePK == null && other.ecDeliveryPreferencePK != null) || (this.ecDeliveryPreferencePK != null && !this.ecDeliveryPreferencePK.equals(other.ecDeliveryPreferencePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcDeliveryPreference[ ecDeliveryPreferencePK=" + ecDeliveryPreferencePK + " ]";
    }
    
}
