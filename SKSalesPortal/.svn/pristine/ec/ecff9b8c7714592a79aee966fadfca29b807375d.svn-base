/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neo.Fu
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_BERSL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpBersl.findAll", query = "SELECT z FROM ZtabExpBersl z"),
    @NamedQuery(name = "ZtabExpBersl.findByBname", query = "SELECT z FROM ZtabExpBersl z WHERE z.ztabExpBerslPK.bname = :bname"),
    @NamedQuery(name = "ZtabExpBersl.findByBersl", query = "SELECT z FROM ZtabExpBersl z WHERE z.ztabExpBerslPK.bersl = :bersl")})
public class ZtabExpBersl implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpBerslPK ztabExpBerslPK;

    public ZtabExpBersl() {
    }

    public ZtabExpBersl(ZtabExpBerslPK ztabExpBerslPK) {
        this.ztabExpBerslPK = ztabExpBerslPK;
    }

    public ZtabExpBersl(String bname, String bersl) {
        this.ztabExpBerslPK = new ZtabExpBerslPK(bname, bersl);
    }

    public ZtabExpBerslPK getZtabExpBerslPK() {
        return ztabExpBerslPK;
    }

    public void setZtabExpBerslPK(ZtabExpBerslPK ztabExpBerslPK) {
        this.ztabExpBerslPK = ztabExpBerslPK;
    }

    public String getBname() {
        return this.ztabExpBerslPK.getBname();
    }

    public String getBersl() {
        return this.ztabExpBerslPK.getBersl();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ztabExpBerslPK != null ? ztabExpBerslPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpBersl)) {
            return false;
        }
        ZtabExpBersl other = (ZtabExpBersl) object;
        if((this.ztabExpBerslPK == null && other.ztabExpBerslPK != null) ||  (this.ztabExpBerslPK != null && !this.ztabExpBerslPK.getBersl().equals(other.ztabExpBerslPK.getBersl()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.worklist.entity.datawarehouse.ZtabExpBersl[ ztabExpBerslPK=" + ztabExpBerslPK + " ]";
    }
}
