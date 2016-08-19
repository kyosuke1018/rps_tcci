/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_PROCESSTEMPLATE")
@NamedQueries({
    @NamedQuery(name = "TcProcesstemplate.findAll", query = "SELECT t FROM TcProcesstemplate t")})
public class TcProcesstemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @Column(name = "PROCESSNAME")
    private String processname;
    @Basic(optional = false)
    @Column(name = "PROCESSVERSION")
    private String processversion;
    @Column(name = "DISABLED")
    private Short disabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "processtemplateid")
    private Collection<TcProcess> tcProcessCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "processtemplateid")
    private Collection<TcActivitytemplate> tcActivitytemplateCollection;

    public TcProcesstemplate() {
    }

    public TcProcesstemplate(Long id) {
        this.id = id;
    }

    public TcProcesstemplate(Long id, String processname, String processversion) {
        this.id = id;
        this.processname = processname;
        this.processversion = processversion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public String getProcessversion() {
        return processversion;
    }

    public void setProcessversion(String processversion) {
        this.processversion = processversion;
    }

    public Short getDisabled() {
        return disabled;
    }

    public void setDisabled(Short disabled) {
        this.disabled = disabled;
    }

    public Collection<TcProcess> getTcProcessCollection() {
        return tcProcessCollection;
    }

    public void setTcProcessCollection(Collection<TcProcess> tcProcessCollection) {
        this.tcProcessCollection = tcProcessCollection;
    }

    public Collection<TcActivitytemplate> getTcActivitytemplateCollection() {
        return tcActivitytemplateCollection;
    }

    public void setTcActivitytemplateCollection(Collection<TcActivitytemplate> tcActivitytemplateCollection) {
        this.tcActivitytemplateCollection = tcActivitytemplateCollection;
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
        if (!(object instanceof TcProcesstemplate)) {
            return false;
        }
        TcProcesstemplate other = (TcProcesstemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcProcesstemplate[ id=" + id + " ]";
    }
    
}
