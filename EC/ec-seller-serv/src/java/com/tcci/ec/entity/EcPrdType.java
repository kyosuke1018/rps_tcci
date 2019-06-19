/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import com.tcci.cm.annotation.InputCheckMeta;
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
@Table(name = "EC_PRD_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcPrdType.findAll", query = "SELECT e FROM EcPrdType e")
    , @NamedQuery(name = "EcPrdType.findById", query = "SELECT e FROM EcPrdType e WHERE e.id = :id")
    , @NamedQuery(name = "EcPrdType.findByStoreId", query = "SELECT e FROM EcPrdType e WHERE e.storeId = :storeId")
    , @NamedQuery(name = "EcPrdType.findByCname", query = "SELECT e FROM EcPrdType e WHERE e.cname = :cname")
    , @NamedQuery(name = "EcPrdType.findByEname", query = "SELECT e FROM EcPrdType e WHERE e.ename = :ename")
    , @NamedQuery(name = "EcPrdType.findByCode", query = "SELECT e FROM EcPrdType e WHERE e.code = :code")
})
public class EcPrdType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_PRD_TYPE", sequenceName = "SEQ_PRD_TYPE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRD_TYPE")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @InputCheckMeta(key="EC_PRD_TYPE.CNAME")
    @Column(name = "CNAME")
    private String cname;
    @InputCheckMeta(key="EC_PRD_TYPE.ENAME")
    @Column(name = "ENAME")
    private String ename;
    @InputCheckMeta(key="EC_PRD_TYPE.CODE")
    @Column(name = "CODE")
    private String code;
    @InputCheckMeta(key="EC_PRD_TYPE.MEMO")
    @Column(name = "MEMO")
    private String memo;
    
    @Column(name = "PARENT")
    private Long parent;
    @Column(name = "LEAF")
    private Boolean leaf;
    @Column(name = "LEVELNUM")
    private Integer levelnum;
    @Column(name = "SORTNUM")
    private Integer sortnum;
    @Column(name = "HAS_PRD")
    private Boolean hasPrd = false;
    @Column(name = "DISABLED")
    private Boolean disabled = false;

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

    public EcPrdType() {
    }

    public EcPrdType(Long id) {
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

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Integer getLevelnum() {
        return levelnum;
    }

    public void setLevelnum(Integer levelnum) {
        this.levelnum = levelnum;
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
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
        if (!(object instanceof EcPrdType)) {
            return false;
        }
        EcPrdType other = (EcPrdType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcPrdType[ id=" + id + " ]";
    }
    
}
