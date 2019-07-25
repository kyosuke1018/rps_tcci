/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "TC_REASSIGN_CONF")
@NamedQueries({
    @NamedQuery(name = "TcReassignConf.findAll", query = "SELECT t FROM TcReassignConf t ORDER BY t.modifytime DESC"),
    @NamedQuery(name = "TcReassignConf.findByOwner", query = "SELECT t FROM TcReassignConf t"
            + " WHERE t.owner=:owner ORDER BY t.modifytime DESC"),
    @NamedQuery(name = "TcReassignConf.findByNewowner", query = "SELECT t FROM TcReassignConf t"
            + " WHERE t.newowner=:newowner ORDER BY t.modifytime DESC"),
    @NamedQuery(name = "TcReassignConf.findCurrent", query = "SELECT t FROM TcReassignConf t"
            + " WHERE t.owner=:owner and t.enable=true"
            + " AND (t.starttime is null or t.starttime<=:now)"
            + " AND (t.endtime is null or t.endtime>=:now)"
            + " ORDER BY t.modifytime DESC")
})
public class TcReassignConf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Column(name = "STARTTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "ENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @Size(max = 100)
    @Column(name = "COMMENTS")
    private String comments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENABLE")
    private boolean enable;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytime;
    @Size(max = 255)
    @Column(name = "PROCESSNAME")
    private String processname;
    @JoinColumn(name = "OWNER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser owner;
    @JoinColumn(name = "NEWOWNER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser newowner;

    public TcReassignConf() {
    }

    // gettet, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public TcUser getOwner() {
        return owner;
    }

    public void setOwner(TcUser owner) {
        this.owner = owner;
    }

    public TcUser getNewowner() {
        return newowner;
    }

    public void setNewowner(TcUser newowner) {
        this.newowner = newowner;
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
        if (!(object instanceof TcReassignConf)) {
            return false;
        }
        TcReassignConf other = (TcReassignConf) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcReassignConf[ id=" + id + " ]";
    }

}
