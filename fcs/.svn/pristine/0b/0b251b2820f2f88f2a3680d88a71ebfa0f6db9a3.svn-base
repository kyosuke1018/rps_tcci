/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.entity;

import com.tcci.fcs.entity.FcCompGroup;
import com.tcci.fcs.entity.FcCompany;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "IRS_COMPANY_TYPE")
@NamedQueries({
    @NamedQuery(name = "IrsCompanyType.findByCompany", query = "SELECT c FROM IrsCompanyType c WHERE  c.group.code = :group and c.company=:company"),
    @NamedQuery(name = "IrsCompanyType.findAll", query = "SELECT c FROM IrsCompanyType c WHERE c.group.code = :group ORDER BY c.company.code")
})
public class IrsCompanyType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @JoinColumn(name = "COMP_GROUP", referencedColumnName = "CODE")
    @ManyToOne
    private FcCompGroup group;
    @NotNull
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany company;
    @Column(name = "R_TYPE")
    private String type;
    
    public IrsCompanyType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FcCompGroup getGroup() {
        return group;
    }

    public void setGroup(FcCompGroup group) {
        this.group = group;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
