/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carl.lin
 */
@Entity
@Table(name = "FACT_SK_ACHIEVEMENT_CHANNEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FactSkAchievementChannel.findAll", query = "SELECT f FROM FactSkAchievementChannel f"),
    @NamedQuery(name = "FactSkAchievementChannel.findByYearMonth", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.factSkAchievementChannelPK.yearMonth = :yearMonth"),
    @NamedQuery(name = "FactSkAchievementChannel.findByChannel", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.factSkAchievementChannelPK.channel = :channel"),
    @NamedQuery(name = "FactSkAchievementChannel.findByAmount", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.amount = :amount"),
    @NamedQuery(name = "FactSkAchievementChannel.findByPremiumDiscount", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.premiumDiscount = :premiumDiscount"),
    @NamedQuery(name = "FactSkAchievementChannel.findBySalesReturn", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.salesReturn = :salesReturn"),
    @NamedQuery(name = "FactSkAchievementChannel.findBySalesDiscount", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.salesDiscount = :salesDiscount"),
    @NamedQuery(name = "FactSkAchievementChannel.findByBudget", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.budget = :budget"),
    @NamedQuery(name = "FactSkAchievementChannel.findByCreatedatetime", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.createdatetime = :createdatetime"),
    @NamedQuery(name = "FactSkAchievementChannel.findByCreatedate", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.factSkAchievementChannelPK.createdate = :createdate"),
    @NamedQuery(name = "FactSkAchievementChannel.findByCost", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.cost = :cost"),
    @NamedQuery(name = "FactSkAchievementChannel.findByTargetRate", query = "SELECT f FROM FactSkAchievementChannel f WHERE f.targetRate = :targetRate")})
public class FactSkAchievementChannel implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FactSkAchievementChannelPK factSkAchievementChannelPK;
    @Column(name = "AMOUNT")
    private BigInteger amount;
    @Column(name = "PREMIUM_DISCOUNT")
    private BigInteger premiumDiscount;
    @Column(name = "SALES_RETURN")
    private BigInteger salesReturn;
    @Column(name = "SALES_DISCOUNT")
    private BigInteger salesDiscount;
    @Column(name = "BUDGET")
    private BigInteger budget;
    @Column(name = "CREATEDATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdatetime;
    @Column(name = "COST")
    private BigInteger cost;
    @Column(name = "TARGET_RATE")
    private BigInteger targetRate;
    @JoinColumn(name = "CHANNEL", referencedColumnName = "CODE", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DimSkChannelName dimSkChannelName;

    public FactSkAchievementChannel() {
    }

    public FactSkAchievementChannel(FactSkAchievementChannelPK factSkAchievementChannelPK) {
        this.factSkAchievementChannelPK = factSkAchievementChannelPK;
    }

    public FactSkAchievementChannel(String yearMonth, String channel, String createdate) {
        this.factSkAchievementChannelPK = new FactSkAchievementChannelPK(yearMonth, channel, createdate);
    }

    public FactSkAchievementChannelPK getFactSkAchievementChannelPK() {
        return factSkAchievementChannelPK;
    }

    public void setFactSkAchievementChannelPK(FactSkAchievementChannelPK factSkAchievementChannelPK) {
        this.factSkAchievementChannelPK = factSkAchievementChannelPK;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getPremiumDiscount() {
        return premiumDiscount;
    }

    public void setPremiumDiscount(BigInteger premiumDiscount) {
        this.premiumDiscount = premiumDiscount;
    }

    public BigInteger getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(BigInteger salesReturn) {
        this.salesReturn = salesReturn;
    }

    public BigInteger getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(BigInteger salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public BigInteger getBudget() {
        return budget;
    }

    public void setBudget(BigInteger budget) {
        this.budget = budget;
    }

    public Date getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(Date createdatetime) {
        this.createdatetime = createdatetime;
    }

    public BigInteger getCost() {
        return cost;
    }

    public void setCost(BigInteger cost) {
        this.cost = cost;
    }

    public BigInteger getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(BigInteger targetRate) {
        this.targetRate = targetRate;
    }

    public DimSkChannelName getDimSkChannelName() {
        return dimSkChannelName;
    }

    public void setDimSkChannelName(DimSkChannelName dimSkChannelName) {
        this.dimSkChannelName = dimSkChannelName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (factSkAchievementChannelPK != null ? factSkAchievementChannelPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FactSkAchievementChannel)) {
            return false;
        }
        FactSkAchievementChannel other = (FactSkAchievementChannel) object;
        if ((this.factSkAchievementChannelPK == null && other.factSkAchievementChannelPK != null) || (this.factSkAchievementChannelPK != null && !this.factSkAchievementChannelPK.equals(other.factSkAchievementChannelPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.FactSkAchievementChannel[ factSkAchievementChannelPK=" + factSkAchievementChannelPK + " ]";
    }
    
}
