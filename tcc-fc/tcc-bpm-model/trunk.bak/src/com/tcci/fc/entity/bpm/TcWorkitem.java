/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_WORKITEM")
@NamedQueries({
    @NamedQuery(name = "TcWorkitem.findAll", query = "SELECT t FROM TcWorkitem t")})
public class TcWorkitem implements Serializable {
    @Basic(optional = false)   
    @Enumerated(EnumType.STRING)    
    @Column(name = "EXECUTIONSTATE")
    private ExecutionStateEnum executionstate;
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "BALLOT")    
    private String ballot;    
    @Column(name = "STARTTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttimestamp;
    @Column(name = "ENDTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtimestamp;
    @OneToMany(mappedBy = "workitem")
    private Collection<TcSignature> tcSignatureCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @Column(name = "ACTIVITYNAME")
    private String activityname;
    @JoinColumn(name = "OWNER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser owner;
    @JoinColumn(name = "ACTIVITYID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcActivity activityid;

    public TcWorkitem() {
    }

    public TcWorkitem(Long id) {
        this.id = id;
    }

    public TcWorkitem(Long id, ExecutionStateEnum state) {
        this.id = id;
        this.executionstate = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public TcUser getOwner() {
        return owner;
    }

    public void setOwner(TcUser owner) {
        this.owner = owner;
    }

    public TcActivity getActivityid() {
        return activityid;
    }

    public void setActivityid(TcActivity activityid) {
        this.activityid = activityid;
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
        if (!(object instanceof TcWorkitem)) {
            return false;
        }
        TcWorkitem other = (TcWorkitem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcWorkitem[ id=" + id + " ]";
    }

    public ExecutionStateEnum getExecutionstate() {
        return executionstate;
    }

    public void setExecutionstate(ExecutionStateEnum executionstate) {
        this.executionstate = executionstate;
    }

    public Date getStarttimestamp() {
        return starttimestamp;
    }

    public void setStarttimestamp(Date starttimestamp) {
        this.starttimestamp = starttimestamp;
    }

    public Date getEndtimestamp() {
        return endtimestamp;
    }

    public void setEndtimestamp(Date endtimestamp) {
        this.endtimestamp = endtimestamp;
    }

    public Collection<TcSignature> getTcSignatureCollection() {
        return tcSignatureCollection;
    }

    public void setTcSignatureCollection(Collection<TcSignature> tcSignatureCollection) {
        this.tcSignatureCollection = tcSignatureCollection;
    }

    /**
     * @return the ballot
     */
    public String getBallot() {
        return ballot;
    }

    /**
     * @param ballot the ballot to set
     */
    public void setBallot(String ballot) {
        this.ballot = ballot;
    }
    
}
