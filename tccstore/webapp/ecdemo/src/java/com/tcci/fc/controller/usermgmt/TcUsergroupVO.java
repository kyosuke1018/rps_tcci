/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Jimmy.Lee
 */
public class TcUsergroupVO extends ExcelVOBase {
    // excel columns, 請確認與 TC_USER, TC_GROUP 欄位大小一致
    @NotNull(message = "loginAccount不能為空")
    @Size(min=1, max = 60, message = "loginAccount至少1個字,最多60個字")
    private String loginAccount; // AD 帳號
    @NotNull(message = "groupCode不能為空")
    @Size(min=1, max = 30, message = "groupCode至少1個字,最多30個字")
    private String groupCode;    // GROUP CODE
    
    // helper field
    private TcUser tcUser;
    private TcGroup tcGroup;
    private TcUsergroup tcUsergroup;

    public void updateStatus() {
        if (null == tcUsergroup) {
            status = Status.ST_INSERT;
        } else {
            status = Status.ST_NOCHANGE;
        }
    }
    
    // getter, setter
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public TcUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TcUser tcUser) {
        this.tcUser = tcUser;
    }

    public TcGroup getTcGroup() {
        return tcGroup;
    }

    public void setTcGroup(TcGroup tcGroup) {
        this.tcGroup = tcGroup;
    }

    public TcUsergroup getTcUsergroup() {
        return tcUsergroup;
    }

    public void setTcUsergroup(TcUsergroup tcUsergroup) {
        this.tcUsergroup = tcUsergroup;
    }

}
