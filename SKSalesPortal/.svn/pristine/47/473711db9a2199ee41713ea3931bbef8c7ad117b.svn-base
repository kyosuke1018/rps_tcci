/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.org;

import com.tcci.fc.entity.essential.DisplayIdentity;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_SALES_CHANNELS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesChannels.findAll", query = "SELECT s FROM SkSalesChannels s"),
    @NamedQuery(name = "SkSalesChannels.findById", query = "SELECT s FROM SkSalesChannels s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesChannels.findByCode", query = "SELECT s FROM SkSalesChannels s WHERE s.code = :code"),
    @NamedQuery(name = "SkSalesChannels.findByName", query = "SELECT s FROM SkSalesChannels s WHERE s.name = :name")})
public class SkSalesChannels implements Serializable, DisplayIdentity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @Size(max = 20)
    @Column(name = "CODE")
    private String code;
    @Size(max = 70)
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "MANAGER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser manager;
    @OneToMany(mappedBy = "parent")
    private Collection<SkSalesChannels> skSalesChannelsCollection;
    @JoinColumn(name = "PARENT", referencedColumnName = "ID")
    @ManyToOne
    private SkSalesChannels parent;
    @OneToMany(mappedBy = "salesChannel")
    private Collection<SkSalesChannelMember> skSalesChannelMemberCollection;

    @Column(name = "SUBSTITUTE")
    private boolean substitute;
    
    public SkSalesChannels() {
    }

    public SkSalesChannels(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TcUser getManager() {
        return manager;
    }

    public void setManager(TcUser manager) {
        this.manager = manager;
    }

    @XmlTransient
    public Collection<SkSalesChannels> getSkSalesChannelsCollection() {
        return skSalesChannelsCollection;
    }

    public void setSkSalesChannelsCollection(Collection<SkSalesChannels> skSalesChannelsCollection) {
        this.skSalesChannelsCollection = skSalesChannelsCollection;
    }

    public SkSalesChannels getParent() {
        return parent;
    }

    public void setParent(SkSalesChannels parent) {
        this.parent = parent;
    }

    public Collection<SkSalesChannelMember> getSkSalesChannelMemberCollection() {
        return skSalesChannelMemberCollection;
    }

    public void setSkSalesChannelMemberCollection(Collection<SkSalesChannelMember> skSalesChannelMemberCollection) {
        this.skSalesChannelMemberCollection = skSalesChannelMemberCollection;
    }

    public boolean isSubstitute() {
        return substitute;
    }

    public void setSubstitute(boolean substitute) {
        this.substitute = substitute;
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
        if (!(object instanceof SkSalesChannels)) {
            return false;
        }
        SkSalesChannels other = (SkSalesChannels) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkSalesChannels[ id=" + id + " ]";
    }

    @Override
    public String getDisplayIdentifier() {
        return this.getName() + "(" + this.getCode() +")";
    }
    
}
