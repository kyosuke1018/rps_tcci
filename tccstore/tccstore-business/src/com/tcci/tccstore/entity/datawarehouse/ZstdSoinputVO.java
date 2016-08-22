/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity.datawarehouse;

/**
 *
 * @author Jimmy.Lee
 */
public class ZstdSoinputVO {

    private Long signi;          // EC_ORDER.ID
    private String procFlag;     // NULL:待處理,B:處理中,C:已完成,F:失敗,E:刪除
    private String orderFlag2;   // 1:新增, 2:刪除
    private String vbelv;        // SAP訂單號碼

    // getter, setter
    public Long getSigni() {
        return signi;
    }

    public void setSigni(Long signi) {
        this.signi = signi;
    }

    public String getProcFlag() {
        return procFlag;
    }

    public void setProcFlag(String procFlag) {
        this.procFlag = procFlag;
    }

    public String getOrderFlag2() {
        return orderFlag2;
    }

    public void setOrderFlag2(String orderFlag2) {
        this.orderFlag2 = orderFlag2;
    }

    public String getVbelv() {
        return vbelv;
    }

    public void setVbelv(String vbelv) {
        this.vbelv = vbelv;
    }

}
