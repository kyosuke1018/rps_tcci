/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.entity.org.TcGroup;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class TcGroupVO extends ExcelVOBase {
    // excel columns, 請確認與 TC_GROUP 欄位大小一致
    @NotNull(message = "code不能為空")
    @Size(min=1, max = 30, message = "code至少1個字,最多30個字")
    private String code;
    @NotNull(message = "name不能為空")
    @Size(min=1, max = 90, message = "name至少1個字,最多90個字")
    private String name;
    //

    // helper field
    private TcGroup tcGroup;

    // public API
    public void updateStatus() {
        if (null == tcGroup) {
            status = Status.ST_INSERT;
        } else {
            // 檢查是否異動
            boolean changed = !StringUtils.equals(code, tcGroup.getCode());
            if (!changed) { changed = !StringUtils.equals(name, tcGroup.getName()); }
            status = changed ? Status.ST_UPDATE : Status.ST_NOCHANGE;
        }
    }

    // getter, setter
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

    public TcGroup getTcGroup() {
        return tcGroup;
    }

    public void setTcGroup(TcGroup tcGroup) {
        this.tcGroup = tcGroup;
    }
    
}
