/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.model.criteria;

/**
 *
 * @author Peter.pan
 */
public class RfqCriteriaVO extends BaseCriteriaVO {
    
    private Long applyId; // 新商申請ID (ET_MEMBER_FORM.ID)
    private String mandt; // SAP CLIENT
    private String lifnrUi; // LIFNR 去前 3 碼 0
    private Long rfqVenderId;// ET_RFQ_VENDER.ID
    
    public Long getApplyId() {
        return applyId;
    }
    
    public void setApplyId(Long applyId) {
        this.applyId = applyId;
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
