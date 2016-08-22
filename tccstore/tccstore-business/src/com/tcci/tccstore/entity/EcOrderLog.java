/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "EC_ORDER_LOG")
@NamedQueries({
    @NamedQuery(name = "EcOrderLog.findAll", query = "SELECT e FROM EcOrderLog e"),
    @NamedQuery(name = "EcOrderLog.findByOrder", query = "SELECT e FROM EcOrderLog e WHERE e.ecOrder=:ecOrder ORDER BY e.createtime DESC")
})
public class EcOrderLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_EC",sequenceName = "SEQ_EC", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_EC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EVENT_TYPE")
    private String eventType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @Size(max = 100)
    @Column(name = "MESSAGE")
    private String message;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcMember ecMember;
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcOrder ecOrder;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TcUser tcUser;

    public EcOrderLog() {
    }

    public EcOrderLog(EcOrder ecOrder, String eventType, EcMember operator, String message) {
        this.ecOrder = ecOrder;
        this.eventType = eventType;
        this.ecMember = operator;
        this.message = message;
        this.createtime = new Date();
    }
    
    public String getOperator() {
        return ecMember != null ? ecMember.getLoginAccount() :
               tcUser != null ? tcUser.getDisplayIdentifier() : null;
    }

    public EcOrderLog(EcOrder ecOrder, String eventType, TcUser operator, String message) {
        this.ecOrder = ecOrder;
        this.eventType = eventType;
        this.tcUser = operator;
        this.message = message;
        this.createtime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public EcOrder getEcOrder() {
        return ecOrder;
    }

    public void setEcOrder(EcOrder ecOrder) {
        this.ecOrder = ecOrder;
    }

    public TcUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TcUser tcUser) {
        this.tcUser = tcUser;
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
        if (!(object instanceof EcOrderLog)) {
            return false;
        }
        EcOrderLog other = (EcOrderLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcOrderLog[ id=" + id + " ]";
    }
    
}
