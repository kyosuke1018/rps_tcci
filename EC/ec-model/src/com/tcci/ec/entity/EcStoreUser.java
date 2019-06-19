/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_STORE_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcStoreUser.findAll", query = "SELECT e FROM EcStoreUser e")
    , @NamedQuery(name = "EcStoreUser.findById", query = "SELECT e FROM EcStoreUser e WHERE e.id = :id")
    , @NamedQuery(name = "EcStoreUser.findByKey", query = "SELECT e FROM EcStoreUser e WHERE e.storeId = :storeId AND e.memberId = :memberId")
    , @NamedQuery(name = "EcStoreUser.findByMember", query = "SELECT e FROM EcStoreUser e WHERE e.memberId = :memberId")
    , @NamedQuery(name = "EcStoreUser.findByStoreId", query = "SELECT e FROM EcStoreUser e WHERE e.storeId = :storeId")})
public class EcStoreUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE_USER", sequenceName = "SEQ_STORE_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE_USER")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "OWNER")
    private Boolean owner;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "FI_USER")
    private Boolean fiUser;
    
    @Column(name = "MEMO")
    private String memo;

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

    public EcStoreUser() {
    }

    public EcStoreUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFiUser() {
        return fiUser;
    }

    public void setFiUser(Boolean fiUser) {
        this.fiUser = fiUser;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        if (!(object instanceof EcStoreUser)) {
            return false;
        }
        EcStoreUser other = (EcStoreUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcStoreUser[ id=" + id + " ]";
    }
    
}
