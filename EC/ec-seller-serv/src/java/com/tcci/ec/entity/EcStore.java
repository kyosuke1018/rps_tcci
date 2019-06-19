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
@Table(name = "EC_STORE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcStore.findAll", query = "SELECT e FROM EcStore e")
    , @NamedQuery(name = "EcStore.findById", query = "SELECT e FROM EcStore e WHERE e.id = :id")
    , @NamedQuery(name = "EcStore.findByType", query = "SELECT e FROM EcStore e WHERE e.type = :type")
    , @NamedQuery(name = "EcStore.findAllBySeller", query = "SELECT e FROM EcStore e WHERE e.sellerId = :sellerId")
    , @NamedQuery(name = "EcStore.findBySellerId", query = "SELECT e FROM EcStore e WHERE e.sellerId = :sellerId AND e.defStore = :defStore")})
public class EcStore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_STORE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")        
    private Long id;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "CNAME")
    private String cname;
    @Column(name = "ENAME")
    private String ename;
    @Column(name = "BRIEF")
    private String brief;
    @Column(name = "STYLE_ID")
    private Long styleId;
    @Column(name = "FLOW_ID")
    private Long flowId;
    @Column(name = "PRD_TYPE_LEVEL")
    private Long prdTypeLevel;
    @Column(name = "SELLER_ID")
    private Long sellerId;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "REMIT_ACCOUNT")
    private String remitAccount;
    @Column(name = "OPENED")
    private Boolean opened;
    @Column(name = "DEF_STORE")
    private Boolean defStore;
        
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

    public EcStore() {
    }

    public EcStore(Long id) {
        this.id = id;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getDefStore() {
        return defStore;
    }

    public void setDefStore(Boolean defStore) {
        this.defStore = defStore;
    }

    public String getRemitAccount() {
        return remitAccount;
    }

    public void setRemitAccount(String remitAccount) {
        this.remitAccount = remitAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }
    
    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
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

    public Long getPrdTypeLevel() {
        return prdTypeLevel;
    }

    public void setPrdTypeLevel(Long prdTypeLevel) {
        this.prdTypeLevel = prdTypeLevel;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
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
        if (!(object instanceof EcStore)) {
            return false;
        }
        EcStore other = (EcStore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcStore[ id=" + id + " ]";
    }
    
}
