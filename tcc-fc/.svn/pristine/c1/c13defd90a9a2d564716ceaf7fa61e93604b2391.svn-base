/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "TC_USERGROUP")
@NamedQueries({
    @NamedQuery(name = "TcUsergroup.findAll", query = "SELECT t FROM TcUsergroup t")})
public class TcUsergroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPDATE_USER")
    private long updateUser;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser userId;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcGroup groupId;

    public TcUsergroup() {
    }

    public TcUsergroup(Long id) {
        this.id = id;
    }

    public TcUsergroup(Long id, long updateUser) {
        this.id = id;
        this.updateUser = updateUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(long updateUser) {
        this.updateUser = updateUser;
    }

    public TcUser getUserId() {
        return userId;
    }

    public void setUserId(TcUser userId) {
        this.userId = userId;
    }

    public TcGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(TcGroup groupId) {
        this.groupId = groupId;
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
        if (!(object instanceof TcUsergroup)) {
            return false;
        }
        TcUsergroup other = (TcUsergroup) object;
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
