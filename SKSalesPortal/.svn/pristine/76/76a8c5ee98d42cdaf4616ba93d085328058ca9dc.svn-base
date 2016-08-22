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
@Table(name = "SD_ZTAB_EXP_TVKOT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpTvkot.findAll", query = "SELECT z FROM ZtabExpTvkot z"),
    @NamedQuery(name = "ZtabExpTvkot.findByMandt", query = "SELECT z FROM ZtabExpTvkot z WHERE z.ztabExpTvkotPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpTvkot.findBySpras", query = "SELECT z FROM ZtabExpTvkot z WHERE z.ztabExpTvkotPK.spras = :spras"),
    @NamedQuery(name = "ZtabExpTvkot.findByVkorg", query = "SELECT z FROM ZtabExpTvkot z WHERE z.ztabExpTvkotPK.vkorg = :vkorg"),
    @NamedQuery(name = "ZtabExpTvkot.findByVtext", query = "SELECT z FROM ZtabExpTvkot z WHERE z.vtext = :vtext")})
public class ZtabExpTvkot implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpTvkotPK ztabExpTvkotPK;
    @Size(max = 40)
    @Column(name = "VTEXT")
    private String vtext;

    public ZtabExpTvkot() {
    }

    public ZtabExpTvkot(ZtabExpTvkotPK ztabExpTvkotPK) {
        this.ztabExpTvkotPK = ztabExpTvkotPK;
    }

    public ZtabExpTvkot(String mandt, String spras, String vkorg) {
        this.ztabExpTvkotPK = new ZtabExpTvkotPK(mandt, spras, vkorg);
    }

    public ZtabExpTvkotPK getZtabExpTvkotPK() {
        return ztabExpTvkotPK;
    }

    public void setZtabExpTvkotPK(ZtabExpTvkotPK ztabExpTvkotPK) {
        this.ztabExpTvkotPK = ztabExpTvkotPK;
    }

    public String getVtext() {
        return vtext;
    }

    public void setVtext(String vtext) {
        this.vtext = vtext;
    }
    
    public String getVkorg() {
        return this.ztabExpTvkotPK.getVkorg();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ztabExpTvkotPK != null ? ztabExpTvkotPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpTvkot)) {
            return false;
        }
        ZtabExpTvkot other = (ZtabExpTvkot) object;
        if ((this.ztabExpTvkotPK == null && other.ztabExpTvkotPK != null) || (this.ztabExpTvkotPK != null && !this.ztabExpTvkotPK.equals(other.ztabExpTvkotPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.worklist.entity.datawarehouse.ZtabExpTvkot[ ztabExpTvkotPK=" + ztabExpTvkotPK + " ]";
    }
    
}
