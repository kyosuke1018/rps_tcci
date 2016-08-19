/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Neo.Fu
 */
public enum PartnerStatusEnum {

    APPLY,
    APPROVE,
    REJECT;

    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public static PartnerStatusEnum fromString(String key) {
        PartnerStatusEnum result = null;
        for (PartnerStatusEnum status : PartnerStatusEnum.values()) {
            if (status.toString().equals(key)) {
                result = status;
                break;
            }
        }
        return result;
    }
}
