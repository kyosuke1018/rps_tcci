/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_TCC_PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcTccProduct.findAll", query = "SELECT e FROM EcTccProduct e")
    , @NamedQuery(name = "EcTccProduct.findById", query = "SELECT e FROM EcTccProduct e WHERE e.id = :id")
    , @NamedQuery(name = "EcTccProduct.findByCode", query = "SELECT e FROM EcTccProduct e WHERE e.code = :code and e.active = :active")
    , @NamedQuery(name = "EcTccProduct.findByName", query = "SELECT e FROM EcTccProduct e WHERE e.name = :name and e.active = :active")
    , @NamedQuery(name = "EcTccProduct.findByDescription", query = "SELECT e FROM EcTccProduct e WHERE e.description = :description")
    , @NamedQuery(name = "EcTccProduct.findByActive", query = "SELECT e FROM EcTccProduct e WHERE e.active = :active")})
public class EcTccProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_TCC_PRODUCT", sequenceName = "SEQ_TCC_PRODUCT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TCC_PRODUCT")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
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
    private Boolean active;
    
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

    public EcTccProduct() {
    }

    public EcTccProduct(Long id) {
        this.id = id;
    }

    public EcTccProduct(Long id, String code, String name, Boolean active) {
        this.id = id;
        this.code = code;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
        if (!(object instanceof EcTccProduct)) {
            return false;
        }
        EcTccProduct other = (EcTccProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcTccProduct[ id=" + id + " ]";
    }
    
}
