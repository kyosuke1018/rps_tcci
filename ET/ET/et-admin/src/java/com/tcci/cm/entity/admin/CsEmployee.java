/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter.pan
 */
@Entity
@Table(name = "CS_EMPLOYEE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CsEmployee.findAll", query = "SELECT c FROM CsEmployee c"),
    @NamedQuery(name = "CsEmployee.findById", query = "SELECT c FROM CsEmployee c WHERE c.id = :id"),
    @NamedQuery(name = "CsEmployee.findByAdaccount", query = "SELECT c FROM CsEmployee c WHERE c.adaccount = :adaccount")})
public class CsEmployee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ADACCOUNT")
    private String adaccount;
    @Size(max = 100)
    @Column(name = "NAME")
    private String name;
    @Size(max = 20)
    @Column(name = "CODE")
    private String code;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 100)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 50)
    @Column(name = "TEL")
    private String tel;
    @Column(name = "DEPTID")
    private Long deptid;
    @Column(name = "COMPANYID")
    private Long companyid;
    @Basic(optional = false)
    @Column(name = "DISABLED")
    private short disabled;
    @Column(name = "CREATOR")
    private Long creator;
    @Basic(optional = false)
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFIER")
    private Long modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;

    public CsEmployee() {
    }

    public CsEmployee(Long id) {
        this.id = id;
    }

    public CsEmployee(Long id, String adaccount, short disabled, Date createtimestamp) {
        this.id = id;
        this.adaccount = adaccount;
        this.disabled = disabled;
        this.createtimestamp = createtimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Long getDeptid() {
        return deptid;
    }

    public void setDeptid(Long deptid) {
        this.deptid = deptid;
    }

    public Long getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Long companyid) {
        this.companyid = companyid;
    }

    public short getDisabled() {
        return disabled;
    }

    public void setDisabled(short disabled) {
        this.disabled = disabled;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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
        if (!(object instanceof CsEmployee)) {
            return false;
        }
        CsEmployee other = (CsEmployee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.cm.entity.admin.CsEmployee[ id=" + id + " ]";
    }
    
}
