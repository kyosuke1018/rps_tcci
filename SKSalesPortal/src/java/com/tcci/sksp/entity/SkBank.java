/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_BANK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkBank.findAll", query = "SELECT s FROM SkBank s"),
    @NamedQuery(name = "SkBank.findById", query = "SELECT s FROM SkBank s WHERE s.id = :id"),
    @NamedQuery(name = "SkBank.findByCode", query = "SELECT s FROM SkBank s WHERE s.code = :code"),
    @NamedQuery(name = "SkBank.findByName", query = "SELECT s FROM SkBank s WHERE s.name = :name"),
    @NamedQuery(name = "SkBank.findByCreatetimestamp", query = "SELECT s FROM SkBank s WHERE s.createtimestamp = :createtimestamp")})
public class SkBank implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Size(max = 10)
    @Column(name = "CODE")
    private String code;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    public SkBank() {
    }

    public SkBank(Long id) {
        this.id = id;
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

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
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
        if (!(object instanceof SkBank)) {
            return false;
        }
        SkBank other = (SkBank) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
}
