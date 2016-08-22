package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Lynn.Huang
 */
public enum OrderTypeEnum {
    SALES_RETURN("ZRE"),
    SALES_ALLOWANCES("ZG");
    
    private String code;
    
    OrderTypeEnum(String code) {
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
