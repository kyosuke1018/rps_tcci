/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Neo.Fu
 */
public enum ReviewOptionEnum {
    A,
    R,
    O;
    
     /**
     * 取得類別名稱(繁中)
     * @return 
     */
    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public static ReviewOptionEnum fromString(String text) {
        if (text != null) {
            for (ReviewOptionEnum reviewOptionEnum : ReviewOptionEnum.values()) {
                if (text.equalsIgnoreCase(reviewOptionEnum.toString())) {
                    return reviewOptionEnum;
                }
            }
        }
        return null;
    }    
}
