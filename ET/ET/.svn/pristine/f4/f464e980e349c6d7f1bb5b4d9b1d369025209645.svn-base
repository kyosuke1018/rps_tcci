/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author penpl
 */
public class QuoteRsVO implements Serializable {

    private Long id; // PK ID
    private Long tenderId; // 標案 ID
    private Long rfqId; // FK ET_RFQ_EKKO.ID
    private Long rfqVenderId; // FK ET_RFQ_VENDER.ID
    private Integer times; // 次數
    private Date expiretime; // 有效日期
    private Boolean invoice; // 是否開發票
    
    private String curRfq; // 詢價單幣別
    private String curQuo; // 報價幣別
    private BigDecimal exRate; // 匯率
    
    private BigDecimal totalAmtRfq; // 總金額(詢價單數量)
    private BigDecimal taxRfq; // 稅金(詢價單數量)
    private BigDecimal netAmtRfq; // 淨總額(詢價單數量)
    
    private BigDecimal totalAmtQuo; // 總金額(報價數量)
    private BigDecimal taxQuo; // 稅金(報價數量)
    private BigDecimal netAmtQuo; // 淨總額(報價數量)
    
    private BigDecimal taxRate;// 稅率
    private String taxType;// 稅別
    
    private BigDecimal discount; // 折扣%
    private String memo; // 備註
    private Date createtime; // 建立時間
    private Date modifytime; // 修改時間
    private String status; // 狀態(T:廠商暫存;C:廠商確認;R:退回報價;D:禁止報價)
    private Boolean disabled; // 無效
    
    private Long memberId; // 報價會員ID
    private Date quotetime; // 報價時間
    
    // for UI
    //private List<QuotationItemVO> itemList;// 報價明細
    private boolean readonly = true;
    private Boolean last; // 最終報價
    
    // 報價廠商
    //private Long venderId;// FK: ET_VENDER.ID
    //private String venderName;
    //private String lifnr;
    //private String lifnrUI;
    //private Long applyId;
     
    // for doc
    //private List<AttachmentVO> docs;
    //private List<AttachmentVO> removedDocs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenderId() {
        return tenderId;
    }

    public void setTenderId(Long tenderId) {
        this.tenderId = tenderId;
    }

    public Long getRfqId() {
        return rfqId;
    }

    public void setRfqId(Long rfqId) {
        this.rfqId = rfqId;
    }

    public Long getRfqVenderId() {
        return rfqVenderId;
    }

    public void setRfqVenderId(Long rfqVenderId) {
        this.rfqVenderId = rfqVenderId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Date getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(Date expiretime) {
        this.expiretime = expiretime;
    }

    public Boolean getInvoice() {
        return invoice;
    }

    public void setInvoice(Boolean invoice) {
        this.invoice = invoice;
    }

    public String getCurRfq() {
        return curRfq;
    }

    public void setCurRfq(String curRfq) {
        this.curRfq = curRfq;
    }

    public String getCurQuo() {
        return curQuo;
    }

    public void setCurQuo(String curQuo) {
        this.curQuo = curQuo;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    public BigDecimal getTotalAmtRfq() {
        return totalAmtRfq;
    }

    public void setTotalAmtRfq(BigDecimal totalAmtRfq) {
        this.totalAmtRfq = totalAmtRfq;
    }

    public BigDecimal getTaxRfq() {
        return taxRfq;
    }

    public void setTaxRfq(BigDecimal taxRfq) {
        this.taxRfq = taxRfq;
    }

    public BigDecimal getNetAmtRfq() {
        return netAmtRfq;
    }

    public void setNetAmtRfq(BigDecimal netAmtRfq) {
        this.netAmtRfq = netAmtRfq;
    }

    public BigDecimal getTotalAmtQuo() {
        return totalAmtQuo;
    }

    public void setTotalAmtQuo(BigDecimal totalAmtQuo) {
        this.totalAmtQuo = totalAmtQuo;
    }

    public BigDecimal getTaxQuo() {
        return taxQuo;
    }

    public void setTaxQuo(BigDecimal taxQuo) {
        this.taxQuo = taxQuo;
    }

    public BigDecimal getNetAmtQuo() {
        return netAmtQuo;
    }

    public void setNetAmtQuo(BigDecimal netAmtQuo) {
        this.netAmtQuo = netAmtQuo;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Date getQuotetime() {
        return quotetime;
    }

    public void setQuotetime(Date quotetime) {
        this.quotetime = quotetime;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }
    
}
