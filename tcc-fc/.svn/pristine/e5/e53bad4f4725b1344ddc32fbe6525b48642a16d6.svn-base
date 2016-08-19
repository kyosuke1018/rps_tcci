/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.role;

import com.tcci.fc.entity.role.TcRole;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_ROLEHOLDERROLEMAP")
@NamedQueries({
    @NamedQuery(name = "TcRoleholderrolemap.findAll", query = "SELECT t FROM TcRoleholderrolemap t")})
public class TcRoleholderrolemap implements Serializable {
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
    @Column(name = "HOLDERCLASSNAME")
    private String holderclassname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HOLDERID")
    private Long holderid;
    @JoinColumn(name = "ROLE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcRole role;

    public TcRoleholderrolemap() {
    }

    public TcRoleholderrolemap(Long id) {
        this.id = id;
    }

    public TcRoleholderrolemap(Long id, String holderclassname, Long holderid) {
        this.id = id;
        this.holderclassname = holderclassname;
        this.holderid = holderid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHolderclassname() {
        return holderclassname;
    }

    public void setHolderclassname(String holderclassname) {
        this.holderclassname = holderclassname;
    }

    public Long getHolderid() {
        return holderid;
    }

    public void setHolderid(Long holderid) {
        this.holderid = holderid;
    }

    public TcRole getRole() {
        return role;
    }

    public void setRole(TcRole role) {
        this.role = role;
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
        if (!(object instanceof TcRoleholderrolemap)) {
            return false;
        }
        TcRoleholderrolemap other = (TcRoleholderrolemap) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.team.TcRoleholderrolemap[ id=" + id + " ]";
    }
    
}
