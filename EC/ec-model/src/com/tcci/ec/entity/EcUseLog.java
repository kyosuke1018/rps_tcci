/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

//import com.sun.istack.NotNull;
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
@Table(name = "EC_USE_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcUseLog.findAll", query = "SELECT e FROM EcUseLog e")
    , @NamedQuery(name = "EcUseLog.findById", query = "SELECT e FROM EcUseLog e WHERE e.id = :id")
    , @NamedQuery(name = "EcUseLog.findByType", query = "SELECT e FROM EcUseLog e WHERE e.type = :type")
    , @NamedQuery(name = "EcUseLog.findByCategory", query = "SELECT e FROM EcUseLog e WHERE e.category = :category")
    , @NamedQuery(name = "EcUseLog.findByMemberId", query = "SELECT e FROM EcUseLog e WHERE e.memberId = :memberId")
    , @NamedQuery(name = "EcUseLog.findByPeriod", query = "SELECT e FROM EcUseLog e WHERE e.period = :period")
    , @NamedQuery(name = "EcUseLog.findBySrc", query = "SELECT e FROM EcUseLog e WHERE e.src = :src")
    , @NamedQuery(name = "EcUseLog.findByClientIp", query = "SELECT e FROM EcUseLog e WHERE e.clientIp = :clientIp")
    , @NamedQuery(name = "EcUseLog.findByClientInfo", query = "SELECT e FROM EcUseLog e WHERE e.clientInfo = :clientInfo")
    , @NamedQuery(name = "EcUseLog.findByMemo", query = "SELECT e FROM EcUseLog e WHERE e.memo = :memo")
    , @NamedQuery(name = "EcUseLog.findByCreator", query = "SELECT e FROM EcUseLog e WHERE e.creator = :creator")
    , @NamedQuery(name = "EcUseLog.findByCreatetime", query = "SELECT e FROM EcUseLog e WHERE e.createtime = :createtime")
    , @NamedQuery(name = "EcUseLog.findByModifier", query = "SELECT e FROM EcUseLog e WHERE e.modifier = :modifier")
    , @NamedQuery(name = "EcUseLog.findByModifytime", query = "SELECT e FROM EcUseLog e WHERE e.modifytime = :modifytime")})
public class EcUseLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
//    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_USE_LOG", sequenceName = "SEQ_USE_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USE_LOG")
    private Long id;
    @Basic(optional = false)
//    @NotNull
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
    private float patrolLatitude;
    @Column(name = "PATROL_LONGITUDE")
    private float patrolLongitude;
    
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

    public EcUseLog() {
    }

    public EcUseLog(Long id) {
        this.id = id;
    }

    public EcUseLog(Long id, String type) {
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
    
    public float getPatrolLatitude() {
        return patrolLatitude;
    }

    public void setPatrolLatitude(float patrolLatitude) {
        this.patrolLatitude = patrolLatitude;
    }

    public float getPatrolLongitude() {
        return patrolLongitude;
    }

    public void setPatrolLongitude(float patrolLongitude) {
        this.patrolLongitude = patrolLongitude;
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
        if (!(object instanceof EcUseLog)) {
            return false;
        }
        EcUseLog other = (EcUseLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcUseLog[ id=" + id + " ]";
    }
    
}
