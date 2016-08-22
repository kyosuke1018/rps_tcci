/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.swls;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
@Entity
@Table(name = "SK_SWLS_SKNG_COMPLAIN_UPLOAD")
@NamedQueries({
    @NamedQuery(name = "ComplainUpload.findAll", query = "SELECT s FROM ComplainUpload s")})
public class ComplainUpload implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    @SequenceGenerator(name="SEQ_SWLS",sequenceName = "SEQ_SWLS", allocationSize=1)       
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_SWLS")           
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "YMD")
    private String ymd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CUST_ID")
    private String custId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "CUST_NAME")
    private String custName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "MAT_NO")
    private String matNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "MAT_NAME")
    private String matName;
    @Size(max = 10)
    @Column(name = "BATCH_NO")
    private String batchNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "REASON_CLASS")
    private String reasonClass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "REASON")
    private String reason;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "CT_DRUG")
    private String ctDrug;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "CH_STATUS")
    private String chStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "REPORT_STATUS")
    private String reportStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "TURN_STATUS")
    private String turnStatus;
    @Size(max = 255)
    @Column(name = "MEMO")
    private String memo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SALES")
    private String sales;

    public ComplainUpload() {
    }

    public ComplainUpload(Long id) {
        this.id = id;
    }

    public ComplainUpload(Long id, String ymd, String custId, String custName, String matNo, String matName, String reasonClass, String reason, String ctDrug, String chStatus, String reportStatus, String turnStatus) {
        this.id = id;
        this.ymd = ymd;
        this.custId = custId;
        this.custName = custName;
        this.matNo = matNo;
        this.matName = matName;
        this.reasonClass = reasonClass;
        this.reason = reason;
        this.ctDrug = ctDrug;
        this.chStatus = chStatus;
        this.reportStatus = reportStatus;
        this.turnStatus = turnStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getMatNo() {
        return matNo;
    }

    public void setMatNo(String matNo) {
        this.matNo = matNo;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getReasonClass() {
        return reasonClass;
    }

    public void setReasonClass(String reasonClass) {
        this.reasonClass = reasonClass;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCtDrug() {
        return ctDrug;
    }

    public void setCtDrug(String ctDrug) {
        this.ctDrug = ctDrug;
    }

    public String getChStatus() {
        return chStatus;
    }

    public void setChStatus(String chStatus) {
        this.chStatus = chStatus;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getTurnStatus() {
        return turnStatus;
    }

    public void setTurnStatus(String turnStatus) {
        this.turnStatus = turnStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    public String getSales() {
        return sales;
    }
    
    public void setSales(String sales) {
        this.sales = sales;
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
        if (!(object instanceof ComplainUpload)) {
            return false;
        }
        ComplainUpload other = (ComplainUpload) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.sksp.entity.swls.SwlsComplainUpload[ id=" + id + " ]";
    }
    
}
