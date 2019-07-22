/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rfq;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class BargainVenderVO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long id;
    private Long tenderId;
    private Long rfqId;
    private Long rfqVenderId;
    private Long bargainId;
    private String memo;

    private Long creatorId;
    private Date createtime;
    private Long modifierId;
    private Date modifytime;
    
    // ET_VENDER_ALL
    private Long venderId; // PK ID
    private String name; // 中文名稱
    private Long applyId; // 新商申請ID (ET_MEMBER_FORM.ID)
    private String mandt; // SAP CLIENT
    private String lifnr; // SAP 供應商代碼
    private String lifnrUi; // LIFNR 去前 3 碼 0
            
    public String getVenderLabel(){
        StringBuilder sb = new StringBuilder();
        sb.append(name!=null? name:"");
        
        if( lifnrUi !=null ){
            sb.append(" (").append(lifnrUi).append(")");
        }else{
            sb.append(" (").append(applyId).append(")");
        }
        
        return sb.toString();
    }

    public BargainVenderVO() {
    }

    public BargainVenderVO(Long id) {
        this.id = id;
    }

    public BargainVenderVO(Long id, Long tenderId, Long rfqId, Long rfqVenderId, Long bargainId) {
        this.id = id;
        this.tenderId = tenderId;
        this.rfqId = rfqId;
        this.rfqVenderId = rfqVenderId;
        this.bargainId = bargainId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVenderId() {
        return venderId;
    }

    public void setVenderId(Long venderId) {
        this.venderId = venderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getLifnrUi() {
        return lifnrUi;
    }

    public void setLifnrUi(String lifnrUi) {
        this.lifnrUi = lifnrUi;
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

    public Long getBargainId() {
        return bargainId;
    }

    public void setBargainId(Long bargainId) {
        this.bargainId = bargainId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BargainVenderVO)) {
            return false;
        }
        BargainVenderVO other = (BargainVenderVO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tcci.et.model.BargainVenderVO[ id=" + id + " ]";
    }
    
}
