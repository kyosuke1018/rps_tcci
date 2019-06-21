/**
 *
 * @author Peter.pan
 */
package com.tcci.et.enums;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.util.ResourceBundleUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 狀態別
 * @author peter.pan
 */
public enum TenderStatusEnum {
    DRAFT("D", "草稿", "#0000FF", false)
    ,NOT_SALE("NS", "未開始", "#0000FF", false)
    ,ON_SALE("OS", "標書發售中", "#FF0000", true)
    ,ON_TENDER("T", "投標中", "#FF0000", true)
    ,VERIFY("V", "評標中", "#FF0000", true)
    ,END("E", "已決標", "#00FFFF", true)
    ,SUSPEND("S", "已暫停", "#FFFF00", false)
    ,REMOVE("R", "已刪除", "#BBBBBB", false)
    ;

    private String code;
    private String name;
    private String color;
    private Boolean active;

    TenderStatusEnum(String code, String name, String color, Boolean active) {
        this.code = code;
        this.name = name;
        this.color = color;
        this.active = active;
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

    public static TenderStatusEnum getFromCode(String code) {
        for (TenderStatusEnum enum1 : TenderStatusEnum.values()) {
            if (code!=null && code.equals(enum1.getCode())) {
                return enum1;
            }
        }
        return null; // default
    }
    
    public static List<String> getCodeByActive(Boolean active) {
        List<String> result = new ArrayList<>();
        for (TenderStatusEnum enum1 : TenderStatusEnum.values()) {
            if (enum1.getActive().equals(active)) {
                result.add(enum1.getCode());
            }
        }
        return result;
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
    
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    //</editor-fold>
}

