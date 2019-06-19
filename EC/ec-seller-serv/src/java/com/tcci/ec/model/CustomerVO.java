/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.cm.annotation.InputCheckMeta;
import com.tcci.cm.annotation.enums.DataTypeEnum;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author Peter.pan
 */
public class CustomerVO extends MemberInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long storeId;
    private Long memberId;
    private Long levelId;
    private String levelName;
    @InputCheckMeta(key="EC_CUSTOMER.CREDITS", type=DataTypeEnum.BIG_DECIMAL)
    private BigDecimal credits;
    private String cusType;
    
    @InputCheckMeta(key="EC_CUSTOMER.EXPECTED_CREDITS", type=DataTypeEnum.BIG_DECIMAL)
    private BigDecimal expectedCredits;// 期望信用額度
    private Date creditsApplyTime; // 申請信用額度時間
    private Date creditsTime; // 最近設定信用額度時間
    private Long creditsUser; // 最近設定信用人員ID
    @InputCheckMeta(key="EC_CUSTOMER.MEMO", type=DataTypeEnum.STRING)
    private String memo; // 備註
    private Long creditsCur; // 信用額度幣別 EC_CURRENCY.ID
    
    private Integer orderCount;
    private Integer inquiryCount;
    private BigDecimal totalAmt;
    private BigDecimal noPayAmt;
    
    private Date lastBuyDate;
    private Date firstBuyDate;

    public CustomerVO() {
    }

    public CustomerVO(Long id) {
        this.id = id;
    }

    public BigDecimal getExpectedCredits() {
        return expectedCredits;
    }

    public void setExpectedCredits(BigDecimal expectedCredits) {
        this.expectedCredits = expectedCredits;
    }

    public Date getCreditsApplyTime() {
        return creditsApplyTime;
    }

    public void setCreditsApplyTime(Date creditsApplyTime) {
        this.creditsApplyTime = creditsApplyTime;
    }

    public Date getCreditsTime() {
        return creditsTime;
    }

    public void setCreditsTime(Date creditsTime) {
        this.creditsTime = creditsTime;
    }

    public Long getCreditsUser() {
        return creditsUser;
    }

    public void setCreditsUser(Long creditsUser) {
        this.creditsUser = creditsUser;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCreditsCur() {
        return creditsCur;
    }

    public void setCreditsCur(Long creditsCur) {
        this.creditsCur = creditsCur;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public String getCusType() {
        return cusType;
    }

    public void setCusType(String cusType) {
        this.cusType = cusType;
    }

    public BigDecimal getNoPayAmt() {
        return noPayAmt;
    }

    public void setNoPayAmt(BigDecimal noPayAmt) {
        this.noPayAmt = noPayAmt;
    }

    public Date getFirstBuyDate() {
        return firstBuyDate;
    }

    public void setFirstBuyDate(Date firstBuyDate) {
        this.firstBuyDate = firstBuyDate;
    }

    public Date getLastBuyDate() {
        return lastBuyDate;
    }

    public void setLastBuyDate(Date lastBuyDate) {
        this.lastBuyDate = lastBuyDate;
    }

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getInquiryCount() {
        return inquiryCount;
    }

    public void setInquiryCount(Integer inquiryCount) {
        this.inquiryCount = inquiryCount;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
        if (!(object instanceof CustomerVO)) {
            return false;
        }
        CustomerVO other = (CustomerVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.ec.model.CustomerVO[ id=" + id + " ]";
    }
    
}
