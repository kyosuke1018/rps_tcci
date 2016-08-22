/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.quotation;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Neo.Fu
 */
    @Entity
@Table(name = "SK_QUOTATION_MAIL_GROUP")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkQuotationMailGroup.findAll", query = "SELECT s FROM SkQuotationMailGroup s")})
public class SkQuotationMailGroup implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "CREATOR",referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name="MODIFIER",referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Collection<SkQuotationMailGroupUser> userCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Collection<SkQuotationMailGroupMember> memberCollection;
    @Column(name="CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name="MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    public SkQuotationMailGroup() {
        this.userCollection = new ArrayList();
        this.memberCollection = new ArrayList();
    }

    public SkQuotationMailGroup(Long id) {
        this.id = id;
    }

    public SkQuotationMailGroup(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    @XmlTransient
    public Collection<SkQuotationMailGroupUser> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<SkQuotationMailGroupUser> userCollection) {
        this.userCollection = userCollection;
    }

    @XmlTransient
    public Collection<SkQuotationMailGroupMember> getMemberCollection() {
        return memberCollection;
    }

    public void setMemberCollection(Collection<SkQuotationMailGroupMember> memberCollection) {
        this.memberCollection = memberCollection;
    }

    @XmlTransient
    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    @XmlTransient
    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
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
        if (!(object instanceof SkQuotationMailGroup)) {
            return false;
        }
        SkQuotationMailGroup other = (SkQuotationMailGroup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "com.tcci.sksp.entity.SkQuotationMailGroup[ id=" + id + " ]";
         return "com.tcci.sksp.entity.SkQuotationMailGroup[ id=" + id + ",\n"+
                 "name="+name+",\n"+
                 "]";
    }
    
}
