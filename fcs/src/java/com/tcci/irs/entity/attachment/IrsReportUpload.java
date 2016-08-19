/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity.attachment;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcs.entity.FcCompany;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "IRS_REPORT_UPLOAD")
@NamedQueries({
    @NamedQuery(name = "IrsReportUpload.findAll", query = "SELECT i FROM IrsReportUpload i"),
    @NamedQuery(name = "IrsReportUpload.findByYearmonthCompany",
        query = "SELECT f FROM IrsReportUpload f WHERE f.yearMonth=:yearmonth AND f.companyCode=:company"),
    @NamedQuery(name = "IrsReportUpload.findByYearmonth",
        query = "SELECT f FROM IrsReportUpload f WHERE f.yearMonth=:yearmonth AND f.companyCode.group.code = :group ORDER BY f.createtimestamp desc")})
public class IrsReportUpload implements Serializable, ContentHolder {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)   
    @Column(name = "ID")
    private Long id;
    @JoinColumn(name = "COMPANY_CODE", referencedColumnName = "CODE")
    @ManyToOne(optional = false)
    private FcCompany companyCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "YEAR_MONTH")
    private String yearMonth;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "PAGE_CODE")
    private String pageCode;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Transient
    private AttachmentVO attachmentVO;
    public AttachmentVO getAttachmentVO() {
        return attachmentVO;
    }
    public void setAttachmentVO(AttachmentVO attachmentVO) {
        this.attachmentVO = attachmentVO;
    }

    public IrsReportUpload() {
    }

    public IrsReportUpload(Long id) {
        this.id = id;
    }

    public IrsReportUpload(Long id, FcCompany companyCode, String yearMonth, String pageCode) {
        this.id = id;
        this.companyCode = companyCode;
        this.yearMonth = yearMonth;
        this.pageCode = pageCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FcCompany getCompanyCode() {
	return companyCode;
    }

    public void setCompanyCode(FcCompany companyCode) {
	this.companyCode = companyCode;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getPageCode() {
        return pageCode;
    }

    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }

    public TcUser getCreator() {
        return creator;
    }

    public void setCreator(TcUser creator) {
        this.creator = creator;
    }

    public Date getCreatetimestamp() {
        return createtimestamp;
    }

    public void setCreatetimestamp(Date createtimestamp) {
        this.createtimestamp = createtimestamp;
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
        if (!(object instanceof IrsReportUpload)) {
            return false;
        }
        IrsReportUpload other = (IrsReportUpload) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.irs.entity.attachment.IrsReportUpload[ id=" + id + " ]";
    }
    
}
