/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.bpm;

import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author wayne
 */
@Entity
@Table(name = "TC_SIGNATURE")
@NamedQueries({
    @NamedQuery(name = "TcSignature.findAll", query = "SELECT t FROM TcSignature t")})
public class TcSignature implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator (name="SEQ_TCC", sequenceName="SEQ_TCC", allocationSize=1)
    private Long id;
    @Size(max = 255)
    @Column(name = "PRIMARYOBJECTCLASSNAME")
    private String primaryobjectclassname;
    @Column(name = "PRIMARYOBJECTID")
    private Long primaryobjectid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BALLOT")
    private String ballot;
    @Basic(optional = false)
    @Size(max = 510)
    @Column(name = "COMMENTS")
    private String comments;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "WORKITEM", referencedColumnName = "ID")
    @ManyToOne
    private TcWorkitem workitem;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;

    public TcSignature() {
    }

    public TcSignature(Long id) {
        this.id = id;
    }

    public TcSignature(Long id, String ballot, String comments) {
        this.id = id;
        this.ballot = ballot;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBallot() {
        return ballot;
    }

    public void setBallot(String ballot) {
        this.ballot = ballot;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public TcWorkitem getWorkitem() {
        return workitem;
    }

    public void setWorkitem(TcWorkitem workitem) {
        this.workitem = workitem;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
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
        if (!(object instanceof TcSignature)) {
            return false;
        }
        TcSignature other = (TcSignature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fc.entity.bpm.enumeration.TcSignature[ id=" + id + " ]";
    }
    
}
