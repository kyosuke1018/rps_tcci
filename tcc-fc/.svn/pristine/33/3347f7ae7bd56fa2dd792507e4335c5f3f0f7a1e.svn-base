/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_ACTIVITY")
@NamedQueries({
    @NamedQuery(name = "TcActivity.findAll", query = "SELECT t FROM TcActivity t")})
public class TcActivity implements Serializable, Persistable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @Basic(optional = false)
    @Column(name = "ACTIVITYNAME")
    private String activityname;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)    
    @Column(name = "ACTIVITYTYPE")
    private ActivityTypeEnum activitytype;
    @Column(name = "ROLENAME")
    private String rolename;
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)    
    @Column(name = "EXECUTIONSTATE")
    private ExecutionStateEnum executionstate;
    @Column(name = "DEADLINE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;        
    @Column(name = "DURATION")
    private Long duration;
    @Lob
    @Column(name = "EXPRESSION")
    private String expression;
    @Column(name = "STARTTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttimestamp;
    @Column(name = "ENDTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtimestamp;
    @JoinColumn(name = "PROCESSID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcProcess processid;
    @JoinColumn(name = "ACTIVITYTEMPLATEID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcActivitytemplate activitytemplateid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activityid")
    private Collection<TcWorkitem> tcWorkitemCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromactivity")
    private Collection<TcActivityroute> tcActivityrouteCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toactivity")
    private Collection<TcActivityroute> tcActivityrouteCollection1;
    
    @JoinColumn(name = "PARTICIPANT", referencedColumnName = "ID")
    @ManyToOne
    private TcUser participant;

    public TcActivity() {
    }

    public TcActivity(Long id) {
        this.id = id;
    }

    public TcActivity(Long id, String activityname, ActivityTypeEnum activitytype, ExecutionStateEnum executionstate) {
        this.id = id;
        this.activityname = activityname;
        this.activitytype = activitytype;
        this.executionstate = executionstate;
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

    public ActivityTypeEnum getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(ActivityTypeEnum activitytype) {
        this.activitytype = activitytype;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public ExecutionStateEnum getExecutionstate() {
        return executionstate;
    }

    public void setExecutionstate(ExecutionStateEnum executionstate) {
        this.executionstate = executionstate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public TcProcess getProcessid() {
        return processid;
    }

    public void setProcessid(TcProcess processid) {
        this.processid = processid;
    }

    public TcActivitytemplate getActivitytemplateid() {
        return activitytemplateid;
    }

    public void setActivitytemplateid(TcActivitytemplate activitytemplateid) {
        this.activitytemplateid = activitytemplateid;
    }

    public Collection<TcWorkitem> getTcWorkitemCollection() {
        return tcWorkitemCollection;
    }

    public void setTcWorkitemCollection(Collection<TcWorkitem> tcWorkitemCollection) {
        this.tcWorkitemCollection = tcWorkitemCollection;
    }

    public Collection<TcActivityroute> getTcActivityrouteCollection() {
        return tcActivityrouteCollection;
    }

    public void setTcActivityrouteCollection(Collection<TcActivityroute> tcActivityrouteCollection) {
        this.tcActivityrouteCollection = tcActivityrouteCollection;
    }

    public Collection<TcActivityroute> getTcActivityrouteCollection1() {
        return tcActivityrouteCollection1;
    }

    public void setTcActivityrouteCollection1(Collection<TcActivityroute> tcActivityrouteCollection1) {
        this.tcActivityrouteCollection1 = tcActivityrouteCollection1;
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
        if (!(object instanceof TcActivity)) {
            return false;
        }
        TcActivity other = (TcActivity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcActivity[ id=" + id + " ]";
    }

    /**
     * @return the deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the participant
     */
    public TcUser getParticipant() {
        return participant;
    }

    /**
     * @param participant the participant to set
     */
    public void setParticipant(TcUser participant) {
        this.participant = participant;
    }

 
    
}
