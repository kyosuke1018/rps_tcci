/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.entity;

import com.tcci.fc.entity.org.TcUser;
//import com.tcci.fc.util.time.DateUtils;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "IRS_TRAN_UPLOAD_RECORD")
@NamedQueries({
    @NamedQuery(name = "IrsTranUploadRecord.findByYearmonthCompany",
        query = "SELECT r FROM IrsTranUploadRecord r"
        + " WHERE r.yearmonth=:yearmonth AND r.companyCode=:companyCode"),
    @NamedQuery(name = "IrsTranUploadRecord.findByYearmonth",
        query = "SELECT r FROM IrsTranUploadRecord r"
        + " WHERE r.yearmonth=:yearmonth")
})
public class IrsTranUploadRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPANY_CODE")
    private String companyCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "YEARMONTH")
    private String yearmonth;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @Column(name = "SAP_USER_NAME")
    private String sapUserName;//SAP user 中文名
//    @Column(name = "UPLOAD_DATE")
//    private String uploadDate;
//    @Column(name = "UPLOAD_TIME")
//    private String uploadTime;
    
    public IrsTranUploadRecord(){
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

//    }

//    public Date getUploadTimestamp() {
//        if(this.uploadDate == null){
//            return null;
//        }
//        Date result = null;
//        if(this.uploadTime == null){
//            result = DateUtils.simpleISODateStringToDate(this.uploadDate);
//        }else{
//            result = DateUtils.simpleISODateTimeStringToDate(this.uploadDate, this.uploadTime);
//        }
//        return result;
    public String getSapUserName() {
        return sapUserName;
    }

    public void setSapUserName(String sapUserName) {
        this.sapUserName = sapUserName;
    }
    
    
    
}
