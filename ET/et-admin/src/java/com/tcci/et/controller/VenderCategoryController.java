/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.facade.admin.CmFactoryFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.facade.EcOptionFacade;
import com.tcci.ec.model.TcFactoryVO;
import com.tcci.ec.model.VenderVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.et.entity.EtVcForm;
import com.tcci.et.enums.BpmRoleEnum;
import com.tcci.et.enums.FormStatusEnum;
import com.tcci.et.facade.EtVcFormFacade;
import com.tcci.et.facade.EtVenderCategoryFacade;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.StringUtils;
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
import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "venderCategoryController")
@ViewScoped
public class VenderCategoryController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 60;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtVenderCategoryFacade etVenderCategoryFacade;
    @EJB private EtVcFormFacade etVcFormFacade;
    @EJB private EcOptionFacade ecOptionFacade;
    
    // 查詢條件
    private BaseCriteriaVO criteriaVO;
    private List<SelectItem> categoryOptions;
    private List<SelectItem> factoryOptions;
    private int selectedFactoryCount;
    
    // 結果
    private BaseLazyDataModel<VenderVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<VenderVO> filterResultList; // datatable filter 後的結果
    
    private VenderVO editVO;
    private boolean editMode = false;
    
    private boolean isAdmin;
    private List<CmFactory> owenerfactoryList;
//    private List<TcFactoryVO> owenerfactoryList;
    
    private String selectedVenderTxt;
    private List<VenderVO> allLfa1List; // 可選取全部供應商 
    private List<LongOptionVO> categoryOptionList;
    private List<Long> selectedCategorys;
    
    private StreamedContent downloadFile;
    
    
    @PostConstruct
    private void init(){
        if( functionDenied ){ return; }
        
        criteriaVO = new BaseCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);
        editVO = new VenderVO();
        
        // Get view Id
        viewId = JsfUtils.getViewId();
        
//        fetchInputParameters();
        
        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        
        selectedVenderTxt = "";

        categoryOptions = buildCategoryOptions();
        factoryOptions = buildFactoryOptions();
        
        // default query
        defQuery();
    }
    
    /**
     * 取得輸入參數
     */
    private void fetchInputParameters() {
        // 回饋
        String venderCode = JsfUtils.getRequestParameter("venderCode");
        logger.debug("venderCode = " + venderCode);
        if( venderCode!=null ){
            try{
                criteriaVO.setKeyword(venderCode);
            }catch(NumberFormatException e){
                // ignore
            }
        }
    }
    
    /**
     * 分類
     * @return 
     */
    List<SelectItem> buildCategoryOptions(){
        List<SelectItem> options = new ArrayList<>();
        List<LongOptionVO> result = ecOptionFacade.findByTypeOptions("tenderCategory", "C");
        for(LongOptionVO item : result){
                options.add(new SelectItem(item.getValue(), item.getLabel()));
        }
        return options;
    }
    
    /**
     * 廠別
     * @return 
     */
    List<SelectItem> buildFactoryOptions(){
        List<SelectItem> options = new ArrayList<>();
        owenerfactoryList = new ArrayList<>();
//        queryfactoryList = new ArrayList<>();
        if(this.isAdmin){
//        if(this.isPower){
            owenerfactoryList = cmUserfactoryFacade.findAllFactories();
//            queryfactoryList.addAll(owenerfactoryList);
        }else{
            logger.info("buildFactoryOptions not admin");
            owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(this.getLoginUser());
            if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
                logger.debug("owenerfactoryList :"+owenerfactoryList.size());
//                queryfactoryList.addAll(owenerfactoryList);
            }
        }
        if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
            logger.debug("buildFactoryOptions owenerfactoryList:"+owenerfactoryList.size());
        }else{
            logger.info("owenerfactoryList no permission!!!");
        }
        
        
        // 查詢工廠選單
        List<CmFactory> result = cmFactoryFacade.findByAreaCode(owenerfactoryList, null, null);
        if (result != null ) {
            logger.debug("buildFactoryOptions options:"+result.size());
            for (CmFactory g : result) {
//                options.add(new SelectItem(g.getId(), g.getCode()+"-"+g.getName()));
                options.add(new SelectItem(g.getCode(), g.getCode()+"-"+g.getName()));
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
            List<VenderVO> resList = etVenderCategoryFacade.findByCriteria(criteriaVO);
            lazyModel = new BaseLazyDataModel<>(resList);
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
        
        // 清除條件
        criteriaVO = new BaseCriteriaVO();
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
     * @param vo 
     */
    public void edit(VenderVO vo){
        logger.debug("=== edit === ");
        
        // 取出原資料
        editVO = vo;
        logger.debug("=== edit === cids:"+editVO.getCids());
        categoryOptionList = new ArrayList<>();
        fetchCategoryOptionList();
        
        editMode = true;
    }
    
    /**
     * 分類下拉選單
     */
    public void fetchCategoryOptionList(){
        categoryOptionList = new ArrayList<>();
        categoryOptionList = ecOptionFacade.findByTypeOptions("tenderCategory", "C");
        prepareSelectedCategoryInfo(editVO);
    }
    
    /**
     * 物料群組選取狀態
     * @param vo
     */
    public void prepareSelectedCategoryInfo(VenderVO vo){
        selectedCategorys = new ArrayList<>();
        if(StringUtils.isNotBlank(vo.getCids())){
            String[] ids = vo.getCids().split(",");
            for(String idStr:ids){
                selectedCategorys.add(Long.parseLong(idStr));
            }
        }
     }
    
    /**
     * 儲存 
     */
    //public void save(ActionEvent event) {    
    public void save() {
        logger.info("=== save === ");
//        ActivityLogEnum acEnum = (editVO.getMemberId()==null ? ActivityLogEnum.U_VENDER : ActivityLogEnum.U_MEMBER);
        RequestContext context;
        
        if( editVO==null ){
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "editVO is null!");
            return;
        }
        
        try{
            logger.info("=== save === selectedCategorys:"+selectedCategorys.size());
            etVenderCategoryFacade.saveCategory(editVO, selectedCategorys, this.getLoginUser());
            editMode = false;
            doQuery();
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
//            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(),
//                editVO.getMemberId(), true, this.getLoginUser(), this.isSimulated());
            logger.error("save Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    /**
     * 已選取 分類
     * @return 
     */
    public List<LongOptionVO> getSelectedCategoryList(){
        if( categoryOptionList==null ){
            return null;
        }
        List<LongOptionVO> selectedList = new ArrayList<>();
        for(LongOptionVO vo:categoryOptionList){
            if( selectedCategorys!=null && selectedCategorys.contains(vo.getValue()) ){
                selectedList.add(vo);
            }
        }
        return selectedList;
    }
    
    /**
     * 編輯申請 
     * @param row
     */
    public void editApply(VenderVO row) {
        try {
            redirect("formEdit.xhtml?mandt=" + row.getMandt() + "&code=" + row.getVenderCode());
        } catch (Exception ex) {
            logger.error("editApply exception, form id:{}", row.getId(), ex);
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    /**
     * 提交申請 
     */
    //public void save(ActionEvent event) {    
    public void apply() {
        logger.info("=== apply === ");
//        ActivityLogEnum acEnum = (editVO.getMemberId()==null ? ActivityLogEnum.U_VENDER : ActivityLogEnum.U_MEMBER);
        RequestContext context;
        
        if( editVO==null ){
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "editVO is null!");
            return;
        }
        
        try{
            
            boolean block = etVcFormFacade.findRunningByVenderVO(editVO);
//            block = true;
            if(block){
                context = JsfUtils.buildErrorCallback();
                context.addCallbackParam("msg", "該供應商已有申請審核中!");
                return;
            }
            
            String cids = "";
            String cnames = "";
            if (CollectionUtils.isNotEmpty(selectedCategorys)) {
                logger.info("=== apply === selectedCategorys:"+selectedCategorys.size());
                cids = StringUtils.longlistToString(selectedCategorys,",");
                StringBuilder sb = new StringBuilder();
                boolean exists = false;
                for(Long cid : selectedCategorys){
                    EcOption ecOption = ecOptionFacade.find(cid);
                    if( ecOption!=null ){
                        if( exists ){
                            sb.append(",").append(ecOption.getCname());
                        }else{
                            sb.append(ecOption.getCname());
                        }
                        exists = true;
                    }
//                    return sb.toString();
                }
                cnames = sb.toString();
                logger.info("=== apply === cids:"+cids);
                logger.info("=== apply === cnames:"+cnames);
            }
            logger.info("=== apply === editVO.getCids():"+editVO.getCids());
            //有異動
            if(!cids.equals(editVO.getCids())){
                //申請廠別
                BaseCriteriaVO criteriaVO2 = new BaseCriteriaVO();
                criteriaVO2.setDisabled(Boolean.FALSE);//排除黑名單
                criteriaVO2.setType(editVO.getMandt());
                criteriaVO2.setCode(editVO.getVenderCode());
                CmFactory factory = etVenderCategoryFacade.findApplyFactory(criteriaVO2);
                if(factory==null){
                    context = JsfUtils.buildErrorCallback();
                    context.addCallbackParam("msg", "查無簽核人員!");
                    return;
                }
                
                Long factoryId = factory.getId();
                Map<String, Object> roleApprovers = reloadApprovers(factory);//size 2
                if(roleApprovers.isEmpty()){
                    context = JsfUtils.buildErrorCallback();
                    context.addCallbackParam("msg", "廠別("+factory.getCode()+") 查無簽核人員!");
                    return;
                }
                
                EtVcForm form = new EtVcForm();
                form.setFactoryId(factoryId);
                form.setMandt(editVO.getMandt());
                form.setVenderCode(editVO.getVenderCode());
                form.setCname(editVO.getCname());
                form.setCids(cids);
                form.setCnames(cnames);
                form.setStatus(FormStatusEnum.SIGNING);
                etVcFormFacade.save(form, this.getLoginUser(), Boolean.FALSE);
                logger.info("apply... vcformID = " + form.getId());
                etVcFormFacade.startProcess(form, this.getLoginUser(), roleApprovers);
            }else{
                context = JsfUtils.buildErrorCallback();
                context.addCallbackParam("msg", "供應商類別無異動 申請未提交!");
                return;
            }
            
            editMode = false;
//            doQuery();
            context = JsfUtils.buildErrorCallback();
            context.addCallbackParam("msg", "已提交申請!");
        }catch(Exception e){
//            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(),
//                editVO.getMemberId(), true, this.getLoginUser(), this.isSimulated());
            logger.error("apply Exception:\n", e);
            JsfUtils.buildErrorCallback();
        }
    }
    
    //簽核人員 1:HZ_MM or HQ_MM
    public Map<String, Object> reloadApprovers(CmFactory factory){
        Map<String, Object> roleApprovers = new HashMap<>();
        boolean isHZ = cmFactorygroupFacade.isSubFactory("1", "HZCN", factory.getCode());
        List<TcUser> approvers = new ArrayList<>();
        if(isHZ){
            approvers = permissionFacade.findUsersByRole(BpmRoleEnum.HZ_MM.name());
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.HZ_MM.name(), approvers);
            }
        }else{//TCDCN
            approvers = permissionFacade.findUsersByRole(BpmRoleEnum.HQ_MM.name());
            if(CollectionUtils.isNotEmpty(approvers)){
                roleApprovers.put(BpmRoleEnum.HQ_MM.name(), approvers);
            }
        }
        
        return roleApprovers;
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    } 
    
    public void createSelectedFactoryLabel(){
        getSelectedFactoryLabel();
    }
    public String getSelectedFactoryLabel(){
        String res = "未選取";
        if( getSelectedFactoryCount()>0 ){
            res = "已選取" + selectedFactoryCount + "項";
        }
        return res;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">

    public BaseCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(BaseCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public BaseLazyDataModel<VenderVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<VenderVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<VenderVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<VenderVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public VenderVO getEditVO() {
        return editVO;
    }

    public void setEditVO(VenderVO editVO) {
        this.editVO = editVO;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getSelectedVenderTxt() {
        return selectedVenderTxt;
    }

    public void setSelectedVenderTxt(String selectedVenderTxt) {
        this.selectedVenderTxt = selectedVenderTxt;
    }

    public List<VenderVO> getAllLfa1List() {
        return allLfa1List;
    }

    public void setAllLfa1List(List<VenderVO> allLfa1List) {
        this.allLfa1List = allLfa1List;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public List<SelectItem> getCategoryOptions() {
        return categoryOptions;
    }

    public List<LongOptionVO> getCategoryOptionList() {
        return categoryOptionList;
    }

    public List<Long> getSelectedCategorys() {
        return selectedCategorys;
    }

    public void setSelectedCategorys(List<Long> selectedCategorys) {
        this.selectedCategorys = selectedCategorys;
    }

    public List<SelectItem> getFactoryOptions() {
        return factoryOptions;
    }

    public void setFactoryOptions(List<SelectItem> factoryOptions) {
        this.factoryOptions = factoryOptions;
    }

    public int getSelectedFactoryCount() {
        selectedFactoryCount = (CollectionUtils.isNotEmpty(criteriaVO.getFactoryList()))? criteriaVO.getFactoryList().size():0;
        return selectedFactoryCount;
    }

    public void setSelectedFactoryCount(int selectedFactoryCount) {
        this.selectedFactoryCount = selectedFactoryCount;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }
    //</editor-fold>
    
    
}
