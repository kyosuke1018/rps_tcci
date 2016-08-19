/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import java.io.Serializable;
import java.util.List;
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

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_GOODS")
@NamedQueries({
    @NamedQuery(name = "EcGoods.findAll", query = "SELECT e FROM EcGoods e"),
    @NamedQuery(name = "EcGoods.findAllActive", query = "SELECT e FROM EcGoods e WHERE e.active=TRUE ORDER BY e.name")
})
public class EcGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 18)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "NAME")
    private String name;
    @Size(max = 500)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecGoods")
    private List<EcGoodsBuy> ecGoodsBuyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecGoods")
    private List<EcPartnerGoods> ecPartnerGoodsList;

    public EcGoods() {
    }

    public EcGoods(Long id) {
        this.id = id;
    }

    public EcGoods(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<EcGoodsBuy> getEcGoodsBuyList() {
        return ecGoodsBuyList;
    }

    public void setEcGoodsBuyList(List<EcGoodsBuy> ecGoodsBuyList) {
        this.ecGoodsBuyList = ecGoodsBuyList;
    }

    public List<EcPartnerGoods> getEcPartnerGoodsList() {
        return ecPartnerGoodsList;
    }

    public void setEcPartnerGoodsList(List<EcPartnerGoods> ecPartnerGoodsList) {
        this.ecPartnerGoodsList = ecPartnerGoodsList;
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
        if (!(object instanceof EcGoods)) {
            return false;
        }
        EcGoods other = (EcGoods) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcGoods[ id=" + id + " ]";
    }
    
}
