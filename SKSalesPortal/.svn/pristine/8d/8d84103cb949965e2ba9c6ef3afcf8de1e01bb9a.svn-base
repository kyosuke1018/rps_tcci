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
public enum SdBstzdEnum {
    BSTZD_1,
    BSTZD_2;
     /**
     * 取得類別名稱(繁中)
     * @return 
     */
    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public static SdBstzdEnum fromString(String text) {
        if (text != null) {
            for (SdBstzdEnum sdBstzdEnum : SdBstzdEnum.values()) {
                if (text.equalsIgnoreCase(sdBstzdEnum.toString())) {
                    return sdBstzdEnum;
                }
            }
        }
        return null;
    }
}
