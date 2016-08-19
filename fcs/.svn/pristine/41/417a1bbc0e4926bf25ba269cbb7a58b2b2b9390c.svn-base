/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.controller;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class CompanyVO extends ExcelVOBase {
    // excel 匯入欄位
    @NotNull(message = "code不能為空")
    @Size(min = 1, max = 6, message = "code至少1個字,最多6個字")
    private String code;
    @NotNull(message = "name不能為空")
    @Size(min = 1, max = 50, message = "name至少1個字,最多50個字")
    private String name;
    @Size(min=0, max = 60, message = "loginAccount至少0個字,最多60個字")
    private String loginAccount; //上傳者帳號
    @Min(value=0, message = "active最小值為0")
    @Max(value=1, message = "active最大值為1")
    private int active;
    @Min(value=0, message = "sort最小值為0")
    @Max(value=99, message = "sort最大值為99")
    private int sort;
    @NotNull(message = "group不能為空")
    @Size(min = 3, max = 5, message = "group至少3個字,最多5個字")
    private String group;
    
    // helper field
    private FcCompany company;
    private TcUser uploader;
    
    public void updateStatus() {
        if (null == company) {
            status = Status.ST_INSERT;
        } else {
            boolean changed = !StringUtils.equals(name, company.getName());
            if (!changed) { changed = (active != (company.getActive() ? 1 : 0)); }
            if (!changed) {
                if (null==uploader && null==company.getUploader()) {
                } else if (null==uploader || null==company.getUploader()) {
                    changed = true;
                } else {
                    changed = !StringUtils.equals(loginAccount, uploader.getLoginAccount());
                }
            }
            if (!changed) {
                changed = (sort != company.getSort());
            }
            if (!changed) {
                if(null != group){
//                    changed = (CompanyGroupEnum.getFromCode(group.toUpperCase()) != company.getGroup());
                    changed = (group.toUpperCase().equals(company.getGroup().getCode()));
                }
            }
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

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public FcCompany getCompany() {
        return company;
    }

    public void setCompany(FcCompany company) {
        this.company = company;
    }

    public TcUser getUploader() {
        return uploader;
    }

    public void setUploader(TcUser tcUser) {
        this.uploader = tcUser;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

}
