/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neo.Fu
 */
@Entity
@Table(name = "EC_SERVICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Service.findAll", query = "SELECT s FROM Service s"),
    @NamedQuery(name = "Service.findAllActive", query = "SELECT s FROM Service s where s.active = TRUE"),
    @NamedQuery(name = "Service.findById", query = "SELECT s FROM Service s WHERE s.id = :id"),
    @NamedQuery(name = "Service.findByService", query = "SELECT s FROM Service s WHERE s.service = :service"),
    @NamedQuery(name = "Service.findByServiceUrl", query = "SELECT s FROM Service s WHERE s.serviceUrl = :serviceUrl"),
    @NamedQuery(name = "Service.findByDescription", query = "SELECT s FROM Service s WHERE s.description = :description"),
    @NamedQuery(name = "Service.findByActive", query = "SELECT s FROM Service s WHERE s.active = :active")})
public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @Column(name = "SERVICE")
    private String service;
    @Basic(optional = false)
    @Column(name = "SERVICE_URL")
    private String serviceUrl;
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @Column(name = "ACTIVE")
    private boolean active;

    public Service() {
    }

    public Service(Long id) {
        this.id = id;
    }

    public Service(Long id, String service, String serviceUrl, boolean active) {
        this.id = id;
        this.service = service;
        this.serviceUrl = serviceUrl;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mobile.entity.Service[ id=" + id + " ]";
    }
    
}
