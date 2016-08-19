package com.tcci.fc.entity.bpm.enumeration;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum ActivityTypeEnum {

    START,
    END,
    TASK,
    AND_GATE,
    OR_GATE,
    OPTION,
    EXPRESSION_ROBOT,
    SETSTATE_ROBOT,
    GROUND,
    CONDITION;
    
    public String value() {
        return name();
    }

    public static ActivityTypeEnum fromValue(String v) {
        return valueOf(v);
    }    
}
