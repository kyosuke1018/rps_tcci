/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.model.partner;

import com.tcci.ecdemo.entity.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Neo.Fu
 */
public class Partner {
    private Long id;
    private String name;
    private String description;
    private String phone;
    private String social;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String theme;
    private short status;
    private Date createtime;
    private short active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ecPartner")
    private List<EcPartnerProduct> ecPartnerProductList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partnerId")
    private List<EcPartnerComment> ecPartnerCommentList;
    @JoinColumn(name = "OWNER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember owner;

    public Partner() {
    }

    public Partner(Long id) {
        this.id = id;
    }

    public Partner(Long id, String name, short status, Date createtime, short active) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createtime = createtime;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public short getActive() {
        return active;
    }

    public void setActive(short active) {
        this.active = active;
    }

    public List<EcPartnerProduct> getEcPartnerProductList() {
        return ecPartnerProductList;
    }

    public void setEcPartnerProductList(List<EcPartnerProduct> ecPartnerProductList) {
        this.ecPartnerProductList = ecPartnerProductList;
    }

    public List<EcPartnerComment> getEcPartnerCommentList() {
        return ecPartnerCommentList;
    }

    public void setEcPartnerCommentList(List<EcPartnerComment> ecPartnerCommentList) {
        this.ecPartnerCommentList = ecPartnerCommentList;
    }

    public EcMember getOwner() {
        return owner;
    }

    public void setOwner(EcMember owner) {
        this.owner = owner;
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
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ecdemo.entity.EcPartner[ id=" + id + " ]";
    }
    
}
