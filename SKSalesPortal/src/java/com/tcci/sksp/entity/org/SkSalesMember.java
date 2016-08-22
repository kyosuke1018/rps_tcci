/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.org;

import com.google.gson.annotations.Expose;
import com.tcci.fc.entity.essential.DisplayIdentity;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_SALES_MEMBER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesMember.findAll", query = "SELECT s FROM SkSalesMember s"),
    @NamedQuery(name = "SkSalesMember.findById", query = "SELECT s FROM SkSalesMember s WHERE s.id = :id"),
    @NamedQuery(name = "SkSalesMember.findByCode", query = "SELECT s FROM SkSalesMember s WHERE s.code = :code"),
    @NamedQuery(name = "SkSalesMember.findByName", query = "SELECT s FROM SkSalesMember s WHERE s.name = :name")})
public class SkSalesMember implements Serializable, DisplayIdentity {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator = "SEQ_TCC")
    @Expose
    private Long id;
    @Size(max = 20)
    @Column(name = "CODE")
    @Expose
    private String code;
    @Size(max = 70)
    @Column(name = "NAME")
    @Expose
    private String name;
    @Column(name = "SELECTABLE")
    @Expose
    private boolean selectable;
    @JoinColumn(name = "MEMBER", referencedColumnName = "ID")
    @ManyToOne
    @Expose
    private TcUser member;
    @OneToMany(mappedBy = "salesMember")
    @Expose(deserialize = false, serialize = false)
    private Collection<SkSalesChannelMember> skSalesChannelMemberCollection;

    public SkSalesMember() {
    }

    public SkSalesMember(Long id) {
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

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public TcUser getMember() {
        return member;
    }

    public void setMember(TcUser member) {
        this.member = member;
    }

    public Collection<SkSalesChannelMember> getSkSalesChannelMemberCollection() {
        return skSalesChannelMemberCollection;
    }

    public void setSkSalesChannelMemberCollection(Collection<SkSalesChannelMember> skSalesChannelMemberCollection) {
        this.skSalesChannelMemberCollection = skSalesChannelMemberCollection;
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
        if (!(object instanceof SkSalesMember)) {
            return false;
        }
        SkSalesMember other = (SkSalesMember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkSalesMember[ id=" + id + " ]";
    }

    @Override
    public String getDisplayIdentifier() {
        String displayIdentifier = "";
        if (this.code != null) {
            displayIdentifier = this.code;
        }
        if (this.member != null) {
            if (displayIdentifier == null) {
                displayIdentifier = this.member.getDisplayIdentifier();
            } else {
                displayIdentifier = displayIdentifier + " - " + this.member.getDisplayIdentifier();
            }
        }
        return displayIdentifier;
    }
}
