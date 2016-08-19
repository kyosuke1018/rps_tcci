/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sap.jco.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Peter.pan
 */
public class RfcProxyRecord implements Serializable, Comparable {
    private long id;
    private String clientIP;
    private String serverIP;
    private String clientCode;
    private String operator;
    private String functionName;
    private String sapClientcode;
    private String inputBrief;
    private Date runTime;
    private long timeConsuming;
    private boolean success;
    
    @Override
    public int compareTo(Object o) {
       if( o!=null ){
           return ((this.id - ((RfcProxyRecord)o).id)>0? -1:1); // 反序排列
       }
       return 0;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(long timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSapClientcode() {
        return sapClientcode;
    }

    public void setSapClientcode(String sapClientcode) {
        this.sapClientcode = sapClientcode;
    }

    public String getInputBrief() {
        return inputBrief;
    }

    public void setInputBrief(String inputBrief) {
        this.inputBrief = inputBrief;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    //</editor-fold>
}
