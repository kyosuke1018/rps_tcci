/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.role.RoleHolder;
import com.tcci.fc.entity.team.TcTeam;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_PROCESS")
@NamedQueries({
    @NamedQuery(name = "TcProcess.findAll", query = "SELECT t FROM TcProcess t")})
public class TcProcess implements Serializable, Persistable, RoleHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @Basic(optional = false)
    @Column(name = "PROCESSNAME")
    private String processname;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)    
    @Column(name = "EXECUTIONSTATE")
    private ExecutionStateEnum executionstate;
    @Column(name = "PRIMARYOBJECTCLASSNAME")
    private String primaryobjectclassname;
    @Column(name = "PRIMARYOBJECTID")
    private Long primaryobjectid;
    @Column(name = "STARTTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttimestamp;
    @Column(name = "ENDTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtimestamp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "processid")
    private Collection<TcActivity> tcActivityCollection;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "PROCESSTEMPLATEID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcProcesstemplate processtemplateid;
    @JoinColumn(name = "TERMINATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser terminator;
    @Column(name = "TERMINATE_REASON")
    private String terminateReason;
    @JoinColumn(name = "team", referencedColumnName = "ID")
    @ManyToOne
    private TcTeam team;    

    public TcProcess() {
    }

    public TcProcess(Long id) {
        this.id = id;
    }

    public TcProcess(Long id, String processname, ExecutionStateEnum executionstate) {
        this.id = id;
        this.processname = processname;
        this.executionstate = executionstate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessname() {
        return processname;
    }

    public void setProcessname(String processname) {
        this.processname = processname;
    }

    public ExecutionStateEnum getExecutionstate() {
        return executionstate;
    }

    public void setExecutionstate(ExecutionStateEnum executionstate) {
        this.executionstate = executionstate;
    }

    public String getPrimaryobjectclassname() {
        return primaryobjectclassname;
    }

    public void setPrimaryobjectclassname(String primaryobjectclassname) {
        this.primaryobjectclassname = primaryobjectclassname;
    }

    public Long getPrimaryobjectid() {
        return primaryobjectid;
    }

    public void setPrimaryobjectid(Long primaryobjectid) {
        this.primaryobjectid = primaryobjectid;
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

    public Collection<TcActivity> getTcActivityCollection() {
        return tcActivityCollection;
    }

    public void setTcActivityCollection(Collection<TcActivity> tcActivityCollection) {
        this.tcActivityCollection = tcActivityCollection;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcProcesstemplate getProcesstemplateid() {
        return processtemplateid;
    }

    public void setProcesstemplateid(TcProcesstemplate processtemplateid) {
        this.processtemplateid = processtemplateid;
    }

    public TcUser getTerminator() {
        return terminator;
    }

    public void setTerminator(TcUser terminator) {
        this.terminator = terminator;
    }

    public String getTerminateReason() {
        return terminateReason;
    }

    public void setTerminateReason(String terminateReason) {
        this.terminateReason = terminateReason;
    }
    
    public TcTeam getTeam() {
        return team;
    }

    public void setTeam(TcTeam team) {
        this.team = team;
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
        if (!(object instanceof TcProcess)) {
            return false;
        }
        TcProcess other = (TcProcess) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcProcess[ id=" + id + " ]";
    }

}
