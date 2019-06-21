/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.model.admin;

import com.tcci.et.enums.ActivityLogEnum;
import java.util.Date;

/**
 *
 * @author Peter
 */
public class ActivityLogVO { 
    private long id;
    private String code;
    private String subject;
    private String viewId;
    private String detail;
    private long creator;
    private Date createTimestamp;
    private boolean success;
    private String loginAccount;
    private String userName;
    private long deptId;
    private String deptName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        if( code!=null ){
            ActivityLogEnum activityLogEnum = ActivityLogEnum.getFromCode(code);
            if( activityLogEnum!=null ){
                this.setSubject(activityLogEnum.getDisplayName());
            }
        }
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCreator() {
        return creator;
    }

    public void setCreator(long creator) {
        this.creator = creator;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

}
