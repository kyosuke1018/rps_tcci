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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "CM_USER_ORG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CmUserOrg.findAll", query = "SELECT p FROM CmUserOrg p")})
public class CmUserOrg implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_GLOBAL")
    @SequenceGenerator (name="SEQ_GLOBAL", sequenceName="SEQ_GLOBAL", allocationSize=1)
    private Long id;
    
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TcUser userId;
    @JoinColumn(name = "ORG_ID", referencedColumnName = "ID")
    @ManyToOne
    private CmOrg orgId;
    
    @Column(name = "CREATOR")
    private Long creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;

    public CmUserOrg() {
    }

    public CmUserOrg(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TcUser getUserId() {
        return userId;
    }

    public void setUserId(TcUser userId) {
        this.userId = userId;
    }

    public CmOrg getOrgId() {
        return orgId;
    }

    public void setOrgId(CmOrg orgId) {
        this.orgId = orgId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmUserOrg)) {
            return false;
        }
        CmUserOrg other = (CmUserOrg) object;
        if( other.getId()==null ){
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.tcci.ic.entity.admin.CmUserOrg[ id=" + id + " ]";
    }
    
}
