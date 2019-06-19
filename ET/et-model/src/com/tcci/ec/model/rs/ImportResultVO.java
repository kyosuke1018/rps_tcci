/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.rs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class ImportResultVO extends BaseResponseVO implements Serializable {
    private List resList =new ArrayList();
    private List errList =new ArrayList();
    
    private String resultMsg;
    private boolean canImport;
    private int errorCount;

    public ImportResultVO(){}
    
    public List getResList() {
        return resList;
    }

    public void setResList(List resList) {
        this.resList = resList;
    }

    public List getErrList() {
        return errList;
    }

    public void setErrList(List errList) {
        this.errList = errList;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public boolean isCanImport() {
        return canImport;
    }

    public void setCanImport(boolean canImport) {
        this.canImport = canImport;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    
}
