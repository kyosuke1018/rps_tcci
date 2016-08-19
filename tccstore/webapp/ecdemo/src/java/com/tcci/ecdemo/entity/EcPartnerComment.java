/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    @NamedQuery(name = "EcPartnerComment.findAll", query = "SELECT e FROM EcPartnerComment e")})
public class EcPartnerComment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RATE")
    private int rate;
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
    private EcPartner partnerId;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember memberId;

    public EcPartnerComment() {
    }

    public EcPartnerComment(Long id) {
        this.id = id;
    }

    public EcPartnerComment(Long id, int rate, Date createtime) {
        this.id = id;
        this.rate = rate;
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
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

    public EcPartner getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(EcPartner partnerId) {
        this.partnerId = partnerId;
    }

    public EcMember getMemberId() {
        return memberId;
    }

    public void setMemberId(EcMember memberId) {
        this.memberId = memberId;
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
        return "com.tcci.ecdemo.entity.EcPartnerComment[ id=" + id + " ]";
    }
    
}
