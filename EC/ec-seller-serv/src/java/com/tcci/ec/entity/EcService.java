/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_SERVICE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcService.findAll", query = "SELECT e FROM EcService e")
    , @NamedQuery(name = "EcService.findById", query = "SELECT e FROM EcService e WHERE e.id = :id")
    , @NamedQuery(name = "EcService.findByService", query = "SELECT e FROM EcService e WHERE e.service = :service")})
public class EcService implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_SERVICE", sequenceName = "SEQ_SERVICE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICE")        
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
    private Boolean active;

    public EcService() {
    }

    public EcService(Long id) {
        this.id = id;
    }

    public EcService(Long id, String service, String serviceUrl, Boolean active) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
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
        if (!(object instanceof EcService)) {
            return false;
        }
        EcService other = (EcService) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcService[ id=" + id + " ]";
    }
    
}
