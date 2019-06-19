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
@Table(name = "EC_CUS_FEEDBACK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCusFeedback.findAll", query = "SELECT e FROM EcCusFeedback e")
    , @NamedQuery(name = "EcCusFeedback.findById", query = "SELECT e FROM EcCusFeedback e WHERE e.id = :id")
    , @NamedQuery(name = "EcCusFeedback.findByStoreId", query = "SELECT e FROM EcCusFeedback e WHERE e.storeId = :storeId")})
public class EcCusFeedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CUS_FEEDBACK", sequenceName = "SEQ_CUS_FEEDBACK", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUS_FEEDBACK")        
    private Long id;
    @Column(name = "STORE_ID")
    private Long storeId;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "TYPE_ID")
    private Long typeId;
    @Column(name = "PRD_ID")
    private Long prdId;
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "CONTENT")
    private String content;
    
    @InputCheckMeta(key="EC_CUS_FEEDBACK.PROCESS")
    @Column(name = "PROCESS")
    private String process;
    
    @Column(name = "PROCESS_USER")
    private Long processUser;
    @Column(name = "PROCESS_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date processTime;
    @Column(name = "CLOSE_USER")
    private Long closeUser;
    @Column(name = "CLOSE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closeTime;
    
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;

    public EcCusFeedback() {
    }

    public EcCusFeedback(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getPrdId() {
        return prdId;
    }

    public void setPrdId(Long prdId) {
        this.prdId = prdId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getProcessUser() {
        return processUser;
    }

    public void setProcessUser(Long processUser) {
        this.processUser = processUser;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Long getCloseUser() {
        return closeUser;
    }

    public void setCloseUser(Long closeUser) {
        this.closeUser = closeUser;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EcCusFeedback)) {
            return false;
        }
        EcCusFeedback other = (EcCusFeedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCusFeedback[ id=" + id + " ]";
    }
    
}
