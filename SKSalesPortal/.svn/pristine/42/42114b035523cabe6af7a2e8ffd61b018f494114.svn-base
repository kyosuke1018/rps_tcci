/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.entity.datawarehouse;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jackson.Lee
 */
@Entity
@Cacheable(false)
@Table(name = "MG_SAPCLIENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TcSapclient.findAll", query = "SELECT t FROM TcSapclient t")})
public class TcSapclient implements Serializable {
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CLIENT")
    private String client;
    @Size(max = 60)
    @Column(name = "NAME")
    private String name;
    @Size(max = 10)
    @Column(name = "CODE")
    private String code;

    public TcSapclient() {
    }

    public TcSapclient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (client != null ? client.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TcSapclient)) {
            return false;
        }
        TcSapclient other = (TcSapclient) object;
        if ((this.client == null && other.client != null) || (this.client != null && !this.client.equals(other.client))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.mygui.entity.TcSapclient[ client=" + client + " ]";
    }

    
}
