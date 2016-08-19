/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import com.tcci.fc.entity.essential.DisplayIdentity;
import com.tcci.fc.entity.essential.Persistable;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "TC_USER")
@NamedQueries({
    @NamedQuery(name = "TcUser.findAll", query = "SELECT t FROM TcUser t")})
public class TcUser implements Serializable, DisplayIdentity, Persistable  {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "LOGIN_NAME")
    private String loginName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "EMP_ID")
    private String empId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROLE_ID")
    private long roleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "IS_VALID")
    private String isValid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPDATE_USER")
    private long updateUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<TcUsergroup> tcUsergroupCollection;

    @Column(name = "EMAIL")
    private String email;

    public TcUser() {
    }

    public TcUser(Long id) {
        this.id = id;
    }

    public TcUser(Long id, String loginAccount, String loginName, String empId, long roleId, String isValid, long updateUser) {
        this.id = id;
        this.loginAccount = loginAccount;
        this.loginName = loginName;
        this.empId = empId;
        this.roleId = roleId;
        this.isValid = isValid;
        this.updateUser = updateUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(long updateUser) {
        this.updateUser = updateUser;
    }

    public Collection<TcUsergroup> getTcUsergroupCollection() {
        return tcUsergroupCollection;
    }

    public void setTcUsergroupCollection(Collection<TcUsergroup> tcUsergroupCollection) {
        this.tcUsergroupCollection = tcUsergroupCollection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof TcUser)) {
            return false;
        }
        TcUser other = (TcUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }

    @Override
    public String getDisplayIdentifier() {
        return getLoginName() + "(" + getEmpId() +")";
    }
    
}
