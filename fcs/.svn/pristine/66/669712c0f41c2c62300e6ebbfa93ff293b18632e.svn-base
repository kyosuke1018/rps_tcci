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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kyle.cheng
 */
@Entity
@Table(name = "IRS_RECONCIL_COMPANY_R")
@XmlRootElement
public class IrsReconcilCompanyR implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @NotNull
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany company;
    @NotNull
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser tcUser;
    @JoinColumn(name = "RECONCIL_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany reconcilCompany;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    
    public IrsReconcilCompanyR() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public TcUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TcUser tcUser) {
        this.tcUser = tcUser;
    }

    public FcCompany getReconcilCompany() {
        return reconcilCompany;
    }

    public void setReconcilCompany(FcCompany reconcilCompany) {
        this.reconcilCompany = reconcilCompany;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }
    
    
}
