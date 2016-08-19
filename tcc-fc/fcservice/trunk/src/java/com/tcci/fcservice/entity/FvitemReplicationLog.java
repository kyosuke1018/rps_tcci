/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Louisz.Cheng
 */
@Entity
@Table(name = "FVITEM_REPLICATION_LOG")
@NamedQueries({
    @NamedQuery(name = "FvitemReplicationLog.findAll", query = "SELECT f FROM FvitemReplicationLog f")})
public class FvitemReplicationLog implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "REPLICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date replicationDate;

    public FvitemReplicationLog() {
    }

    public FvitemReplicationLog(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getReplicationDate() {
        return replicationDate;
    }

    public void setReplicationDate(Date replicationDate) {
        this.replicationDate = replicationDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FvitemReplicationLog)) {
            return false;
        }
        FvitemReplicationLog other = (FvitemReplicationLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcservice.entity.FvitemReplicationLog[ id=" + id + " ]";
    }
    
}
