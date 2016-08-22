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
public enum SalesAllowancesPageEnum {
    ALL("ALL"),
    PREVIOUS_TWO("PREVIOUS_TWO");
    
    private String code;
    
    SalesAllowancesPageEnum(String code) {
        this.code = code;
    }
    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName() , this.toString());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
