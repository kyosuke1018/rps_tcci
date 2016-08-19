/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.org;

import com.tcci.fc.entity.essential.DisplayIdentity;
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
@Table(name = "TC_GROUP")
@NamedQueries({
    @NamedQuery(name = "TcGroup.findAll", query = "SELECT t FROM TcGroup t")})
public class TcGroup implements Serializable, TcPrincipal {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupId")
    private Collection<TcUsergroup> tcUsergroupCollection;

    public TcGroup() {
    }

    public TcGroup(Long id) {
        this.id = id;
    }

    public TcGroup(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String getDisplayIdentifier() {
        return toString();
    }
}
