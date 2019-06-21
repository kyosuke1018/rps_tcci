/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.et.controller;

import com.tcci.cm.enums.SecurityRoleEnum;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.et.enums.AuthLevelEnum;
import com.tcci.et.entity.EtMember;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Peter.pan
 */
abstract public class SessionAwareController extends AbstractController {
    
    @ManagedProperty(value="#{etSessionController}")
    protected EtSessionController sessionController;
    
    protected boolean accessDenied = true;// 資料權限(預設禁止)
    protected boolean functionDenied = true;// 功能權限(預設禁止)
    protected boolean internetDenied = true;// 外網權限(預設禁止)
    
    protected boolean needCheckFactoryPermission = true; // 是否需依廠別權限檢核
    protected boolean readOnly;// 只有檢視權

    @PostConstruct
    private void init(){
        // Get view Id
        viewId = JsfUtils.getViewId();
        logger.debug("SessionAwareController init viewId = "+viewId);
        
    }
        
    /**
     * 模擬使用者
     * @return 
     */
    public boolean isSimulated(){
        return (this.sessionController!=null && this.sessionController.isSimulated());
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public EtSessionController getSessionController() {
        return sessionController;
    }
    
    public void setSessionController(EtSessionController sessionController) {
        this.sessionController = sessionController;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isInternetDenied() {
        return internetDenied;
    }

    public void setInternetDenied(boolean internetDenied) {
        this.internetDenied = internetDenied;
    }
    
    /*public List<SelectItem> getMyFactorys() {
        return myFactorys;
    }
    
    public void setMyFactorys(List<SelectItem> myFactorys) {
        this.myFactorys = myFactorys;
    }*/
    
    public boolean isNeedCheckFactoryPermission() {
        return needCheckFactoryPermission;
    }
    
    public void setNeedCheckFactoryPermission(boolean needCheckFactoryPermission) {
        this.needCheckFactoryPermission = needCheckFactoryPermission;
    }
    
    public EtMember getLoginUser(){
        return this.sessionController.getLoginMember();
    }
    
    public String getLoginUserCode(){
        return this.sessionController.getLoginUser();
    }
    
    public Long getLoginUserId(){
        return this.sessionController.getLoginMember().getId();
    }
    
    public boolean isAccessDenied() {
        return accessDenied;
    }
    
    public void setAccessDenied(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }
    
    public boolean isFunctionDenied() {
        return functionDenied;
    }
    
    public void setFunctionDenied(boolean functionDenied) {
        this.functionDenied = functionDenied;
    }
    
    /*public String getSelectedRptFactory() {
        return selectedRptFactory;
    }
    
    public void setSelectedRptFactory(String selectedRptFactory) {
        this.selectedRptFactory = selectedRptFactory;
    }*/
    
    /*public Map<String, List<CmFactoryGroupVO>> getSpecGroupFacoryMap() {
        if( sessionController==null ){
            return null;
        }
        return sessionController.getSpecGroupFacoryMap();
    }*/
    //</editor-fold>
}
