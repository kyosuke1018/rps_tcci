/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Jason.Yu
 */
public enum CategoryEnum {

    PR,
    PO_DEPARTMENT,
    PO_FACTORY,
    NO,
    ME,
    SD;

    /**
     * 取得類別名稱(繁中)
     * @return 
     */
    public String getTwDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public static CategoryEnum fromString(String text) {
        if (text != null) {
            for (CategoryEnum categoryEnum : CategoryEnum.values()) {
                if (text.equalsIgnoreCase(categoryEnum.toString())) {
                    return categoryEnum;
                }
            }
        }
        return null;
    }
}
