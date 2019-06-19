/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmOrg;
import com.tcci.cm.entity.admin.CmUserOrg;
import com.tcci.cm.enums.OrgTypeEnum;
import com.tcci.cm.facade.admin.CmActivityLogFacade;
import com.tcci.cm.facade.admin.CmOrgFacade;
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
import com.tcci.cm.enums.ActivityLogEnum;
import com.tcci.cm.model.admin.OrganizationVO;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.CollectionUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "usersController")
@ViewScoped
public class UsersController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 3;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    public static final long MAX_ORG_LEVEL = 5;// 組織遞迴最多層限制
    
    @EJB private UserFacade usersFacade;
    @EJB private TcGroupFacade tcGroupFacade;
    @EJB private CmOrgFacade cmOrgFacade;
    @EJB private CmActivityLogFacade cmActivityLogFacade;

    // 查詢條件
    private UsersCriteriaVO criteriaVO;
    
    // 結果
    private BaseLazyDataModel<UsersVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<UsersVO> filterResultList; // datatable filter 後的結果
    
    // 編輯
    private TcUser editUsers;
    private UserEditVO editUserVO;
    private List<String> selectedGroups;
    private List<String> selectedOrgs;
    private boolean editMode = false;
    
    private CmOrg allOrg;// 可存取所有組織
    private Boolean allOrgCheck;  // 所有組織選單選取狀態
    private Map<Long, Boolean> firstOrgCheckMap;  // 第一層(公司)組織選取狀態
    private Map<Long, Boolean> secondOrgCheckMap;  // 第二層(部門 廠區)組織選取狀態
    private List<CmOrg> allOrgList;//所有組織
    private List<CmOrg> mainOrgList; // 第一層
    private Map<CmOrg, List<CmOrg>> subOrgMap; // 第二層 包含 組織資訊
    // tree
    private TreeNode root;
    private TreeNode[] selectedNodes;
    private boolean showOrgTree = true;
    private int selectedOrgCount = 0;

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
    private boolean showCmnRptGroup = true;// 顯示 Common Report 動態群組
    private boolean showFBGroupOnly = false;// 只顯示回饋相關群組
    private List<String> selectedCmnRptGroups;// 已選取 CmnRpt 群組
    
    private boolean defPlantUser; // 預設為廠端使用者
    private UsersVO viewUserVO;
    private String rptTypeCode;
    
    private boolean removePermission = false;// 設定使用者無效時，同時刪除權限關聯。
    // for Copy Permission
    private List<UsersVO> allCopyUserList; // 可選取全部User 
    private UsersVO desUser;
    
    private StreamedContent downloadFile;
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        
        criteriaVO = new UsersCriteriaVO();
        editUsers = new TcUser();
        editUserVO = new UserEditVO();
        selectedGroups = new ArrayList<String>();
        selectedCmnRptGroups = new ArrayList<String>();
        selectedOrgs = new ArrayList<String>();
        
        // Get view Id
        viewId = JsfUtils.getViewId();

        // 組織列表
        allOrgList = cmOrgFacade.findAll();
        buildUserOrgsTree();
        
        fetchInputParameters();
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
     * 開始編輯
     * @param usersVO 
     */
    public void edit(UsersVO usersVO){
        logger.debug("=== edit === ");
        
        showCmnRptGroup = true; // 預設顯示
        selectedGroups = new ArrayList<String>();
        selectedCmnRptGroups = new ArrayList<String>();

        // 取出原 USER 資料
        editUsers = usersFacade.find(usersVO.getId());
        //user基本資訊編輯
        editUserVO = new UserEditVO();
        if(editUsers != null){
            editUserVO.setLoginAccount(editUsers.getLoginAccount());
            editUserVO.setEnabled(!editUsers.getDisabled());
            if(null!=editUsers.getSendEmail()){
            editUserVO.setSendEmail(!editUsers.getSendEmail());
            }
            editUserVO.setCname(editUsers.getCname());
            editUserVO.setEmpId(editUsers.getEmpId());
            editUserVO.setEmail(editUsers.getEmail());
            editUserVO.setId(editUsers.getId());
        }

        fetchGroupList(editUsers);// user group
//        selectedOrgs = new ArrayList<String>();
//        if( editUsers.getCmUserOrgCollection()!=null ){
//            for(CmUserOrg cmUserOrg : editUsers.getCmUserOrgCollection()){
//                selectedOrgs.add(cmUserOrg.getOrgId().getId().toString());
//            }
//        }
        
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
    
    public void changeUserOrg(){
        logger.debug("changeUserOrg editUsers.getOrgId() = " + editUsers.getOrgId());
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
        selectedCmnRptGroups = new ArrayList<String>();

        // 新增 user 預設為 PLANT_USER
        TcGroup tcGroup = tcGroupFacade.findGroupByCode(GlobalConstant.UG_PLANT_USER);
        if( tcGroup!=null ){
            selectedGroups.add(tcGroup.getId().toString());// 預設勾選
        }
        
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
        //RequestContext context;
        
        if( editUsers==null || editUserVO==null ){
            //context = JsfUtils.buildErrorCallback();
            //context.addCallbackParam("msg", "editUsers is null!");
            JsfUtils.addErrorMessage("editUsers is null!");
            return;
        }
        logger.debug("=== editUsers = " + editUsers.toString());
        
        //所屬組織必填
        /*if(editUsers.getOrgId() == null || editUsers.getOrgId() <= 0){
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "請選擇所屬組織!");
            return;
        }*/
        
        try{
            boolean disabledORI = (editUsers.getDisabled()==null)? false:editUsers.getDisabled();
            editUsers.setLoginAccount(editUserVO.getLoginAccount());
            editUsers.setDisabled(!editUserVO.isEnabled());
            editUsers.setSendEmail(!editUserVO.isSendEmail());
            editUsers.setCname(editUserVO.getCname());
            editUsers.setEmpId(editUserVO.getEmpId());
            editUsers.setEmail(editUserVO.getEmail());
            editUsers.setId(editUserVO.getId());
            
            //selectedGroups = CollectionUtils.union(selectedGroups, selectedCmnRptGroups);// 補回 CmnRpt 群組
            // 儲存(同時移除多餘廠別關聯設定)
            usersFacade.save(editUsers, selectedGroups, removePermission, this.getLoginUser());
            
            editMode = false;
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_FUNC_PERMISSION, viewId, 
                    editUsers.getId(), editUsers.getLoginAccount(), editUsers.getName(), true, this.getLoginUser());
            // 刪除
            if( !disabledORI && editUsers.getDisabled()!=null && editUsers.getDisabled() ){
                cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.D_USER, viewId, 
                        editUsers.getId(), editUsers.getLoginAccount(), editUsers.getName(), true, this.getLoginUser());
            }

            if( editUserVO.getId()==0 ){// 新增
                this.criteriaVO.setKeyword(editUserVO.getLoginAccount());
            }
            doQuery();

            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_FUNC_PERMISSION, viewId, 
                    editUsers.getId(), editUsers.getLoginAccount(), editUsers.getName(), false, this.getLoginUser());
            
            logger.error("save Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    public void changeDisabled(){
        logger.debug("changeDisabled..."+editUsers.getDisabled());
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
            usersFacade.delete(user, removePermission, this.getLoginUser());
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.D_USER, viewId, 
                    user.getId(), user.getLoginAccount(), user.getName(), true, this.getLoginUser());

            doQuery();
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.D_USER, viewId, 
                    user.getId(), user.getLoginAccount(), user.getName(), false, this.getLoginUser());
            
            logger.error("delete Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  
    
    /**
     * 開始編輯
     * @param usersVO 
     */
    public void editUserOrg(IPresentationVO usersVO){
        logger.debug("===editUserOrg==="+usersVO.getId());
        editUsers = usersFacade.find(usersVO.getId());
        selectedOrgs = new ArrayList<String>();
        if( editUsers.getCmUserOrgCollection()!=null ){
            for(CmUserOrg cmUserOrg : editUsers.getCmUserOrgCollection()){
                selectedOrgs.add(cmUserOrg.getOrgId().getId().toString());
            }
        }
        
        buildUserOrgOrgs();
    }
    
    /**
     * 依 DB 資訊建置組織圖
     */
    private void buildUserOrgOrgs() {
        allOrg = new CmOrg();
        allOrg.setId(0L);
        allOrg.setCode("");
        allOrg.setCtype(OrgTypeEnum.GROUP.getCode());
        allOrg.setName(GlobalConstant.SYS_ORG_ROOT);
        
        allOrgCheck = false;  // 所有組織選單選取狀態
        firstOrgCheckMap = new HashMap<Long, Boolean>();  // 第一層(公司)組織選取狀態
        secondOrgCheckMap = new HashMap<Long, Boolean>();// 第二層(部門 廠區)組織選取狀態
        
        mainOrgList = new ArrayList<CmOrg>(); // 第一層組織
        subOrgMap = new HashMap<CmOrg, List<CmOrg>>(); // 第二層組織(部門 廠端) 對應第一層
        
//        allOrgList = cmOrgFacade.findAll();
        List<CmOrg> selectedOrgList = new ArrayList<CmOrg>();
        List<CmUserOrg> cmUserOrgList;
        if (allOrgList != null) {
            if (editUsers!=null && editUsers.getCmUserOrgCollection() != null) {
                cmUserOrgList = new ArrayList(editUsers.getCmUserOrgCollection());
                for (CmUserOrg cmUserOrg : cmUserOrgList) {
                    logger.debug("===buildOrgsTree===cmUserOrg:" + cmUserOrg.getOrgId());
                    selectedOrgList.add(cmUserOrg.getOrgId());
                }
            }
            
            for(CmOrg cmOrg : allOrgList){
                if(cmOrg.getParent()!=null){// 子項目
                    if(selectedOrgList.contains(cmOrg)){
                        secondOrgCheckMap.put(cmOrg.getId(), Boolean.TRUE);
                    }else{
                        secondOrgCheckMap.put(cmOrg.getId(), Boolean.FALSE);
                    }
                }else{
                    if (!mainOrgList.contains(cmOrg)) {// 第一層組織(公司)第一次出現
                        mainOrgList.add(cmOrg);
                        for(CmOrg cmOrg2 : allOrgList){
                            //if(cmOrg2.getParent()!=null && cmOrg2.getParent()==cmOrg){
                            if(cmOrg2.getParent()!=null && cmOrg2.getParent().equals(cmOrg.getId())){
                                if(subOrgMap.get(cmOrg)==null){
                                    subOrgMap.put(cmOrg, new ArrayList<CmOrg>());
                                }
                                subOrgMap.get(cmOrg).add(cmOrg2);//第二層組織(部門 廠端)
                            }
                        }
                    }
                    if(selectedOrgList.contains(cmOrg)){
                        firstOrgCheckMap.put(cmOrg.getId(), Boolean.TRUE);
                    }else{
                        firstOrgCheckMap.put(cmOrg.getId(), Boolean.FALSE);
                    }
                }
            }
        }
    }
    
    /**
     * 選取所有組織
     */
    public void selectAllOrg() {
        logger.debug("selectAllOrg ... allOrgCheck = " + allOrgCheck);

        for (CmOrg mainOrg : mainOrgList) {
            firstOrgCheckMap.put(mainOrg.getId(), allOrgCheck);
            if (subOrgMap.get(mainOrg) != null) {
                for (CmOrg subOrg : subOrgMap.get(mainOrg)) {
                    secondOrgCheckMap.put(subOrg.getId(), allOrgCheck);
                }
            }
        }
    }
    
    /**
     * 選取第一層組織(公司)
     * @param mainOrg
     */
    public void selectMainOrg(CmOrg mainOrg){
        logger.debug("selectMainOrg ... mainOrg = "+mainOrg.getName()+":"+firstOrgCheckMap.get(mainOrg.getId()));
        boolean mainOrgCheck = firstOrgCheckMap.get(mainOrg.getId());
        if (subOrgMap.get(mainOrg) != null) {
            for (CmOrg subOrg : subOrgMap.get(mainOrg)) {
                secondOrgCheckMap.put(subOrg.getId(), mainOrgCheck);
            }
        }
        
        if(!mainOrgCheck){
            allOrgCheck = false;//取消全選
        }
    }
    
    /**
     * 選取第二層組織(部門 廠端)
     */
    public void selectSubOrg(){
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long mid = new Long((String)map.get("mid"));
        Long sid = new Long((String)map.get("sid"));
        logger.debug("selectSubOrg ... sid = "+sid+":"+secondOrgCheckMap.get(sid));
        boolean subOrgCheck = secondOrgCheckMap.get(sid);
        
        if(!subOrgCheck){
            allOrgCheck = false;//取消全選
            firstOrgCheckMap.put(mid, Boolean.FALSE);//取消上層選取
        }
    }
    
    /**
     * 儲存可存取組織
     * @param event 
     */
    public void saveOrgs(ActionEvent event) {    
        logger.debug("=== saveOrgs === ");        
        //RequestContext context = RequestContext.getCurrentInstance();
        
        if( editUsers==null ){
            //context.addCallbackParam("success", false);
            //context.addCallbackParam("msg", "editUsers is null!");
            JsfUtils.addErrorMessage("editUsers is null!");
            return;
        }
        logger.debug("=== editUsers = " + editUsers.toString());
        
        selectedOrgs = new ArrayList<String>();
        for (CmOrg cmOrg : allOrgList) {
            if (cmOrg.getParent() != null) {// 子項目
                if(secondOrgCheckMap.get(cmOrg.getId())){
                    selectedOrgs.add(cmOrg.getId().toString());
                }
            } else {
                if(firstOrgCheckMap.get(cmOrg.getId())){
                    selectedOrgs.add(cmOrg.getId().toString());
                }
            }
        }
        
        logger.debug("=== selectedOrgs = " + selectedOrgs);
        
        try{
            usersFacade.saveUserOrg(editUsers, selectedOrgs, this.getLoginUser());
            editMode = false;
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_ORG_PERMISSION, viewId, 
                    editUsers.getId(), editUsers.getLoginAccount(), editUsers.getName(), true, this.getLoginUser());

            doQuery();
        
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.U_ORG_PERMISSION, viewId, 
                    editUsers.getId(), editUsers.getLoginAccount(), editUsers.getName(), false, this.getLoginUser());
            
            logger.error("delete Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
        
    /**
     * 顯示 Common Report 動態群組
     */
    public void displayCmnRptGroup(){
        logger.debug("displayCmnRptGroup showCmnRptGroup = "+showCmnRptGroup);
        //showFBGroupOnly = !showCmnRptGroup;
        /*if( showCmnRptGroup ){
            selectedGroups = CollectionUtils.union(selectedGroups, selectedCmnRptGroups);
        }else{
            selectedGroups = CollectionUtils.diff(selectedGroups, selectedCmnRptGroups);
        }*/
        fetchGroupList(editUsers);
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
    
    //<editor-fold defaultstate="collapsed" desc="for org tree">
    /**
     * 建立組織樹狀圖
     */
    private void buildUserOrgsTree(){
        CmOrg cmOrg = new CmOrg();
        cmOrg.setId(0L);
        cmOrg.setCode("");
        cmOrg.setCtype(OrgTypeEnum.GROUP.getCode());
        cmOrg.setName(GlobalConstant.SYS_ORG_ROOT);
        
        root = new DefaultTreeNode(new OrganizationVO(0, false, cmOrg), null);
        buildSubOrgTree(root, 0); // 遞迴建立組織圖

        JsfUtils.collapsingORExpandingTree(root, false); // 預設全部展開 
    }
    
    /**
     * 遞迴建立組織圖子樹
     * @param node
     * @param level
     */
    public void buildSubOrgTree(TreeNode node, int level) {
        if( allOrgList==null || level>MAX_ORG_LEVEL ){
            return;
        }
        OrganizationVO organizationVO = (OrganizationVO)node.getData();
        long parentId = organizationVO.getCmOrg().getId();
        
        for(CmOrg cmOrg: allOrgList){
            if( (cmOrg.getParent()==null && parentId==0) 
                    // || (cmOrg.getParent()!=null && cmOrg.getParent().getId()==parentId) ){// 子項目
                    || (cmOrg.getParent()!=null && cmOrg.getParent()==parentId) ){// 子項目
                OrganizationVO newNodeVO = new OrganizationVO();
                newNodeVO.setCmOrg(cmOrg);
                newNodeVO.setKey(cmOrg.getId());// Id為唯一，可用來做 key   
                
                organizationVO.setHasChild(true);
                
                TreeNode newNode = new DefaultTreeNode(newNodeVO, node);
                
                buildSubOrgTree(newNode, level+1);
            }
        }
    }
    
    /*
     * 選擇組織節點查詢
     */
    public void onNodeSelect(NodeSelectEvent event) {
        logger.debug("onNodeSelect selectedNodes:"+selectedNodes);
        if (selectedNodes != null && selectedNodes.length > 0) {
            List<Long> orgids = new ArrayList<Long>();
            criteriaVO.reset();
            for (TreeNode node : selectedNodes) {
                Object data = node.getData();
                if (data != null) {
                    if (data instanceof OrganizationVO) {
                        OrganizationVO selectdOrganizationVO = (OrganizationVO) data;
                        orgids.add(selectdOrganizationVO.getCmOrg().getId());
                    }
                }
            }
            criteriaVO.setOrgIds(orgids);
            criteriaVO.setSelectOrg(true);
        }
        doQuery();
    }
    
    /**
     * 設定使用者無效時，同時刪除權限關聯。
     */
    public void changeRemovePermission(){
        logger.debug("changeRemovePermission ... removePermission =" + removePermission);
    }
    
    /**
     * 取消組織選取
     */
    public void cancelSelectOrg(){
        logger.debug("cancelSelectOrg ...");
        if( this.criteriaVO!=null ){
            this.criteriaVO.setOrgIds(null);
            this.criteriaVO.setSelectOrg(false);
            
            doQuery();
        }
    }    
    
    public String getSelectedOrgLabel(){
        String res = "";
        if (CollectionUtils.isNotEmpty(criteriaVO.getOrgIds())) {
            selectedOrgCount = criteriaVO.getOrgIds().size();
        } else {
            selectedOrgCount = 0;
        }
        if (selectedOrgCount > 0) {
            res = "已選取" + selectedOrgCount + "項";
        }
        return res;
    }
    
    public void changeShowOrgTree(){
        logger.debug("changeShowOrgTree showOrgTree = " + showOrgTree);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for copy permission">
    /**
     * 開啟複製權限對話框
     * @param user 
     */
    public void openCopyPermissionDlg(UsersVO user){
        logger.debug("copyPermission ...");
        desUser = user;
        selectedUserTxt = "";
        //if( allCopyUserList==null ){
        allCopyUserList = usersFacade.findUserForCopy(user);
        //}
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
                tcUser.setSendEmail(false);
            }
        }
        // check
        if( tcUser.getLoginAccount()==null || tcUser.getLoginAccount().isEmpty() ){
            JsfUtils.addErrorMessage("無符合輸入的人員!");
            return;
        }
        
        try{
            // 儲存匯入人
            usersFacade.copyPermission(tcUser.getId(), desUser.getId(), this.getLoginUser());            

            // 顯示該帳號
            doReset();
            this.criteriaVO.setKeyword(desUser.getLogin_account());
            doQuery();
            
            JsfUtils.addSuccessMessage("權限已複製完成 (注意，權限複製只新增，不刪除)!");
        }catch(Exception e){
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
            JsfUtils.buildErrorCallback();
        }else{
            JsfUtils.buildSuccessCallback();
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
        logger.debug("selectUser ...");
        TcUser tcUser = new TcUser();
        for(UsersVO user : allUserList){// 有權選取的所有User
            String txt = user.getCname() + "(" + user.getLogin_account() + ")";
            if( txt.toUpperCase().indexOf(selectedUserTxt.toUpperCase()) >= 0 ){// 符合輸入
                tcUser.setCname(user.getCname());
                tcUser.setEmail(user.getEmail());
                tcUser.setEmpId(user.getEmp_id());
                tcUser.setLoginAccount(user.getLogin_account());
                tcUser.setDisabled(false);
                tcUser.setSendEmail(false);
            }
        }
        // check
        if( tcUser.getLoginAccount()==null || tcUser.getLoginAccount().isEmpty() ){
            JsfUtils.addErrorMessage("無符合輸入的人員!");
            logger.error("selectUser tcUser.getLoginAccount()==null || tcUser.getLoginAccount().isEmpty()");
            return;
        }else{
            List<TcUser> existsUsers = usersFacade.findByLoginAccount(tcUser.getLoginAccount());
            if( existsUsers!=null && !existsUsers.isEmpty() ){
                JsfUtils.addErrorMessage("匯入失敗! 帳號 "+tcUser.getLoginAccount()+" 已存在!");
                logger.error("selectUser existsUsers!=null && !existsUsers.isEmpty()");
                return;
            }
        }
        
        try{
            // 儲存匯入人
            usersFacade.save(tcUser, this.getLoginUser());            
            // 新增 user 預設為 PLANT_USER
            userGroupFacade.defPlantUser(tcUser, this.getLoginUser());

            // 顯示該帳號
            doReset();
            this.criteriaVO.setKeyword(tcUser.getLoginAccount());
            doQuery();
            
            JsfUtils.addSuccessMessage("已匯入 ["+tcUser.getDisplayIdentifier()+"]，請記得執行後續[群組]及[工廠]權限設定!");
        }catch(Exception e){
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
        JsfUtils.buildSuccessCallback();
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
        try{
            int count = usersFacade.tranWebCSToTcUser(selectedImportUsers, this.getLoginUser());
            if( count==0 ){
                JsfUtils.addWarningMessage("未匯入任何項目!");
                return;
            } 
            clearImpMultiUsers();

            doReset();
            doQuery();
            
            JsfUtils.addSuccessMessage(count + "筆勾選項目匯入完成，請記得執行後續[群組]及[工廠]權限設定!");
        }catch(Exception e){
            logger.error("selectUser exception:\n", e);
            JsfUtils.addErrorMessage("匯入失敗! (請檢查人員是否已存在!)");
        }
    }
    
    /**
     * 取消匯入
     */
    public void cancelImport(){
        logger.debug("cancelImport ...");
        
        clearImpMultiUsers();
    }
    //</editor-fold>

    public void selectGroup(){
        logger.info("selectGroup tcGroupList = " + (tcGroupList!=null?tcGroupList.size():0));
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public TcUser getEditUsers() {
        return editUsers;
    }

    public void setEditUsers(TcUser editUsers) {
        this.editUsers = editUsers;
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

    public List<String> getSelectedCmnRptGroups() {
        return selectedCmnRptGroups;
    }

    public void setSelectedCmnRptGroups(List<String> selectedCmnRptGroups) {
        this.selectedCmnRptGroups = selectedCmnRptGroups;
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

    public boolean isShowFBGroupOnly() {
        return showFBGroupOnly;
    }

    public void setShowFBGroupOnly(boolean showFBGroupOnly) {
        this.showFBGroupOnly = showFBGroupOnly;
    }

    public boolean isShowCmnRptGroup() {
        return showCmnRptGroup;
    }

    public void setShowCmnRptGroup(boolean showCmnRptGroup) {
        this.showCmnRptGroup = showCmnRptGroup;
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

    public List<CmOrg> getAllOrgList() {
        return allOrgList;
    }

    public void setAllOrgList(List<CmOrg> allOrgList) {
        this.allOrgList = allOrgList;
    }

    public int getSelectedOrgCount() {
        return selectedOrgCount;
    }

    public void setSelectedOrgCount(int selectedOrgCount) {
        this.selectedOrgCount = selectedOrgCount;
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
    
    public List<String> getSelectedOrgs() {
        return selectedOrgs;
    }

    public void setSelectedOrgs(List<String> selectedOrgs) {
        this.selectedOrgs = selectedOrgs;
    }
    
    public Boolean getAllOrgCheck() {
        return allOrgCheck;
    }

    public void setAllOrgCheck(Boolean allOrgCheck) {
        this.allOrgCheck = allOrgCheck;
    }

    public Map<Long, Boolean> getFirstOrgCheckMap() {
        return firstOrgCheckMap;
    }

    public void setFirstOrgCheckMap(Map<Long, Boolean> firstOrgCheckMap) {
        this.firstOrgCheckMap = firstOrgCheckMap;
    }

    public Map<Long, Boolean> getSecondOrgCheckMap() {
        return secondOrgCheckMap;
    }

    public void setSecondOrgCheckMap(Map<Long, Boolean> secondOrgCheckMap) {
        this.secondOrgCheckMap = secondOrgCheckMap;
    }

    public CmOrg getAllOrg() {
        return allOrg;
    }

    public void setAllOrg(CmOrg allOrg) {
        this.allOrg = allOrg;
    }

    public List<CmOrg> getMainOrgList() {
        return mainOrgList;
    }

    public void setMainOrgList(List<CmOrg> mainOrgList) {
        this.mainOrgList = mainOrgList;
    }

    public Map<CmOrg, List<CmOrg>> getSubOrgMap() {
        return subOrgMap;
    }

    public void setSubOrgMap(Map<CmOrg, List<CmOrg>> subOrgMap) {
        this.subOrgMap = subOrgMap;
    }
    
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }
    
    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }
    
    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public boolean isShowOrgTree() {
        return showOrgTree;
    }

    public void setShowOrgTree(boolean showOrgTree) {
        this.showOrgTree = showOrgTree;
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

    public UserEditVO getEditUserVO() {
        return editUserVO;
    }

    public void setEditUserVO(UserEditVO editUserVO) {
        this.editUserVO = editUserVO;
    }
    //</editor-fold>

}
