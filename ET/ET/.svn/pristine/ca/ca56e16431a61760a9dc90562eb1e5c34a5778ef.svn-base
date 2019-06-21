/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.entity.admin;

import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "CM_USERFACTORY")
public class CmUserfactory implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)    
    private Long id;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser userId;
    @JoinColumn(name = "FACTORY_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CmFactory factoryId;

    @Column(name = "CREATOR")
    private Long creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public CmUserfactory() {
    }

    public CmUserfactory(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public TcUser getUserId() {
        return userId;
    }

    public void setUserId(TcUser userId) {
        this.userId = userId;
    }

    public CmFactory getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(CmFactory factoryId) {
        this.factoryId = factoryId;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }
    //</editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmUserfactory)) {
            return false;
        }
        CmUserfactory other = (CmUserfactory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+"[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        CmUserfactory target = (CmUserfactory)o;
        return this.factoryId.getCode().compareTo(target.factoryId.getCode());
    }
}
