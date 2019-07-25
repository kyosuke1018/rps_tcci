/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import com.tcci.et.enums.TenderStatusEnum;
import java.util.Date;

/**
 * 已投標/邀標標案
 * @author Peter.pan
 */
public class TenderingVO {
    // ET_TENDER
    private Long id;
    
    private String code;//案件編號
    private String title;//標題
    private String summary;//投標公告
    private String status;//狀態代碼
    private Date datadate;//發佈日期
    private Date stenderDate;//投標日 起
    private Date etenderDate;//迄
    private Date verifyDate;//評標日期
    private Date closeDate;//決標日期
    private Date tenderDate;// 投標/邀標日
    private String categorys;
    
    // 報價
    private Boolean openQuote;// 開放報價
    private Boolean quoted;// 已報價
    private Long venderId;// 投標廠商
    private Long rfqVenderId;// ET_RFQ_VENDER.ID
    private Integer lastQuote;// 最後確認報價次數
    private String venderName;
    private String lifnrUi;
    private Long memberId;

    private String statusLabel;

    public void setStatus(String status) {
        this.status = status;
        
        this.statusLabel = null;
        if( status!=null ){
            TenderStatusEnum statusEnum = TenderStatusEnum.getFromCode(code);
            if( statusEnum!=null ){
                statusLabel = statusEnum.getDisplayName();
            }
        }
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getVenderName() {
        return venderName;
    }

    public void setVenderName(String venderName) {
        this.venderName = venderName;
    }

    public String getLifnrUi() {
        return lifnrUi;
    }

    public void setLifnrUi(String lifnrUi) {
        this.lifnrUi = lifnrUi;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    public Integer getLastQuote() {
        return lastQuote;
    }

    public void setLastQuote(Integer lastQuote) {
        this.lastQuote = lastQuote;
    }

    public Long getRfqVenderId() {
        return rfqVenderId;
    }

    public void setRfqVenderId(Long rfqVenderId) {
        this.rfqVenderId = rfqVenderId;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public Boolean getQuoted() {
        return quoted;
    }

    public void setQuoted(Boolean quoted) {
        this.quoted = quoted;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public Date getDatadate() {
        return datadate;
    }

    public void setDatadate(Date datadate) {
        this.datadate = datadate;
    }

    public Date getStenderDate() {
        return stenderDate;
    }

    public void setStenderDate(Date stenderDate) {
        this.stenderDate = stenderDate;
    }

    public Date getEtenderDate() {
        return etenderDate;
    }

    public void setEtenderDate(Date etenderDate) {
        this.etenderDate = etenderDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Date getTenderDate() {
        return tenderDate;
    }

    public void setTenderDate(Date tenderDate) {
        this.tenderDate = tenderDate;
    }

    public Boolean getOpenQuote() {
        return openQuote;
    }

    public void setOpenQuote(Boolean openQuote) {
        this.openQuote = openQuote;
    }
    
    
}
