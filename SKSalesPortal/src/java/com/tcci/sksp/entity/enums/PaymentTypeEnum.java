package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Lynn.Huang
 */
public enum PaymentTypeEnum {
    CASH,
    CHECK;    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName() , this.toString());
    }    
}
