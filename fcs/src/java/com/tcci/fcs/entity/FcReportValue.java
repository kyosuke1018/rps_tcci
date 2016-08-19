/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.entity;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.model.global.GlobalConstant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.persistence.PostLoad;
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
@Table(name = "FC_REPORT_VALUE")
@NamedQueries({
    @NamedQuery(name = "FcReportValue.findAll", query = "SELECT f FROM FcReportValue f")})
public class FcReportValue implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SHEET")
    private String sheet;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "AMOUNT_ORIG")
    private BigDecimal amountOrig;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    @JoinColumn(name = "REPORT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcReportUpload report;
    @JoinColumn(name = "COID2", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany coid2;
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Column(name = "AMOUNT_XLS")
    private BigDecimal amountXls; // excel原始值

    @PostLoad
    public void afterLoad() {
        if (null != amount) {
            amount = amount.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
        }
        if (null != amountOrig) {
            amountOrig = amountOrig.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
        }
        if (null != amountXls) {
            amountXls = amountXls.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
        }
    }
    
    public FcReportValue() {
    }
    
    public FcReportValue(String sheet, FcCompany coid2, String code, BigDecimal amount, BigDecimal amountXls) {
        this.sheet = sheet;
        this.coid2 = coid2;
        this.code = code;
        this.amount = amount;
        this.amountOrig = amount;
        this.amountXls = amountXls;
    }
    
    public FcReportValue(FcReportUpload report, String sheet, FcCompany coid2, String code, BigDecimal amount, TcUser modifier, Date modifytimestamp) {
        this.report = report;
        this.sheet = sheet;
        this.coid2 = coid2;
        this.code = code;
        if (null != amount) {
            this.amount = amount.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
            this.amountOrig = this.amount;
        }
        this.modifier = modifier;
        this.modifytimestamp = modifytimestamp;
    }

    public FcReportValue(Long id) {
        this.id = id;
    }

    public FcReportValue(Long id, String sheet, String code, Date modifytimestamp) {
        this.id = id;
        this.sheet = sheet;
        this.code = code;
        this.modifytimestamp = modifytimestamp;
    }

    public boolean isModified() {
        if (null==amount && null==amountOrig) {
            return false;
        } else if (null!=amount && null!=amountOrig) {
            return amount.compareTo(amountOrig)!=0;
        } else {
            return true;
        }
    }
    
    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = (null==amount) ? null : amount.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmountOrig() {
        return amountOrig;
    }

    public void setAmountOrig(BigDecimal amountOrig) {
        this.amountOrig = (null==amountOrig) ? null : amountOrig.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
    }

    public Date getModifytimestamp() {
        return modifytimestamp;
    }

    public void setModifytimestamp(Date modifytimestamp) {
        this.modifytimestamp = modifytimestamp;
    }

    public FcReportUpload getReport() {
        return report;
    }

    public void setReport(FcReportUpload report) {
        this.report = report;
    }

    public FcCompany getCoid2() {
        return coid2;
    }

    public void setCoid2(FcCompany coid2) {
        this.coid2 = coid2;
    }

    public TcUser getModifier() {
        return modifier;
    }

    public void setModifier(TcUser modifier) {
        this.modifier = modifier;
    }

    public BigDecimal getAmountXls() {
        return amountXls;
    }

    public void setAmountXls(BigDecimal amountXls) {
        this.amountXls = amountXls;
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
        if (!(object instanceof FcReportValue)) {
            return false;
        }
        FcReportValue other = (FcReportValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.fcs.entity.FcReportValue[ id=" + id + " ]";
    }

}
