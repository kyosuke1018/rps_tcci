package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFunction;
import com.tcci.cm.facade.admin.CmGroupFunctionRFacade;
import com.tcci.cm.facade.admin.UserGroupFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.admin.MenuFunctionVO;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.et.enums.AuthLevelEnum;
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

/**
 * 工廠群組維護
 *
 * @author Peter
 */
@ManagedBean(name = "groupPermissionController")
@ViewScoped
public class GroupPermissionController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 4;

    //<editor-fold defaultstate="collapsed" desc="Inject">
    @EJB private TcGroupFacade tcGroupFacade;
    @EJB private UserGroupFacade userGroupFacade;
    @EJB private CmGroupFunctionRFacade cmGroupFunctionRFacade;
    //</editor-fold>
    
    private long groupId;
    private TcGroup tcGroup;
    private List<TcGroup> tcGroupList;
    private List<MenuFunctionVO> functionInfoList;   // 功能列表
    private List<MenuFunctionVO> groupFuncInfoList;   // 群組擁有功能列表
    private Map<Long, Boolean> mainMenuCheckMap;  // 第一層選單選取狀態
    private Map<Long, Boolean> subMenuCheckMap;  // 第二層選單選取狀態
    private Map<Long, Boolean> functionCheckMap;  // 功能選取狀態
    private Map<Long, String> functionAuthMap;  // 功能授權階層
    
    private List<CmFunction> mainList; // 第一層選單
    private Map<CmFunction, List<CmFunction>> mainSubMap; // 第一層 包含 第二層資訊
    private Map<CmFunction, List<CmFunction>> subFuncMap; // 第二層 包含 功能資訊

    private boolean showCmnRptGroup = false;// 顯示 Common Report 動態群組
    private boolean showFBGroupOnly = false;// 只顯示回饋相關群組
    
    @PostConstruct
    protected void init() {
        logger.debug("GroupPermissionController init ...");
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();

        groupId = 0;
        tcGroup = new TcGroup();

        fetchGroupList();// 抓取群組選單
        
        initFunctionInfo();// 抓取所有功能資訊
        initFuncSelect();// 清空勾選
    }
    
    /**
     * 抓取所有功能資訊
     *
     * @return
     */
    public void initFunctionInfo() {
        functionInfoList = permissionFacade.fetchFunctionInfoAll();
    }
    
    /**
     * 顯示 Common Report 動態群組
     */
    public void displayCmnRptGroup(){
        logger.debug("displayCmnRptGroup showCmnRptGroup = "+showCmnRptGroup);
        showFBGroupOnly = !showCmnRptGroup;
        fetchGroupList();
    }
    
    /**
     * 群組下拉選單
     */
    public void fetchGroupList(){
        // tcGroupList = userGroupFacade.fetchGroupList(showCmnRptGroup, showFBGroupOnly);
        tcGroupList = userGroupFacade.findAll();
    }
    
    /**
     * 初始功能選取狀況
     */
    public void initFuncSelect(){
        mainMenuCheckMap = new HashMap<Long, Boolean>();
        subMenuCheckMap = new HashMap<Long, Boolean>();
        functionCheckMap = new HashMap<Long, Boolean>();
        functionAuthMap = new HashMap<Long, String>();

        mainList = new ArrayList<CmFunction>(); // 第一層選單
        mainSubMap = new HashMap<CmFunction, List<CmFunction>>(); // 第一層 包含 第二層資訊
        subFuncMap = new HashMap<CmFunction, List<CmFunction>>(); // 第二層 包含 功能資訊

        if( functionInfoList!=null ){
            for(MenuFunctionVO menuFunctionVO : functionInfoList){
                mainMenuCheckMap.put(menuFunctionVO.getMid(), Boolean.FALSE);
                subMenuCheckMap.put(menuFunctionVO.getSid(), Boolean.FALSE);
                functionCheckMap.put(menuFunctionVO.getId(), Boolean.FALSE);
                functionAuthMap.put(menuFunctionVO.getId(), AuthLevelEnum.ALL.getCode());

                CmFunction mainMenu = new CmFunction();
                mainMenu.setId(menuFunctionVO.getMid());
                mainMenu.setTitle(menuFunctionVO.getMtitle());
                mainMenu.setSortnum(menuFunctionVO.getMsortnum());
                mainMenu.setCode(menuFunctionVO.getMcode());

                CmFunction subMenu = new CmFunction();
                subMenu.setId(menuFunctionVO.getSid());
                subMenu.setTitle(menuFunctionVO.getStitle());
                subMenu.setSortnum(menuFunctionVO.getSsortnum());
                subMenu.setCode(menuFunctionVO.getScode());

                CmFunction function = new CmFunction();
                function.setId(menuFunctionVO.getId());
                function.setTitle(menuFunctionVO.getTitle());
                function.setSortnum(menuFunctionVO.getSortnum());
                function.setUrl(menuFunctionVO.getUrl());
                function.setCode(menuFunctionVO.getCode());

                if( !mainList.contains(mainMenu) ){// 主選單第一次出現
                    mainList.add(mainMenu);
                    
                    List<CmFunction> subList = new ArrayList<CmFunction>();
                    subList.add(subMenu);
                    mainSubMap.put(mainMenu, subList);
                    
                    List<CmFunction> funcList = new ArrayList<CmFunction>();
                    funcList.add(function);
                    subFuncMap.put(subMenu, funcList);
                }else{
                    if( !mainSubMap.get(mainMenu).contains(subMenu) ){// 第二層選單第一次出現
                        mainSubMap.get(mainMenu).add(subMenu);
                        
                        List<CmFunction> funcList = new ArrayList<CmFunction>();
                        funcList.add(function);
                        subFuncMap.put(subMenu, funcList);
                    }else{
                        subFuncMap.get(subMenu).add(function);
                    }
                }
            }
        }
    }
    
    /**
     * 初始特定群組功能選取狀況
     */
    public void initFuncSelectByUserGroup(){
        initFuncSelect();// 清空勾選
        
        groupFuncInfoList = permissionFacade.fetchFunctionInfoByGroup(tcGroup.getId()); // fetchFunctionInfo(0, 0, tcGroup.getId(), null, false, false);
        if( groupFuncInfoList!=null ){
            for(MenuFunctionVO menuFunctionVO : groupFuncInfoList){
                functionCheckMap.put(menuFunctionVO.getId(), Boolean.TRUE);
                functionAuthMap.put(menuFunctionVO.getId(), menuFunctionVO.getAuth());
            }
        }
    }
                
    /**
     * 選取現有群組
     */
    public void selectGroup(){
        logger.debug("selectGroup ...");
        tcGroup = tcGroupFacade.find(groupId);
        if( tcGroup==null ){
            initFunctionInfo();
            initFuncSelect();// 清空勾選
        }else{
            logger.debug("selectGroup ... tcGroup = "+tcGroup.getId()+":"+tcGroup.getCode());
            initFuncSelectByUserGroup();
        }
    }
    
    /**
     * 選取主選單
     * @param mid 
     */
    public void selectMainMenu(long mid){
        logger.debug("selectMainMenu ... mid = "+mid+":"+mainMenuCheckMap.get(mid));
        
        CmFunction main = new CmFunction();
        main.setId(mid);
        
        for(CmFunction sub : mainSubMap.get(main)){// 第二層選項處理
            subMenuCheckMap.put(sub.getId(), mainMenuCheckMap.get(mid));
            if( subFuncMap.get(sub)!=null ){
                for(CmFunction func : subFuncMap.get(sub)){// 功能處理
                    functionCheckMap.put(func.getId(), mainMenuCheckMap.get(mid));
                }
            }
        }
    }
    
    /**
     * 選取第二層選單 
     */
    public void selectSubMenu(){
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Long sid = new Long((String)map.get("sid"));
        
        logger.debug("selectSubMenu ... sid = "+sid);
        // 功能選項處理
        CmFunction sub = new CmFunction();
        sub.setId(sid);
        if( subFuncMap.get(sub)!=null ){
            for(CmFunction func : subFuncMap.get(sub)){// 功能處理
                functionCheckMap.put(func.getId(), subMenuCheckMap.get(sid));
            }
        }
    }
    
    /**
     * 儲存
     */
    public void save() {
        logger.debug("save ... tcGroup = "+tcGroup.getId()+":"+tcGroup.getCode());
        
        if( tcGroup == null || tcGroup.getId()==null || tcGroup.getId()<=0 ){
            JsfUtils.buildErrorCallback("未選取群組!");
            return;
        }

        List<Long> ids = new ArrayList<Long>();
        for(Long fid : functionCheckMap.keySet()){
            if( functionCheckMap.get(fid) ){
                ids.add(fid);
            }
        }
        
        try{
            cmGroupFunctionRFacade.saveGroupFunctionR(tcGroup, ids, functionAuthMap, this.getLoginUser(), this.isSimulated());
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            logger.error("save exception ...\n", e);
            JsfUtils.buildErrorCallback("儲存錯誤!");
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
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public TcGroup getTcGroup() {
        return tcGroup;
    }

    public void setTcGroup(TcGroup tcGroup) {
        this.tcGroup = tcGroup;
    }

    public boolean isShowCmnRptGroup() {
        return showCmnRptGroup;
    }

    public void setShowCmnRptGroup(boolean showCmnRptGroup) {
        this.showCmnRptGroup = showCmnRptGroup;
    }

    public List<TcGroup> getTcGroupList() {
        return tcGroupList;
    }

    public void setTcGroupList(List<TcGroup> tcGroupList) {
        this.tcGroupList = tcGroupList;
    }

    public List<MenuFunctionVO> getFunctionInfoList() {
        return functionInfoList;
    }

    public void setFunctionInfoList(List<MenuFunctionVO> functionInfoList) {
        this.functionInfoList = functionInfoList;
    }

    public List<MenuFunctionVO> getGroupFuncInfoList() {
        return groupFuncInfoList;
    }

    public void setGroupFuncInfoList(List<MenuFunctionVO> groupFuncInfoList) {
        this.groupFuncInfoList = groupFuncInfoList;
    }

    public Map<Long, Boolean> getMainMenuCheckMap() {
        return mainMenuCheckMap;
    }

    public void setMainMenuCheckMap(Map<Long, Boolean> mainMenuCheckMap) {
        this.mainMenuCheckMap = mainMenuCheckMap;
    }

    public Map<Long, Boolean> getSubMenuCheckMap() {
        return subMenuCheckMap;
    }

    public void setSubMenuCheckMap(Map<Long, Boolean> subMenuCheckMap) {
        this.subMenuCheckMap = subMenuCheckMap;
    }

    public Map<Long, Boolean> getFunctionCheckMap() {
        return functionCheckMap;
    }

    public void setFunctionCheckMap(Map<Long, Boolean> functionCheckMap) {
        this.functionCheckMap = functionCheckMap;
    }
    
    public Map<CmFunction, List<CmFunction>> getMainSubMap() {
        return mainSubMap;
    }

    public void setMainSubMap(Map<CmFunction, List<CmFunction>> mainSubMap) {
        this.mainSubMap = mainSubMap;
    }

    public Map<CmFunction, List<CmFunction>> getSubFuncMap() {
        return subFuncMap;
    }

    public void setSubFuncMap(Map<CmFunction, List<CmFunction>> subFuncMap) {
        this.subFuncMap = subFuncMap;
    }

    public List<CmFunction> getMainList() {
        return mainList;
    }

    public void setMainList(List<CmFunction> mainList) {
        this.mainList = mainList;
    }

    public boolean isShowFBGroupOnly() {
        return showFBGroupOnly;
    }

    public void setShowFBGroupOnly(boolean showFBGroupOnly) {
        this.showFBGroupOnly = showFBGroupOnly;
    }

    public Map<Long, String> getFunctionAuthMap() {
        return functionAuthMap;
    }

    public void setFunctionAuthMap(Map<Long, String> functionAuthMap) {
        this.functionAuthMap = functionAuthMap;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    //</editor-fold>
}
