/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.storeadmin.facade.sync;

/**
 *
 * @author Jimmy.Lee
 */
public class SyncBaseVO {

    private SyncStatus status = SyncStatus.NOCHANGE;
    private boolean valid = true; // true:資料正確, false:資料有誤
    private String message;

    public void setStatusMessage(SyncStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    
    // getter, setter
    public SyncStatus getStatus() {
        return status;
    }

    public void setStatus(SyncStatus status) {
        this.status = status;
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

}
