/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.entity;

import com.tcci.fc.entity.org.TcUser;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "FC_MONTHLY_EXCHANGE_RATE")
@NamedQueries({
    @NamedQuery(name = "FcMonthlyExchangeRate.findByYM", query = "SELECT e FROM FcMonthlyExchangeRate e WHERE e.yearmonth=:yearmonth ORDER BY e.currency"),
    @NamedQuery(name = "FcMonthlyExchangeRate.findByYMAndCurrency", query = "SELECT e FROM FcMonthlyExchangeRate e WHERE e.yearmonth=:yearmonth AND e.currency=:currency AND e.toCurrency=:toCurrency"),
    @NamedQuery(name = "FcMonthlyExchangeRate.findByYMAndCurrencyCode", query = "SELECT e FROM FcMonthlyExchangeRate e WHERE e.yearmonth=:yearmonth AND e.currency.code=:currCode AND e.toCurrency.code=:toCurrCode"),
    @NamedQuery(name = "FcMonthlyExchangeRate.findByYMAndToCurrency", query = "SELECT e FROM FcMonthlyExchangeRate e WHERE e.yearmonth=:yearmonth AND e.toCurrency=:toCurrency"),
    @NamedQuery(name = "FcMonthlyExchangeRate.findByYMAndToCurrencyCode", query = "SELECT e FROM FcMonthlyExchangeRate e WHERE e.yearmonth=:yearmonth AND e.toCurrency.code=:toCurrCode")
})
public class FcMonthlyExchangeRate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @JoinColumn(name = "CURRENCY", referencedColumnName = "ID")
    @ManyToOne
    private FcCurrency currency;
    @JoinColumn(name = "TO_CURRENCY", referencedColumnName = "ID")
    @ManyToOne
    private FcCurrency toCurrency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @Column(name = "RATE")
    private BigDecimal rate;
    @Column(name = "AVG_RATE")
    private BigDecimal avgRate;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    
    public FcMonthlyExchangeRate() {
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FcCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(FcCurrency currency) {
        this.currency = currency;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(BigDecimal avgRate) {
        this.avgRate = avgRate;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public FcCurrency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(FcCurrency toCurrency) {
        this.toCurrency = toCurrency;
    }
    
    
}
