/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.entity;

import javax.validation.constraints.NotNull;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@Entity
@Table(name = "EC_TCC_DEALER_DS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcTccDealerDs.findAll", query = "SELECT e FROM EcTccDealerDs e")
    , @NamedQuery(name = "EcTccDealerDs.findById", query = "SELECT e FROM EcTccDealerDs e WHERE e.id = :id")
    , @NamedQuery(name = "EcTccDealerDs.findByDealer", query = "SELECT e FROM EcTccDealerDs e WHERE e.dealerId = :dealerId")
    , @NamedQuery(name = "EcTccDealerDs.findByDs", query = "SELECT e FROM EcTccDealerDs e WHERE e.dsId = :dsId")
    , @NamedQuery(name = "EcTccDealerDs.findByKey", query = "SELECT e FROM EcTccDealerDs e WHERE e.dealerId = :dealerId AND e.dsId = :dsId")
})
public class EcTccDealerDs implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_TCC_DEALER_DS", sequenceName = "SEQ_TCC_DEALER_DS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TCC_DEALER_DS")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEALER_ID")
    private long dealerId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DS_ID")
    private long dsId;
    @Column(name = "STORE_ID")
    private long storeId;
    
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private EcMember creator;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private EcMember modifier;
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;

    public EcTccDealerDs() {
    }

    public EcTccDealerDs(Long id) {
        this.id = id;
    }

    public EcTccDealerDs(long dealerId, long dsId) {
        this.dealerId = dealerId;
        this.dsId = dsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getDsId() {
        return dsId;
    }

    public void setDsId(long dsId) {
        this.dsId = dsId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public EcMember getCreator() {
        return creator;
    }

    public void setCreator(EcMember creator) {
        this.creator = creator;
    }

    public EcMember getModifier() {
        return modifier;
    }

    public void setModifier(EcMember modifier) {
        this.modifier = modifier;
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
        if (!(object instanceof EcTccDealerDs)) {
            return false;
        }
        EcTccDealerDs other = (EcTccDealerDs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.entity.EcTccDealerDs[ id=" + id + " ]";
    }
    
}
