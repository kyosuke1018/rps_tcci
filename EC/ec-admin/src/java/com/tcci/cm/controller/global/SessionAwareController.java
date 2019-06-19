/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.controller.global;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.enums.SecurityRoleEnum;
import com.tcci.cm.facade.admin.CmActivityLogFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.facade.admin.CmFactorygroupFacade;
import com.tcci.cm.facade.admin.CmUserfactoryFacade;
import com.tcci.cm.facade.admin.PermissionFacade;
import com.tcci.cm.facade.admin.UserGroupFacade;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.admin.CmFactoryGroupVO;
import com.tcci.cm.model.interfaces.IOperator;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.fc.entity.org.TcUser;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Peter.pan
 */
abstract public class SessionAwareController extends AbstractController {
    @EJB protected SysResourcesFacade sys;
    @EJB protected PermissionFacade permissionFacade;
    @EJB protected UserGroupFacade userGroupFacade;
    @EJB protected CmUserfactoryFacade cmUserfactoryFacade;
    @EJB protected CmFactorygroupFacade cmFactorygroupFacade;
    @EJB protected CmFactoryFacade cmFactoryFacade;
    @EJB protected CmActivityLogFacade activityLogFacade;
    
    @ManagedProperty(value="#{tcSessionController}")
    protected TcSessionController sessionController;
    
    protected boolean accessDenied = true;// 資料權限(預設禁止)
    protected boolean functionDenied = true;// 功能權限(預設禁止)
    protected boolean internetDenied = true;// 外網權限(預設禁止)
    
    protected boolean needCheckFactoryPermission = true; // 是否需依廠別權限檢核
    protected List<CmFactory> factoryListPermission; // 有權限存取廠別 (一般為 sessionController.findCmFactoryByLoginUser())
    
    protected Locale locale;
    
    @PostConstruct
    private void init(){
        // Get view Id
        viewId = JsfUtils.getViewId();
        locale = JsfUtils.getRequestLocale();
        logger.debug("SessionAwareController init viewId = "+viewId+", locale = "+locale);
        
        // for 依 viewId 檢查功能授權
        if( !checkAuthorizedByViewId(viewId) ){
            return;
        }
        
        // 有權限存取廠別
        factoryListPermission = permissionFacade.findCmFactoryByLoginUser(this.getLoginUser());
        logger.debug("SessionAwareController init factoryListPermission = "+((factoryListPermission==null)?0:factoryListPermission.size()));
    }
    
    public EcMember getLoginMember(){
        return (this.sessionController!=null)?sessionController.getMember():null;
    }
      
    public Long getLoginMemberId(){
        EcMember member = getLoginMember();
        return member!=null?member.getId():null;
    }
    
    public void processUnknowException(IOperator operator, String methodName, Exception e, boolean callback){
        processUnknowException(operator, methodName, e, callback, null);
    }
    public void processUnknowException(IOperator operator, String methodName, Exception e, boolean callback, String msg){
        sys.processUnknowException(this.getLoginUser(), methodName, e);
        if( callback ){
            JsfUtils.buildErrorCallback(msg);
        }
        if( msg!=null ){
            JsfUtils.addErrorMessage(msg);
        }
    }    

    /**
     * 外網權限檢核 :
     * 依 viewId 檢查功能授權 (true:已授權;  false:未授權)
     * 搭配前端 include src="/inc/inc_authorizationDlg.xhtml" 顯示提示訊息
     * @param viewId
     * @return
     */
    public boolean checkInternetByViewId(String viewId){
        logger.debug("checkInternetByViewId viewId = "+viewId);
        if( sessionController!=null ){
            internetDenied = sessionController.isFromInternet() && viewId.indexOf("/internet/")<0;
        }else{
            logger.error("checkInternetByViewId error sessionController == null ");
        }
        
        logger.debug("internetDenied = "+internetDenied);
        return !internetDenied;
    }
    
    /**
     * 功能權限檢核 :
     * 依 viewId 檢查功能授權 (true:已授權;  false:未授權)
     * 搭配前端 include src="/inc/inc_authorizationDlg.xhtml" 顯示提示訊息
     * @param viewId
     * @return
     */
    public boolean checkAuthorizedByViewId(String viewId){
        logger.debug("isAuthorizedByViewId viewId = "+viewId);
        
        // 先做外網權限檢核
        functionDenied = !checkInternetByViewId(viewId);
        
        if( !functionDenied ){
            if( sessionController!=null ){
                functionDenied = !permissionFacade.checkAuthorizationByViewId(sessionController.getFunctions(), viewId);
            }else{
                logger.error("checkAuthorizedByViewId error sessionController == null ");
            }
        }
        
        logger.debug("functionDenied = "+functionDenied);
        return !functionDenied;
    }
    
    /**
     * 資料權限檢核 :
     * 搭配前端最外層 form 加上 rendered="#{!controller.accessDenied}"
     * override in controller
     * @return
     */
    public boolean checkDataPermission(){
        return !accessDenied;
    }
    
    /**
     * 檢核指定廠權限
     * @param plantId
     * @return 
     */
    public boolean checkFactoryPermission(long plantId){
        if( this.factoryListPermission==null ){
            return false;
        }
        for(CmFactory cmFactory : factoryListPermission){
            if( cmFactory.getId()==plantId ){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 是否是廠端業務人員
     * @param user
     * @return 
     */
    public boolean isPlantSales(TcUser user){
        return permissionFacade.inUserGroup(user, SecurityRoleEnum.PLANT_SALES.getCode());
    }
    /**
     * 是否是ADMINISTRATORS
     * @param user
     * @return 
     */
    public boolean isAdministrators(TcUser user){
        return permissionFacade.inUserGroup(user, SecurityRoleEnum.ADMINISTRATORS.getCode());
    }
    public boolean isAdministrator(){
        return isAdministrators(this.getLoginUser());
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public TcSessionController getSessionController() {
        return sessionController;
    }
    
    public void setSessionController(TcSessionController sessionController) {
        this.sessionController = sessionController;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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
    
    public List<CmFactory> getFactoryListPermission() {
        return factoryListPermission;
    }
    
    public void setFactoryListPermission(List<CmFactory> factoryListPermission) {
        this.factoryListPermission = factoryListPermission;
    }
    
    public TcUser getLoginUser(){
        return this.sessionController.getLoginTcUser();
    }
    
    public String getLoginUserCode(){
        return this.sessionController.getLoginUser();
    }
    
    public Long getLoginUserId(){
        return this.sessionController.getLoginTcUser().getId();
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
    
    public Map<String, List<CmFactoryGroupVO>> getSpecGroupFacoryMap() {
        if( sessionController==null ){
            return null;
        }
        return sessionController.getSpecGroupFacoryMap();
    }
    //</editor-fold>
}
