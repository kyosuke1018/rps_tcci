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
 * @author neo
 */
@Entity
@Cacheable(false)
@Table(name = "SD_ZTAB_EXP_TJ10T")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZtabExpTj10t.findAll", query = "SELECT z FROM ZtabExpTj10t z"),
    @NamedQuery(name = "ZtabExpTj10t.findByMandt", query = "SELECT z FROM ZtabExpTj10t z WHERE z.ztabExpTj10tPK.mandt = :mandt"),
    @NamedQuery(name = "ZtabExpTj10t.findByBersl", query = "SELECT z FROM ZtabExpTj10t z WHERE z.ztabExpTj10tPK.bersl = :bersl"),
    @NamedQuery(name = "ZtabExpTj10t.findBySpras", query = "SELECT z FROM ZtabExpTj10t z WHERE z.ztabExpTj10tPK.spras = :spras"),
    @NamedQuery(name = "ZtabExpTj10t.findByTxt", query = "SELECT z FROM ZtabExpTj10t z WHERE z.txt = :txt")})
public class ZtabExpTj10t implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZtabExpTj10tPK ztabExpTj10tPK;
    @Size(max = 60)
    @Column(name = "TXT")
    private String txt;

    public ZtabExpTj10t() {
    }

    public ZtabExpTj10t(ZtabExpTj10tPK ztabExpTj10tPK) {
        this.ztabExpTj10tPK = ztabExpTj10tPK;
    }

    public ZtabExpTj10t(String mandt, String bersl, String spras) {
        this.ztabExpTj10tPK = new ZtabExpTj10tPK(mandt, bersl, spras);
    }

    public ZtabExpTj10tPK getZtabExpTj10tPK() {
        return ztabExpTj10tPK;
    }

    public void setZtabExpTj10tPK(ZtabExpTj10tPK ztabExpTj10tPK) {
        this.ztabExpTj10tPK = ztabExpTj10tPK;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getBersl() {
        return this.getZtabExpTj10tPK().getBersl();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ztabExpTj10tPK != null ? ztabExpTj10tPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpTj10t)) {
            return false;
        }
        ZtabExpTj10t other = (ZtabExpTj10t) object;
        if ((this.ztabExpTj10tPK == null && other.ztabExpTj10tPK != null) || (this.ztabExpTj10tPK != null && !this.ztabExpTj10tPK.equals(other.ztabExpTj10tPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sapapprover.entity.datawarehouse.ZtabExpTj10t[ ztabExpTj10tPK=" + ztabExpTj10tPK + " ]";
    }
}
