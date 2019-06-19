/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model.admin;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jason.Yu
 */
public class UserLoaderVO {
    private String factoryFullName; 
    private String departmentFullName;
    private String employeeId; //<td>#{msg['userloader.employee.id']}</td>
    private String employeeName; //<td>#{msg['userloader.employee.name']}</td>
    private String loginAccount; //<td>#{msg['userloader.user.ad.account']}</td>
    private String emailAddress; //<td>#{msg['userloader.user.email.address']}</td>

    private String groupNames;
    private String bookingNotify; //<td>#{msg['userloader.is.booking.notify.user']}</td>
    private String beBookedNotify;
    private String disableAccount; //<td>#{msg['userloader.is.disable.user']}</td>
    private String notifyAccessLog;
    private String factorycodes;
    
    private String companies; // 公司
    private String factoryGroups; // 工廠群組
    
    private boolean isError = false;
    private boolean isImported = false;//是否匯入成功

    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
   public String getFactoryGroups() {
        return factoryGroups;
    }

    public void setFactoryGroups(String factoryGroups) {
        this.factoryGroups = factoryGroups;
    }

    public String getNotifyAccessLog() {
        return notifyAccessLog;
    }
    
    public void setNotifyAccessLog(String notifyAccessLog) {
        this.notifyAccessLog = notifyAccessLog;
    }
    
    public String getBookingNotify() {
        return bookingNotify;
    }
    
    public void setBookingNotify(String bookingNotify) {
        this.bookingNotify = bookingNotify;
    }
    
    public String getDepartmentFullName() {
        return departmentFullName;
    }
    
    public void setDepartmentFullName(String departmentFullName) {
        this.departmentFullName = departmentFullName;
    }
    
    public String getFactoryFullName() {
        return factoryFullName;
    }
    
    public void setFactoryFullName(String factoryFullName) {
        this.factoryFullName = factoryFullName;
    }
    
    public String getDisableAccount() {
        return disableAccount;
    }
    
    public void setDisableAccount(String disableAccount) {
        this.disableAccount = disableAccount;
    }
    
    public String getEmailAddress() {
        return emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLoginAccount() {
        return loginAccount;
    }
    
    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }
    
    public String getGroupNames() {
        return groupNames;
    }
    
    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }
    
    public String getBeBookedNotify() {
        return beBookedNotify;
    }
    
    public void setBeBookedNotify(String beBookedNotify) {
        this.beBookedNotify = beBookedNotify;
    }
    
    public String[] splitGroupName(){
        String[] groupName = null;
        String reg = ",";
        if( StringUtils.contains(groupNames, reg)){
            groupName = groupNames.split(reg);
        }
        reg=";";
        if( StringUtils.contains(groupNames, reg)) {
            groupName = groupNames.split(reg);
        }
        if( groupName == null && !StringUtils.isEmpty(groupNames) ){
            groupName = new String[1];
            groupName[0] = groupNames;
        }
        return groupName;
    }
    
    private boolean checkYesOrTrue(String s){
        boolean flag = false;
        if( !StringUtils.isEmpty(s) && ( StringUtils.startsWithIgnoreCase(s,"Y") || StringUtils.startsWithIgnoreCase(s,"T") ) ){
            flag = true;
        }
        return flag;
    }
    
    public boolean checkDisableAccount(){
        return checkYesOrTrue(disableAccount);
    }
    
    public boolean checkBookingNotify(){
        return checkYesOrTrue(bookingNotify);
    }
    
    public boolean checkBeBookedgNotify(){
        return checkYesOrTrue(beBookedNotify);
    }
    
    public boolean checkNotifyAccessLog(){
        return checkYesOrTrue(notifyAccessLog);
    }
    
    public String getFactoryNumber(){
        String factoryNumber = null;
        if( !StringUtils.isEmpty(factoryFullName) ){
            int start = this.factoryFullName.indexOf("(");
            int end = this.factoryFullName.indexOf(")");
            if( start >0 && end > 0){
                factoryNumber = factoryFullName.substring(start+1, end);
            }
        }
        return factoryNumber;
    }
    
    public String getFactorycodes() {
        return factorycodes;
    }
    
    public void setFactorycodes(String factorycodes) {
        this.factorycodes = factorycodes;
    }
    public boolean isIsError() {
        return isError;
    }
    
    public void setIsError(boolean isError) {
        this.isError = isError;
    }
    public boolean isIsImported() {
        return isImported;
    }

    public void setIsImported(boolean isImported) {
        this.isImported = isImported;
    }    
    
    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }
    //</editor-fold>
}
