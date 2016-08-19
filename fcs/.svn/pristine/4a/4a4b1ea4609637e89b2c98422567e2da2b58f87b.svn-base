/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.entity;

import com.tcci.fc.entity.content.ContentHolder;
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
@Table(name = "FC_REPORT_UPLOAD")
@NamedQueries({
    @NamedQuery(name = "FcReportUpload.findByYearmonthCompany",
        query = "SELECT f FROM FcReportUpload f"
        + " WHERE f.yearmonth=:yearmonth AND f.company=:company"),
    @NamedQuery(name = "FcReportUpload.findByYearmonth",
        query = "SELECT f FROM FcReportUpload f"
        + " WHERE f.yearmonth=:yearmonth AND f.company.group.code = :group ")
})
public class FcReportUpload implements Serializable, ContentHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "COMPANY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany company;
    @JoinColumn(name = "UPLOADER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser uploader;

    public FcReportUpload() {
    }

    public FcReportUpload(Long id) {
        this.id = id;
    }

    public FcReportUpload(Long id, String yearmonth, Date modifytimestamp) {
        this.id = id;
        this.yearmonth = yearmonth;
        this.modifytimestamp = modifytimestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public TcUser getUploader() {
        return uploader;
    }

    public void setUploader(TcUser uploader) {
        this.uploader = uploader;
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
        if (!(object instanceof FcReportUpload)) {
            return false;
        }
        FcReportUpload other = (FcReportUpload) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcs.entity.FcReportUpload[ id=" + id + " ]";
    }

}
