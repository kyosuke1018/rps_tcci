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

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "CM_USER_FACTORYGROUP_R")
@NamedQueries({
    @NamedQuery(name = "CmUserFactorygroupR.findAll", query = "SELECT p FROM CmUserFactorygroupR p")})
public class CmUserFactorygroupR implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)    
    private Long id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TcUser userId;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "FACTORYGROUP_ID", referencedColumnName = "ID")
    @ManyToOne
    private CmFactorygroup factorygroupId;
    
    @Column(name = "USERGROUP_ID")
    private Long usergroupId; // for 特定角色的設定(回饋機制)

    public CmUserFactorygroupR() {
    }

    public CmUserFactorygroupR(Long id) {
        this.id = id;
    }

    public Long getUsergroupId() {
        return usergroupId;
    }

    public void setUsergroupId(Long usergroupId) {
        this.usergroupId = usergroupId;
    }

    public TcUser getUserId() {
        return userId;
    }

    public void setUserId(TcUser userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public CmFactorygroup getFactorygroupId() {
        return factorygroupId;
    }

    public void setFactorygroupId(CmFactorygroup factorygroupId) {
        this.factorygroupId = factorygroupId;
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
        if (!(object instanceof CmUserFactorygroupR)) {
            return false;
        }
        CmUserFactorygroupR other = (CmUserFactorygroupR) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ic.entity.admin.CmUserFactorygroupR[ id=" + id + " ]";
    }
    
}
