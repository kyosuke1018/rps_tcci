/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmFactoryCategory;
import com.tcci.cm.entity.admin.CmFactorygroup;
import com.tcci.cm.entity.admin.CmCompany;
import com.tcci.cm.entity.admin.CmUserFactorygroupR;
import com.tcci.cm.entity.admin.CmUserfactory;
import com.tcci.cm.entity.admin.CmUsercompany;
import com.tcci.cm.facade.admin.CmActivityLogFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.model.admin.QueryFilter;
import com.tcci.cm.model.admin.UserFactoryVO;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.enums.ActivityLogEnum;
import com.tcci.fc.entity.org.TcUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.primefaces.PrimeFaces;
//import org.primefaces.context.RequestContext;

/**
 *
 * @author Gilbert.Lin
 *  20130506 Peter.Pan 新增查詢條件、filter、編輯畫面修改
 */
@ManagedBean(name="userFactoryController")
@ViewScoped
public class UserFactoryController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 12;
    public static final String DATATABLE_RESULT = "queryForm:dtResult";
    
    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB private UserFacade userFacade;
    @EJB private CmCompanyFacade cmCompanyFacade;
    @EJB private CmFactoryFacade cmFactoryFacade;
    @EJB private CmActivityLogFacade cmActivityLogFacade;
    //</editor-fold>
    
    // 查詢頁面
    private QueryFilter filter = new QueryFilter(); // 查詢條件
    private List<UserFactoryVO> userList = null; // 查詢結果
    // 編輯頁面
    private TcUser editUser = null;// 編輯中的使用者 (TC_USER)
    private List<SelectItem> companyList = new ArrayList<SelectItem>(); // 所有公司
    private List<SelectItem> factoryList = new ArrayList<SelectItem>(); // 所有工廠
    private boolean rcvPriceChangeMail; // 是否收到價格異動通知
    
    private List<CmFactoryCategory>  CmFactoryCategoryList;   // 編輯視窗:工廠資訊 (依主廠區分)
    private Map<CmFactoryCategory, Boolean> categoryCheckMap;  // 編輯視窗:主工廠選取狀態
    private Map<CmFactory, Boolean> factoryCheckMap;  // 編輯視窗:子工廠選取狀態
    
    private List<UserFactoryVO> filterUserList = null; // datatable filter 後的結果
    private int countAfterFilter = 0; // 結果筆數(filter 後隨之異動)
    private SelectItem[] YNOptions; // 是/否 filter 選單
    
    private Map<String, String> companys; // 公司別
    private List<String> companySelected = new ArrayList<String>(); // 選取公司別
    
    private Map<CmFactorygroup, Boolean> factoryGroupCheckMap;// 編輯視窗:工廠群組選取狀態
    private List<CmFactorygroup> cmFactorygroupList; // 工廠群組
    
    private boolean selectAllGroup = false; // 全選群組
    
    private long userId;// querystring param
    
    protected List<SelectItem> userPlantRoles; // 關聯廠別設定的角色
    private long specPlantRole; // 指定關聯廠別設定的角色(使用者群組)
    private boolean closeDlgAfterSave = true;// 儲存後關閉對話框
    
    @PostConstruct
    protected void init() {
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // 初始查詢公司選單
        companys = new HashMap<String, String>();
        List<CmCompany> cmCompanyList = cmCompanyFacade.findAllAndSort();
        if (cmCompanyList != null ) {
            for (CmCompany cmCompany : cmCompanyList) {
                SelectItem item = new SelectItem(cmCompany.getSapClientCode(), cmCompany.getCompanyName());
                companyList.add(item);
                
                companys.put(cmCompany.getSapClientCode(), // label
                        cmCompany.getSapClientCode()); // key
            }
        }
        
        // 初始查詢工廠選單
        List<CmFactory> ppFactoryList = cmUserfactoryFacade.findAllFactories();
        if (ppFactoryList != null && !ppFactoryList.isEmpty()) {
            for (CmFactory g : ppFactoryList) {
                SelectItem item = new SelectItem(g.getId(), g.getCode()+"-"+g.getName());
                factoryList.add(item);
            }
        }
        
        // 工廠群組
        // cmFactorygroupList = cmFactorygroupFacade.findByType(FactoryGroupTypeEnum.CMNRPT.getCode());
        cmFactorygroupList = cmFactorygroupFacade.findAll();
        
        YNOptions = new SelectItem[3];
        YNOptions[0] = new SelectItem("", "選取");
        YNOptions[1] = new SelectItem("是", "是");
        YNOptions[2] = new SelectItem("否", "否");
        
        initCmFactoryCategoryList(); // 初始編輯視窗工廠資訊
        
        this.needCheckFactoryPermission = false;
        
        // 傳入參數 (指定人員)
        fetchInputParameters();
        if( userId>0 ){
            filter.setUserId(userId);
            query();
            UserFactoryVO vo = (userList!=null && !userList.isEmpty())? userList.get(0):null;
            if( vo!=null ){
                this.edit(vo);
            }
        }
        
        // 有權Feedback By Rule角色(使用者群組)
        // this.buildRoleSelectItems();
    }
    
    /**
     * 取得輸入參數
     */
    private void fetchInputParameters() {
        // 回饋
        String userIdStr = JsfUtils.getRequestParameter("userId");
        logger.debug("userIdStr = " + userIdStr);
        
        if( userIdStr!=null ){
            try{
                userId = Long.parseLong(userIdStr);
            }catch(NumberFormatException e){
                // ignore
            }
        }
    }
    
    /**
     * 查詢
     * 
     */
    public void query() {
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
        this.filterUserList = null; // filterValue 初始化
        
        needCheckFactoryPermission = false;
        loadData();
    }
    
    /**
     * 按下查詢建
     */
    public void doQuery() {
        filter.setUserId(0);// 清除單一USER條件
        query();
    }
    
    /**
     * 查詢資料
     */
    protected void loadData() {
        userList = cmUserfactoryFacade.query(filter);
        if( userList!=null ){
            countAfterFilter = userList.size(); // before do filter set default value
        }else{
            countAfterFilter = 0;
        }
    }
    
    /**
     * 對話框動作設定
     */
    public void configDlgAction(){
        logger.debug("configDlgAction closeDlgAfterSave = "+this.closeDlgAfterSave);
    }
    
    /**
     * 初始編輯視窗工廠資訊
     */
    public void initCmFactoryCategoryList(){
        // 抓取所有工廠資訊
        CmFactoryCategoryList = cmUserfactoryFacade.fetchCmFactoryCategoryList();
        
        // 初始工廠點選狀況
        categoryCheckMap = new HashMap<CmFactoryCategory, Boolean>(); // 主工廠
        factoryCheckMap = new HashMap<CmFactory, Boolean>(); // 子工廠
        for(CmFactoryCategory CmFactoryCategory:CmFactoryCategoryList){
            categoryCheckMap.put(CmFactoryCategory, Boolean.FALSE);
            
            for(CmFactory cmFactory:CmFactoryCategory.getCmFactoryList()){
                factoryCheckMap.put(cmFactory, Boolean.FALSE);
            }
        }
        
        factoryGroupCheckMap = new HashMap<CmFactorygroup, Boolean>(); // 工廠群組
        for(CmFactorygroup cmFactorygroup:cmFactorygroupList){
            factoryGroupCheckMap.put(cmFactorygroup, Boolean.FALSE);
        }
    }
    
    /**
     * 變更公司
     * @param e
     */
    public void changeCompany(ValueChangeEvent e){
        if( e==null || e.getNewValue()==null ){
            return;
        }
        
        // 查詢工廠選單
        List<CmFactory> ppFactoryList = cmFactoryFacade.findBySapClientCode((String)e.getNewValue());
        factoryList.clear();
        if (ppFactoryList != null ) {
            for (CmFactory g : ppFactoryList) {
                SelectItem item = new SelectItem(g.getId(), g.getCode()+"-"+g.getName());
                factoryList.add(item);
            }
        }
    }
    
    /**
     * 點選主工廠處理
     * @param category
     */
    public void selectCategory(CmFactoryCategory category){
        logger.debug("selectFactory => category = " + category + " : checked = " + categoryCheckMap.get(category));
        
        if( category==null || category.getCmFactoryList()==null ){
            return;
        }
        if( !categoryCheckMap.containsKey(category) ){
            return;
        }
        for (CmFactory cmFactory : category.getCmFactoryList()) {
            factoryCheckMap.put(cmFactory, categoryCheckMap.get(category));
            logger.debug("selectFactory => cmFactory = " + cmFactory.getCode() + " : checked = " + factoryCheckMap.get(cmFactory));
        }
    }
    
    /**
     * 點選個別工廠處理
     * @param factory
     */
    public void selectFactory(CmFactory factory){
        logger.debug("selectFactory => factory = " + factory + " : checked = " + factoryCheckMap.get(factory));
    }
    
    /**
     * Click 編輯按鈕
     * @param user
     */
    public void edit(UserFactoryVO user) {
        logger.debug("edit user = "+user.getCname());
        // 取得完整 TcUser 資料
        editUser = userFacade.findUserByLoginAccount(user.getLoginAccount());
        logger.debug("edit editUser = "+editUser.getCname());
        if (editUser == null){
            return;
        }
        
        // 目前關聯公司
        List<CmUsercompany> companies = editUser.getCmUsercompanyList();
        companySelected = new ArrayList<String>();
        for(CmUsercompany cmUsercompany:companies){
            companySelected.add(cmUsercompany.getSapClientCode());
        }
        
        // 初始編輯視窗工廠資訊
        initCmFactoryCategoryList();
        
        // 目前關聯工廠
        Collection<CmUserfactory> ppUserFactoryList = editUser.getCmUserfactoryList();
        
        // 標示關聯工廠
        if (ppUserFactoryList != null && !ppUserFactoryList.isEmpty()) {
            int i=0;
            for (CmUserfactory cmUserfactory : ppUserFactoryList) {
                CmFactory cmFactory = new CmFactory();
                cmFactory.setId(cmUserfactory.getFactoryId().getId());
                factoryCheckMap.put(cmFactory, Boolean.TRUE);
                
                i++;
            }
        }
        
        // 目前關聯工廠群組       
        // 標示關聯工廠
        specPlantRole = 0;// default
        //userPlantRoles = buildUserPlantRoleOptions(editUser);
        
        prepareFactorygroupCheckMap(editUser);
    }
    
    /**
     * 目前關聯工廠群組勾選狀態
     * 
     * @param editUser 
     */
    public void prepareFactorygroupCheckMap(TcUser editUser){
        // 目前關聯工廠群組
        List<CmUserFactorygroupR> cmUserFactorygroupRList = editUser.getCmUserFactorygroupRList();
        
        // 先 uncheck
        for(CmFactorygroup cmFactorygroup:cmFactorygroupList){
            factoryGroupCheckMap.put(cmFactorygroup, Boolean.FALSE);
        }
        
        // check 標示關聯工廠
        if (cmUserFactorygroupRList != null && !cmUserFactorygroupRList.isEmpty()) {
            for (CmUserFactorygroupR cmUserFactorygroupR : cmUserFactorygroupRList) {
                if( cmUserFactorygroupR.getUsergroupId().equals(specPlantRole) ){// 針對指定角色
                    CmFactorygroup CmFactorygroup = new CmFactorygroup();
                    CmFactorygroup.setId(cmUserFactorygroupR.getFactorygroupId().getId());

                    factoryGroupCheckMap.put(CmFactorygroup, Boolean.TRUE);
                }
            }
        }
    }
    
    public void changeRole(){
        logger.debug("changeRole ... specPlantRole = "+specPlantRole);
        // 標示關聯工廠
        prepareFactorygroupCheckMap(editUser);
    }
    
    public List<CmFactory> getFactoryListByCategory(CmFactoryCategory cmFactoryCategory){
        /*logger.debug("getFactoryListByCategory ...");
        List<CmFactory> unsortList = cmFactoryCategory.getCmFactoryList();
        Collections.sort(unsortList); // QAS & PRD 環境不正常排序????
        return unsortList;*/
        return cmFactoryFacade.findByCategory(cmFactoryCategory);
    }
    
    /**
     * 儲存工廠關連設定
     */
    public void save() {
        if (editUser.getId() == null) {
            String error = "edit user not exists!";
            //RequestContext rc = JsfUtils.buildErrorCallback();
            //rc.addCallbackParam("msg", error);
            JsfUtils.addErrorMessage(error);
            return;
        }
        
        // 準備更新工廠關連設定結果
        List<String> selectedFactory = new ArrayList<String>();
        if( factoryCheckMap!=null ){
            for(CmFactory cmFactory : factoryCheckMap.keySet()){
                if( (Boolean)factoryCheckMap.get(cmFactory) ){
                    selectedFactory.add(cmFactory.getId().toString());
                }
            }
        }
        
        // 準備更新[工廠群組]關連設定結果
        List<String> selectedFactoryGroup = new ArrayList<String>();
        List<String> selFactoryGroupCodes = new ArrayList<String>();
        if( factoryGroupCheckMap!=null ){
            for(CmFactorygroup cmFactorygroup : factoryGroupCheckMap.keySet()){
                if( (Boolean)factoryGroupCheckMap.get(cmFactorygroup) ){
                    selectedFactoryGroup.add(cmFactorygroup.getId().toString());
                    selFactoryGroupCodes.add(cmFactorygroup.getCode());
                }
            }
        }
        
        //  廠群組階層檢查
        String checkMsg = checkSaveResult(selFactoryGroupCodes);
        if( checkMsg!=null ){
            //RequestContext rc = JsfUtils.buildErrorCallback();
            //rc.addCallbackParam("msg", checkMsg);
            JsfUtils.addErrorMessage(checkMsg);
            return;
        }
        
        // 重要操作紀錄
        StringBuilder contentSB = new StringBuilder();
        contentSB.append("[").append(editUser.getId()).append("]").append(editUser.getName()).append(editUser.getLoginAccount());
        
        try{
            // 資料更新
            permissionFacade.update(editUser, null, 
                companySelected, selectedFactory, selectedFactoryGroup, 
                specPlantRole, 
                sessionController.getLoginTcUser());

            cmActivityLogFacade.logActiveCommon(ActivityLogEnum.U_PLANT_PERMISSION, viewId, contentSB.toString(), true, this.getLoginUser());
        
            // 有查詢資料時才更新 userList
            if (userList != null) {
                filter.setUserId(editUser.getId());
                query();
            }
            
            //RequestContext requestContext = JsfUtils.buildSuccessCallback();
            PrimeFaces requestContext = JsfUtils.buildSuccessCallback();
            // 是否自動關閉對話框
            //requestContext.addCallbackParam("closeDlgAfterSave", closeDlgAfterSave);
            requestContext.ajax().addCallbackParam("closeDlgAfterSave", closeDlgAfterSave);
        }catch(Exception e){
            cmActivityLogFacade.logActiveCommon(ActivityLogEnum.U_PLANT_PERMISSION, viewId, contentSB.toString(), false, this.getLoginUser());
            logger.error("save exception:\n", e);
            JsfUtils.buildErrorCallback(JsfUtils.getResourceTxt("儲存錯誤!"));
        }
    }
    
    /**
     * 廠群組階層檢查
     * 
     * @return 
     */
    private String checkSaveResult(List<String> selFactoryGroupCodes){
        // 有設定[TCCF台灣水泥製品各廠]，卻沒設定[TCCTF台北水泥製品各廠]、[TCCFF台中水泥製品各廠]、[TCCGF鼓山水泥製品各廠]或[1071花蓮預拌]        
        /*if( selFactoryGroupCodes.contains(GlobalConstant.FG_PDTW) 
           && ( 
                !selFactoryGroupCodes.contains(GlobalConstant.FG_PDTPE) ||
                !selFactoryGroupCodes.contains(GlobalConstant.FG_PDTXG) ||
                !selFactoryGroupCodes.contains(GlobalConstant.FG_PDKHH) ||
                !selFactoryGroupCodes.contains(GlobalConstant.FG_PDHUN)
              )
          ){
            return "有勾選[台灣水泥製品各廠]，卻沒勾選[台北水泥製品各廠]、\n[台中水泥製品各廠]、[鼓山水泥製品各廠]或[花蓮預拌]，\n請修正後再儲存!";
        }
        
        // 有設定[TCCCN台泥大陸各廠]，卻沒設定[HZCN新台泥各廠]
        if( selFactoryGroupCodes.contains(GlobalConstant.FG_CN) 
           && ( 
                !selFactoryGroupCodes.contains(GlobalConstant.FG_HZ)
              )
          ){
            return "有勾選[台泥大陸各廠]，卻沒勾選[新台泥各廠]，\n請修正後再儲存!";
        }*/
      
        return null;
    }
    
    /**
     * 重設查詢條件及結果
     */
    public void reset() {
        // 條件
        filter = new QueryFilter(); // 查詢條件
        // 結果
        userList = new ArrayList<UserFactoryVO>();
        filterUserList = new ArrayList<UserFactoryVO>();
        countAfterFilter = 0;
        
        // 移除 datatable 目前排序、filter 效果
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    
    /**
     * 處理 datatable 的 filter event
     * @param event
     */
    public void onFilter(AjaxBehaviorEvent event) {
        countAfterFilter = (filterUserList==null)? 0:filterUserList.size();
    }
    
    /**
     * 全選群組
     */
    public void changeSelectAllGroup(){
        logger.debug("changeSelectAllGroup ... selectAllGroup = "+selectAllGroup);
        if( factoryGroupCheckMap!=null ){
            for(CmFactorygroup cmFactorygroup : factoryGroupCheckMap.keySet()){
                factoryGroupCheckMap.put(cmFactorygroup, Boolean.valueOf(selectAllGroup));
            }
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
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Map<CmFactorygroup, Boolean> getFactoryGroupCheckMap() {
        return factoryGroupCheckMap;
    }
    
    public void setFactoryGroupCheckMap(Map<CmFactorygroup, Boolean> factoryGroupCheckMap) {
        this.factoryGroupCheckMap = factoryGroupCheckMap;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<SelectItem> getUserPlantRoles() {
        return userPlantRoles;
    }

    public void setUserPlantRoles(List<SelectItem> userPlantRoles) {
        this.userPlantRoles = userPlantRoles;
    }

    public long getSpecPlantRole() {
        return specPlantRole;
    }

    public void setSpecPlantRole(long specPlantRole) {
        this.specPlantRole = specPlantRole;
    }

    public boolean isSelectAllGroup() {
        return selectAllGroup;
    }

    public void setSelectAllGroup(boolean selectAllGroup) {
        this.selectAllGroup = selectAllGroup;
    }
    
    public List<CmFactorygroup> getCmFactorygroupList() {
        return cmFactorygroupList;
    }
    
    public void setCmFactorygroupList(List<CmFactorygroup> cmFactorygroupList) {
        this.cmFactorygroupList = cmFactorygroupList;
    }
    
    public Map<String, String> getCompanys() {
        return companys;
    }
    
    public void setCompanys(Map<String, String> companys) {
        this.companys = companys;
    }
    
    public List<String> getCompanySelected() {
        return companySelected;
    }
    
    public void setCompanySelected(List<String> companySelected) {
        this.companySelected = companySelected;
    }
    
    public List<SelectItem> getCompanyList() {
        return companyList;
    }
    
    public void setCompanyList(List<SelectItem> companyList) {
        this.companyList = companyList;
    }
    
    public List<SelectItem> getFactoryList() {
        return factoryList;
    }
    
    public void setFactoryList(List<SelectItem> factoryList) {
        this.factoryList = factoryList;
    }
    
    public Map<CmFactoryCategory, Boolean> getCategoryCheckMap() {
        return categoryCheckMap;
    }
    
    public void setCategoryCheckMap(Map<CmFactoryCategory, Boolean> categoryCheckMap) {
        this.categoryCheckMap = categoryCheckMap;
    }
    
    public int getCountAfterFilter() {
        return countAfterFilter;
    }
    
    public void setCountAfterFilter(int countAfterFilter) {
        this.countAfterFilter = countAfterFilter;
    }
    
    public List<UserFactoryVO> getFilterUserList() {
        return filterUserList;
    }
    
    public void setFilterUserList(List<UserFactoryVO> filterUserList) {
        this.filterUserList = filterUserList;
    }
    
    public QueryFilter getFilter() {
        return filter;
    }
    
    public void setFilter(QueryFilter filter) {
        this.filter = filter;
    }
    
    public List<UserFactoryVO> getUserList() {
        return userList;
    }
    
    public void setUserList(List<UserFactoryVO> userList) {
        this.userList = userList;
    }
    
    public TcUser getEditUser() {
        return editUser;
    }
    
    public void setEditUser(TcUser editUser) {
        this.editUser = editUser;
    }
    
    public boolean isRcvPriceChangeMail() {
        return rcvPriceChangeMail;
    }
    
    public void setRcvPriceChangeMail(boolean rcvPriceChangeMail) {
        this.rcvPriceChangeMail = rcvPriceChangeMail;
    }
    
    public List<CmFactoryCategory> getCmFactoryCategoryList() {
        return CmFactoryCategoryList;
    }
    
    public void setCmFactoryCategoryList(List<CmFactoryCategory> CmFactoryCategoryList) {
        this.CmFactoryCategoryList = CmFactoryCategoryList;
    }
    
    public Map<CmFactory, Boolean> getFactoryCheckMap() {
        return factoryCheckMap;
    }
    
    public void setFactoryCheckMap(Map<CmFactory, Boolean> factoryCheckMap) {
        this.factoryCheckMap = factoryCheckMap;
    }

    public boolean isCloseDlgAfterSave() {
        return closeDlgAfterSave;
    }

    public void setCloseDlgAfterSave(boolean closeDlgAfterSave) {
        this.closeDlgAfterSave = closeDlgAfterSave;
    }
    
    public SelectItem[] getYNOptions() {
        return YNOptions;
    }
    
    public void setYNOptions(SelectItem[] YNOptions) {
        this.YNOptions = YNOptions;
    }
    //</editor-fold>
    
}
