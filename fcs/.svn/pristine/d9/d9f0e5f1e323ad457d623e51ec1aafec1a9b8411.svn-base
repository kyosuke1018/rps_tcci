/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller.sheetdata;

import com.tcci.irs.entity.reconciling.IrsSheetdataReconcilingD;
import com.tcci.irs.entity.sheetdata.IrsSheetdataM;
import java.math.BigDecimal;

/**
 *
 * @author Kyle.Cheng
 */
public class SheetMstVO {
    private IrsSheetdataM master;
    private BigDecimal reAnnounceAmount;//個體方 公告幣別金額
    private BigDecimal paAnnounceAmount;//對帳方 公告幣別金額

    //調整數
//    private BigDecimal announceDiff;//調整後差異
    public IrsSheetdataM getMaster() {
        return master;
    }

    public void setMaster(IrsSheetdataM master) {
        this.master = master;
    }

    public BigDecimal getReAnnounceAmount() {
        return reAnnounceAmount;
    }

    public void setReAnnounceAmount(BigDecimal reAnnounceAmount) {
        this.reAnnounceAmount = reAnnounceAmount;
    }

    public BigDecimal getPaAnnounceAmount() {
        return paAnnounceAmount;
    }

    public void setPaAnnounceAmount(BigDecimal paAnnounceAmount) {
        this.paAnnounceAmount = paAnnounceAmount;
    }
    
    
    
    //<editor-fold defaultstate="collapsed" desc="helper">
    //調整後差異
    public BigDecimal getAnnounceDiff() {
        //RE+PA+ReconcilingAmount
        BigDecimal reconcilingAmount_sum = getAnnounceReconcilingAmount();
        return this.reAnnounceAmount.add(this.paAnnounceAmount).add(reconcilingAmount_sum);
    }
    //調整數
    public BigDecimal getAnnounceReconcilingAmount() {
        //reconcilingAmount_sum=Reconciling.RE+Reconciling.PA
        BigDecimal result = BigDecimal.ZERO;
        for (IrsSheetdataReconcilingD entity : this.master.getIrsSheetdataReconcilingDCollection()) {
            String accountType = entity.getAccountType();
            BigDecimal amountAdjustments = entity.getAmountAdjustments();
            if(null != amountAdjustments){
//                if("RE".equalsIgnoreCase(accountType)){
//                    result = result.add(amountAdjustments);
//                }else{//PA
//                    result = result.add(amountAdjustments);
//                }
                //只加總 設定為台幣調節的
            }

        }
        return result;
    }
    
    //不調節備註
    public String getRemarkOnly(){
        for (IrsSheetdataReconcilingD entity : this.master.getIrsSheetdataReconcilingDCollection()) {
            if(entity.isRemarkOnly()){
                return entity.getRemark();
            }
        }
        return "";
    }
    //</editor-fold>
}
