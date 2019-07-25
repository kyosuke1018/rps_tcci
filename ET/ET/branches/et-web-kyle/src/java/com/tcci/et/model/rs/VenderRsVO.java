/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import java.io.Serializable;

/**
 *
 * @author penpl
 */
public class VenderRsVO implements Serializable {
    private Long id; // PK ID
    private String name; // 中文名稱
    private Long applyId; // 新商申請ID (ET_MEMBER_FORM.ID)
    private String mandt; // SAP CLIENT
    private String lifnr; // SAP 供應商代碼
    private String lifnrUi; // LIFNR 去前 3 碼 0
    private Long rfqVenderId;// ET_RFQ_VENDER.ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getRfqVenderId() {
        return rfqVenderId;
    }

    public void setRfqVenderId(Long rfqVenderId) {
        this.rfqVenderId = rfqVenderId;
    }
   
}
