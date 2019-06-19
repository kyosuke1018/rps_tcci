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
@Table(name = "EC_FAVORITE_STORE")
@NamedQueries({
    @NamedQuery(name = "EcFavoriteStore.findAll", query = "SELECT e FROM EcFavoriteStore e"),
    @NamedQuery(name = "EcFavoriteStore.findByMember", query = "SELECT e FROM EcFavoriteStore e WHERE e.member=:member order by e.createtime desc"),
    @NamedQuery(name = "EcFavoriteStore.findByPrimary", query = "SELECT e FROM EcFavoriteStore e WHERE e.member=:member and e.store=:store")
})
public class EcFavoriteStore implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_FAVORITE_STORE", sequenceName = "SEQ_FAVORITE_STORE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FAVORITE_STORE")
    private Long id;
//    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
//    @ManyToOne
//    private EcCustomer customer;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcMember member;
    @JoinColumn(name = "STORE_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcStore store;
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

    public EcFavoriteStore() {
    }

    public EcFavoriteStore(Long id) {
        this.id = id;
    }
    
    public EcFavoriteStore(EcMember member, EcStore store) {
        this.member = member;
        this.store = store;
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

    public EcStore getStore() {
        return store;
    }

    public void setStore(EcStore store) {
        this.store = store;
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
        if (!(object instanceof EcFavoriteStore)) {
            return false;
        }
        EcFavoriteStore other = (EcFavoriteStore) object;
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
