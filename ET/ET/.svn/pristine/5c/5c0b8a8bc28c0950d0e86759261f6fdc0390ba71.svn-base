/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.global;

import com.tcci.cm.enums.ActionEnum;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.OptionEnum;
import com.tcci.et.facade.EtOptionFacade;
import com.tcci.et.model.OptionVO;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.model.criteria.BaseCriteriaVO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "genericOptions")
@ViewScoped
public class GenericOptionsController extends SessionAwareController implements Serializable {
    protected static final String DATATABLE_RESULT = "fmMain:dtResult";

    protected @EJB EtOptionFacade optionsFacade;
    
    protected int category = 1; // 1:保種記錄關聯； 2:維管記錄關聯
    // edit
    protected OptionVO editVO;

    // for records.xhtml
    //List<OptionVO> allList;
    //List<OptionVO> resList;
    protected String defAddType;
    
    // for options.xhtml
    protected ActionEnum mode = ActionEnum.QUERY;
    protected BaseLazyDataModel<OptionVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    protected List<OptionVO> resultList;
    protected List<OptionVO> filterResultList; // datatable filter 後的結果
    protected BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();
        logger.debug("viewId = "+viewId);
    }

    /**
     * 變更類別
     */
    public void changeType(){
        logger.debug("changeType type = "+criteriaVO.getType());
        doQuery();
    }
    
    /**
     * 類別名稱
     * @return 
     */
    public String getTypeName(){
        OptionEnum enums = null;
        if( viewId.endsWith("/records.xhtml") ){
            if( editVO!=null ){
                enums = OptionEnum.getFromCode(editVO.getType());
            }
        }else{
            if( criteriaVO!=null ){
                enums = OptionEnum.getFromCode(criteriaVO.getType());
            }
        }
        return enums==null?"":enums.getDisplayName();
    }
    
    /**
     * autoComplete 選單
     * @param intxt
     * @return 
     */
    /*public List<OptionVO> autoCompleteOptionsOptions(String intxt){
        logger.debug("autoCompleteOptionsOptions intxt = "+intxt);
        BaseCriteriaVO criteria = new BaseCriteriaVO();
        criteria.setKeyword(intxt);
        List<OptionVO> list = optionsFacade.findByCriteria(criteria);
        
        return list;
    }*/

    /**
     * 查詢
     */
    public void doQuery(){
        logger.debug("doQuery ...");

        try{
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化
            
            criteriaVO.setDisabled(false);
            resultList = optionsFacade.findByCriteria(criteriaVO);

            lazyModel = new BaseLazyDataModel<OptionVO>(resultList);
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "doQuery", e, false);
        }
    }

    /**
     * 清除
     */
    public void doReset(){
        logger.debug("doReset ...");
        try{
            // 清除條件
            criteriaVO = new BaseCriteriaVO();
            criteriaVO.setCategory(category);
            // default
            if( this.category==1 ) {
                criteriaVO.setType(OptionEnum.AREA.getCode());
            }
            
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);

            filterResultList = null; // filterValue 初始化
            resultList = null;
            lazyModel = new BaseLazyDataModel<OptionVO>();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "doReset", e, false);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for modify">
    /**
     * 新增
     * @param type
     */
    public void prepareCreate(String type){
        logger.debug("prepareCreate ...");
        try{
            this.editVO = new OptionVO();
            editVO.setType(type);
            if( type==null ){
                editVO.setType(criteriaVO.getType());
            }
            editVO.setDisabled(false);
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "prepareCreate", e, true);
        }
    }
    public void prepareCreate(){
        prepareCreate(null);
    }
    
    /**
     * 編輯
     * @param id
     */
    public void prepareEdit(Long id){
        logger.debug("prepareEdit ...");
        try{
            if( id==null ){
                JsfUtils.buildErrorCallback("未指定編輯項目!");
                return;
            }

            this.editVO = optionsFacade.findById(id);
            logger.debug("prepareEdit editVO = "+editVO);
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "prepareEdit", e, true);
        }
    }
    
    public boolean canEdit(OptionVO vo){
        // TODO
        return true;
    }
    
    public boolean canDelete(OptionVO vo){
        return isAdministrators(this.getLoginUser());
    }

    /**
     * 刪除前檢查
     * @param vo
     * @return 
     */
    public boolean checkDelete(OptionVO vo){
        if( vo==null ){
            JsfUtils.buildErrorCallback("未指定刪除項目!");
            return false;
        }

        this.editVO = optionsFacade.findById(vo.getId());
        if( editVO==null ){
            JsfUtils.addErrorMessage("此項目已刪除!");
            return false;
        }
        
        /*if( vo.getTypeEnum()==OptionEnum.CONTACTS ){
            if( optionsFacade.isRelToContacts(vo) ){
                JsfUtils.addErrorMessage("與往來對象尚有關聯不可刪除!");
                return false;
            }
        }else {
            if( this.category==1 ){
                if( optionsFacade.isRelToAccession(vo) ){
                    JsfUtils.addErrorMessage("與保種紀錄尚有關聯不可刪除!");
                    return false;
                }
            }else if( this.category==2 ){
                if( optionsFacade.isRelToOperation(vo) ){
                    JsfUtils.addErrorMessage("與維管記錄尚有關聯不可刪除!");
                    return false;
                }
            }
        }*/
        
        return true;
    }
    
    /**
     * 刪除 
     * @param vo
     */
    public void delete(OptionVO vo){
        logger.debug("delete vo="+vo);
        ActivityLogEnum acEnum = ActivityLogEnum.D_OPTION;
        try{
            if( !checkDelete(vo) ){
                return;
            }
            
            // 改為只 Disabled
            vo.setDisabled(true);
            optionsFacade.saveVO(vo, this.getLoginUser(), this.isSimulated());
            //if( !optionsFacade.remove(vo.getId()) ){
            //    JsfUtils.buildErrorCallback("移除失敗!(該項目可能已剛被他人刪除)");
            //    return;
            //}

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(), vo.getType(), null, true, this.getLoginUser(), this.isSimulated());

            JsfUtils.buildSuccessCallback();
            
            if( viewId.endsWith("/options.xhtml") ){
                int first = JsfUtils.getPageDataTable(DATATABLE_RESULT);
                this.doQuery();// refresh
                JsfUtils.changePageDataTable(DATATABLE_RESULT, (lazyModel!=null && first<lazyModel.getRowCount())?first:0);
            }
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "deleteOptions", e, true);
                
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(), vo.getType(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     * 輸入資料檢核
     * @return 
     */
    public boolean checkInputData(){
        logger.debug("checkInputData ...");
        boolean hasErr = false;
        if( StringUtils.isBlank(editVO.getCname()) && StringUtils.isBlank(editVO.getEname()) ){
            JsfUtils.addErrorMessage("中、英名稱不可皆為空白!");
            hasErr = true;
        }
    
        // 長度檢核
        if( StringUtils.length(editVO.getCname())>50 ){
            JsfUtils.addErrorMessage("[中文名稱]欄位輸入長度過長!(最多50個字)");
            hasErr = true;
        }
        if( StringUtils.length(editVO.getCname())>100 ){
            JsfUtils.addErrorMessage("[英文名稱]欄位輸入長度過長!(最多100個字元)");
            hasErr = true;
        }
        if( StringUtils.length(editVO.getMemo())>200 ){
            JsfUtils.addErrorMessage("[註解]欄位輸入長度過長!(最多200個字)");
            hasErr = true;
        }
        // 中文或英文名稱，皆不可與其他同類別項目相同
        if( optionsFacade.existsSameName(editVO) ){
            JsfUtils.addErrorMessage("中文或英文名稱，皆不可與其他同類別項目相同!");
            hasErr = true;
        }
    
        return !hasErr;
    }
    
    /**
     *  可儲存
     * @return 
     */
    public boolean canSave(){
        return !readOnly 
            && (viewId.endsWith("/admin/options.xhtml"));
    }
    
    /**
     * 儲存
     */
    public void save(){
        logger.debug("save editVO = "+editVO);
        ActivityLogEnum acEnum = (editVO.getId()!=null)?ActivityLogEnum.U_OPTION:ActivityLogEnum.A_OPTION;
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }
            optionsFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getType(), editVO.getLabel(), true, this.getLoginUser(), this.isSimulated());
            
            JsfUtils.buildSuccessCallback();
            
            if( viewId.endsWith("/options.xhtml") ){    
                int first = JsfUtils.getPageDataTable(DATATABLE_RESULT);
                this.doQuery();// 回查詢列表畫面
                JsfUtils.changePageDataTable(DATATABLE_RESULT, first);
            }
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "save", e, true);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getType(), editVO.getLabel(), false, this.getLoginUser(), this.isSimulated());
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Export">
    /**
     * 匯出
     */
    public void prepareExport() {
        logger.debug("prepareExport ...");
        if (CollectionUtils.isEmpty(filterResultList)) {
            // 無查詢結果
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("common.no.result"));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public OptionVO getEditVO() {
        return editVO;
    }

    public void setEditVO(OptionVO editVO) {
        this.editVO = editVO;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDefAddType() {
        return defAddType;
    }

    public void setDefAddType(String defAddType) {
        this.defAddType = defAddType;
    }

    public ActionEnum getMode() {
        return mode;
    }

    public void setMode(ActionEnum mode) {
        this.mode = mode;
    }

    public BaseLazyDataModel<OptionVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<OptionVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<OptionVO> getResultList() {
        return resultList;
    }

    public void setResultList(List<OptionVO> resultList) {
        this.resultList = resultList;
    }

    public List<OptionVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<OptionVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public BaseCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(BaseCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }
    //</editor-fold>
}
