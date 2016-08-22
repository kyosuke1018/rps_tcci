/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author carl.lin
 */
@Embeddable
public class FactWeb3PK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "YYYYMM")
    private String yyyymm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "AREAID")
    private String areaid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "SAPID")
    private String sapid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "DOMAINID")
    private String domainid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ACCESSID")
    private String accessid;

    public FactWeb3PK() {
    }

    public FactWeb3PK(String yyyymm, String areaid, String sapid, String domainid, String accessid) {
        this.yyyymm = yyyymm;
        this.areaid = areaid;
        this.sapid = sapid;
        this.domainid = domainid;
        this.accessid = accessid;
    }

    public String getYyyymm() {
        return yyyymm;
    }

    public void setYyyymm(String yyyymm) {
        this.yyyymm = yyyymm;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public String getDomainid() {
        return domainid;
    }

    public void setDomainid(String domainid) {
        this.domainid = domainid;
    }

    public String getAccessid() {
        return accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (yyyymm != null ? yyyymm.hashCode() : 0);
        hash += (areaid != null ? areaid.hashCode() : 0);
        hash += (sapid != null ? sapid.hashCode() : 0);
        hash += (domainid != null ? domainid.hashCode() : 0);
        hash += (accessid != null ? accessid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactWeb3PK)) {
            return false;
        }
        FactWeb3PK other = (FactWeb3PK) object;
        if ((this.yyyymm == null && other.yyyymm != null) || (this.yyyymm != null && !this.yyyymm.equals(other.yyyymm))) {
            return false;
        }
        if ((this.areaid == null && other.areaid != null) || (this.areaid != null && !this.areaid.equals(other.areaid))) {
            return false;
        }
        if ((this.sapid == null && other.sapid != null) || (this.sapid != null && !this.sapid.equals(other.sapid))) {
            return false;
        }
        if ((this.domainid == null && other.domainid != null) || (this.domainid != null && !this.domainid.equals(other.domainid))) {
            return false;
        }
        if ((this.accessid == null && other.accessid != null) || (this.accessid != null && !this.accessid.equals(other.accessid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactWeb3PK[ yyyymm=" + yyyymm + ", areaid=" + areaid + ", sapid=" + sapid + ", domainid=" + domainid + ", accessid=" + accessid + " ]";
    }
    
}
