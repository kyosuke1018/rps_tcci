/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.usermgmt;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.entity.org.TcUser;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jimmy
 */
public class TcUserVO extends ExcelVOBase {
    // excel columns, 請確認與 TC_USER 欄位大小一致
    @NotNull(message = "loginAccount不能為空")
    @Size(min=1, max = 60, message = "loginAccount至少1個字,最多60個字")
    private String loginAccount; // AD 帳號
    @Size(max = 60, message = "email最多60個字")
    private String email;        // 電子郵件
    @Size(max = 20, message = "empId最多20個字")
    private String empId;        // 員工編號
    @Size(max = 20, message = "cname最多20個字")
    private String cname;        // 中文姓名

    // helper field
    private TcUser tcUser;

    public void updateStatus() {
        if (null == tcUser) {
            status = Status.ST_INSERT;
        } else {
            // 檢查是否異動
            boolean changed = !StringUtils.equals(email, tcUser.getEmail());
            if (!changed) { changed = !StringUtils.equals(empId, tcUser.getEmpId()); }
            if (!changed) { changed = !StringUtils.equals(cname, tcUser.getCname()); }
            status = changed ? Status.ST_UPDATE : Status.ST_NOCHANGE;
        }
    }
    
    // getter, setter
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public TcUser getTcUser() {
        return tcUser;
    }

    public void setTcUser(TcUser tcUser) {
        this.tcUser = tcUser;
    }

}
