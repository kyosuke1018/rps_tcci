/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.org;

import com.tcci.fc.entity.org.OrgCompany;
import com.tcci.fc.entity.org.OrgDepartment;
import com.tcci.fc.entity.org.OrgEmployee;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class EmployeeVO {
    // 是否新增,更新
    public final static int ST_NOCHANGE = 0;
    public final static int ST_INSERT = 1;
    public final static int ST_UPDATE = 2;

    // ExcelUtil.importList 保留字
    private int rowIndex;  // excel row
    private boolean valid = true; // true:驗證成功, false:驗證成功
    private String message; // 錯誤訊息
    private int status;

    // excel columns, 請確認與 ORG_COMPANY 欄位大小一致
    @NotNull(message = "compCode不能為空")
    @Size(min=1, max = 50, message = "compCode至少1個字,最多50個字")
    private String compCode;
    @NotNull(message = "deptCode不能為空")
    @Size(min=1, max = 50, message = "deptCode至少1個字,最多50個字")
    private String deptCode;
    @NotNull(message = "code不能為空")
    @Size(min=1, max = 50, message = "code至少1個字,最多50個字")
    private String code;
    @NotNull(message = "name不能為空")
    @Size(min=1, max = 100, message = "name至少1個字,最多100個字")
    private String name;
    private String email;
    private String adaccount;
    private boolean activation = true;

    private OrgCompany orgCompany;
    private OrgDepartment orgDepartment;
    private OrgEmployee orgEmployee;

    // public API
    public void checkForUpdate() {
        if (orgEmployee == null) {
            status = ST_INSERT;
            message = "new employee";
        } else {
            // 檢查是否異動
            boolean changed = !StringUtils.equals(code, orgEmployee.getCode());
            if (!changed) { changed = !StringUtils.equals(name, orgEmployee.getName()); }
            if (!changed) { changed = !StringUtils.equals(email, orgEmployee.getEmail()); }
            if (!changed) { changed = !StringUtils.equals(adaccount, orgEmployee.getAdaccount()); }
            if (!changed) { changed = (activation != orgEmployee.isActivation()); }
            if (!changed) { changed = !StringUtils.equals(compCode, orgEmployee.getOrgCompany().getCode()); }
            if (!changed) { changed = !StringUtils.equals(deptCode, orgEmployee.getOrgDepartment().getCode()); }
            status = changed ? ST_UPDATE : ST_NOCHANGE;
            message = changed ? "update employee" : "no change";
        }
    }

    // getter, setter
    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdaccount() {
        return adaccount;
    }

    public void setAdaccount(String adaccount) {
        this.adaccount = adaccount;
    }

    public boolean isActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public OrgCompany getOrgCompany() {
        return orgCompany;
    }

    public void setOrgCompany(OrgCompany orgCompany) {
        this.orgCompany = orgCompany;
    }

    public OrgDepartment getOrgDepartment() {
        return orgDepartment;
    }

    public void setOrgDepartment(OrgDepartment orgDepartment) {
        this.orgDepartment = orgDepartment;
    }

    public OrgEmployee getOrgEmployee() {
        return orgEmployee;
    }

    public void setOrgEmployee(OrgEmployee orgEmployee) {
        this.orgEmployee = orgEmployee;
    }
}
