/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class BargainVO implements Serializable {
    private Long id; // PK ID
    private Long tenderId; // 案件ET_TENDER.ID
    private Long rfqId; // FK: ET_RFQ_EKKO.ID
    private Integer times; // 第幾次議價
    private Date SDate; // 報價期間-起
    private Date EDate; // 報價期間-迄
    private Boolean disabled;
    private String memo; // 備註
    private Long creatorId; // 建立人
    private Date createtime; // 建立時間
    private Long modifierId; // 最近修改人
    private Date modifytime; // 最近修改時間
    
    private String creatorName;
    private String creatorAccount;
    private String modifierName;
    private String modifierAccount;
    
    // for UI
    private boolean readonly;
    // 已設定廠商
    private List<BargainVenderVO> bargainVenderList;// 已設定廠商
    private List<Long> rfqVenderIds;// 已設定廠商 RFQ_VENDER_ID
    private String bargainVenderLabel;
    private List<Long> newRfqVenderIds;// 本次設定廠商 RFQ_VENDER_ID

    /**
     * 已設定廠商
     * @return 
     */
    public String genBargainVenderInfo(){
        StringBuilder sb = new StringBuilder();
        rfqVenderIds = new ArrayList<Long>();
        
        if( bargainVenderList!=null ){
            for(BargainVenderVO vo : bargainVenderList){
                rfqVenderIds.add(vo.getRfqVenderId());
                sb.append(sb.toString().isEmpty()?"":",");
                sb.append(vo.getVenderLabel());
            }
        }
        
        bargainVenderLabel = sb.toString();
        return bargainVenderLabel;
    }

    public void setBargainVenderList(List<BargainVenderVO> bargainVenderList) {
        this.bargainVenderList = bargainVenderList;
        // 已設定廠商
        genBargainVenderInfo();
    }
    
    public Date getLastUpdateTime(){
        return (this.modifytime!=null)?modifytime:this.createtime;
    }
    
    public String getLastUpdateUser(){
        return (this.modifierAccount!=null)?modifierName+"("+modifierAccount+")":creatorName+"("+creatorAccount+")";
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(String creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifierAccount() {
        return modifierAccount;
    }

    public void setModifierAccount(String modifierAccount) {
        this.modifierAccount = modifierAccount;
    }

    public List<Long> getNewRfqVenderIds() {
        return newRfqVenderIds;
    }

    public void setNewRfqVenderIds(List<Long> newRfqVenderIds) {
        this.newRfqVenderIds = newRfqVenderIds;
    }

    public List<BargainVenderVO> getBargainVenderList() {
        return bargainVenderList;
    }

    public String getBargainVenderLabel() {
        return bargainVenderLabel;
    }

    public void setBargainVenderLabel(String bargainVenderLabel) {
        this.bargainVenderLabel = bargainVenderLabel;
    }

    public List<Long> getRfqVenderIds() {
        return rfqVenderIds;
    }

    public void setRfqVenderIds(List<Long> rfqVenderIds) {
        this.rfqVenderIds = rfqVenderIds;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
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

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Date getSDate() {
        return SDate;
    }

    public void setSDate(Date sDate) {
        this.SDate = sDate;
    }

    public Date getEDate() {
        return EDate;
    }

    public void setEDate(Date eDate) {
        this.EDate = eDate;
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
        
}
