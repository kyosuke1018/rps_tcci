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
@Table(name = "DIM_SK_CHANNEL_NAME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DimSkChannelName.findAll", query = "SELECT d FROM DimSkChannelName d"),
    @NamedQuery(name = "DimSkChannelName.findByCode", query = "SELECT d FROM DimSkChannelName d WHERE d.code = :code"),
    @NamedQuery(name = "DimSkChannelName.findByName", query = "SELECT d FROM DimSkChannelName d WHERE d.name = :name")})
public class DimSkChannelName implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dimSkChannelName")
    private Collection<FactSkAchievementChannel> factSkAchievementChannelCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 70)
    @Column(name = "NAME")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "code")
    private Collection<DimSkSaleName> dimSkSaleNameCollection;

    public DimSkChannelName() {
    }

    public DimSkChannelName(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    @JsonIgnore
    public Collection<DimSkSaleName> getDimSkSaleNameCollection() {
        return dimSkSaleNameCollection;
    }

    public void setDimSkSaleNameCollection(Collection<DimSkSaleName> dimSkSaleNameCollection) {
        this.dimSkSaleNameCollection = dimSkSaleNameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DimSkChannelName)) {
            return false;
        }
        DimSkChannelName other = (DimSkChannelName) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.DimSkChannelName[ code=" + code + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<FactSkAchievementChannel> getFactSkAchievementChannelCollection() {
        return factSkAchievementChannelCollection;
    }

    public void setFactSkAchievementChannelCollection(Collection<FactSkAchievementChannel> factSkAchievementChannelCollection) {
        this.factSkAchievementChannelCollection = factSkAchievementChannelCollection;
    }
    
}
