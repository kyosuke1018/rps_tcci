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
@Table(name = "EC_VEHICLE_PREFERENCE")
@NamedQueries({
    @NamedQuery(name = "EcVehiclePreference.findAll", query = "SELECT e FROM EcVehiclePreference e"),
    @NamedQuery(name = "EcVehiclePreference.findByMember", query = "SELECT e.pk.vehicle FROM EcVehiclePreference e WHERE e.ecMember=:ecMember ORDER BY e.pk.vehicle")
})
public class EcVehiclePreference implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EcVehiclePreferencePK pk;
    @Size(max = 20)
    @Column(name = "DUMMY")
    private String dummy;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EcMember ecMember;

    public EcVehiclePreference() {
    }

    public EcVehiclePreference(EcVehiclePreferencePK ecVehiclePreferencePK) {
        this.pk = ecVehiclePreferencePK;
    }

    public EcVehiclePreference(long memberId, String vehicle) {
        this.pk = new EcVehiclePreferencePK(memberId, vehicle);
    }

    public EcVehiclePreferencePK getPk() {
        return pk;
    }

    public void setPk(EcVehiclePreferencePK pk) {
        this.pk = pk;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcVehiclePreference)) {
            return false;
        }
        EcVehiclePreference other = (EcVehiclePreference) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcVehiclePreference[ ecVehiclePreferencePK=" + pk + " ]";
    }
    
}
