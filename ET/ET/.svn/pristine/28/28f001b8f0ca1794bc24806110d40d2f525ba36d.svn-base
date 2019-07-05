package com.tcci.sap.jco.entity;

import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public interface TcRfcZtab {
    public Object getPrimaryKey();
    public void setSyncTimeStamp(Date date);
    public String getMandt();
    public String getDelKey();
    public default boolean isDelMainTab(){// 是否是刪除依據判斷主檔
        return false;
    };
}
