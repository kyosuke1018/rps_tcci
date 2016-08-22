/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Jason.Yu
 */
public enum RemitMasterStatusEnum {
    NOT_YET,
    REVIEWED,
    TRANSFER_ADVANCE,
    TRANSFER_SAP,
    TRANSFER_OK,
    TRANSFER_FAILED,
    CANCELED;
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName() , this.toString());
    }    
}
