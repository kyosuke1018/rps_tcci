/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.controller.global;

import com.tcci.cm.enums.SecurityRoleEnum;
import com.tcci.cm.facade.admin.CmActivityLogFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.facade.admin.CmFactorygroupFacade;
import com.tcci.cm.facade.admin.CmUserfactoryFacade;
import com.tcci.cm.facade.admin.PermissionFacade;
import com.tcci.cm.facade.admin.UserGroupFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.et.enums.AuthLevelEnum;
import com.tcci.et.model.admin.MenuFunctionVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Peter.pan
 */
abstract public class SessionAwareController extends AbstractController {
    @EJB protected PermissionFacade permissionFacade;
    @EJB protected UserGroupFacade userGroupFacade;
    @EJB protected CmActivityLogFacade cmActivityLogFacade;
    @EJB protected CmFactorygroupFacade cmFactorygroupFacade;
    @EJB protected CmUserfactoryFacade cmUserfactoryFacade;
    @EJB protected CmFactoryFacade cmFactoryFacade;
    
    @ManagedProperty(value="#{tcSessionController}")
    protected TcSessionController sessionController;
    
    protected ResourceBundle msgApp = ResourceBundle.getBundle("messages",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    protected boolean accessDenied = true;// 資料權限(預設禁止)
    protected boolean functionDenied = true;// 功能權限(預設禁止)
    protected boolean internetDenied = true;// 外網權限(預設禁止)
    
    protected boolean needCheckFactoryPermission = true; // 是否需依廠別權限檢核
    protected List<CmFactoryVO> factoryListPermission;// 有權限存取廠別 (一般為 sessionController.findCmFactoryByLoginUser())
    protected List<CmCompanyVO> companyListPermission;// 有權限公司別
    protected boolean readOnly;// 只有檢視權
    
    protected Locale locale;

    @PostConstruct
    private void init(){
        // Get view Id
        viewId = JsfUtils.getViewId();
        logger.debug("SessionAwareController init viewId = "+viewId);
        
        // for 依 viewId 檢查功能授權
        if( !checkAuthorizedByViewId(viewId) ){
            return;
        }
        
        checkReadOnly(viewId);// 只有檢視權判斷
        
        // 有權限存取廠別
        companyListPermission = permissionFacade.findCmCompanyByLoginUser(this.getLoginUser());
        factoryListPermission = permissionFacade.findCmFactoryByLoginUser(this.getLoginUser());
        
        logger.debug("SessionAwareController init factoryListPermission = "+((factoryListPermission==null)?0:factoryListPermission.size()));
    }
    
    public List<Long> getMyCompanyIds(){
        List<Long> list = new ArrayList<Long>();
        if( companyListPermission!=null ){
            for(CmCompanyVO vo : companyListPermission){
                list.add(vo.getId());
            }
        }
        return list;
    }
    
    public List<Long> getMyFactoryIds(){
        List<Long> list = new ArrayList<Long>();
        if( factoryListPermission!=null ){
            for(CmFactoryVO vo : factoryListPermission){
                list.add(vo.getId());
            }
        }
        return list;
    }
    
    /**
     * 模擬使用者
     * @return 
     */
    public boolean isSimulated(){
        return (this.sessionController!=null && this.sessionController.isSimulated());
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
     * 是否是總處物料窗口
     * @param user
     * @return 
     */
    public boolean isAdministrators(TcUser user){
        return permissionFacade.inUserGroup(user, SecurityRoleEnum.ADMINISTRATORS.getCode());
    }
    
    /**
     * 只有檢視權 
     * @param wiewId
     */
    public void checkReadOnly(String wiewId){
        if( sessionController.getFunctions()!=null ){
            for(MenuFunctionVO vo : sessionController.getFunctions()){
                if( vo.getUrl()!=null && vo.getUrl().indexOf(wiewId) > 0 ){
                    if( AuthLevelEnum.VIEW==AuthLevelEnum.getFromCode(vo.getAuth()) ){
                        this.readOnly = true;
                    }
                }
            }
        }
    }
    
    protected void redirect(String outcome) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getFlash().setKeepMessages(true);
        try {
            context.redirect(outcome);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public TcSessionController getSessionController() {
        return sessionController;
    }
    
    public void setSessionController(TcSessionController sessionController) {
        this.sessionController = sessionController;
    }

    public ResourceBundle getMsgApp() {
        return msgApp;
    }

    public void setMsgApp(ResourceBundle msgApp) {
        this.msgApp = msgApp;
    }

    public List<CmFactoryVO> getFactoryListPermission() {
        return factoryListPermission;
    }

    public void setFactoryListPermission(List<CmFactoryVO> factoryListPermission) {
        this.factoryListPermission = factoryListPermission;
    }

    public List<CmCompanyVO> getCompanyListPermission() {
        return companyListPermission;
    }

    public void setCompanyListPermission(List<CmCompanyVO> companyListPermission) {
        this.companyListPermission = companyListPermission;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
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

    public boolean isNeedCheckFactoryPermission() {
        return needCheckFactoryPermission;
    }
    
    public void setNeedCheckFactoryPermission(boolean needCheckFactoryPermission) {
        this.needCheckFactoryPermission = needCheckFactoryPermission;
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
    //</editor-fold>
}
