/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Kyle.Cheng
 */
@Entity
@Table(name = "FC_REPORT_A0102")
public class FcReportA0102 implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SEQ_TCC")
    @SequenceGenerator(name = "SEQ_TCC", sequenceName = "SEQ_TCC", allocationSize = 1)
    private Long id;
    @JoinColumn(name = "REPORT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FcReportUpload report;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODE")
    private String code;
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Column(name = "AMOUNT_XLS")
    private BigDecimal amountXls; // excel原始值
    @JoinColumn(name = "MODIFIER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TcUser modifier;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODIFYTIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifytimestamp;
    
    @PostLoad
    public void afterLoad() {
        if (null != amount) {
            amount = amount.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
        }
        if (null != amountXls) {
            amountXls = amountXls.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
        }
    }
    
    public FcReportA0102(){
    }
    public FcReportA0102(String code, BigDecimal amount, BigDecimal amountXls){
        this.code = code;
        if (null != amount) {
            this.amount = amount.setScale(GlobalConstant.AMOUNT_SCALE_3, RoundingMode.HALF_UP);
        }
        this.amountXls = amountXls;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FcReportUpload getReport() {
        return report;
    }

    public void setReport(FcReportUpload report) {
        this.report = report;
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
