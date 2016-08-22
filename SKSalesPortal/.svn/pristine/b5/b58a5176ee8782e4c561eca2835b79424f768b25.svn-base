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
public class FactWeb2PK implements Serializable {
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
    @Size(min = 1, max = 2)
    @Column(name = "DOMAINID")
    private String domainid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ACCESSID")
    private String accessid;

    public FactWeb2PK() {
    }

    public FactWeb2PK(String yyyymm, String areaid, String domainid, String accessid) {
        this.yyyymm = yyyymm;
        this.areaid = areaid;
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
        hash += (domainid != null ? domainid.hashCode() : 0);
        hash += (accessid != null ? accessid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactWeb2PK)) {
            return false;
        }
        FactWeb2PK other = (FactWeb2PK) object;
        if ((this.yyyymm == null && other.yyyymm != null) || (this.yyyymm != null && !this.yyyymm.equals(other.yyyymm))) {
            return false;
        }
        if ((this.areaid == null && other.areaid != null) || (this.areaid != null && !this.areaid.equals(other.areaid))) {
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
        return "com.tcci.sksp.entity.FactWeb2PK[ yyyymm=" + yyyymm + ", areaid=" + areaid + ", domainid=" + domainid + ", accessid=" + accessid + " ]";
    }
    
}
