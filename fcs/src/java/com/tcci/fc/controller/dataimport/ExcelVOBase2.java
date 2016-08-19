/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.dataimport;

import java.math.BigDecimal;

/**
 *
 * @author Jimmy.Lee
 */
public class ExcelVOBase2 {
    public enum Status {
        ST_NOCHANGE, ST_INSERT, ST_UPDATE
    }
    
    protected int rowIndex;         // excel row# (1 base)
    protected boolean valid = true; // 資料是否正確, 或寫入成功失敗
    protected String message;       // insert,update,no change, insert update success, exception msg
    protected Status status = Status.ST_NOCHANGE;  // 資料狀態
    
    public void updateStatus(){}
            
    protected boolean isValueChanged(BigDecimal voValue, BigDecimal currentValue) {
        if (null == voValue) { // null 表示不改原始值
            return false;
        }
        if (null == currentValue) {
            return true;
        }
        return voValue.compareTo(currentValue) != 0;
    }    
    protected boolean isValueChanged(String voValue, String currentValue) {
        if (null == voValue) { // null 表示不改原始值
            return false;
        }
        if (null == currentValue) {
            return true;
        }
        return !voValue.equals(currentValue);
    }
    
    // getter, setter
//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public int getRowIndex() {
        return rowIndex;
    }
    
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
//</editor-fold>
    
}
