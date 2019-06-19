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
@Table(name = "EC_PREREQUISTITE_IDS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcPrerequistiteIds.findAll", query = "SELECT e FROM EcPrerequistiteIds e")
    , @NamedQuery(name = "EcPrerequistiteIds.findById", query = "SELECT e FROM EcPrerequistiteIds e WHERE e.id = :id")
    , @NamedQuery(name = "EcPrerequistiteIds.findByType", query = "SELECT e FROM EcPrerequistiteIds e WHERE e.type = :type")
    , @NamedQuery(name = "EcPrerequistiteIds.findByMid", query = "SELECT e FROM EcPrerequistiteIds e WHERE e.mid = :mid")
    , @NamedQuery(name = "EcPrerequistiteIds.findByRid", query = "SELECT e FROM EcPrerequistiteIds e WHERE e.rid = :rid")})
public class EcPrerequistiteIds implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_PREREQUISTITE_IDS", sequenceName = "SEQ_PREREQUISTITE_IDS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PREREQUISTITE_IDS")        
    private Long id;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "MID")
    private Long mid;
    @Column(name = "RID")
    private Long rid;

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

    public EcPrerequistiteIds() {
    }

    public EcPrerequistiteIds(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
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
        if (!(object instanceof EcPrerequistiteIds)) {
            return false;
        }
        EcPrerequistiteIds other = (EcPrerequistiteIds) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcPrerequistiteIds[ id=" + id + " ]";
    }
    
}
