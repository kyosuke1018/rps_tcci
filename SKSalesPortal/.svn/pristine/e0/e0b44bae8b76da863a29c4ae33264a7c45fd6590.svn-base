/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.quotation;

import com.tcci.fc.entity.org.TcUser;
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
 * @author Neo.Fu
 */
@Entity
@Table(name = "SK_QUOTATION_MAIL_GROUP_USER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkQuotationMailGroupUser.findAll", query = "SELECT s FROM SkQuotationMailGroupUser s")})
public class SkQuotationMailGroupUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser user;
    @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SkQuotationMailGroup group;

    public SkQuotationMailGroupUser() {
    }

    public SkQuotationMailGroupUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TcUser getUser() {
        return user;
    }

    public void setUser(TcUser user) {
        this.user = user;
    }

    public SkQuotationMailGroup getGroup() {
        return group;
    }

    public void setGroup(SkQuotationMailGroup group) {
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
        if (!(object instanceof SkQuotationMailGroupUser)) {
            return false;
        }
        SkQuotationMailGroupUser other = (SkQuotationMailGroupUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkQuotationMailGroupUser[ id=" + id + " ]";
    }
    
}
