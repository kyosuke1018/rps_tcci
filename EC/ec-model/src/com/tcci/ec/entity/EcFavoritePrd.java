/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_FAVORITE_PRD")
@NamedQueries({
    @NamedQuery(name = "EcFavoritePrd.findAll", query = "SELECT e FROM EcFavoritePrd e"),
    @NamedQuery(name = "EcFavoritePrd.findByMember", query = "SELECT e FROM EcFavoritePrd e WHERE e.member=:member order by e.createtime desc"),
    @NamedQuery(name = "EcFavoritePrd.findByPrimary", query = "SELECT e FROM EcFavoritePrd e WHERE e.member=:member and e.product=:product")
})
public class EcFavoritePrd implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_FAVORITE_PRD", sequenceName = "SEQ_FAVORITE_PRD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FAVORITE_PRD")
    private Long id;
//    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcCustomer customer;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcMember member;
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcProduct product;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcFavoritePrd() {
    }

    public EcFavoritePrd(Long id) {
        this.id = id;
    }
    
    public EcFavoritePrd(EcMember member, EcProduct product) {
        this.member = member;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EcMember getMember() {
        return member;
    }

    public void setMember(EcMember member) {
        this.member = member;
    }

    public EcProduct getProduct() {
        return product;
    }

    public void setProduct(EcProduct product) {
        this.product = product;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
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
        if (!(object instanceof EcFavoritePrd)) {
            return false;
        }
        EcFavoritePrd other = (EcFavoritePrd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCusCur[ id=" + id + " ]";
    }

}
