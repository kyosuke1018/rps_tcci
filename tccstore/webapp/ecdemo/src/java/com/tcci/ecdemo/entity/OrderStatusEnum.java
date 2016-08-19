/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.entity;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 *
 * @author Neo.Fu
 */
public enum OrderStatusEnum {
    OPEN,
    CLOSE;
    
    public String getDisplayName(){
        return ResourceBundleUtil.getDisplayName( this.getClass().getCanonicalName() , this.toString() );
    } 
}
