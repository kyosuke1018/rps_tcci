/**
 *
 * @author Peter.pan
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;

/**
 * 狀態別
 * @author peter.pan
 */
public enum ContentStatusEnum {
    DRAFT("D", "草稿", "#000099")
    ,PUBLISH("P", "發佈", "#990000")
    ,SUSPEND("S", "下架", "#999999")
    //,REMOVE("R", "已刪除", "#BBBBBB")
    ;

    private String code;
    private String name;
    private String color;

    ContentStatusEnum(String code, String name, String color) {
        this.code = code;
        this.name = name;
        this.color = color;
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

    public static ContentStatusEnum getFromCode(String code) {
        for (ContentStatusEnum enum1 : ContentStatusEnum.values()) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //</editor-fold>
}

