/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_MEMBER_FACTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcMemberFactory.findAll", query = "SELECT e FROM EcMemberFactory e")
    , @NamedQuery(name = "EcMemberFactory.findById", query = "SELECT e FROM EcMemberFactory e WHERE e.id = :id")
    , @NamedQuery(name = "EcMemberFactory.findByMemberId", query = "SELECT e FROM EcMemberFactory e WHERE e.memberId = :memberId")
    , @NamedQuery(name = "EcMemberFactory.findByFactoryId", query = "SELECT e FROM EcMemberFactory e WHERE e.factoryId = :factoryId")
    , @NamedQuery(name = "EcMemberFactory.findByKey", query = "SELECT e FROM EcMemberFactory e WHERE e.memberId = :memberId AND e.factoryId = :factoryId")
})
public class EcMemberFactory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TCC")        
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEMBER_ID")
    private long memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FACTORY_ID")
    private long factoryId;

    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcMemberFactory() {
    }

    public EcMemberFactory(Long id) {
        this.id = id;
    }

    public EcMemberFactory(Long id, long memberId, long factoryId) {
        this.id = id;
        this.memberId = memberId;
        this.factoryId = factoryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(long factoryId) {
        this.factoryId = factoryId;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
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
        if (!(object instanceof EcMemberFactory)) {
            return false;
        }
        EcMemberFactory other = (EcMemberFactory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcMemberFactory[ id=" + id + " ]";
    }
    
}
