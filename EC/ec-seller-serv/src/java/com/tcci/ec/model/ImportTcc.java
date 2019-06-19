/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.util.List;
import java.util.Locale;

/**
 *
 * @author Peter.pan
 */
public interface ImportTcc {
    public boolean isHasData();
    public String getLoginAccount();
    public String getName();
    public List<String> getErrList();
    public int getRowNum();
    
    public void setCname(String phone);
    public void setPhone(String phone);
    public void setCategory(Long category);
    public void setCategoryName(String categoryName);
    public void setState(Long state);
    public void setStateName(String stateName);
    
    public void setRowNum(int rowNum);
    public void setHasData(boolean hasData);
    public void setHasError(boolean hasError);
    public void setErrList(List<String> list);

    public String getCname();
    public String getPhone();
    public Long getCategory();
    public String getCategoryName();
    public Long getState();
    public String getStateName();
    public boolean isHasError();
    
    public void genErrMsgs(Locale locale);
}
