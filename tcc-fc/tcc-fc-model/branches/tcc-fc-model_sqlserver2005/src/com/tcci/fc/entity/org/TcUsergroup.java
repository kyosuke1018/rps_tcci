/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
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
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser userId;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcGroup groupId;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;

    public TcUsergroup() {
    }

    public TcUsergroup(Long id) {
        this.id = id;
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

    public TcGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(TcGroup groupId) {
        this.groupId = groupId;
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
