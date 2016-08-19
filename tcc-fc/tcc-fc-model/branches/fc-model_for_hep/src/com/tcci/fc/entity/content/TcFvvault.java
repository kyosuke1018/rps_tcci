/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.content;

import com.tcci.fc.entity.essential.TcDomain;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "TC_FVVAULT")
@NamedQueries({
    @NamedQuery(name = "TcFvvault.findAll", query = "SELECT t FROM TcFvvault t")})
public class TcFvvault implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "HOSTNAME")
    private String hostname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 600)
    @Column(name = "LOCATION")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Column(name = "READONLY")
    private short readonly;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENABLED")
    private short enabled;
    @Size(max = 600)
    @Column(name = "URL")
    private String url;
    @JoinColumn(name = "DOMAIN", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcDomain domain;

    public TcFvvault() {
    }

    public TcFvvault(Long id) {
        this.id = id;
    }

    public TcFvvault(Long id, String hostname, String location, short readonly, short enabled) {
        this.id = id;
        this.hostname = hostname;
        this.location = location;
        this.readonly = readonly;
        this.enabled = enabled;
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

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public short getReadonly() {
        return readonly;
    }

    public void setReadonly(short readonly) {
        this.readonly = readonly;
    }

    public short getEnabled() {
        return enabled;
    }

    public void setEnabled(short enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TcDomain getDomain() {
        return domain;
    }

    public void setDomain(TcDomain domain) {
        this.domain = domain;
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
        if (!(object instanceof TcFvvault)) {
            return false;
        }
        TcFvvault other = (TcFvvault) object;
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
