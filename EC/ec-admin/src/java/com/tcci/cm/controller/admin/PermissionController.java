/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.CommonQueryController;
import com.tcci.cm.entity.admin.CmCompany;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.facade.admin.CmCompanyFacade;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.facade.admin.UserFacade;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.admin.PermissionCriteriaVO;
import com.tcci.cm.model.admin.PlantPermissionVO;
import com.tcci.cm.model.admin.UserPermissionVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "permissionController")
@ViewScoped
public class PermissionController extends CommonQueryController implements Serializable {
    public static final long FUNC_OPTION = 81;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    public static final String DATATABLE_RESULT2 = "fmMain:dtResultPlant";
    
    // 報表類型 : by User 統計、
    enum RptTypeEnum {USER, PLANT}
    
    @EJB private CmCompanyFacade cmCompanyFacade;
    @EJB private CmFactoryFacade cmFactoryFacade;
    @EJB private UserFacade userFacade;
    
    // 查詢條件
    PermissionCriteriaVO criteriaVO;
    
    // 選單
    protected List<SelectItem> sapClientOps;// SAP CLIENT別
    //protected List<SelectItem> companyOps;// 公司別
    protected List<SelectItem> plantOps;// 廠別
    protected List<SelectItem> rptTypeOps;// 廠別
    
    // 以使用者為主
    List<UserPermissionVO> resultList;
    List<UserPermissionVO> filterResultList; // datatable filter 後的結果
    
    // 以廠別為主
    List<PlantPermissionVO> resultPlantList;
    List<PlantPermissionVO> filterResultPlantList; // datatable filter 後的結果
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();
        
        // SAP CLIENT
        buildSapClientOptions();
        // 公司別選項
        // buildCompanyOptions();
        // 報表類型選項
        buildRptTypeOptions();
        
        // 預設條件
        initCriteria();
    }
    
    /**
     * 初始條件
     */
    public void initCriteria(){
        criteriaVO = new PermissionCriteriaVO();
        criteriaVO.setRptType(RptTypeEnum.USER.toString());// 預設以使用者
        criteriaVO.setIncludeTCCI(true);// 預設含台訊
        criteriaVO.setAllowOnly(true);// 只顯示有權限的資料
        
        if( sapClientOps!=null && !sapClientOps.isEmpty() ){
            criteriaVO.setSapClientCode(sapClientOps.get(0).getValue().toString());// SAP CLIENT別
            this.changeSapClient();
        }
    }
    
    /**
     * SapClient別選項
     */
    public void buildSapClientOptions(){
        logger.debug("buildSapClientOptions ...");
        sapClientOps = new ArrayList<SelectItem>();
        List<CmCompany> coms = cmCompanyFacade.findAllAndSort();
        if( coms != null ) {
            for (CmCompany com : coms) {
                sapClientOps.add(new SelectItem(com.getSapClientCode(), com.getSapClientCode()));
            }
        }
    }
    
    /**
     * 公司別選項
     * @param mandt
     */
    /*public void buildCompanyOptions(String mandt){
        logger.debug("buildCompanyOptions ...");
        companyOps = new ArrayList<SelectItem>();
        //List<AmsCompany> coms = amsCompanyFacade.findAllSort();
        CompanyCriteriaVO theCriteriaVO = new CompanyCriteriaVO();
        theCriteriaVO.setMandt(mandt);
        List<AmsCompany> coms = amsCompanyFacade.findCompanyByCriteria(theCriteriaVO);
        if( coms != null ) {
            for (AmsCompany com : coms) {
                companyOps.add(new SelectItem(com.getId(), com.getDisplayLabel()));
            }
        }
    }*/
    
    /**
     * 廠別選單 
     * @param sapClient
     */
    public void buildPlantOptions(String sapClient){
        logger.debug("buildPlantOptions ...sapClient="+sapClient);
        plantOps = new ArrayList<SelectItem>();
        // List<CmFactory> plants = amsCompanyFacade.findCompanyPlant(companyId);
        List<CmFactory> plants = cmFactoryFacade.findByCompanyCode(sapClient);
        if( plants != null ) {
            for (CmFactory plant : plants) {
                plantOps.add(new SelectItem(plant.getId(), plant.getDisplayLabel()));
            }
        }
    }
    /*public void buildPlantOptions(long companyId){
        logger.debug("buildPlantOptions ...companyId="+companyId);
        plantOps = new ArrayList<SelectItem>();
        List<CmFactory> plants = amsCompanyFacade.findCompanyPlant(companyId);
        if( plants != null ) {
            for (CmFactory plant : plants) {
                plantOps.add(new SelectItem(plant.getId(), plant.getDisplayLabel()));
            }
        }
    }*/
    
    /**
     * 報表類型選項 
     */
    public void buildRptTypeOptions(){
        logger.debug("buildRptTypeOptions ...");
        rptTypeOps = new ArrayList<SelectItem>();
        rptTypeOps.add(new SelectItem(RptTypeEnum.USER.toString(), "以使用者為主"));
        rptTypeOps.add(new SelectItem(RptTypeEnum.PLANT.toString(), "以廠別為主"));
    }
    
    /**
     * 考慮廠別條件
     */
    public void changeSelPlant(){
        logger.debug("changeSelPlant ... "+this.criteriaVO.isSelPlant());
        if( !criteriaVO.isSelPlant() ){
            this.criteriaVO.setSapClientCode(null);
            //this.criteriaVO.setCompany(null);
            this.criteriaVO.setPlant(null);
        }else{
            if( sapClientOps!=null && !sapClientOps.isEmpty() ){
                criteriaVO.setSapClientCode(sapClientOps.get(0).getValue().toString());// SAP CLIENT別
                changeSapClient();
            }
        }
    }
            
    
    /**
     * 變更SAP CLIENT
     */
    public void changeSapClient(){
        logger.debug("changeSapClient ... "+this.criteriaVO.getSapClientCode());
        /*buildCompanyOptions(this.criteriaVO.getMandt());
        if( companyOps!=null && !companyOps.isEmpty() ){
            criteriaVO.setCompany((Long)companyOps.get(0).getValue());// 公司別
            changeCompany();
        }*/
        buildPlantOptions(this.criteriaVO.getSapClientCode());
    }
    
    /**
     * 變更公司
     */
    /*public void changeCompany(){
        logger.debug("changeCompany ... "+this.criteriaVO.getCompany());
        buildPlantOptions(this.criteriaVO.getCompany());
        if( plantOps!=null && !plantOps.isEmpty() ){
            criteriaVO.setPlant((Long)plantOps.get(0).getValue());// 廠別
        }
    }*/
    
    /**
     * 變更廠別
     */
    public void changePlant(){
        logger.debug("changePlant ... "+this.criteriaVO.getPlant());
    }
    
    /**
     * default query
     */
    public void defQuery(){
        doQuery();
    }
    
    /**
     * 查詢參數檢核
     * @return 
     */
    public boolean doCheck(){
        // 至少輸入[廠別]、[當責單位]或[關鍵字]其中一條件
        /*if( criteriaVO==null 
         || (StringUtils.isEmpty(criteriaVO.getPlantCode()) 
            && StringUtils.isEmpty(criteriaVO.getModule()) 
            && StringUtils.isEmpty(criteriaVO.getKeyword())) 
        ){
            JsfUtils.addErrorMessage("請至少輸入[廠別]、[當責單位]或[關鍵字]其中一條件!");
            return false;
        }*/
        
        return true;
    }
    
    /**
     * 查詢
     */
    @Override
    public void doQuery(){
        logger.debug("doQuery ...");
        if( !doCheck() ){
            return;
        }
        
        criteriaVO.setSetMaxResultsSize(GlobalConstant.DEF_MAX_RESULT_SIZE);//設定最大回傳筆數

        try {
            resetResult();
            if( RptTypeEnum.USER.toString().equals(criteriaVO.getRptType()) ){// 以使用者為主
                resultList = permissionFacade.findPermissionByCriteria(criteriaVO);
            }else if( RptTypeEnum.PLANT.toString().equals(criteriaVO.getRptType()) ){// 以廠別為主
                resultPlantList = permissionFacade.findPlantPermissionByCriteria(criteriaVO);
            }
        }catch(Exception e){
            String msg = ExceptionHandlerUtils.getSimpleMessage("查詢失敗:", e);
            logger.error(msg);
            JsfUtils.addErrorMessage(msg);
        }
    }
    
    /**
     * 重設表單、結果
     */
    @Override
    public void doReset(){
        logger.debug("doReset ...");
        
        resetResult();
        
        criteriaVO.reset();
    }
    
    private void resetResult(){
        if( RptTypeEnum.USER.toString().equals(criteriaVO.getRptType()) ){
            filterResultList = null;
            resultList = null;
            resetDataTable();
        }else if( RptTypeEnum.PLANT.toString().equals(criteriaVO.getRptType()) ){
            filterResultPlantList = null;
            resultPlantList = null;
            resetDataTablePlant();
        }
    }
    
    /**
     * 移除 datatable 目前排序、filter 效果
     */
    public void resetDataTable(){
        JsfUtils.resetDataTable(DATATABLE_RESULT);
    }
    public void resetDataTablePlant(){
        JsfUtils.resetDataTable(DATATABLE_RESULT2);
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }

    //<editor-fold defaultstate="collapsed" desc="for Excel Export">
    /**
     * 設定欄寬
     * @return 
     */
    @Override
    protected Map<Integer, Integer> getColsWidth(){
        Map<Integer, Integer> colsWidthMap = new HashMap<Integer, Integer>();       
        if( RptTypeEnum.USER.toString().equals(criteriaVO.getRptType()) ){
            for(int i=0; i<2; i++){
                colsWidthMap.put(i, 8);
            }
            for(int i=2; i<4; i++){
                colsWidthMap.put(i, 32);
            }
        }else if( RptTypeEnum.PLANT.toString().equals(criteriaVO.getRptType()) ){
            for(int i=0; i<4; i++){
                colsWidthMap.put(i, 8);
            }
            for(int i=4; i<5; i++){
                colsWidthMap.put(i, 32);
            }
        }
        
        return colsWidthMap;
    }
    
    /**
     * 內文 換行文字過濾
     * @return 
     */
    @Override
    protected int[] getContentColumns(){
        int[] contentCols = null;
    
        if( RptTypeEnum.USER.toString().equals(criteriaVO.getRptType()) ){
            contentCols = new int[2];
            for(int i=2; i<4; i++){
                contentCols[i-2]=i;
            }
        }else if( RptTypeEnum.PLANT.toString().equals(criteriaVO.getRptType()) ){
            contentCols = new int[1];
            contentCols[0] = 4;
        }
        
        return contentCols;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public PermissionCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(PermissionCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public List<SelectItem> getPlantOps() {
        return plantOps;
    }

    public void setPlantOps(List<SelectItem> plantOps) {
        this.plantOps = plantOps;
    }

    public List<SelectItem> getRptTypeOps() {
        return rptTypeOps;
    }

    public void setRptTypeOps(List<SelectItem> rptTypeOps) {
        this.rptTypeOps = rptTypeOps;
    }

    public List<SelectItem> getSapClientOps() {
        return sapClientOps;
    }

    public void setSapClientOps(List<SelectItem> sapClientOps) {
        this.sapClientOps = sapClientOps;
    }
    
    public List<UserPermissionVO> getResultList() {
        return resultList;
    }

    public void setResultList(List<UserPermissionVO> resultList) {
        this.resultList = resultList;
    }

    public List<PlantPermissionVO> getResultPlantList() {
        return resultPlantList;
    }

    public void setResultPlantList(List<PlantPermissionVO> resultPlantList) {
        this.resultPlantList = resultPlantList;
    }

    public List<PlantPermissionVO> getFilterResultPlantList() {
        return filterResultPlantList;
    }

    public void setFilterResultPlantList(List<PlantPermissionVO> filterResultPlantList) {
        this.filterResultPlantList = filterResultPlantList;
    }

    public List<UserPermissionVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<UserPermissionVO> filterResultList) {
        this.filterResultList = filterResultList;
    }
    //</editor-fold>
}
