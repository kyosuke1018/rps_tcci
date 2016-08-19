/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity;

import com.tcci.tccstore.enums.NotifyTypeEnum;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.Timeout;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Neo.Fu
 */
@Entity
@Table(name = "EC_NOTIFY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EcNotify.findAll", query = "SELECT e FROM EcNotify e"),
    @NamedQuery(name = "EcNotify.findById", query = "SELECT e FROM EcNotify e WHERE e.id = :id"),
    @NamedQuery(name = "EcNotify.findByType", query = "SELECT e FROM EcNotify e WHERE e.type = :type"),
    @NamedQuery(name = "EcNotify.findBySubject", query = "SELECT e FROM EcNotify e WHERE e.subject = :subject"),
    @NamedQuery(name = "EcNotify.findByReadCount", query = "SELECT e FROM EcNotify e WHERE e.readCount = :readCount"),
    @NamedQuery(name = "EcNotify.findByNotifyClassname", query = "SELECT e FROM EcNotify e WHERE e.notifyClassname = :notifyClassname"),
    @NamedQuery(name = "EcNotify.findByNotifyClassid", query = "SELECT e FROM EcNotify e WHERE e.notifyClassid = :notifyClassid")})
public class EcNotify implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_EC",sequenceName = "SEQ_EC", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_EC")
    private Long id;
    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private NotifyTypeEnum type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "SUBJECT")
    private String subject;
    @Column(name = "READ_COUNT")
    private Integer readCount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOTIFY_CLASSNAME")
    private String notifyClassname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOTIFY_CLASSID")
    private long notifyClassid;
    @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EcMember memberId;
    @Column(name = "CREATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtime = new Date();
    
    public EcNotify() {
    }

    public EcNotify(Long id) {
        this.id = id;
    }

    public EcNotify(Long id, NotifyTypeEnum type, String subject, String notifyClassname, long notifyClassid) {
        this.id = id;
        this.type = type;
        this.subject = subject;
        this.notifyClassname = notifyClassname;
        this.notifyClassid = notifyClassid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotifyTypeEnum getType() {
        return type;
    }

    public void setType(NotifyTypeEnum type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getNotifyClassname() {
        return notifyClassname;
    }

    public void setNotifyClassname(String notifyClassname) {
        this.notifyClassname = notifyClassname;
    }

    public long getNotifyClassid() {
        return notifyClassid;
    }

    public void setNotifyClassid(long notifyClassid) {
        this.notifyClassid = notifyClassid;
    }

    public EcMember getMemberId() {
        return memberId;
    }

    public void setMemberId(EcMember memberId) {
        this.memberId = memberId;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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
        if (!(object instanceof EcNotify)) {
            return false;
        }
        EcNotify other = (EcNotify) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.tccstore.entity.EcNotify[\n"
                + "id=" + id + "\n"
                + "type=" + type + "\n"
                + "subject=" + subject + "\n"
                + "notifyClassname=" + notifyClassname + "\n"
                + "notifyClassid=" + notifyClassid + "\n"
                + "readCount=" + readCount + "\n"
                + "createtime=" + createtime + "\n"
                + "member=EcMember:" + memberId.getId() + "\n"
                + "]";
    }

}
