/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.team;

import com.tcci.fc.entity.role.RoleHolder;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_TEAM")
@NamedQueries({
    @NamedQuery(name = "TcTeam.findAll", query = "SELECT t FROM TcTeam t")})
public class TcTeam implements Serializable, RoleHolder {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "NAME")
    private String name;
    @Size(max = 4000)
    @Column(name = "DESCRIPTION")
    private String description;

    public TcTeam() {
    }

    public TcTeam(Long id) {
        this.id = id;
    }

    public TcTeam(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof TcTeam)) {
            return false;
        }
        TcTeam other = (TcTeam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.team.TcTeam[ id=" + id + " ]";
    }
    
}
