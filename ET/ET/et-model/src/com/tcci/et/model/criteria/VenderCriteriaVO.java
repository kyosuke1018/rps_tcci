/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.criteria;

/**
 *
 * @author penpl
 */
public class VenderCriteriaVO extends BaseCriteriaVO {
    private String mandt;
    private String lifnr;
    private String lifnrUi;
    private String src; // 來源
    private Long applyId;// 申請單號
    private boolean joinLFA1;// 是否 JOIN TC_ZTAB_EXP_LFA1
    private boolean outerJoinRfq;// 是否 OUTER JOIN ET_RFQ_VENDER 
    
    @Override
    public void clear(){
        this.keyword = null;
        this.nameKeyword = null;
        this.applyId = null;
    }

    public boolean isOuterJoinRfq() {
        return outerJoinRfq;
    }

    public void setOuterJoinRfq(boolean outerJoinRfq) {
        this.outerJoinRfq = outerJoinRfq;
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

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public boolean isJoinLFA1() {
        return joinLFA1;
    }

    public void setJoinLFA1(boolean joinLFA1) {
        this.joinLFA1 = joinLFA1;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
