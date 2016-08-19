/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.role;

import com.tcci.fc.entity.role.TcRole;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Wayne.Hu
 */
@Entity
@Table(name = "TC_ROLEPRINCIPALLINK")
@NamedQueries({
    @NamedQuery(name = "TcRoleprincipallink.findAll", query = "SELECT t FROM TcRoleprincipallink t")})
public class TcRoleprincipallink implements Serializable {
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
    @Column(name = "PRINCIPALCLASSNAME")
    private String principalclassname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRINCIPALID")
    private BigInteger principalid;
    @JoinColumn(name = "ROLE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcRole role;

    public TcRoleprincipallink() {
    }

    public TcRoleprincipallink(Long id) {
        this.id = id;
    }

    public TcRoleprincipallink(Long id, String principalclassname, BigInteger principalid) {
        this.id = id;
        this.principalclassname = principalclassname;
        this.principalid = principalid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrincipalclassname() {
        return principalclassname;
    }

    public void setPrincipalclassname(String principalclassname) {
        this.principalclassname = principalclassname;
    }

    public BigInteger getPrincipalid() {
        return principalid;
    }

    public void setPrincipalid(BigInteger principalid) {
        this.principalid = principalid;
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
        if (!(object instanceof TcRoleprincipallink)) {
            return false;
        }
        TcRoleprincipallink other = (TcRoleprincipallink) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.team.TcRoleprincipallink[ id=" + id + " ]";
    }
    
}
