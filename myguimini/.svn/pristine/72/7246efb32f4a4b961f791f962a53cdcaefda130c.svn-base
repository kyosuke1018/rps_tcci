/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.entity;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.Persistable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "MY_MOBILE_APP")
@NamedQueries({
    @NamedQuery(name = "MyMobileApp.findAll", query = "SELECT m FROM MyMobileApp m"),
    @NamedQuery(name = "MyMobileApp.findByPlatform", query = "SELECT m FROM MyMobileApp m WHERE m.platform=:platform")
})
public class MyMobileApp implements Serializable, Persistable, ContentHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_TCC",sequenceName = "SEQ_TCC", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_TCC")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PLATFORM")
    private String platform;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "APP_VERSION")
    private String appVersion;

    public MyMobileApp() {
    }

    public MyMobileApp(Long id) {
        this.id = id;
    }

    public MyMobileApp(Long id, String platform, String appVersion) {
        this.id = id;
        this.platform = platform;
        this.appVersion = appVersion;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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
        if (!(object instanceof MyMobileApp)) {
            return false;
        }
        MyMobileApp other = (MyMobileApp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.myguimini.entity.MyMobileApp[ id=" + id + " ]";
    }
    
}
