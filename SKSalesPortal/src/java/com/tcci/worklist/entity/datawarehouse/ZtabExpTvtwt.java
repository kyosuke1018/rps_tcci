/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nEO
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_TVTWT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpTvtwt.findAll", query = "SELECT z FROM ZtabExpTvtwt z"),
    @NamedQuery(name = "ZtabExpTvtwt.findByMandt", query = "SELECT z FROM ZtabExpTvtwt z WHERE z.ztabExpTvtwtPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpTvtwt.findBySpras", query = "SELECT z FROM ZtabExpTvtwt z WHERE z.ztabExpTvtwtPK.spras = :spras"),
    @NamedQuery(name = "ZtabExpTvtwt.findByVtweg", query = "SELECT z FROM ZtabExpTvtwt z WHERE z.ztabExpTvtwtPK.vtweg = :vtweg"),
    @NamedQuery(name = "ZtabExpTvtwt.findByVtext", query = "SELECT z FROM ZtabExpTvtwt z WHERE z.vtext = :vtext")})
public class ZtabExpTvtwt implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpTvtwtPK ztabExpTvtwtPK;
    @Size(max = 40)
    @Column(name = "VTEXT")
    private String vtext;

    public ZtabExpTvtwt() {
    }

    public ZtabExpTvtwt(ZtabExpTvtwtPK ztabExpTvtwtPK) {
        this.ztabExpTvtwtPK = ztabExpTvtwtPK;
    }

    public ZtabExpTvtwt(String mandt, String spras, String vtweg) {
        this.ztabExpTvtwtPK = new ZtabExpTvtwtPK(mandt, spras, vtweg);
    }

    public ZtabExpTvtwtPK getZtabExpTvtwtPK() {
        return ztabExpTvtwtPK;
    }

    public void setZtabExpTvtwtPK(ZtabExpTvtwtPK ztabExpTvtwtPK) {
        this.ztabExpTvtwtPK = ztabExpTvtwtPK;
    }

    public String getVtext() {
        return vtext;
    }

    public void setVtext(String vtext) {
        this.vtext = vtext;
    }

    public String getVtweg() {
        return this.ztabExpTvtwtPK.getVtweg();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ztabExpTvtwtPK != null ? ztabExpTvtwtPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpTvtwt)) {
            return false;
        }
        ZtabExpTvtwt other = (ZtabExpTvtwt) object;
        if ((this.ztabExpTvtwtPK == null && other.ztabExpTvtwtPK != null) || (this.ztabExpTvtwtPK != null && !this.ztabExpTvtwtPK.equals(other.ztabExpTvtwtPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.worklist.entity.datawarehouse.ZtabExpTvtwt[ ztabExpTvtwtPK=" + ztabExpTvtwtPK + " ]";
    }
}
