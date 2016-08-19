/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.org;

import com.tcci.fc.entity.org.OrgCompany;
import com.tcci.fc.entity.org.OrgDepartment;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jimmy
 */
public class DepartmentVO {
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
    @NotNull(message = "code不能為空")
    @Size(min=1, max = 50, message = "code至少1個字,最多50個字")
    private String code;
    @NotNull(message = "name不能為空")
    @Size(min=1, max = 100, message = "name至少1個字,最多100個字")
    private String name;
    @Size(min=0, max = 50, message = "parentCode最多50個字")
    private String parentCode;
    private boolean activation = true;

    private OrgCompany orgCompany;
    private OrgDepartment orgDepartment;

    // public API
    public void checkForUpdate() {
        if (orgDepartment == null) {
            status = ST_INSERT;
            message = "new department";
        } else {
            // 檢查是否異動
            boolean changed = !StringUtils.equals(code, orgDepartment.getCode());
            if (!changed) { changed = !StringUtils.equals(name, orgDepartment.getName()); }
            if (!changed) { changed = (activation != orgDepartment.isActivation()); }
            if (!changed) { changed = !StringUtils.equals(compCode, orgDepartment.getOrgCompany().getCode()); }
            if (!changed) {
                String p1 = StringUtils.trimToNull(parentCode);
                String p2 = orgDepartment.getParent() == null ? null
                        : StringUtils.trimToNull(orgDepartment.getParent().getCode());
                changed = !StringUtils.equals(p1, p2);
            }
            status = changed ? ST_UPDATE : ST_NOCHANGE;
            message = changed ? "update department" : "no change";
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
    
}
