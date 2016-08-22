package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author nEO.Fu
 */
public enum QuotationStatusEnum {

    OPEN,
    READED,
    APPROVED,
    REJECT,
    FAILED,
    CLOSED;
    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName() , this.toString());
    }
    
}
