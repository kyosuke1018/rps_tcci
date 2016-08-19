/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Embeddable
public class EcVehiclePreferencePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "VEHICLE")
    private String vehicle;

    public EcVehiclePreferencePK() {
    }

    public EcVehiclePreferencePK(long memberId, String vehicle) {
        this.memberId = memberId;
        this.vehicle = vehicle;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) memberId;
        hash += (vehicle != null ? vehicle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcVehiclePreferencePK)) {
            return false;
        }
        EcVehiclePreferencePK other = (EcVehiclePreferencePK) object;
        if (this.memberId != other.memberId) {
            return false;
        }
        if ((this.vehicle == null && other.vehicle != null) || (this.vehicle != null && !this.vehicle.equals(other.vehicle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcVehiclePreferencePK[ memberId=" + memberId + ", vehicle=" + vehicle + " ]";
    }
    
}
