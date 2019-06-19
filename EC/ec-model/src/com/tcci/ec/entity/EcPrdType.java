/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "EC_PRD_TYPE")
@NamedQueries({
    @NamedQuery(name = "EcPrdType.findAll", query = "SELECT e FROM EcPrdType e"),
    @NamedQuery(name = "EcPrdType.findAllActive", query = "SELECT e FROM EcPrdType e　WHERE e.disabled != TRUE ORDER by e.parent, e.sortnum "),
    @NamedQuery(name = "EcPrdType.findLevel1", query = "SELECT e FROM EcPrdType e WHERE e.levelnum=1 and e.disabled != TRUE ORDER by e.sortnum,  e.id "),
    @NamedQuery(name = "EcPrdType.findByParent", query = "SELECT e FROM EcPrdType e WHERE e.parent=:parent and e.disabled != TRUE ORDER by e.sortnum ")
//    @NamedQuery(name = "EcCustomer.findAll", query = "SELECT e FROM EcCustomer e ORDER by e.code"),
//    @NamedQuery(name = "EcCustomer.findByCode", query = "SELECT e FROM EcCustomer e WHERE e.code=:code")
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
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "CODE")
    private String code;
    @Column(name = "MEMO")
    private String memo;
    @JoinColumn(name = "PARENT", referencedColumnName = "ID")
    @ManyToOne
    private EcPrdType parent;
    @Column(name = "LEAF")
    private int leaf;
    @Column(name = "LEVELNUM")
    private int levelnum;
    @Column(name = "SORTNUM")
    private int sortnum;
    
    @Column(name = "DISABLED")
    private boolean disabled;
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

    public EcPrdType getParent() {
        return parent;
    }

    public void setParent(EcPrdType parent) {
        this.parent = parent;
    }

    public int getLeaf() {
        return leaf;
    }

    public void setLeaf(int leaf) {
        this.leaf = leaf;
    }

    public int getLevelnum() {
        return levelnum;
    }

    public void setLevelnum(int levelnum) {
        this.levelnum = levelnum;
    }

    public int getSortnum() {
        return sortnum;
    }

    public void setSortnum(int sortnum) {
        this.sortnum = sortnum;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
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
