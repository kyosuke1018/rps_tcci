/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity.sheetdata;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "IRS_COMPANY_CLOSE")
@NamedQueries({
    @NamedQuery(name = "IrsCompanyClose.findAll", query = "SELECT i FROM IrsCompanyClose i"),
    @NamedQuery(name = "IrsCompanyClose.findByGroup", query = "SELECT i FROM IrsCompanyClose i WHERE i.group = :group"),
    @NamedQuery(name = "IrsCompanyClose.findByYearMonth", query = "SELECT i FROM IrsCompanyClose i WHERE i.yearMonth = :yearMonth")})
public class IrsCompanyClose implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @Basic(optional = false)
//    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
//    @JoinColumn(name = "COMPANY_CODE", referencedColumnName = "CODE")
//    @ManyToOne
//    @QueryCondFieldMeta(headerName = "公司", subShowField="company")
//    private FcCompany company;
//    @Size(max = 4)
//    @Column(name = "COMPANY_CODE")
//    private String companyCode;
//    @NotNull
//    @Size(min = 6, max = 6, message = "[年月]須為6碼!")
    @Column(name = "YEAR_MONTH")
    private String yearMonth;
//    @Column(name = "CREATOR")
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @Enumerated(EnumType.STRING)
    @Column(name = "COMP_GROUP")
    private CompanyGroupEnum group;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EDIT_LOCK")
    private boolean editLock = false;//對帳調節表 編輯鎖定
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOP_UPLOAD")
    private boolean stopUpload = false;//對帳資料匯入 停止上傳 關帳

    public IrsCompanyClose() {
    }

    public IrsCompanyClose(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getCompanyCode() {
//        return companyCode;
//    }
//
//    public void setCompanyCode(String companyCode) {
//        this.companyCode = companyCode;
//    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
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

//    public FcCompany getCompany() {
//        return company;
//    }
//
//    public void setCompany(FcCompany company) {
//        this.company = company;
//    }

    public CompanyGroupEnum getGroup() {
        return group;
    }

    public void setGroup(CompanyGroupEnum group) {
        this.group = group;
    }

    public boolean isEditLock() {
        return editLock;
    }

    public void setEditLock(boolean editLock) {
        this.editLock = editLock;
    }

    public boolean isStopUpload() {
        return stopUpload;
    }

    public void setStopUpload(boolean stopUpload) {
        this.stopUpload = stopUpload;
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
        if (!(object instanceof IrsCompanyClose)) {
            return false;
        }
        IrsCompanyClose other = (IrsCompanyClose) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.irs.entity.sheetdata.IrsCompanyClose[ id=" + id + " ]";
    }
    
}
