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
    private String src; // 來源
    private Long applyId;// 申請單號
    private boolean joinLFA1;
    private boolean outerJoinRfq;
    
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
