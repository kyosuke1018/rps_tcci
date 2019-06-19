/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 角色相關部門別
 * @author peter.pan
 */
public enum DeptRoleEnum {
    PM("PM", "工務")
    ,MM("MM", "物料")
    ,CO("CO", "總務")
    ;

    private String code;
    private String name;

    DeptRoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 顯示名稱 (取自enum.properties => [class name].[enum name])
     *
     * @return
     */
    public String getDisplayName() {
        String res = ResourceBundleUtils.getDisplayName(GlobalConstant.ENUM_RESOURCE, this.getClass().getSimpleName() + "." + this.toString());
        if (res == null) {
            res = name;
        }
        return res;
    }

    public static DeptRoleEnum getFromCode(String code) {
        for (DeptRoleEnum enum1 : DeptRoleEnum.values()) {
            if (code!=null && code.equals(enum1.getCode())) {
                return enum1;
            }
        }
        return null; // default
    }

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}
