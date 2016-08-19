/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

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
@Table(name = "TC_GROUP")
@NamedQueries({
    @NamedQuery(name = "TcGroup.findAll", query = "SELECT t FROM TcGroup t")})
public class TcGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "GROUP_ID")
    private String groupId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPDATE_USER")
    private long updateUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupId")
    private Collection<TcUsergroup> tcUsergroupCollection;

    public TcGroup() {
    }

    public TcGroup(Long id) {
        this.id = id;
    }

    public TcGroup(Long id, String groupId, String groupName, long updateUser) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.updateUser = updateUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcGroup)) {
            return false;
        }
        TcGroup other = (TcGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + ":" + getId();
    }
    
}
