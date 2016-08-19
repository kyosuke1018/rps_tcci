/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.entity;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author kyle.cheng
 */
@Entity
@Table(name = "RPT_D_VALUE")
public class RptDValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @JoinColumn(name = "UPLOAD_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private RptSheetUpload upload;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "SHEET")
    private String sheet;
    @JoinColumn(name = "COID2", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcCompany coid2;
    @NotNull
    @Column(name = "CODE")
    private String code;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "AMOUNT_XLS")
    private BigDecimal amountXls; // excel原始值
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    
    public RptDValue() {
    }
    
    public RptDValue(String sheet, FcCompany coid2, String code, BigDecimal amount, BigDecimal amountXls) {
        this.sheet = sheet;
        this.coid2 = coid2;
        this.code = code;
        this.amount = amount;
        this.amountXls = amountXls;
    }
    
    // getter, setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RptSheetUpload getUpload() {
        return upload;
    }

    public void setUpload(RptSheetUpload upload) {
        this.upload = upload;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public FcCompany getCoid2() {
        return coid2;
    }

    public void setCoid2(FcCompany coid2) {
        this.coid2 = coid2;
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
        this.amount = amount;
    }

    public BigDecimal getAmountXls() {
        return amountXls;
    }

    public void setAmountXls(BigDecimal amountXls) {
        this.amountXls = amountXls;
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
    
}
