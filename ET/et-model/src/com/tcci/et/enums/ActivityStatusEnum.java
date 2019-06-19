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
public enum ActivityStatusEnum {
    DRAFT("D", "草稿", "#0000FF")
    ,PUBLISH("P", "已發佈", "#00FF00")
    ,START("S", "已開始", "#FF0000")
    ,END("E", "已結束", "#00FFFF")
    ,SUSPEND("S", "已暫停", "#FFFF00")
    ,REMOVE("R", "已刪除", "#BBBBBB")
    ;

    private String code;
    private String name;
    private String color;

    ActivityStatusEnum(String code, String name, String color) {
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

    public static ActivityStatusEnum getFromCode(String code) {
        for (ActivityStatusEnum enum1 : ActivityStatusEnum.values()) {
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

