package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author nEO.Fu
 */
public enum QuotationReviewOptionEnum {

    READED,
    APPROVED,
    REJECT;
    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName() , this.toString());
    }
    
}
