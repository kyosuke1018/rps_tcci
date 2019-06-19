/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "CM_USERCOMPANY")
@NamedQueries({
    @NamedQuery(name = "CmUsercompany.findAll", query = "SELECT p FROM CmUsercompany p"),
    @NamedQuery(name = "CmUsercompany.findById", query = "SELECT p FROM CmUsercompany p WHERE p.id = :id"),
    @NamedQuery(name = "CmUsercompany.findByUserId", query = "SELECT p FROM CmUsercompany p WHERE p.userId = :userId"),
    @NamedQuery(name = "CmUsercompany.findBySapClientCode", query = "SELECT p FROM CmUsercompany p WHERE p.sapClientCode = :sapClientCode"),
    @NamedQuery(name = "CmUsercompany.findByCreator", query = "SELECT p FROM CmUsercompany p WHERE p.creator = :creator"),
    @NamedQuery(name = "CmUsercompany.findByCreatetimestamp", query = "SELECT p FROM CmUsercompany p WHERE p.createtimestamp = :createtimestamp")})
public class CmUsercompany implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SAP_CLIENT_CODE")
    private String sapClientCode;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    public CmUsercompany() {
    }

    public CmUsercompany(Long id) {
        this.id = id;
    }

    public CmUsercompany(Long id, TcUser userId, String sapClientCode) {
        this.id = id;
        this.userId = userId;
        this.sapClientCode = sapClientCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TcUser getUserId() {
        return userId;
    }

    public void setUserId(TcUser userId) {
        this.userId = userId;
    }

    public String getSapClientCode() {
        return sapClientCode;
    }

    public void setSapClientCode(String sapClientCode) {
        this.sapClientCode = sapClientCode;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
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
        if (!(object instanceof CmUsercompany)) {
            return false;
        }
        CmUsercompany other = (CmUsercompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.pp.entity.org.PpUsercompany[ id=" + id + " ]";
    }
    
}
