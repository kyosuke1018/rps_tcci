/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.org;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_SALES_CHANNEL_MEMBER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkSalesChannelMember.findAll", query = "SELECT s FROM SkSalesChannelMember s"),
    @NamedQuery(name = "SkSalesChannelMember.findById", query = "SELECT s FROM SkSalesChannelMember s WHERE s.id = :id")})
public class SkSalesChannelMember implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @JoinColumn(name = "SALES_MEMBER", referencedColumnName = "ID")
    @ManyToOne
    private SkSalesMember salesMember;
    @JoinColumn(name = "SALES_CHANNEL", referencedColumnName = "ID")
    @ManyToOne
    private SkSalesChannels salesChannel;

    public SkSalesChannelMember() {
    }

    public SkSalesChannelMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkSalesMember getSalesMember() {
        return salesMember;
    }

    public void setSalesMember(SkSalesMember salesMember) {
        this.salesMember = salesMember;
    }

    public SkSalesChannels getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(SkSalesChannels salesChannel) {
        this.salesChannel = salesChannel;
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
        if (!(object instanceof SkSalesChannelMember)) {
            return false;
        }
        SkSalesChannelMember other = (SkSalesChannelMember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.org.SkSalesChannelMember[ id=" + id + " ]";
    }
    
}
