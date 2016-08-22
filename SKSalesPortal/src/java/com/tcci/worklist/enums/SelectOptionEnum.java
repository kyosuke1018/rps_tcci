package com.tcci.worklist.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author nEO.Fu
 */
public enum SelectOptionEnum {
    ALL,
    TOP_MANAGER;
    
     /**
     * 取得類別名稱(繁中)
     * @return 
     */
    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public static SelectOptionEnum fromString(String text) {
        if (text != null) {
            for (SelectOptionEnum reviewOptionEnum : SelectOptionEnum.values()) {
                if (text.equalsIgnoreCase(reviewOptionEnum.toString())) {
                    return reviewOptionEnum;
                }
            }
        }
        return null;
    }    
}
