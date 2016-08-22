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
@Table(name = "EC_REWARD_LOG")
@NamedQueries({
    @NamedQuery(name = "EcRewardLog.findAll", query = "SELECT e FROM EcRewardLog e"),
    @NamedQuery(name = "EcRewardLog.findByMemberType", 
            query = "SELECT e FROM EcRewardLog e WHERE e.ecMember=:ecMember AND e.type=:type ORDER BY e.eventTime DESC")
})
public class EcRewardLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_REWARD_LOG",sequenceName = "SEQ_REWARD_LOG", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_REWARD_LOG")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TYPE")
    private int type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POINTS")
    private int points;
    @Basic(optional = false)
    @NotNull
    @Column(name = "POINT_BALANCE")
    private int pointBalance;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "EVENT_TYPE")
    private String eventType;
    @Size(max = 300)
    @Column(name = "EVENT_DETAIL")
    private String eventDetail;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventTime;
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID")
    @ManyToOne
    private EcOrder ecOrder;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember ecMember;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;

    public EcRewardLog() {
    }

    public EcRewardLog(EcMember ecMember, int type, int points, int pointBalance, String eventType, String eventDetail, TcUser creator, EcOrder ecOrder) {
        this.ecMember = ecMember;
        this.type = type;
        this.points = points;
        this.pointBalance = pointBalance;
        this.eventType = eventType;
        this.eventDetail = eventDetail;
        this.creator = creator;
        this.ecOrder = ecOrder;
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(int pointBalance) {
        this.pointBalance = pointBalance;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public EcOrder getEcOrder() {
        return ecOrder;
    }

    public void setEcOrder(EcOrder ecOrder) {
        this.ecOrder = ecOrder;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
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
        if (!(object instanceof EcRewardLog)) {
            return false;
        }
        EcRewardLog other = (EcRewardLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcRewardLog[ id=" + id + " ]";
    }
    
}
