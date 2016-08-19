/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.irs.entity.reconciling;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.irs.entity.AccountNode;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;

/**
 *
 * @author Gilbert.Lin
 */
@Entity
@Table(name = "IRS_SHEETDATA_RECONCILING_D")
@NamedQueries({
    @NamedQuery(name = "IrsSheetdataReconcilingD.findAll", query = "SELECT i FROM IrsSheetdataReconcilingD i")})
public class IrsSheetdataReconcilingD implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @Basic(optional = false)
//    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "SHEETDATAM_ID")
//    private long sheetdatamId;
    @JoinColumn(name = "SHEETDATAM_ID", referencedColumnName = "ID")
    @ManyToOne
    private IrsSheetdataM sheetdatam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
//    @NotNull
    @Column(name = "AMOUNT_ADJUSTMENTS")
    private BigDecimal amountAdjustments;
    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 300)
    @Column(name = "REMARK")
    private String remark;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne
    private TcUser creator;
    @Column(name = "CREATETIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createtimestamp;
    @JoinColumn(name = "ACCOUNT_NODE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AccountNode accountNode;
    @NotNull
    @Column(name = "REMARK_ONLY")
    private boolean remarkOnly = true;

    public IrsSheetdataReconcilingD() {
    }

    public IrsSheetdataReconcilingD(Long id) {
        this.id = id;
    }

    public IrsSheetdataReconcilingD(Long id, IrsSheetdataM sheetdatam, String accountType, BigDecimal amountAdjustments, String remark) {
        this.id = id;
        this.sheetdatam = sheetdatam;
        this.accountType = accountType;
        this.amountAdjustments = amountAdjustments;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IrsSheetdataM getSheetdatam() {
        return sheetdatam;
    }
    public void setSheetdatam(IrsSheetdataM sheetdatam) {
        this.sheetdatam = sheetdatam;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getAmountAdjustments() {
        return amountAdjustments;
    }

    public void setAmountAdjustments(BigDecimal amountAdjustments) {
        this.amountAdjustments = amountAdjustments;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public AccountNode getAccountNode() {
        return accountNode;
    }

    public void setAccountNode(AccountNode accountNode) {
        this.accountNode = accountNode;
    }

    public boolean isRemarkOnly() {
        return remarkOnly;
    }

    public void setRemarkOnly(boolean remarkOnly) {
        this.remarkOnly = remarkOnly;
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
        if (!(object instanceof IrsSheetdataReconcilingD)) {
            return false;
        }
        IrsSheetdataReconcilingD other = (IrsSheetdataReconcilingD) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.irs.entity.sheetdata.IrsSheetdataReconcilingD[ id=" + id + " ]";
    }
    
    public String getAccountName() {
        String result = "";
        if("RE".equalsIgnoreCase(accountType)){
            result = sheetdatam.getReAccountCode()+"("+ sheetdatam.getReAccountName() +")";
        }else{
            result = sheetdatam.getPaAccountCode()+"("+ sheetdatam.getPaAccountName() +")";
        }
        return result;
    }
    
}
