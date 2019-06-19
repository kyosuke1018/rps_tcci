/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmFactoryCategory;
import com.tcci.cm.entity.admin.CmFactoryGroupR;
import com.tcci.cm.entity.admin.CmFactorygroup;
import com.tcci.cm.facade.admin.CmUserfactoryFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.enums.FactoryGroupTypeEnum;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 * 工廠群組維護
 *
 * @author Peter
 */
@ManagedBean(name = "factoryGroupController")
@ViewScoped
public class FactoryGroupController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 19;

    private final static int SOURCE_NEW = 0; // 新增
    private final static int SOURCE_ORI = 1; // 原有
    
    //<editor-fold defaultstate="collapsed" desc="EJB">
    @EJB private CmUserfactoryFacade userFactoryFacade;
    //</editor-fold>
    
    private int source;
    private CmFactorygroup cmFactorygroup;
    private long cmFactorygroupId;
    private String groupNameModify;

    private CmFactorygroup editGroup;
    
    private List<SelectItem> groupTypes; // 群組類別
    private List<CmFactorygroup> cmFactorygroupList;
    private List<CmFactoryCategory> CmFactoryCategoryList;   // 編輯視窗:工廠資訊 (依主廠區分)
    private Map<CmFactoryCategory, Boolean> categoryCheckMap;  // 編輯視窗:主工廠選取狀態
    private Map<CmFactory, Boolean> factoryCheckMap;  // 編輯視窗:子工廠選取狀態

    @PostConstruct
    protected void init() {
        logger.debug("FactoryGroupController init ...");
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        cmFactorygroup = new CmFactorygroup();
        // Get view Id
        viewId = JsfUtils.getViewId();
        // for 依 viewId 檢查功能授權
        //if( !checkAuthorizedByViewId(viewId) ){
        //    return;
        //}

        // cmFactorygroupList = cmFactorygroupFacade.findByType(FactoryGroupTypeEnum.COMMON.getCode()); // 群組選單
        cmFactorygroupList = cmFactorygroupFacade.findAll();
        
        initCmFactoryCategoryList();// 廠別資訊
        
        initFactorySelect();// 廠別選取狀態
        
        // 群組類別選單
        groupTypes = new ArrayList<SelectItem>();
        for(FactoryGroupTypeEnum item : FactoryGroupTypeEnum.values()) {
            SelectItem si = new SelectItem();
            si.setLabel(item.getName());
            si.setValue(item.getCode());
            groupTypes.add(si);
        }
        
        editGroup = new CmFactorygroup();
        editGroup.setGrouptype(FactoryGroupTypeEnum.COMMON.getCode());
        initInput();
    }
    
    /**
     * 工廠資訊
     *
     */
    public void initCmFactoryCategoryList() {
        // 抓取所有工廠資訊
        CmFactoryCategoryList = userFactoryFacade.fetchCmFactoryCategoryList();
        Collections.sort(CmFactoryCategoryList);
    }
    
    /**
     * 初始輸入項目
     */
    public void initInput(){
        groupNameModify = "";
        editGroup.setSortnum(0);
        
        initFactorySelect();
    }
    
    /**
     * 初始廠別選取狀況
     */
    public void initFactorySelect(){
        // 初始工廠點選狀況
        categoryCheckMap = new HashMap<CmFactoryCategory, Boolean>(); // 主工廠
        factoryCheckMap = new HashMap<CmFactory, Boolean>(); // 子工廠
        for (CmFactoryCategory CmFactoryCategory : CmFactoryCategoryList) {
            categoryCheckMap.put(CmFactoryCategory, Boolean.FALSE);
            for (CmFactory cmFactory : CmFactoryCategory.getCmFactoryList()) {
                factoryCheckMap.put(cmFactory, Boolean.FALSE);
            }
        }
    }
    
    /**
     * 顯示此群組包含工廠
     */
    public void fetchGroupFactory(){
        logger.debug("fetchGroupFactory ...");
        if( cmFactorygroup==null || cmFactorygroup.getCmFactoryGroupRList()==null ){
            return;
        }
        
        for (CmFactoryCategory CmFactoryCategory : CmFactoryCategoryList) {
            for (CmFactory cmFactory : CmFactoryCategory.getCmFactoryList()) {
                for(CmFactoryGroupR cmFactoryGroupR : cmFactorygroup.getCmFactoryGroupRList()) {
                    if( cmFactory.equals(cmFactoryGroupR.getFactoryId()) ){
                        factoryCheckMap.put(cmFactory, Boolean.TRUE);
                    }
                }
            }
        }        
    }
    
    
    /**
     * 選取類別
     */
    public void changeType(){
        logger.debug("changeType ... editGroup.grouptype = "+editGroup.getGrouptype());
        
        initInput();
    }
    
    /**
     * 選取群組來源
     */
    public void selectSource(){
        logger.debug("selectSource ... source = "+source);
        
        initInput();
    }
                
    /**
     * 選取現有群組
     */
    public void selectGroup(){
        logger.debug("selectGroup ... cmFactorygroup = "+cmFactorygroupId);
        initInput();
        
        if( cmFactorygroupId>0 ){
            cmFactorygroup = cmFactorygroupFacade.find(cmFactorygroupId);
            ExtBeanUtils.copyProperties(editGroup, cmFactorygroup);
            
            fetchGroupFactory();
        }
    }
    
    /**
     * 點選主工廠處理
     *
     * @param category
     */
    public void selectCategory(CmFactoryCategory category) {
        logger.debug("selectFactory => category = " + category + " : checked = " + categoryCheckMap.get(category));

        if (category == null || category.getCmFactoryList() == null) {
            return;
        }

        if (!categoryCheckMap.containsKey(category)) {
            return;
        }

        for (CmFactory cmFactory : category.getCmFactoryList()) {
            factoryCheckMap.put(cmFactory, categoryCheckMap.get(category));
            logger.debug("selectFactory => cmFactory = " + cmFactory.getCode() + " : checked = " + factoryCheckMap.get(cmFactory));
        }
    }

    /**
     * 刪除群組
     */
    public void deleteGroup(){
        if( cmFactorygroup!=null ){
            logger.debug("deleteGroup ... group = "+((cmFactorygroup==null)?0:cmFactorygroup.getId()));
            
            cmFactorygroupFacade.remove(cmFactorygroup);
 
            initInput();
            
            source = SOURCE_ORI;
            cmFactorygroupId = 0;
            //cmFactorygroupList = cmFactorygroupFacade.findByType(FactoryGroupTypeEnum.COMMON.getCode()); // 群組選單
            cmFactorygroupList = cmFactorygroupFacade.findAll();
        }
    }
    
    /**
     * 可否刪除
     * @return 
     */
    public boolean canDelete(){
        boolean canDelete = (source==SOURCE_ORI && cmFactorygroupId>GlobalConstant.DEF_FACTORY_GROUP_ID);
        
        // 有使用者關連的群組
        if( canDelete
                && cmFactorygroup!=null 
                && cmFactorygroup.getCmUserFactorygroupRList()!=null 
                && !cmFactorygroup.getCmUserFactorygroupRList().isEmpty() ){
            canDelete = false; 
        }
        return canDelete;
    }
    
    /**
     * 儲存
     */
    public void save() {
        logger.debug("save ... groupName = "+((source==SOURCE_NEW)?editGroup.getGroupname():cmFactorygroup.getGroupname()));
        
        if (source == SOURCE_NEW) {// 新增群組
            cmFactorygroup = new CmFactorygroup();
            
            ExtBeanUtils.copyProperties(cmFactorygroup, editGroup);
            cmFactorygroup.setId(null);
            cmFactorygroup.setCreator(getLoginUser());
            cmFactorygroup.setCreatetimestamp(new Date());
            
            cmFactorygroupFacade.add(cmFactorygroup, factoryCheckMap, getLoginUser());
            
            logger.debug("create ...");
            
            // 跳至現有群組畫面
            source = SOURCE_ORI;
            cmFactorygroupId = cmFactorygroup.getId();
        } else { // 現有群組
            if( cmFactorygroupId<=0 ){
                JsfUtils.addErrorMessage("未選取群組!");
                return;
            }
            
            ExtBeanUtils.copyProperties(cmFactorygroup, editGroup);
            
            if( !groupNameModify.isEmpty() ){
                cmFactorygroup.setGroupname(groupNameModify);
            }
            cmFactorygroup.setModifier(getLoginUser());
            cmFactorygroup.setModifytimestamp(new Date());
            
            cmFactorygroupFacade.modify(cmFactorygroup, factoryCheckMap, getLoginUser());
                        
            logger.debug("edit ...");
        }
        
        //cmFactorygroupList = cmFactorygroupFacade.findByType(FactoryGroupTypeEnum.CMNRPT.getCode()); // 群組選單
        cmFactorygroupList = cmFactorygroupFacade.findAll();
        
        fetchGroupFactory();// 重取關聯資料
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
    public List<SelectItem> getGroupTypes() {
        return groupTypes;
    }

    public void setGroupTypes(List<SelectItem> groupTypes) {
        this.groupTypes = groupTypes;
    }
    
    public List<CmFactoryCategory> getCmFactoryCategoryList() {
        return CmFactoryCategoryList;
    }

    public void setCmFactoryCategoryList(List<CmFactoryCategory> CmFactoryCategoryList) {
        this.CmFactoryCategoryList = CmFactoryCategoryList;
    }

    public Map<CmFactoryCategory, Boolean> getCategoryCheckMap() {
        return categoryCheckMap;
    }

    public void setCategoryCheckMap(Map<CmFactoryCategory, Boolean> categoryCheckMap) {
        this.categoryCheckMap = categoryCheckMap;
    }

    public Map<CmFactory, Boolean> getFactoryCheckMap() {
        return factoryCheckMap;
    }

    public void setFactoryCheckMap(Map<CmFactory, Boolean> factoryCheckMap) {
        this.factoryCheckMap = factoryCheckMap;
    }

    public CmFactorygroup getCmFactorygroup() {
        return cmFactorygroup;
    }

    public void setCmFactorygroup(CmFactorygroup cmFactorygroup) {
        this.cmFactorygroup = cmFactorygroup;
    }

    public List<CmFactorygroup> getCmFactorygroupList() {
        return cmFactorygroupList;
    }

    public void setCmFactorygroupList(List<CmFactorygroup> cmFactorygroupList) {
        this.cmFactorygroupList = cmFactorygroupList;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
    
    public String getGroupNameModify() {
        return groupNameModify;
    }

    public void setGroupNameModify(String groupNameModify) {
        this.groupNameModify = groupNameModify;
    }

    public long getCmFactorygroupId() {
        return cmFactorygroupId;
    }

    public void setCmFactorygroupId(long cmFactorygroupId) {
        this.cmFactorygroupId = cmFactorygroupId;
    }

    public CmFactorygroup getEditGroup() {
        return editGroup;
    }

    public void setEditGroup(CmFactorygroup editGroup) {
        this.editGroup = editGroup;
    }
    //</editor-fold>
}
