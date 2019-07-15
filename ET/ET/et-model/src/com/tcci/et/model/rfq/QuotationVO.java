/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *　ET_QUOTATION
 * @author Peter.pan
 */
public class QuotationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; // PK ID
    private Long tenderId; // 標案 ID
    private Long rfqId; // FK ET_RFQ_EKKO.ID
    private Long rfqVenderId; // FK ET_RFQ_VENDER.ID
    private Integer times; // 次數
    private Boolean last; // 最終報價
    private Date expiretime; // 有效日期
    private Boolean invoice; // 是否開發票
    private String curRfq; // 詢價單幣別
    private String curQuo; // 報價幣別
    private BigDecimal exRate; // 匯率
    private BigDecimal totalAmtRfq; // 總金額(詢價單幣別)
    private BigDecimal taxRfq; // 稅金(詢價單幣別)
    private BigDecimal netAmtRfq; // 淨總額(詢價單幣別)
    private BigDecimal totalAmtQuo; // 總金額(報價幣別)
    private BigDecimal taxQuo; // 稅金(報價幣別)
    private BigDecimal netAmtQuo; // 淨總額(報價幣別)
    private BigDecimal discount; // 折扣%
    private String memo; // 備註
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 修改人
    private Date modifytime; // 修改時間
    private String status; // 狀態(T:廠商暫存;C:廠商確認;R:退回報價;D:禁止報價)
    private Boolean disabled; // 無效
    
    private Long memberId; // 報價會員ID
    private Date quotetime; // 報價時間
    private BigDecimal taxRate;// 稅率
    
    // for UI
    private Long venderId;// FK: ET_VENDER_ALL.ID
    private String venderName;
    private List<QuotationItemVO> itemList;// 報價明細
    
    public QuotationVO() {
    }

    public QuotationVO(Long id) {
        this.id = id;
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

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public List<QuotationItemVO> getItemList() {
        return itemList;
    }

    public void setItemList(List<QuotationItemVO> itemList) {
        this.itemList = itemList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getModifierId() {
        return modifierId;
    }

    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationVO)) {
            return false;
        }
        QuotationVO other = (QuotationVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.QuotationVO[ id=" + id + " ]";
    }
    
}