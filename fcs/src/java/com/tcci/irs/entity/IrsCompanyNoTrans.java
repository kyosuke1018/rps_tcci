/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.entity;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
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

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "IRS_COMPANY_NO_TRANS")
@NamedQueries({
    @NamedQuery(name = "IrsCompanyNoTrans.findByYearmonthCompany",
        query = "SELECT r FROM IrsCompanyNoTrans r"
        + " WHERE r.yearmonth=:yearmonth AND r.company=:company"),
    @NamedQuery(name = "IrsCompanyNoTrans.findByYearmonth",
        query = "SELECT r FROM IrsCompanyNoTrans r"
        + " WHERE r.yearmonth=:yearmonth")
})
public class IrsCompanyNoTrans implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name = "ID")
    private Long id;
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @NotNull
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany company;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser user;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    
    public IrsCompanyNoTrans(){
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public TcUser getUser() {
        return user;
    }

    public void setUser(TcUser user) {
        this.user = user;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }
    
}
