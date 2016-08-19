/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.role;

import com.tcci.fc.entity.role.RoleEnum;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_ROLE")
@NamedQueries({
    @NamedQuery(name = "TcRole.findAll", query = "SELECT t FROM TcRole t")})
public class TcRole implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Enumerated(EnumType.STRING)    
    @Column(name = "NAME")
    private RoleEnum name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<TcRoleprincipallink> tcRoleprincipallinkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<TcRoleholderrolemap> tcRoleholderrolemapCollection;

    public TcRole() {
    }

    public TcRole(Long id) {
        this.id = id;
    }

    public TcRole(Long id, RoleEnum name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }

    public Collection<TcRoleprincipallink> getTcRoleprincipallinkCollection() {
        return tcRoleprincipallinkCollection;
    }

    public void setTcRoleprincipallinkCollection(Collection<TcRoleprincipallink> tcRoleprincipallinkCollection) {
        this.tcRoleprincipallinkCollection = tcRoleprincipallinkCollection;
    }

    public Collection<TcRoleholderrolemap> getTcRoleholderrolemapCollection() {
        return tcRoleholderrolemapCollection;
    }

    public void setTcRoleholderrolemapCollection(Collection<TcRoleholderrolemap> tcRoleholderrolemapCollection) {
        this.tcRoleholderrolemapCollection = tcRoleholderrolemapCollection;
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
        if (!(object instanceof TcRole)) {
            return false;
        }
        TcRole other = (TcRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.team.TcRole[ id=" + id + " ]";
    }
    
}
