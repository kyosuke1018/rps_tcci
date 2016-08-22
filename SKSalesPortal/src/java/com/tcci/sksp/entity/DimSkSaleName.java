/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carl.lin
 */
@Entity
@Table(name = "DIM_SK_SALE_NAME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DimSkSaleName.findAll", query = "SELECT d FROM DimSkSaleName d"),
    @NamedQuery(name = "DimSkSaleName.findBySapId", query = "SELECT d FROM DimSkSaleName d WHERE d.sapId = :sapId"),
    @NamedQuery(name = "DimSkSaleName.findByCname", query = "SELECT d FROM DimSkSaleName d WHERE d.cname = :cname")})
public class DimSkSaleName implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "SAP_ID")
    private String sapId;
    @Size(max = 20)
    @Column(name = "CNAME")
    private String cname;
    @JoinColumn(name = "CODE", referencedColumnName = "CODE")
    @ManyToOne(optional = false)
    private DimSkChannelName code;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dimSkSaleName")
    private Collection<FactSkAchievementMonth> factSkAchievementMonthCollection;

    public DimSkSaleName() {
    }

    public DimSkSaleName(String sapId) {
        this.sapId = sapId;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public DimSkChannelName getCode() {
        return code;
    }

    public void setCode(DimSkChannelName code) {
        this.code = code;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<FactSkAchievementMonth> getFactSkAchievementMonthCollection() {
        return factSkAchievementMonthCollection;
    }

    public void setFactSkAchievementMonthCollection(Collection<FactSkAchievementMonth> factSkAchievementMonthCollection) {
        this.factSkAchievementMonthCollection = factSkAchievementMonthCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sapId != null ? sapId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DimSkSaleName)) {
            return false;
        }
        DimSkSaleName other = (DimSkSaleName) object;
        if ((this.sapId == null && other.sapId != null) || (this.sapId != null && !this.sapId.equals(other.sapId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.DimSkSaleName[ sapId=" + sapId + " ]";
    }
    
}
