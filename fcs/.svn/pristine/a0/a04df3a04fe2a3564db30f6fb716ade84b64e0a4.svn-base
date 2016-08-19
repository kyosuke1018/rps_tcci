/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.entity;

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

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "RPT_COMPANY_ORG")
@NamedQueries({
    @NamedQuery(name = "RptCompanyOrg.findByCompany", query = "SELECT c FROM RptCompanyOrg c WHERE  c.group.code = :group and c.code=:companyCode"),
    @NamedQuery(name = "RptCompanyOrg.findByParent", query = "SELECT c FROM RptCompanyOrg c WHERE  c.group.code = :group and c.parent=:parent"),
    @NamedQuery(name = "RptCompanyOrg.findAll", query = "SELECT c FROM RptCompanyOrg c WHERE c.group.code = :group ORDER BY c.code")
})
public class RptCompanyOrg implements Serializable {
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
    @Column(name = "CODE")
    private String code; 
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private RptCompanyOrg parent;
    
    @JoinColumn(name = "CODE",  referencedColumnName = "CODE", insertable = false, updatable = false)//read only
    @ManyToOne(optional = false)
    private FcCompany company;
    @Column(name = "HLEVEL")
    private Integer hlevel;
    
    public RptCompanyOrg() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RptCompanyOrg getParent() {
        return parent;
    }

    public void setParent(RptCompanyOrg parent) {
        this.parent = parent;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public Integer getHlevel() {
        return hlevel;
    }

    public void setHlevel(Integer hlevel) {
        this.hlevel = hlevel;
    }
    
}
