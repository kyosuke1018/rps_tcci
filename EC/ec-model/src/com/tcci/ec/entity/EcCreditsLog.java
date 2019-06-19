/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

//import com.sun.istack.NotNull;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_CREDITS_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcCreditsLog.findAll", query = "SELECT e FROM EcCreditsLog e")
    , @NamedQuery(name = "EcCreditsLog.findById", query = "SELECT e FROM EcCreditsLog e WHERE e.id = :id")
    , @NamedQuery(name = "EcCreditsLog.findByMemberId", query = "SELECT e FROM EcCreditsLog e WHERE e.memberId = :memberId")
    , @NamedQuery(name = "EcCreditsLog.findByStoreId", query = "SELECT e FROM EcCreditsLog e WHERE e.storeId = :storeId")})
public class EcCreditsLog implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
//    @com.sun.istack.NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_CREDITS_LOG", sequenceName = "SEQ_CREDITS_LOG", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CREDITS_LOG")        
    private Long id;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "MEMBER_ID")
    private Long memberId;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "STORE_ID")
    private Long storeId;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "CREDITS")
    private BigDecimal credits;
    @Column(name = "CREDITS_ORI")
    private BigDecimal creditsOri;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "CREDITS_DIFF")
    private BigDecimal creditsDiff;
    @Basic(optional = false)
//    @NotNull
    @Column(name = "CREDITS_CUR")
    private Long creditsCur;
    @Column(name = "REASON")
    private String reason;
    
    @Column(name = "ORDER_ID")
    private Long orderId;
    @Column(name = "TYPE")
    private String type;
    
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

    public EcCreditsLog() {
    }

    public EcCreditsLog(Long id) {
        this.id = id;
    }

    public EcCreditsLog(Long id, Long memberId, Long storeId, BigDecimal credits, BigDecimal creditsDiff, Long creditsCur) {
        this.id = id;
        this.memberId = memberId;
        this.storeId = storeId;
        this.credits = credits;
        this.creditsDiff = creditsDiff;
        this.creditsCur = creditsCur;
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

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public BigDecimal getCreditsOri() {
        return creditsOri;
    }

    public void setCreditsOri(BigDecimal creditsOri) {
        this.creditsOri = creditsOri;
    }

    public BigDecimal getCreditsDiff() {
        return creditsDiff;
    }

    public void setCreditsDiff(BigDecimal creditsDiff) {
        this.creditsDiff = creditsDiff;
    }

    public Long getCreditsCur() {
        return creditsCur;
    }

    public void setCreditsCur(Long creditsCur) {
        this.creditsCur = creditsCur;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(object instanceof EcCreditsLog)) {
            return false;
        }
        EcCreditsLog other = (EcCreditsLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcCreditsLog[ id=" + id + " ]";
    }
    
}
