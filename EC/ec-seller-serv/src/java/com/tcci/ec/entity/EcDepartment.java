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
@Table(name = "EC_DEPARTMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcDepartment.findAll", query = "SELECT e FROM EcDepartment e")
    , @NamedQuery(name = "EcDepartment.findById", query = "SELECT e FROM EcDepartment e WHERE e.id = :id")
    , @NamedQuery(name = "EcDepartment.findByStoreId", query = "SELECT e FROM EcDepartment e WHERE e.storeId = :storeId")})
public class EcDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_DEPARTMENT", sequenceName = "SEQ_DEPARTMENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEPARTMENT")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "CODE")
    private String code;
    @Column(name = "MEMO")
    private String memo;
    @Column(name = "SORTNUM")
    private Long sortnum;
    @Column(name = "HAS_PRD")
    private Boolean hasPrd;
    @Column(name = "DISABLED")
    private Boolean disabled;

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

    public EcDepartment() {
    }

    public EcDepartment(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getSortnum() {
        return sortnum;
    }

    public void setSortnum(Long sortnum) {
        this.sortnum = sortnum;
    }

    public Boolean getHasPrd() {
        return hasPrd;
    }

    public void setHasPrd(Boolean hasPrd) {
        this.hasPrd = hasPrd;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
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
        if (!(object instanceof EcDepartment)) {
            return false;
        }
        EcDepartment other = (EcDepartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcDepartment[ id=" + id + " ]";
    }
    
}