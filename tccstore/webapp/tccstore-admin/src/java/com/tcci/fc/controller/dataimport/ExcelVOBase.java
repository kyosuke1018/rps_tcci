/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.dataimport;

/**
 *
 * @author Jimmy.Lee
 */
public class ExcelVOBase {
    public enum Status {
        ST_NOCHANGE, ST_INSERT, ST_UPDATE, ST_DELETE
    }
    
    protected int rowIndex;         // excel row# (1 base)
    protected boolean valid = true; // 資料是否正確, 或寫入成功失敗
    protected String message;       // insert,update,no change, insert update success, exception msg
    protected Status status = Status.ST_NOCHANGE;  // 資料狀態
    
    // getter, setter
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
    
}
