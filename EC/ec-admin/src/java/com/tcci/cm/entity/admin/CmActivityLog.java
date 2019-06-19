/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Peter
 */
@Entity
@Table(name = "CM_ACTIVITY_LOG")
@NamedQueries({
    @NamedQuery(name = "CmActivityLog.findAll", query = "SELECT p FROM CmActivityLog p"),
    @NamedQuery(name = "CmActivityLog.findById", query = "SELECT p FROM CmActivityLog p WHERE p.id = :id"),
    @NamedQuery(name = "CmActivityLog.findByCode", query = "SELECT p FROM CmActivityLog p WHERE p.code = :code"),
    @NamedQuery(name = "CmActivityLog.findByViewId", query = "SELECT p FROM CmActivityLog p WHERE p.viewId = :viewId"),
    @NamedQuery(name = "CmActivityLog.findByDetail", query = "SELECT p FROM CmActivityLog p WHERE p.detail = :detail"),
    @NamedQuery(name = "CmActivityLog.findByCreator", query = "SELECT p FROM CmActivityLog p WHERE p.creator = :creator"),
    @NamedQuery(name = "CmActivityLog.findByCreatetimestamp", query = "SELECT p FROM CmActivityLog p WHERE p.createtimestamp = :createtimestamp")})
public class CmActivityLog implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_GLOBAL")
    @SequenceGenerator (name="SEQ_GLOBAL", sequenceName="SEQ_GLOBAL", allocationSize=1)
    private Long id;
    @Size(max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 60)
    @Column(name = "VIEW_ID")
    private String viewId;
    @Size(max = 1024)
    @Column(name = "DETAIL")
    private String detail;
    @Column(name = "SUCCESS")
    private Boolean success;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    public CmActivityLog() {
    }

    public CmActivityLog(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getDetail() {
        return detail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
        if (!(object instanceof CmActivityLog)) {
            return false;
        }
        CmActivityLog other = (CmActivityLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ic.entity.admin.CmActivityLog[ id=" + id + " ]";
    }
    
}
