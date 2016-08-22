/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_CALENDAR")
@Cacheable(value=false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkCalendar.findAll", query = "SELECT s FROM SkCalendar s"),
    @NamedQuery(name = "SkCalendar.findById", query = "SELECT s FROM SkCalendar s WHERE s.id = :id"),
    @NamedQuery(name = "SkCalendar.findByBaselineTimestamp", query = "SELECT s FROM SkCalendar s WHERE s.baselineTimestamp = :baselineTimestamp"),
    @NamedQuery(name = "SkCalendar.findByIsworkingday", query = "SELECT s FROM SkCalendar s WHERE s.isworkingday = :isworkingday")})
public class SkCalendar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Column(name = "BASELINE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date baselineTimestamp;
    @Column(name = "ISWORKINGDAY")
    private boolean isworkingday;

    public SkCalendar() {
    }

    public SkCalendar(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBaselineTimestamp() {
        return baselineTimestamp;
    }

    public void setBaselineTimestamp(Date baselineTimestamp) {
        this.baselineTimestamp = baselineTimestamp;
    }

    public boolean getIsworkingday() {
        return isworkingday;
    }

    public void setIsworkingday(boolean isworkingday) {
        this.isworkingday = isworkingday;
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
        if (!(object instanceof SkCalendar)) {
            return false;
        }
        SkCalendar other = (SkCalendar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkCalendar[ id=" + id + " ]";
    }
    
}
