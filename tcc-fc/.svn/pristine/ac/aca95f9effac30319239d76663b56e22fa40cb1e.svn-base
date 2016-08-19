/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import java.io.Serializable;
import javax.persistence.Basic;
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
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "ORG_COMPANY")
@NamedQueries({
    @NamedQuery(name = "OrgCompany.findAll", query = "SELECT o FROM OrgCompany o")})
public class OrgCompany implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_ORG",sequenceName = "SEQ_ORG", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_ORG")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVATION")
    private boolean activation;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyid")
//    private List<OrgDepartment> orgDepartmentList;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyid")
//    private List<OrgEmployee> orgEmployeeList;

    public OrgCompany() {
    }

    public OrgCompany(Long id) {
        this.id = id;
    }

    public OrgCompany(String code, String name, boolean activation) {
        this.code = code;
        this.name = name;
        this.activation = activation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

//    public List<OrgDepartment> getOrgDepartmentList() {
//        return orgDepartmentList;
//    }
//
//    public void setOrgDepartmentList(List<OrgDepartment> orgDepartmentList) {
//        this.orgDepartmentList = orgDepartmentList;
//    }
//
//    public List<OrgEmployee> getOrgEmployeeList() {
//        return orgEmployeeList;
//    }
//
//    public void setOrgEmployeeList(List<OrgEmployee> orgEmployeeList) {
//        this.orgEmployeeList = orgEmployeeList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrgCompany)) {
            return false;
        }
        OrgCompany other = (OrgCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.org.OrgCompany[ id=" + id + " ]";
    }

}
