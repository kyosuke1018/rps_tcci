/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Neo.Fu
 */
@Embeddable
public class ZtabExpBerslPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 24)
    @Column(name = "BNAME")
    private String bname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "BERSL")
    private String bersl;

    public ZtabExpBerslPK() {
    }

    public ZtabExpBerslPK(String bname, String bersl) {
        this.bname = bname;
        this.bersl = bersl;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBersl() {
        return bersl;
    }

    public void setBersl(String bersl) {
        this.bersl = bersl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bname != null ? bname.hashCode() : 0);
        hash += (bersl != null ? bersl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZtabExpBerslPK)) {
            return false;
        }
        ZtabExpBerslPK other = (ZtabExpBerslPK) object;
        if ((this.bname == null && other.bname != null) || (this.bname != null && !this.bname.equals(other.bname))) {
            return false;
        }
        if ((this.bersl == null && other.bersl != null) || (this.bersl != null && !this.bersl.equals(other.bersl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.worklist.entity.datawarehouse.ZtabExpBerslPK[ bname=" + bname + ", bersl=" + bersl + " ]";
    }
    
}
