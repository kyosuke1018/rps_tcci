package com.tcci.fc.entity.access;

import com.tcci.fc.entity.essential.TcObject;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nEO.Fu
 */
@Entity
@Table(name = "tc_foldered_aclentry")
public class TcFolderedAclEntry implements Serializable, AccessControlList, TcObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "acltargetclassname", nullable = false)
    private String aclTargetClassName;
    @Column(name = "acltargetid", nullable = false)
    private Long aclTargetId;
    @Column(name = "permissionmask", nullable = false)
    private String permissionmask;
    @Column(name = "inheritancemask",nullable=false)
    private String inheritancemask;
    @Column(name = "aclprincipalclassname", nullable = false)
    private String aclPrincipalClassName;
    @Column(name = "aclprincipalid", nullable = false)
    private Long aclPrincipalId;
    @Column(name = "createtimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "modifytimestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "creator", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "modifier", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;

    public TcFolderedAclEntry() {
    }

    public TcFolderedAclEntry(Long id) {
        this.id = id;
    }

    public TcFolderedAclEntry(Long id, String aclTargetClassName, Long aclTargetId, String permissionmask, String aclPrincipalClassName, Long aclPrincipalId) {
        this.id = id;
        this.aclTargetClassName = aclTargetClassName;
        this.aclTargetId = aclTargetId;
        this.permissionmask = permissionmask;
        this.aclPrincipalClassName = aclPrincipalClassName;
        this.aclPrincipalId = aclPrincipalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAclTargetClassName() {
        return aclTargetClassName;
    }

    public void setAclTargetClassName(String aclTargetClassName) {
        this.aclTargetClassName = aclTargetClassName;
    }

    public Long getAclTargetId() {
        return aclTargetId;
    }

    public void setAclTargetId(Long aclTargetId) {
        this.aclTargetId = aclTargetId;
    }

    public String getPermissionmask() {
        return permissionmask;
    }

    public void setPermissionmask(String permissionmask) {
        this.permissionmask = permissionmask;
    }

    public String getInheritancemask() {
        return inheritancemask;
    }

    public void setInheritancemask(String inheritancemask) {
        this.inheritancemask = inheritancemask;
    }

    public String getAclPrincipalClassName() {
        return aclPrincipalClassName;
    }

    public void setAclPrincipalClassName(String aclPrincipalClassName) {
        this.aclPrincipalClassName = aclPrincipalClassName;
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

    public Long getAclPrincipalId() {
        return aclPrincipalId;
    }

    public void setAclPrincipalId(Long aclPrincipalId) {
        this.aclPrincipalId = aclPrincipalId;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof TcFolderedAclEntry)) {
            return false;
        }
        TcFolderedAclEntry other = (TcFolderedAclEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.access.TcAclEntry:" + id;
    }
    
    @Override
    public String getDisplayIdentifier() {
        return "";
    }
}
