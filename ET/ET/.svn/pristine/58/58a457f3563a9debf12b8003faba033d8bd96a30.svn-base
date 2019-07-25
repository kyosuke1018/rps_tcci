/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "ET_TENDER_CATEGORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtTenderCategory.findAll", query = "SELECT e FROM EtTenderCategory e")
    , @NamedQuery(name = "EtTenderCategory.findById", query = "SELECT e FROM EtTenderCategory e WHERE e.id = :id")
    , @NamedQuery(name = "EtTenderCategory.findByTenderId", query = "SELECT e FROM EtTenderCategory e WHERE e.tenderId = :tenderId")
    , @NamedQuery(name = "EtTenderCategory.findByCategoryId", query = "SELECT e FROM EtTenderCategory e WHERE e.categoryId = :categoryId")
    , @NamedQuery(name = "EtTenderCategory.findByCreator", query = "SELECT e FROM EtTenderCategory e WHERE e.creator = :creator")
    , @NamedQuery(name = "EtTenderCategory.findByCreatetime", query = "SELECT e FROM EtTenderCategory e WHERE e.createtime = :createtime")
    , @NamedQuery(name = "EtTenderCategory.findByModifier", query = "SELECT e FROM EtTenderCategory e WHERE e.modifier = :modifier")
    , @NamedQuery(name = "EtTenderCategory.findByModifytime", query = "SELECT e FROM EtTenderCategory e WHERE e.modifytime = :modifytime")})
public class EtTenderCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_TENDER_CATEGORY", sequenceName = "SEQ_TENDER_CATEGORY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TENDER_CATEGORY")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TENDER_ID")
    private long tenderId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CATEGORY_ID")
    private long categoryId;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    @Column(name = "DISABLED")
    private Boolean disabled;

    public EtTenderCategory() {
    }

    public EtTenderCategory(Long id) {
        this.id = id;
    }

    public EtTenderCategory(Long id, long tenderId, long categoryId) {
        this.id = id;
        this.tenderId = tenderId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public long getTenderId() {
        return tenderId;
    }

    public void setTenderId(long tenderId) {
        this.tenderId = tenderId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
        if (!(object instanceof EtTenderCategory)) {
            return false;
        }
        EtTenderCategory other = (EtTenderCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.cm.annotation.EtTenderCategory[ id=" + id + " ]";
    }
    
}
