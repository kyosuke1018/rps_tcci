/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.sksp.entity.enums.AccessLogActionEnum;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jason.Yu
 */
@Entity
@Table(name = "SK_ACCESS_LOG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SkAccessLog.findAll", query = "SELECT s FROM SkAccessLog s"),
    @NamedQuery(name = "SkAccessLog.findById", query = "SELECT s FROM SkAccessLog s WHERE s.id = :id"),
    @NamedQuery(name = "SkAccessLog.findByActionName", query = "SELECT s FROM SkAccessLog s WHERE s.actionName = :actionName"),
    @NamedQuery(name = "SkAccessLog.findByCreatetimestamp", query = "SELECT s FROM SkAccessLog s WHERE s.createtimestamp = :createtimestamp")})
public class SkAccessLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(generator="SEQ_TCC")
    private Long id;
    @Column(name = "ACTION_NAME")
    @Enumerated(EnumType.STRING)
    private AccessLogActionEnum actionName;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne
    private TcUser userId;

    public SkAccessLog() {
    }

    public SkAccessLog(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccessLogActionEnum getActionName() {
        return actionName;
    }

    public void setActionName(AccessLogActionEnum actionName) {
        this.actionName = actionName;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public TcUser getUserId() {
        return userId;
    }

    public void setUserId(TcUser userId) {
        this.userId = userId;
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
        if (!(object instanceof SkAccessLog)) {
            return false;
        }
        SkAccessLog other = (SkAccessLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.SkAccessLog[ id=" + id + " ]";
    }
    
}
