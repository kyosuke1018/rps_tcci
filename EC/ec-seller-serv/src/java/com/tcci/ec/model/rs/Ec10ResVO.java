/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class Ec10ResVO {
    private String status;// 狀態
    private String msg;// 結果訊息
    private List<String> errors;// 詳細錯誤
    private Long orderId;// EC1.0 訂單ID : TCCSTORE_USER.EC_ORDER.ID
    private String carNo;// 對應車號 (for UI 顯示)
    
    public Ec10ResVO(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
