/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Neo.Fu
 */
public enum OrderStatusEnum {

    OPEN,      // 待審核
    OPEN_AUTO, // 自動審核中
    APPROVE,   // 已核准
    FAIL,      // 失敗(SAP無法建立訂單)
    CANCEL,    // 已取消
    CLOSE;     // 已出貨

    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public static OrderStatusEnum fromString(String key) {
        OrderStatusEnum result = null;
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.toString().equals(key)) {
                result = status;
                break;
            }
        }
        return result;
    }
}
