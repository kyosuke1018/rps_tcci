/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.model.criteria;

import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class RfqCriteriaVO extends BaseCriteriaVO {
    
    private Long applyId; // 新商申請ID (ET_MEMBER_FORM.ID)
    private String mandt; // SAP CLIENT
    private BigInteger extrow;// PM extrow;
    private String lifnrUi; // LIFNR 去前 3 碼 0
    private Long rfqVenderId;// ET_RFQ_VENDER.ID
    
    private String purOrg;
    private String purGroup;
    
    private String prNo;
    private String rfqNo;
    
    private String matnr;
    private String matnrUI;
    
    // quote
    private boolean withNoQuoteItem;// 含未報價項目查詢
    private Integer quoteTimes;// 第?次報價
    private Boolean lastQuote;// 最後一次報價
    
    private List<Long> quoteList;
    
    public Long getApplyId() {
        return applyId;
    }
    
    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getPurOrg() {
        return purOrg;
    }

    public void setPurOrg(String purOrg) {
        this.purOrg = purOrg;
    }

    public String getPurGroup() {
        return purGroup;
    }

    public void setPurGroup(String purGroup) {
        this.purGroup = purGroup;
    }

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }

    public String getRfqNo() {
        return rfqNo;
    }

    public void setRfqNo(String rfqNo) {
        this.rfqNo = rfqNo;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMatnrUI() {
        return matnrUI;
    }

    public void setMatnrUI(String matnrUI) {
        this.matnrUI = matnrUI;
    }

    public boolean isWithNoQuoteItem() {
        return withNoQuoteItem;
    }

    public void setWithNoQuoteItem(boolean withNoQuoteItem) {
        this.withNoQuoteItem = withNoQuoteItem;
    }

    public BigInteger getExtrow() {
        return extrow;
    }

    public void setExtrow(BigInteger extrow) {
        this.extrow = extrow;
    }

    public List<Long> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<Long> quoteList) {
        this.quoteList = quoteList;
    }

    public Integer getQuoteTimes() {
        return quoteTimes;
    }

    public void setQuoteTimes(Integer quoteTimes) {
        this.quoteTimes = quoteTimes;
    }

    public Boolean getLastQuote() {
        return lastQuote;
    }

    public void setLastQuote(Boolean lastQuote) {
        this.lastQuote = lastQuote;
    }

    public Long getRfqVenderId() {
        return rfqVenderId;
    }

    public void setRfqVenderId(Long rfqVenderId) {
        this.rfqVenderId = rfqVenderId;
    }
    
    public String getMandt() {
        return mandt;
    }
    
    public void setMandt(String mandt) {
        this.mandt = mandt;
    }
    
    public String getLifnrUi() {
        return lifnrUi;
    }
    
    public void setLifnrUi(String lifnrUi) {
        this.lifnrUi = lifnrUi;
    }
    
    
}
