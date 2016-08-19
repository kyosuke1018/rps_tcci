/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.entity.log;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author nEO
 */
@Entity
@Table(name = "TC_DOWNLOAD_LOG")
@XmlRootElement
public class TcDownloadLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TOTAL_COUNT")
    private int totalCount;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @OneToMany(mappedBy = "downloadLog")
    private Collection<TcDownloadLogDetail> downloadLogDetailCollection;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne
    private TcUser modifier;
    @JoinColumn(name = "TC_APPLICATIONDATA_ID", referencedColumnName = "ID")
    @ManyToOne
    private TcApplicationdata applicationdata;

    public TcDownloadLog() {
    }

    public TcDownloadLog(Long id) {
        this.id = id;
    }

    public TcDownloadLog(Long id, int totalCount) {
        this.id = id;
        this.totalCount = totalCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    @XmlTransient
    public Collection<TcDownloadLogDetail> getDownloadLogDetailCollection() {
        return downloadLogDetailCollection;
    }

    public void setDownloadLogDetailCollection(Collection<TcDownloadLogDetail> downloadLogDetailCollection) {
        this.downloadLogDetailCollection = downloadLogDetailCollection;
    }

    public TcUser getCreator() {
        return creator;
    }
    
    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public TcApplicationdata getApplicationdata() {
        return applicationdata;
    }

    public void setApplicationdata(TcApplicationdata applicationdata) {
        this.applicationdata = applicationdata;
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
        if (!(object instanceof TcDownloadLog)) {
            return false;
        }
        TcDownloadLog other = (TcDownloadLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.entity.log.TcDownloadLog[ id=" + id + " ]";
    }
    
}
