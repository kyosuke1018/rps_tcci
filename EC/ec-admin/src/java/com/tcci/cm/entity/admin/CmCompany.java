/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jackson.Lee
 */
@Entity
@Table(name = "CM_COMPANY")
@NamedQueries({
    @NamedQuery(name = "CmCompany.findAll", query = "SELECT p FROM CmCompany p"),
    @NamedQuery(name = "CmCompany.findAllAndSort", query = "SELECT p FROM CmCompany p order by p.sortNum, p.id"),
    @NamedQuery(name = "CmCompany.findById", query = "SELECT p FROM CmCompany p WHERE p.id = :id"),
    @NamedQuery(name = "CmCompany.findByCompanyName", query = "SELECT p FROM CmCompany p WHERE p.companyName = :companyName"),
    @NamedQuery(name = "CmCompany.findBySapClientCode", query = "SELECT p FROM CmCompany p WHERE p.sapClientCode = :sapClientCode")})
@Cacheable(value=false)
public class CmCompany implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String COMPANY_CODE_TCC = "tcc"; // 台泥
    public static final String COMPANY_CODE_TCC_CN = "tcc_cn"; // 台泥大陸
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TCC")
    private Long id;
    @Size(max = 60)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 10)
    @Column(name = "SAP_CLIENT")
    private String sapClient;
    @Column(name = "SAP_CLIENT_CODE")
    private String sapClientCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LANGUAGE")
    private String language;
    
    @Column(name = "SORT_NUM")
    private Integer sortNum;
    
    public CmCompany() {
    }

    public CmCompany(Long id) {
        this.id = id;
    }

    //<editor-fold defaultstate="collapsed" desc="for getter & setter">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public String getSapClient() {
        return sapClient;
    }

    public void setSapClient(String sapClient) {
        this.sapClient = sapClient;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    //</editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CmCompany)) {
            return false;
        }
        CmCompany other = (CmCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.cm.entity.admin.CmCompany[ id=" + id + " ]";
    }
    
}
