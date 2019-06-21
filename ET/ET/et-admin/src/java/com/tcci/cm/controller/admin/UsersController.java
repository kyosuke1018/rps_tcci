/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.model.admin.UserEditVO;
import com.tcci.cm.model.admin.UsersCriteriaVO;
import com.tcci.cm.model.admin.UsersVO;
import com.tcci.cm.model.admin.WebCSEmpVO;
import com.tcci.cm.model.admin.WebCSEmpVODataModel;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.model.interfaces.IPresentationVO;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcGroupComparator;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "usersController")
@ViewScoped
public class UsersController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 3;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private UserFacade usersFacade;
    @EJB private TcGroupFacade tcGroupFacade;
    
    private List<SelectItem> tcGroups;// 系統群組
    private List<SelectItem> tcGroupsMultiSelect;// 系統群組多選

    // 查詢條件
    private UsersCriteriaVO criteriaVO;
    
    // 結果
    private BaseLazyDataModel<UsersVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<UsersVO> filterResultList; // datatable filter 後的結果
    
    // 編輯
    private TcUser editUsers;
    private UserEditVO editUserVO;
    private List<String> selectedGroups;
    private boolean editMode = false;

    // WebCS 匯入人員
    private String selectedUserTxt;
    private List<UsersVO> allUserList; // 可選取全部User 
    private String importTxt;
    private WebCSEmpVODataModel webCSEmpVODataModel;
    private List<WebCSEmpVO> importUserList; 
    private WebCSEmpVO[] selectedImportUsers;
    
    // 檢視權限
    //private List<UserRuleCmnRpVO> permissionList;
    
    private List<TcGroup> tcGroupList;
    
    private boolean defPlantUser; // 預設為廠端使用者
    private UsersVO viewUserVO;
    private String rptTypeCode;
    
    private boolean removePermission = false;// 設定使用者無效時，同時刪除權限關聯。

    // for Copy Permission
    private List<UsersVO> allCopyUserList; // 可選取全部User 
    private UsersVO desUser;
    private boolean appendOnly;
    
    private StreamedContent downloadFile;
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        
        criteriaVO = new UsersCriteriaVO();
        editUsers = new TcUser();
        editUserVO = new UserEditVO();
        selectedGroups = new ArrayList<String>();

        // Get view Id
        viewId = JsfUtils.getViewId();
        
        fetchInputParameters();
        
        tcGroups = buildGroupOptions(true);
        tcGroupsMultiSelect = buildGroupOptions(false);
        // default query
        defQuery();
    }
    
    /**
     * 取得輸入參數
     */
    private void fetchInputParameters() {
        // 回饋
        String loginAccount = JsfUtils.getRequestParameter("loginAccount");
        logger.debug("loginAccount = " + loginAccount);
        
        if( loginAccount!=null ){
            try{
                criteriaVO.setKeyword(loginAccount);
            }catch(NumberFormatException e){
                // ignore
            }
        }
    }
        
    /**
     * 系統角色群組選單
     * @param includeNoSelect
     * @return 
     */
    private List<SelectItem> buildGroupOptions(boolean includeNoSelect){
        List<SelectItem> options = new ArrayList<SelectItem>();
        if( includeNoSelect ){
            options.add(new SelectItem(Long.valueOf(0), "---"));
        }
        
        List<TcGroup> groupList = tcGroupFacade.findAll();
        // 排序 by code
        TcGroupComparator tcGroupComparator = new TcGroupComparator();
        Collections.sort(groupList, tcGroupComparator);
        
        if( groupList!=null ){
            for (TcGroup group : groupList) {
                if (includeNoSelect) {
                    options.add(new SelectItem(group.getId(), group.getCode() + "-" + group.getName()));
                } else {
                    options.add(new SelectItem(group.getId(), group.getCode() + "-" + group.getName()));
                }
            }
        }
        return options;
    }
    
    /**
     * default query
     */
    public void defQuery(){
        // criteriaVO.setGroup(GlobalConstant.UG_ADMINISTRATORS_ID); // ADMINISTRATORS
        doQuery();
    }
    
    /**
     * 查詢參數檢核
     * @return 
     */
    public boolean doCheck(){
        if( criteriaVO.getKeyword()!=null ){
            criteriaVO.setKeyword(criteriaVO.getKeyword().trim());
        }
        return true;
    }
    
    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");
        if( !doCheck() ){
            return;
        }
        
        resetDataTable();
        criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數

        try {
            List<UsersVO> resList = usersFacade.findUsersByCriteria(criteriaVO);
            lazyModel = new BaseLazyDataModel<UsersVO>(resList);
        }catch(Exception e){
            String msg = ExceptionHandlerUtils.getSimpleMessage("查詢失敗:", e);
            logger.error(msg);
            JsfUtils.addErrorMessage(msg);
        }
    }
    
    /**
     * 重設表單、結果
     */
    public void doReset(){
        logger.debug("doReset ...");
        if( lazyModel!=null ){
            lazyModel.reset();
        }
        
        criteriaVO.reset();
        resetDataTable();
    }
    
    /**
     * 移除 datatable 目前排序、filter 效果
     */
    public void resetDataTable(){
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
    /**
     * 設定使用者無效時，同時刪除權限關聯。
     */
    public void changeRemovePermission(){
        logger.debug("changeRemovePermission ... removePermission =" + removePermission);
    }
    
    /**
     * 開始編輯
     * @param usersVO 
     */
    public void edit(UsersVO usersVO){
        logger.debug("=== edit === ");
        
        selectedGroups = new ArrayList<String>();

        // 取出原 USER 資料
        editUsers = usersFacade.find(usersVO.getId());
        //user基本資訊編輯
        editUserVO = new UserEditVO();
        if(editUsers != null){
            editUserVO.setLoginAccount(editUsers.getLoginAccount());
            editUserVO.setEnabled(!editUsers.getDisabled());
            editUserVO.setCname(editUsers.getCname());
            editUserVO.setEmpId(editUsers.getEmpId());
            editUserVO.setEmail(editUsers.getEmail());
            editUserVO.setId(editUsers.getId());
            editUserVO.setUserOrg(usersVO.getUserOrg());
        }

        fetchGroupList(editUsers);// user group

        editMode = true;
    }
    
    /**
     * 群組選取狀態
     * @param editUser
     */
    public void prepareSelectedGroupInfo(TcUser editUser){
        if( editUsers!=null && editUsers.getTcUsergroupCollection()!=null ){
            for(TcUsergroup tcUsergroup : editUsers.getTcUsergroupCollection()){
                //if( showCmnRptGroup ){
                    selectedGroups.add(tcUsergroup.getGroupId().getId().toString());
                /*}else{
                    if( tcUsergroup.getGroupId().getCode().startsWith(GlobalConstant.UG_CMNRPT_PREFIX) ){
                        selectedCmnRptGroups.add(tcUsergroup.getGroupId().getId().toString());
                    }else{
                        selectedGroups.add(tcUsergroup.getGroupId().getId().toString());
                    }
                }*/
            }
        }
     }
    
    /**
     * 群組下拉選單
     * @param editUser
     */
    public void fetchGroupList(TcUser editUser){
        // tcGroupList = userGroupFacade.fetchGroupList(showCmnRptGroup, showFBGroupOnly);
        tcGroupList = userGroupFacade.findAll();
        prepareSelectedGroupInfo(editUser);
    }

    public void selectedUserGroup(){
        logger.debug("selectedUserGroup selectedGroups = " + selectedGroups);
    }
    
    /**
     * 開始新增
     */
    public void add(){
        logger.debug("=== add === ");
        editUsers = new TcUser();
        editUserVO = new UserEditVO();
        selectedGroups = new ArrayList<String>();

        // 新增 user 預設為 CSRC_USER
        //TcGroup tcGroup = tcGroupFacade.findGroupByCode(GlobalConstant.UG_CSRC_USER);
        //if( tcGroup!=null ){
        //    selectedGroups.add(tcGroup.getId().toString());// 預設勾選
        //}
        
        fetchGroupList(editUsers);// user group
        
        editMode = true;
        defPlantUser = true;
    }
    
    /**
     * 儲存 
     */
    //public void save(ActionEvent event) {    
    public void save() {
        logger.info("=== save === ");
        RequestContext context;
        
        if( editUsers==null || editUserVO==null ){
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "editUsers is null!");
            return;
        }
        logger.debug("=== editUsers = " + editUsers.toString());
        
        boolean disabledORI = (editUsers.getDisabled()==null)? false:editUsers.getDisabled();
        editUsers.setLoginAccount(editUserVO.getLoginAccount());
        editUsers.setDisabled(!editUserVO.isEnabled());
        editUsers.setCname(editUserVO.getCname());
        editUsers.setEmpId(editUserVO.getEmpId());
        editUsers.setEmail(editUserVO.getEmail());
        editUsers.setId(editUserVO.getId());

        ActivityLogEnum acEnum = (editUserVO.getId()==0)?ActivityLogEnum.A_USER:ActivityLogEnum.U_FUNC_PERMISSION;
        acEnum = (!disabledORI && editUsers.getDisabled()!=null && editUsers.getDisabled())? ActivityLogEnum.D_USER:acEnum;
        try{
            
            // 儲存(同時移除多餘廠別關聯設定)
            usersFacade.save(editUsers, selectedGroups, removePermission, this.getLoginUser(), this.isSimulated());
            
            editMode = false;
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editUsers.getId(), 
                    editUsers.getLoginAccount(), editUsers.getName(), true, this.getLoginUser(), this.isSimulated());

            if( editUserVO.getId()==0 ){// 新增
                this.criteriaVO.setKeyword(editUserVO.getLoginAccount());
            }
            doQuery();

            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editUsers.getId(), 
                    editUsers.getLoginAccount(), editUsers.getName(), false, this.getLoginUser(), this.isSimulated());
            
            logger.error("save Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    public void changeDisabled(){
        logger.debug("changeDisabled..."+editUserVO.isEnabled());
    }
    
    /**
     * 刪除
     * @param usersVO 
     */
    public void delete(IPresentationVO usersVO){
        logger.debug("=== delete usersVO = "+usersVO.getId());
        
        TcUser user = usersFacade.find(usersVO.getId());
        try{
            // usersFacade.remove(user);
            usersFacade.delete(user, removePermission, this.getLoginUser(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.D_USER, viewId, JsfUtils.getClientIP(), 
                    user.getId(), user.getLoginAccount(), user.getName(), true, this.getLoginUser(), this.isSimulated());

            doQuery();
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.D_USER, viewId, JsfUtils.getClientIP(), 
                    user.getId(), user.getLoginAccount(), user.getName(), false, this.getLoginUser(), this.isSimulated());
            
            logger.error("delete Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    /**
     * 開始編輯
     * @param usersVO 
     */
    public void editUserOrg(IPresentationVO usersVO){
        logger.debug("===editUserOrg==="+usersVO.getId());
        editUsers = usersFacade.find(usersVO.getId());
    }
    
    /**
     * 已選取 Group
     * @return 
     */
    public List<TcGroup> getSelectedGroupList(){
        if( tcGroupList==null ){
            return null;
        }
        List<TcGroup> selectedList = new ArrayList<TcGroup>();
        for(TcGroup tcGroup:tcGroupList){
            if( selectedGroups!=null && selectedGroups.contains(tcGroup.getId().toString()) ){
                selectedList.add(tcGroup);
            }
        }
        return selectedList;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for copy permission">
    /**
     * 開啟複製權限對話框
     * @param user 
     */
    public void openCopyPermissionDlg(UsersVO user){
        logger.debug("copyPermission ...");
        desUser = user;
        selectedUserTxt = "";
        appendOnly = true;
        allCopyUserList = usersFacade.findUserForCopy(user);

        if( allCopyUserList==null || allCopyUserList.isEmpty() ){
            JsfUtils.addErrorMessage("無可複製的人員!");
        }
    }
    
    /**
     * 選取複製權限來源人 autoComplete 使用者列表
     * @param intxt
     * @return 
     */
    public List<String> autoCompleteCopyUserOptions(String intxt){
        List<String> resList = new ArrayList<String>();
        
        for(UsersVO user : allCopyUserList){// 有權選取的所有User
            String txt = user.getCname() + "(" + user.getLogin_account() + ")";
            if( txt.toUpperCase().indexOf(intxt.toUpperCase()) >= 0 ){// 符合輸入
                resList.add(txt);
            }
        }
        
        return resList;
    }  
    
    
    /**
     * 複製權限
     */
    public void selectCopyUser(){
        TcUser tcUser = new TcUser();
        for(UsersVO user : allCopyUserList){// 有權選取的所有User
            String txt = user.getCname() + "(" + user.getLogin_account() + ")";
            if( txt.toUpperCase().indexOf(selectedUserTxt.toUpperCase()) >= 0 ){// 符合輸入
                tcUser.setId(user.getId());
                tcUser.setCname(user.getCname());
                tcUser.setEmail(user.getEmail());
                tcUser.setEmpId(user.getEmp_id());
                tcUser.setLoginAccount(user.getLogin_account());
                tcUser.setDisabled(false);
            }
        }
        // check
        if( tcUser.getLoginAccount()==null || tcUser.getLoginAccount().isEmpty() ){
            JsfUtils.addErrorMessage("無符合輸入的人員!");
            return;
        }
        
        try{
            // 儲存匯入人
            usersFacade.copyPermission(tcUser.getId(), desUser.getId(), appendOnly, this.getLoginUser(), this.isSimulated());            

            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_FUNC_PERMISSION, viewId, JsfUtils.getClientIP(), desUser.getId(), 
                    desUser.getLogin_account(), desUser.getCname(), true, this.getLoginUser(), this.isSimulated());
            
            // 顯示該帳號
            doReset();
            this.criteriaVO.setKeyword(desUser.getLogin_account());
            doQuery();
            
            JsfUtils.addSuccessMessage("權限已複製完成。");
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_FUNC_PERMISSION, viewId, JsfUtils.getClientIP(), desUser.getId(), 
                    desUser.getLogin_account(), desUser.getCname(), false, this.getLoginUser(), this.isSimulated());
            logger.error("selectUser exception:\n", e);
            JsfUtils.addErrorMessage("權限複製失敗!");
        }
    }            
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Import Users from WebCS Table">
    /**
     * 開啟選取匯入人對話框
     */
    public void openImpUserDlg(){
        selectedUserTxt = "";
        if( allUserList==null ){
            allUserList = usersFacade.findUserForImport();
        }
        if( allUserList==null || allUserList.isEmpty() ){
            JsfUtils.addErrorMessage("無可匯入的人員!");
        }
    }
    
    /**
     * 選取收件人 autoComplete 使用者列表
     * @param intxt
     * @return 
     */
    public List<String> autoCompleteUserOptions(String intxt){
        List<String> resList = new ArrayList<String>();
        
        for(UsersVO user : allUserList){// 有權選取的所有User
            String txt = user.getCname() + "(" + user.getLogin_account() + ")";
            if( txt.toUpperCase().indexOf(intxt.toUpperCase()) >= 0 ){// 符合輸入
                resList.add(txt);
            }
        }
        
        return resList;
    }
    
    /**
     * 匯入選取USER
     */
    public void selectUser(){
        TcUser tcUser = new TcUser();
        for(UsersVO user : allUserList){// 有權選取的所有User
            String txt = user.getCname() + "(" + user.getLogin_account() + ")";
            if( txt.toUpperCase().indexOf(selectedUserTxt.toUpperCase()) >= 0 ){// 符合輸入
                tcUser.setCname(user.getCname());
                tcUser.setEmail(user.getEmail());
                tcUser.setEmpId(user.getEmp_id());
                tcUser.setLoginAccount(user.getLogin_account());
                tcUser.setDisabled(false);
            }
        }
        // check
        if( tcUser.getLoginAccount()==null || tcUser.getLoginAccount().isEmpty() ){
            JsfUtils.addErrorMessage("無符合輸入的人員!");
            return;
        }else{
            List<TcUser> existsUsers = usersFacade.findByLoginAccount(tcUser.getLoginAccount());
            if( existsUsers!=null && !existsUsers.isEmpty() ){
                JsfUtils.addErrorMessage("匯入失敗! 帳號 "+tcUser.getLoginAccount()+" 已存在!");
                return;
            }
        }
        
        try{
            // 儲存匯入人
            usersFacade.save(tcUser, this.getLoginUser(), this.isSimulated());            
            //if( tcUser.getEmail()!=null && tcUser.getEmail().endsWith(GlobalConstant.CSRC_DOMAIN) ){
                // 新增 預設為CSRC_USER
            //    userGroupFacade.setDefUserGroup(tcUser, this.getLoginUser(), this.isSimulated());
            //}
            
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.A_USER, viewId, JsfUtils.getClientIP(), tcUser.getId(), 
                    tcUser.getLoginAccount(), tcUser.getCname(), true, this.getLoginUser(), this.isSimulated());
            // 顯示該帳號
            doReset();
            this.criteriaVO.setKeyword(tcUser.getLoginAccount());
            doQuery();
            
            JsfUtils.addSuccessMessage("已匯入 ["+tcUser.getDisplayIdentifier()+"]，請記得執行後續[群組]權限設定!");
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.A_USER, viewId, JsfUtils.getClientIP(), tcUser.getId(), 
                    tcUser.getLoginAccount(), tcUser.getCname(), false, this.getLoginUser(), this.isSimulated());
            logger.error("selectUser exception:\n", e);
            JsfUtils.addErrorMessage("匯入失敗! (請檢查該人員是否已存在!)");
        }
    }
    
    /**
     * 開啟 自 WebCS 匯入多筆人員 對話框
     */
    public void openImpMultiUsersDlg(){
        logger.debug("openImpMultiUsersDlg ...");
        
        clearImpMultiUsers();
    }
    
    public void clearImpMultiUsers(){
        this.importTxt = "";
        this.selectedImportUsers = null;
        this.importUserList = null;
    }
    
    /**
     * 多筆匯入
     */
    public void doImport(){
        logger.debug("doImport ...importTxt = \n"+importTxt);
        
        if( this.importTxt==null || this.importTxt.isEmpty() ){
            JsfUtils.addErrorMessage("未輸入任何帳號!");
            return;
        }
        
        String[] rows = importTxt.split("\n");
        if( rows==null ){
            logger.info("doImport 輸入錯誤 :\n"+importTxt);
            return;
        }
        
        // 匯入資訊
        importUserList = new ArrayList<WebCSEmpVO>();
        List<String> acaccounts = new ArrayList<String>();
        for(String row : rows){
            String[] items = row.split(";");
            if( items!=null ){// 1
                String adaccount = (items.length>=1)? items[0].trim():"";
                String groups = (items.length>=2)? items[1].trim():"";
                List<String> groupList = new ArrayList<String>();
                
                if( !adaccount.isEmpty() ){// 2
                    if( !groups.isEmpty() ){// 3
                        String[] groupCodes = groups.split(",");
                        if( groupCodes!=null ){
                            groupList = new ArrayList<String>();
                            for(String groupCode : groupCodes){
                                if( !groupCode.trim().isEmpty() ){
                                    groupList.add(groupCode);
                                }
                            }
                        }
                    }// end of if // 3
                    
                    adaccount = adaccount.toLowerCase(); // 轉小寫
                    
                    WebCSEmpVO webCSEmpVO = new WebCSEmpVO();
                    webCSEmpVO.setAdaccount(adaccount);
                    webCSEmpVO.setGroups(groups);
                    webCSEmpVO.setGroupList(groupList);
                    
                    logger.debug("adaccount = "+adaccount);
                    logger.debug("groups = "+groups);
                    acaccounts.add(adaccount);
                    importUserList.add(webCSEmpVO);
                }// end of if / 2
            }// end of if // 1
        }// end of for 
        
        logger.debug("importUserList = " + ((importUserList!=null)? importUserList.size():0));
        
        // 是否存在
        List<UsersVO> existedUsers = usersFacade.findByLoginAccounts(acaccounts);
        logger.debug("existedUsers = " + ((existedUsers!=null)? existedUsers.size():0));
        
        // WebCS 資訊
        List<WebCSEmpVO> webcsUsers = usersFacade.findByWebCSAccounts(acaccounts);
        logger.debug("webcsUsers = " + ((webcsUsers!=null)? webcsUsers.size():0));
        
        // 準備預覽資訊
        for(WebCSEmpVO webCSEmpVO : importUserList){// 1
            for(UsersVO UsersVO : existedUsers){// 是否存在
                if( webCSEmpVO.getAdaccount().equals(UsersVO.getLogin_account()) ){
                    webCSEmpVO.setExisted(true);
                    logger.debug(webCSEmpVO.getAdaccount() + " existed...");
                    break;
                }
            }
            
            for(WebCSEmpVO webCSUser : webcsUsers){// WebCS 資訊
                if( webCSEmpVO.getAdaccount().equals(webCSUser.getAdaccount()) ){
                    webCSEmpVO.setId(webCSUser.getId());
                    webCSEmpVO.setName(webCSUser.getName());
                    webCSEmpVO.setCode(webCSUser.getCode());
                    webCSEmpVO.setEmail(webCSUser.getEmail());
                    webCSEmpVO.setCompanyName(webCSUser.getCompanyName());
                    logger.debug(webCSEmpVO.getAdaccount() + " get from WebCS ...");
                    break;
                }
            }
        }// end of for // 1
        
        webCSEmpVODataModel = new WebCSEmpVODataModel(importUserList);
    }
    
    /**
     * 儲存勾選匯入項目
     */
    public void saveImport(){
        logger.debug("saveImport ...");
        
        usersFacade.tranWebCSToTcUser(selectedImportUsers, viewId, this.getLoginUser(), this.isSimulated());
        
        clearImpMultiUsers();
    }
    
    /**
     * 取消匯入
     */
    public void cancelImport(){
        logger.debug("cancelImport ...");
        
        clearImpMultiUsers();
    }
    //</editor-fold>
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public TcUser getEditUsers() {
        return editUsers;
    }

    public void setEditUsers(TcUser editUsers) {
        this.editUsers = editUsers;
    }

    public boolean isAppendOnly() {
        return appendOnly;
    }

    public void setAppendOnly(boolean appendOnly) {
        this.appendOnly = appendOnly;
    }

    public List<SelectItem> getTcGroups() {
        return tcGroups;
    }

    public void setTcGroups(List<SelectItem> tcGroups) {
        this.tcGroups = tcGroups;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public UsersVO getDesUser() {
        return desUser;
    }

    public void setDesUser(UsersVO desUser) {
        this.desUser = desUser;
    }

    public List<UsersVO> getAllCopyUserList() {
        return allCopyUserList;
    }

    public void setAllCopyUserList(List<UsersVO> allCopyUserList) {
        this.allCopyUserList = allCopyUserList;
    }

    public boolean isRemovePermission() {
        return removePermission;
    }

    public void setRemovePermission(boolean removePermission) {
        this.removePermission = removePermission;
    }

    public String getRptTypeCode() {
        return rptTypeCode;
    }

    public void setRptTypeCode(String rptTypeCode) {
        this.rptTypeCode = rptTypeCode;
    }

    public UsersVO getViewUserVO() {
        return viewUserVO;
    }

    public void setViewUserVO(UsersVO viewUserVO) {
        this.viewUserVO = viewUserVO;
    }

    public boolean isDefPlantUser() {
        return defPlantUser;
    }

    public void setDefPlantUser(boolean defPlantUser) {
        this.defPlantUser = defPlantUser;
    }

    public List<TcGroup> getTcGroupList() {
        return tcGroupList;
    }

    public void setTcGroupList(List<TcGroup> tcGroupList) {
        this.tcGroupList = tcGroupList;
    }

    public String getImportTxt() {
        return importTxt;
    }

    public void setImportTxt(String importTxt) {
        this.importTxt = importTxt;
    }

    public List<WebCSEmpVO> getImportUserList() {
        return importUserList;
    }

    public void setImportUserList(List<WebCSEmpVO> importUserList) {
        this.importUserList = importUserList;
    }

    public String getSelectedUserTxt() {
        return selectedUserTxt;
    }

    public void setSelectedUserTxt(String selectedUserTxt) {
        this.selectedUserTxt = selectedUserTxt;
    }

    public List<UsersVO> getAllUserList() {
        return allUserList;
    }

    public void setAllUserList(List<UsersVO> allUserList) {
        this.allUserList = allUserList;
    }

    public BaseLazyDataModel<UsersVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<UsersVO> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    public List<UsersVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<UsersVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public UsersCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(UsersCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public List<String> getSelectedGroups() {
        return selectedGroups;
    }

    public void setSelectedGroups(List<String> selectedGroups) {
        this.selectedGroups = selectedGroups;
    }

    public WebCSEmpVO[] getSelectedImportUsers() {
        return selectedImportUsers;
    }

    public void setSelectedImportUsers(WebCSEmpVO[] selectedImportUsers) {
        this.selectedImportUsers = selectedImportUsers;
    }

    public WebCSEmpVODataModel getWebCSEmpVODataModel() {
        return webCSEmpVODataModel;
    }

    public void setWebCSEmpVODataModel(WebCSEmpVODataModel webCSEmpVODataModel) {
        this.webCSEmpVODataModel = webCSEmpVODataModel;
    }

    public List<SelectItem> getTcGroupsMultiSelect() {
        return tcGroupsMultiSelect;
    }

    public void setTcGroupsMultiSelect(List<SelectItem> tcGroupsMultiSelect) {
        this.tcGroupsMultiSelect = tcGroupsMultiSelect;
    }

    public UserEditVO getEditUserVO() {
        return editUserVO;
    }

    public void setEditUserVO(UserEditVO editUserVO) {
        this.editUserVO = editUserVO;
    }
    //</editor-fold>

}
