/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity;

import com.tcci.fcs.entity.FcCompany;
import com.tcci.irs.enums.ReclPortfolioStateEnum;
import java.io.Serializable;
import java.util.Objects;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author David.Jen
 */
@Entity
@Table(name = "IRS_RECLPORTFOLIO")
@XmlRootElement
public class ReclPortfolio implements Serializable {
    //
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "INSTANCE_STATE")
    private ReclPortfolioStateEnum state;
    
    @Column(name = "YEAR")
    private int year;
    
    @Column(name = "MONTH")
    private int month;
    
    @ManyToOne
    @JoinColumn(name = "COMPANY1_ID", referencedColumnName = "ID")
    private FcCompany company1;
    
    @ManyToOne
    @JoinColumn(name = "COMPANY2_ID", referencedColumnName = "ID")
    private FcCompany company2;
    
    //

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReclPortfolio other = (ReclPortfolio) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReclPortfolioStateEnum getState() {
        return state;
    }

    public void setState(ReclPortfolioStateEnum state) {
        this.state = state;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public FcCompany getCompany1() {
        return company1;
    }

    public void setCompany1(FcCompany company1) {
        this.company1 = company1;
    }

    public FcCompany getCompany2() {
        return company2;
    }

    public void setCompany2(FcCompany company2) {
        this.company2 = company2;
    }
    
    
}
