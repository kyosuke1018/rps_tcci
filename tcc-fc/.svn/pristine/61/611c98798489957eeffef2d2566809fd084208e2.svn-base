/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_ACTIVITYTEMPLATE")
@NamedQueries({
    @NamedQuery(name = "TcActivitytemplate.findAll", query = "SELECT t FROM TcActivitytemplate t")})
public class TcActivitytemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
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
    @Column(name = "DEADLINE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;    
    @Column(name = "DURATION")
    private Integer duration;
    @Column(name="NOTIFICATION")
    private Boolean notification;
    @Lob
    @Column(name = "EXPRESSION")
    private String expression;
    @Column(name = "OPTIONS")
    private long options;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromactivity")
    private Collection<TcActivityroutetemplate> tcActivityroutetemplateCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toactivity")
    private Collection<TcActivityroutetemplate> tcActivityroutetemplateCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "activitytemplateid")
    private Collection<TcActivity> tcActivityCollection;
    @JoinColumn(name = "PROCESSTEMPLATEID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcProcesstemplate processtemplateid;

    public TcActivitytemplate() {
        notification = true;
    }

    public TcActivitytemplate(Long id) {
        notification = true;
        this.id = id;
    }

    public TcActivitytemplate(Long id, String activityname, ActivityTypeEnum activitytype) {
        notification = true;
        this.id = id;
        this.activityname = activityname;
        this.activitytype = activitytype;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public long getOptions() {
        return options;
    }

    public void setOptions(long options) {
        this.options = options;
    }
    
    public Boolean getNotification() {
        return notification;
    }

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    public Collection<TcActivityroutetemplate> getTcActivityroutetemplateCollection() {
        return tcActivityroutetemplateCollection;
    }

    public void setTcActivityroutetemplateCollection(Collection<TcActivityroutetemplate> tcActivityroutetemplateCollection) {
        this.tcActivityroutetemplateCollection = tcActivityroutetemplateCollection;
    }

    public Collection<TcActivityroutetemplate> getTcActivityroutetemplateCollection1() {
        return tcActivityroutetemplateCollection1;
    }

    public void setTcActivityroutetemplateCollection1(Collection<TcActivityroutetemplate> tcActivityroutetemplateCollection1) {
        this.tcActivityroutetemplateCollection1 = tcActivityroutetemplateCollection1;
    }

    public Collection<TcActivity> getTcActivityCollection() {
        return tcActivityCollection;
    }

    public void setTcActivityCollection(Collection<TcActivity> tcActivityCollection) {
        this.tcActivityCollection = tcActivityCollection;
    }

    public TcProcesstemplate getProcesstemplateid() {
        return processtemplateid;
    }

    public void setProcesstemplateid(TcProcesstemplate processtemplateid) {
        this.processtemplateid = processtemplateid;
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
        if (!(object instanceof TcActivitytemplate)) {
            return false;
        }
        TcActivitytemplate other = (TcActivitytemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.TcActivitytemplate[ id=" + id + " ]";
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

}
