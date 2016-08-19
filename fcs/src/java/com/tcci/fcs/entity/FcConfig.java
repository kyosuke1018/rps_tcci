/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.entity;

import com.tcci.fcs.enums.CompanyGroupEnum;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "FC_CONFIG")
@NamedQueries({
    @NamedQuery(name = "FcConfig.findAll", query = "SELECT f FROM FcConfig f"),
    @NamedQuery(name = "FcConfig.findByKey", query = "SELECT f FROM FcConfig f WHERE f.configKey=:key"),
    @NamedQuery(name = "FcConfig.findByKeyAndComp", query = "SELECT f FROM FcConfig f WHERE f.configKey=:key AND f.group = :group")
})
public class FcConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CONFIG_KEY")
    private String configKey;
    @Size(max = 50)
    @Column(name = "CONFIG_VALUE")
    private String configValue;
    @Enumerated(EnumType.STRING)
    @Column(name = "COMP_GROUP")
    private CompanyGroupEnum group;

    public FcConfig() {
    }

    public FcConfig(Long id) {
        this.id = id;
    }

    public FcConfig(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
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
        if (!(object instanceof FcConfig)) {
            return false;
        }
        FcConfig other = (FcConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcs.entity.FcConfig[ id=" + id + " ]";
    }
    
}
