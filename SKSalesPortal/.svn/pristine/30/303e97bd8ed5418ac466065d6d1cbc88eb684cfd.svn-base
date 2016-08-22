package com.tcci.sksp.entity.enums;

import com.tcci.fc.util.ResourceBundleUtil;

/**
 * 
 * @author nEO.Fu
 */
public enum ReasonEnum {

    PRECENT_5("101", "ZCR2"),
    // SAMPLE("102", "ZCR2"),
    FEEDBACK("103", "ZCR2"),
    GAP("104", "ZCR2"),
    PRECENT_5_SPECIAL("105", "ZCR2"),
    PRECENT_3("105", "ZCR2"), // 3%, 原因同5% 特殊
    OTHER("199", "ZCR2");
    private String code;
    private String category;

    ReasonEnum(String code, String category) {
        this.code = code;
        this.category = category;
    }

    public String getDisplayName() {
        return ResourceBundleUtil.getDisplayName(this.getClass().getCanonicalName(), this.toString());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
