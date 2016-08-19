/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.tccstore.enums.CommentStatusEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "EC_PARTNER_COMMENT")
@NamedQueries({
    @NamedQuery(name = "EcPartnerComment.findAll", query = "SELECT e FROM EcPartnerComment e"),
    @NamedQuery(name = "EcPartnerComment.findByPartner", query = "SELECT e FROM EcPartnerComment e WHERE e.active=TRUE AND e.ecPartner=:ecPartner ORDER BY e.createtime DESC")})
public class EcPartnerComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RATE")
    private Double rate;
    @Size(max = 500)
    @Column(name = "MESSAGE")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "PARTNER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcPartner ecPartner;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember ecMember;
    @Column(name = "ACTIVE")
    private boolean active;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private CommentStatusEnum status;
    @JoinColumn(name = "APPROVER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser approver;
    @Column(name = "APPROVAL_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvalTime;

    public EcPartnerComment() {
    }

    public EcPartnerComment(EcMember ecMmeber, EcPartner ecPartner, Double rate, String message) {
        this.ecMember = ecMmeber;
        this.ecPartner = ecPartner;
        this.rate = rate;
        this.message = message;
        this.createtime = new Date();
        this.active = false;
        this.status = CommentStatusEnum.APPLY;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcPartner getEcPartner() {
        return ecPartner;
    }

    public void setEcPartner(EcPartner partner) {
        this.ecPartner = partner;
    }

    public EcMember getEcMember() {
        return ecMember;
    }

    public void setEcMember(EcMember ecMember) {
        this.ecMember = ecMember;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CommentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CommentStatusEnum status) {
        this.status = status;
    }

    public TcUser getApprover() {
        return approver;
    }

    public void setApprover(TcUser approver) {
        this.approver = approver;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
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
        if (!(object instanceof EcPartnerComment)) {
            return false;
        }
        EcPartnerComment other = (EcPartnerComment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcPartnerComment[ id=" + id + " ]";
    }

}
