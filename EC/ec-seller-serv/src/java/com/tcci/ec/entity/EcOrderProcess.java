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
@Table(name = "EC_ORDER_PROCESS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcOrderProcess.findAll", query = "SELECT e FROM EcOrderProcess e")
    , @NamedQuery(name = "EcOrderProcess.findById", query = "SELECT e FROM EcOrderProcess e WHERE e.id = :id")
    , @NamedQuery(name = "EcOrderProcess.findByStoreId", query = "SELECT e FROM EcOrderProcess e WHERE e.storeId = :storeId")
    , @NamedQuery(name = "EcOrderProcess.findByOrderId", query = "SELECT e FROM EcOrderProcess e WHERE e.orderId = :orderId")})
public class EcOrderProcess implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_ORDER_PROCESS", sequenceName = "SEQ_ORDER_PROCESS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_PROCESS")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    
    @Column(name = "PROCESS")
    @InputCheckMeta(key="EC_ORDER_PROCESS.PROCESS")
    private String process;
    
    @Column(name = "PROCESS_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processTime;
    @Column(name = "PROCESS_USER")
    private Long processUser;
    
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

    public EcOrderProcess() {
    }

    public EcOrderProcess(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Long getProcessUser() {
        return processUser;
    }

    public void setProcessUser(Long processUser) {
        this.processUser = processUser;
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
        if (!(object instanceof EcOrderProcess)) {
            return false;
        }
        EcOrderProcess other = (EcOrderProcess) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcOrderProcess[ id=" + id + " ]";
    }
    
}
