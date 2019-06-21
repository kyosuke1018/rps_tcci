/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.entity;

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
@Table(name = "ET_USE_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EtUseLog.findAll", query = "SELECT e FROM EtUseLog e")
    , @NamedQuery(name = "EtUseLog.findById", query = "SELECT e FROM EtUseLog e WHERE e.id = :id")
    , @NamedQuery(name = "EtUseLog.findByType", query = "SELECT e FROM EtUseLog e WHERE e.type = :type")
    , @NamedQuery(name = "EtUseLog.findByCategory", query = "SELECT e FROM EtUseLog e WHERE e.category = :category")
    , @NamedQuery(name = "EtUseLog.findByMemberId", query = "SELECT e FROM EtUseLog e WHERE e.memberId = :memberId")
    , @NamedQuery(name = "EtUseLog.findByPeriod", query = "SELECT e FROM EtUseLog e WHERE e.period = :period")
    , @NamedQuery(name = "EtUseLog.findBySrc", query = "SELECT e FROM EtUseLog e WHERE e.src = :src")
    , @NamedQuery(name = "EtUseLog.findByClientIp", query = "SELECT e FROM EtUseLog e WHERE e.clientIp = :clientIp")})
public class EtUseLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_USE_LOG", sequenceName = "SEQ_USE_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USE_LOG")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TYPE")
    private String type;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Column(name = "PERIOD")
    private Long period;
    @Column(name = "SRC")
    private String src;
    @Column(name = "CLIENT_IP")
    private String clientIp;
    @Column(name = "CLIENT_INFO")
    private String clientInfo;
    @Column(name = "MEMO")
    private String memo;
    @Column(name = "PATROL_LATITUDE")
    private Double patrolLatitude;
    @Column(name = "PATROL_LONGITUDE")
    private Double patrolLongitude;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EtMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EtMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EtUseLog() {
    }

    public EtUseLog(Long id) {
        this.id = id;
    }

    public EtUseLog(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Double getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(Double patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public Double getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(Double patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EtMember getCreator() {
        return creator;
    }

    public void setCreator(EtMember creator) {
        this.creator = creator;
    }

    public EtMember getModifier() {
        return modifier;
    }

    public void setModifier(EtMember modifier) {
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
        if (!(object instanceof EtUseLog)) {
            return false;
        }
        EtUseLog other = (EtUseLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.entity.EtUseLog[ id=" + id + " ]";
    }
    
}
