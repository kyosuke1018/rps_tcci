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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ORG_EMPLOYEE")
@NamedQueries({
    @NamedQuery(name = "OrgEmployee.findAll", query = "SELECT o FROM OrgEmployee o")})
public class OrgEmployee implements Serializable {
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
    @Size(min = 1, max = 40)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 40)
    @Column(name = "ADACCOUNT")
    private String adaccount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVATION")
    private boolean activation;
    @JoinColumn(name = "DEPTID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrgDepartment orgDepartment;
    @JoinColumn(name = "COMPANYID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrgCompany orgCompany;

    public OrgEmployee() {
    }

    public OrgEmployee(OrgCompany orgCompany, OrgDepartment orgDepartment, String code, String name, String email, String adaccount, boolean activation) {
        this.orgCompany = orgCompany;
        this.orgDepartment = orgDepartment;
        this.code = code;
        this.name = name;
        this.email = email;
        this.adaccount = adaccount;
        this.activation = activation;
    }

    public OrgEmployee(Long id) {
        this.id = id;
    }

    public OrgEmployee(Long id, String code, String name, boolean activation) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public OrgDepartment getOrgDepartment() {
        return orgDepartment;
    }

    public void setOrgDepartment(OrgDepartment orgDepartment) {
        this.orgDepartment = orgDepartment;
    }

    public OrgCompany getOrgCompany() {
        return orgCompany;
    }

    public void setOrgCompany(OrgCompany orgCompany) {
        this.orgCompany = orgCompany;
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
        if (!(object instanceof OrgEmployee)) {
            return false;
        }
        OrgEmployee other = (OrgEmployee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.org.OrgEmployee[ id=" + id + " ]";
    }

}
