/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.myguimini.controller;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.myguimini.entity.MyService;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
public class MyServiceVO extends ExcelVOBase {
    // excel columns, 請確認與 TC_USER 欄位大小一致
    @NotNull(message = "service不能為空")
    @Size(min = 1, max = 255, message = "service至少1個字,最多255個字")
    private String service;
    @NotNull(message = "serviceUrl不能為空")
    @Size(min = 1, max = 255, message = "serviceUrl至少1個字,最多255個字")
    private String serviceUrl;
    @Size(max = 500, message = "description最多500個字")
    private String description;
    @Min(value=0, message = "active最小值為0")
    @Max(value=1, message = "active最大值為1")
    private int active = 1;

    // helper field
    private MyService myService;
    
    public void updateStatus() {
        if (null == myService) {
            status = Status.ST_INSERT;
        } else {
            // 檢查是否異動
            boolean changed = !StringUtils.equals(serviceUrl, myService.getServiceUrl());
            if (!changed) { changed = !StringUtils.equals(description, myService.getDescription()); }
            if (!changed) { changed = (active!=(myService.isActive() ? 1 : 0)); }
            status = changed ? Status.ST_UPDATE : Status.ST_NOCHANGE;
        }
    }
    
    // getter, setter
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public MyService getMyService() {
        return myService;
    }

    public void setMyService(MyService myService) {
        this.myService = myService;
    }
    
}
