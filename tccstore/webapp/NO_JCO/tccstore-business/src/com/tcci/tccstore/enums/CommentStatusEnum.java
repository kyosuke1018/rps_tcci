/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Jimmy.Lee
 */
public enum CommentStatusEnum {
    APPLY,
    APPROVE,
    REJECT;
    
    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

}
