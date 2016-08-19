/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
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
public class TcUser implements Serializable, TcPrincipal {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize=1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LOGIN_ACCOUNT")
    private String loginAccount;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<TcUsergroup> tcUsergroupCollection;
    @Column(name = "EMP_ID")
    private String empId;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DISABLED")
    private Boolean disabled;
    @Column(name = "CNAME")
    private String cname;

    public TcUser() {
    }

    public TcUser(Long id) {
        this.id = id;
    }

    public TcUser(Long id, String loginAccount) {
        this.id = id;
        this.loginAccount = loginAccount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Collection<TcUsergroup> getTcUsergroupCollection() {
        return tcUsergroupCollection;
    }

    public void setTcUsergroupCollection(Collection<TcUsergroup> tcUsergroupCollection) {
        this.tcUsergroupCollection = tcUsergroupCollection;
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

    /**
     * @return the empId
     */
    public String getEmpId() {
        return empId;
    }

    /**
     * @param empId the empId to set
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String getDisplayIdentifier() {
        StringBuilder displayIdentifier = new StringBuilder("");
        boolean nameIsEmpty = true;
        if (getCname() != null) {
            nameIsEmpty = false;
            displayIdentifier.append(getCname());
        }
        if (!nameIsEmpty) {
            displayIdentifier.append("(").append(getLoginAccount()).append(")");
        } else {
            displayIdentifier.append(getLoginAccount());
        }
        if (getDisabled()) {
            displayIdentifier.append("{Disabled}");
        }
        return displayIdentifier.toString();
    }

    @Override
    public String getName() {
        return getCname();
    }

    @Override
    public void setName(String name) {
        cname = name;
    }
}
