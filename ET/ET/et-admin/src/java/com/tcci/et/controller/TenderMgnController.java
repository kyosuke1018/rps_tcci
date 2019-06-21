/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.FileController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.enums.LanguageEnum;
import com.tcci.et.facade.EtOptionFacade;
import com.tcci.et.model.OptionVO;
import com.tcci.et.model.TenderVO;
import com.tcci.et.model.criteria.TenderCriteriaVO;
import com.tcci.et.model.rs.LongOptionVO;
import com.tcci.et.entity.EtTender;
import com.tcci.et.enums.TenderMethodEnum;
import com.tcci.et.enums.TenderStatusEnum;
import com.tcci.et.facade.EtTenderFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "tenderMgnController")
@ViewScoped
public class TenderMgnController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 31;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB private EtOptionFacade etOptionFacade;
    @EJB private EtTenderFacade etTenderFacade;
    @EJB SysResourcesFacade sysResourcesFacade;
    
    @ManagedProperty(value = "#{fileController}")
    private FileController fileController;

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }
    
    private boolean showDetail = false;// 顯示詳細資料區塊

    private BaseLazyDataModel<TenderVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<TenderVO> resultList;

    private List<TenderVO> filterResultList; // datatable filter 後的結果
    
    // 查詢條件
    private TenderCriteriaVO criteriaVO;
    private List<SelectItem> areaOptions;
    private List<SelectItem> categoryOptions;
    private List<SelectItem> factoryOptions;
    private List<SelectItem> typeOptions;
    
    private boolean isAdmin;
    private List<CmFactory> owenerfactoryList;
    
    private TenderVO editVO;
    
    @PostConstruct
    private void init() {
        if( functionDenied ){ return; }
        
        // Get view Id
        viewId = JsfUtils.getViewId();

        getInputParams();// 取得輸入參數
        if (viewId.endsWith("/tinymceDlg.xhtml")) {
//            initForInsertUpload(type);// 上傳插入圖片/文件/影片對話框初始動作
            initForInsertDoc();//上傳插入文件對話框初始動作
            return;
        }
        
        isAdmin = sessionController.isUserInRole("ADMINISTRATORS");
        
        criteriaVO = new TenderCriteriaVO();
        criteriaVO.setActive(Boolean.TRUE);//預設排除已刪除
        areaOptions = buildAreaOptions();
        categoryOptions = buildCategoryOptions();
        factoryOptions = buildFactoryOptions();
        typeOptions = buildTypeOptions();
        
        doQuery();
        // 刪除兩小時前的暫存資料 (管理者全刪、使用者刪自己的)
//        etTenderFacade.deleteTemp(isAdministrators(getLoginUser()) ? null : getLoginUser());
    }
    
    /**
     * 取得輸入參數
     */
    private void getInputParams() {
        // 文章刊物類型 (注意: publication.xhtml: A、R、P、F; tinymceDlg.xhtml: I、D、V)
//        String typeCode = JsfUtils.getRequestParameter("type");
//        logger.debug("typeCode = " + typeCode);
//        if (typeCode != null) {
//            type = PublicationEnum.getFromCode(typeCode);
//        }

        // for tinymceDlg.xhtml and link redirect
        String idStr = JsfUtils.getRequestParameter("id");
        if (idStr != null) {
            try {
                Long id = Long.parseLong(idStr);
                editVO = etTenderFacade.findById(id, true);
//                if (type == null) {// link redirect
//                    type = editVO.getTypeEnum();
//                }
                criteriaVO.setId(id);
            } catch (Exception e) {
                logger.error("getInputParams exception:\n", e);
            }
        }
    }
    
    /**
     * 查詢參數檢核
     * @return 
     */
    public boolean doCheck(){
        if( criteriaVO.getKeyword()!=null ){
            criteriaVO.setKeyword(criteriaVO.getKeyword().trim());
        }
        
        if(this.criteriaVO.getStartCloseDate()!=null && this.criteriaVO.getEndCloseDate()!=null){
            if( this.criteriaVO.getStartCloseDate().compareTo(this.criteriaVO.getEndCloseDate()) > 0 ){
                JsfUtils.addErrorMessage("輸入[決標日期]錯誤!(起日需小於等於迄日)");
                return false;
            }
        }
        if(this.criteriaVO.getStartVerifyDate()!=null && this.criteriaVO.getEndVerifyDate()!=null){
            if( this.criteriaVO.getStartVerifyDate().compareTo(this.criteriaVO.getEndVerifyDate()) > 0 ){
                JsfUtils.addErrorMessage("輸入[評標日期]錯誤!(起日需小於等於迄日)");
                return false;
            }
        }
        if(this.criteriaVO.getStartPublishDate()!=null && this.criteriaVO.getEndPublishDate()!=null){
            if( this.criteriaVO.getStartPublishDate().compareTo(this.criteriaVO.getEndPublishDate()) > 0 ){
                JsfUtils.addErrorMessage("輸入[發佈日期]錯誤!(起日需小於等於迄日)");
                return false;
            }
        }
        
        if( criteriaVO.getFactoryId()==null && !isAdmin ){
            List<Long> idList = new ArrayList<>();
            for(SelectItem item:factoryOptions){
                idList.add((Long)item.getValue());
            }
            criteriaVO.setIdList(idList);
        }
        
        return true;
    }
    
    /**
     * 查詢
     */
    public void doQuery() {
        logger.debug("doQuery ...");
        if( !doCheck() ){
            return;
        }
//        if (type == null || !type.isRichContent()) {
//            JsfUtils.addErrorMessage("未指定刊登文章類別!");
//            return;
//        }
        this.showDetail = false;

        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化

//            criteriaVO.setType(type.getCode());
            resultList = etTenderFacade.findByCriteria(this.criteriaVO);
            lazyModel = new BaseLazyDataModel<>(resultList);
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "doQuery", e, false);
        }
    }

    /**
     * 清除
     */
    public void doReset() {
        logger.debug("doReset ...");
        try {
            // 清除條件
            criteriaVO = new TenderCriteriaVO();

            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);

            filterResultList = null; // filterValue 初始化
            resultList = null;
            lazyModel = new BaseLazyDataModel<>();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "doReset", e, false);
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
     * 地區
     * @return 
     */
    List<SelectItem> buildAreaOptions(){
        List<SelectItem> options = new ArrayList<>();
        List<LongOptionVO> result = etOptionFacade.findByTypeOptions("area", "C");
        for(LongOptionVO item : result){
                options.add(new SelectItem(item.getValue(), item.getLabel()));
        }
        return options;
    }
    
    /**
     * 分類
     * @return 
     */
    List<SelectItem> buildCategoryOptions(){
        List<SelectItem> options = new ArrayList<>();
        List<LongOptionVO> result = etOptionFacade.findByTypeOptions("tenderCategory", "C");
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
            owenerfactoryList = cmUserfactoryFacade.findUserFactoryPermission(this.getLoginUser());
            if (CollectionUtils.isNotEmpty(owenerfactoryList)) {
                logger.debug("owenerfactoryList :"+owenerfactoryList.size());
//                queryfactoryList.addAll(owenerfactoryList);
            }
        }
        
        // 查詢工廠選單
        List<CmFactory> result = cmFactoryFacade.findByAreaCode(owenerfactoryList, null, null);
        if (result != null ) {
            logger.debug("buildFactoryOptions options:"+result.size());
            for (CmFactory g : result) {
                options.add(new SelectItem(g.getId(), g.getCode()+"-"+g.getName()));
            }
        }
        
        return options;
    }
    
    /**
     * 招標方式
     * @return 
     */
    List<SelectItem> buildTypeOptions(){
        List<SelectItem> options = new ArrayList<>();
        for (TenderMethodEnum enum1 : TenderMethodEnum.values()) {
            options.add(new SelectItem(enum1.getCode(), enum1.getName()));
        }
        return options;
    }
    
    public String getTypeName(String code){
        TenderMethodEnum enum1 =  TenderMethodEnum.getFromCode(code);
        return enum1!=null?enum1.getName():null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for detail operation">
    /**
     * 新增植物
     */
    public void prepareCreate() {
        logger.debug("prepareCreate ...");
        try {
            this.showDetail = true;
            editVO = new TenderVO();
            saveTemp();// 暫存取得ID for 插入上傳圖
//            editVO.setDataType((type.getDefDataType() != null) ? type.getDefDataType().getCode() : DataTypeEnum.HTML.getCode());// 預設
//            editVO.setNews(type.getNews());
            editVO.setTemp(true);
//            editVO.setDataDate(DateUtils.getToday());
//            editVO.setCoverImg("未上傳封面圖示!");
            editVO.setStatus(TenderStatusEnum.NOT_SALE.getCode());
            editVO.setLang(LanguageEnum.SIMPLIFIED_CHINESE.getShortCode());
            editVO.setDatadate(new Date());

            fileController.init();
//            imgFile = null;
//            uploadCoverImage = false;

            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "prepareCreate", e, false);
        }
    }
    
    /**
     * 預設編輯資料模式
     *
     * @return
     */
    public boolean showRichContent() {
//        return type != null && (type.getDefDataType() == DataTypeEnum.HTML);
        return true;
    }

    /**
     * 暫存取得ID for 插入上傳圖
     */
    public void saveTemp() {
        editVO = new TenderVO();
//        editVO.setType(PublicationEnum.TEMP.getCode());
        etTenderFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());
        logger.debug("saveTemp editVO = " + editVO);
    }

    /**
     * 檢視
     *
     * @param id
     */
    public void prepareView(Long id) {
        logger.debug("prepareView ...");
        this.showDetail = true;
        editVO = new TenderVO();

        JsfUtils.buildSuccessCallback();
    }
    
    /**
     * 可否新增
     *
     * @return
     */
    public boolean canAddDoc() {
        // 固定網頁只有系統管理人員可新增
        // return type != PublicationEnum.FIXEDPAGE || this.isAdministrators(this.getLoginUser());
        return true;
    }
    
    /**
     * 編輯
     *
     * @param id
     */
    public void prepareEdit(Long id) {
        try {
            logger.debug("prepareEdit id = " + id);
            this.showDetail = true;
            etTenderFacade.find(id);
            editVO = etTenderFacade.findById(id, true);
            editVO.setTemp(false);

            fileController.init();
            // for 封面圖示
//            if (type != null && type.getHasCoverImg()) {
//                imgFile = null;
//                uploadCoverImage = false;
//                // 準備顯示封面圖片
//                photoGalleryFacade.loadCoverImage(editVO);
//                if (editVO.getCoverImgVO() != null) {
//                    imageController.setDomainName(PhotoGalleryEnum.DOC.getDomainName());
//                    imageController.prepareImage(editVO.getCoverImgVO(), true);
//                    logger.debug("prepareEdit editVO.getCoverImgVO() = " + editVO.getCoverImgVO().getFileName());
//                }
//            }

            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "prepareEdit", e, false);
        }
    }

    /**
     * 可否編輯
     *
     * @param vo
     * @return
     */
    public boolean canEditDoc(TenderVO vo) {
        return true;
    }
    
    /**
     * 可否下架
     *
     * @param vo
     * @return
     */
    public boolean canOutofDoc(TenderVO vo) {
        if (vo.getStatusEnum() == TenderStatusEnum.REMOVE) {
            return false;
        }
        // 固定網頁只有系統管理人員可刪除
        return (this.getLoginUser() != null && this.getLoginUser().equals(vo.getLastUpdateUser()));
    }
    /**
     * 下架文章
     *
     * @param vo
     */
    public void outofDoc(TenderVO vo) {
        logger.debug("deleteDoc ...");
        ActivityLogEnum acEnum = ActivityLogEnum.U_TENDER;
        try {
            vo.setStatus(TenderStatusEnum.DRAFT.getCode());
            etTenderFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getTitle(), null, true, this.getLoginUser(), this.isSimulated());

            this.doQuery();
            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "outofDoc", e, false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getTitle(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }

    /**
     * 可否刪除
     *
     * @param vo
     * @return
     */
    public boolean canDeleteDoc(TenderVO vo) {
        // 固定網頁只有系統管理人員可刪除
        //return (type != PublicationEnum.FIXEDPAGE || this.isAdministrators(this.getLoginUser()))
        //        && (this.getLoginUser() != null && this.getLoginUser().equals(vo.getLastUpdateUser()));
        return (this.getLoginUser() != null && this.getLoginUser().equals(vo.getLastUpdateUser()));
    }
    
    /**
     * 刪除前檢查
     *
     * @param vo
     * @return
     */
    public boolean checkDelete(TenderVO vo) {
        if (vo.getStatusEnum() != TenderStatusEnum.DRAFT) {
            JsfUtils.addErrorMessage("已發佈案件不可刪除!(若確定要刪除，請先[下架]後，再刪除。)");
//            JsfUtils.addErrorMessage("已公告案件不可刪除!(若確定要刪除，請先[下架]後，再刪除。)");
            return false;
        }
        return true;
    }
    
    /**
     * 刪除文章
     *
     * @param vo
     */
    public void deleteDoc(TenderVO vo) {
        logger.debug("deleteDoc ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_TENDER;
        try {
            if (!checkDelete(vo)) {// 刪除前檢查
                return;
            }

            etTenderFacade.deleteDoc(vo, this.getLoginUser(), this.isSimulated());

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getTitle(), null, true, this.getLoginUser(), this.isSimulated());

            editVO = new TenderVO();
            this.doQuery();

            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "deleteDoc", e, false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getTitle(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     * 刪除上傳檔
     *
     * @param attachmentVO
     */
    public void deleteFile(AttachmentVO attachmentVO) {
        logger.debug("deleteFile ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_TENDER_FILE;
        try {
            if (editVO.getDocs() != null) {
                // 未建立關聯直接刪除
                if (attachmentVO.getApplicationdata() == null
                        || attachmentVO.getApplicationdata().getId() == null) {
                    fileController.deleteAttachment(attachmentVO, this.isSimulated());
                } else {
                    // 註記刪除，於儲存時處理。
                    if (editVO.getRemovedDocs() == null) {
                        editVO.setRemovedDocs(new ArrayList<>());
                    }
                    editVO.getRemovedDocs().add(attachmentVO);
                }
                //logger.debug("deleteFile editVO.getDocs() = "+editVO.getDocs().size());
                editVO.getDocs().remove(attachmentVO);// for UI
                //logger.debug("deleteFile remove "+attachmentVO.getFileName());
                //logger.debug("deleteFile editVO.getDocs() = "+editVO.getDocs().size());
                
                cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                        editVO.getTitle(), null, true, this.getLoginUser(), this.isSimulated());
            }
            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "deleteFile", e, true);
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getTitle(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     * 關閉明細
     */
    public void closeDocDetail() {
        logger.debug("closeDocDetail ...");
        showDetail = false;
        // 編輯頁較複雜，儲存後維持在編輯頁，關閉才Refresh
        this.doQuery();// Refresh
    }
    
    /**
     * 輸入資料檢核
     *
     * @return
     */
    public boolean checkInputData() {
        logger.debug("checkInputData ...");
        boolean hasErr = false;

        // 共用欄位
        if (StringUtils.isBlank(editVO.getTitle())) {// 必填
            JsfUtils.addErrorMessage("未輸入[標題]!");
            hasErr = true;
        }
        if (editVO.getDatadate() == null) {// 必填
            JsfUtils.addErrorMessage("未輸入[發佈日期]!");
            hasErr = true;
        }
        if (editVO.getSsaleDate() == null || editVO.getEsaleDate() == null) {// 必填
//            JsfUtils.addErrorMessage("未輸入[標書發售日期]!");
//            hasErr = true;
        }else{
            if( this.editVO.getSsaleDate().compareTo(this.editVO.getEsaleDate()) > 0 ){
                JsfUtils.addErrorMessage("輸入[標書發售日期]錯誤!(起日需小於等於迄日)");
                hasErr = true;
            }
        }
        if (editVO.getStenderDate() == null || editVO.getEtenderDate() == null) {// 必填
            JsfUtils.addErrorMessage("未輸入[招標日期]!");
            hasErr = true;
        }else{
            if( this.editVO.getStenderDate().compareTo(this.editVO.getEtenderDate()) > 0 ){
                JsfUtils.addErrorMessage("輸入[招標日期]錯誤!(起日需小於等於迄日)");
                hasErr = true;
            }
        }
        if (StringUtils.length(editVO.getTitle()) > 200) {
            JsfUtils.addErrorMessage("[標題]欄位輸入長度過長!(最多200個字)");
            hasErr = true;
        }
//        if (StringUtils.length(editVO.getSummary()) > 500) {
//            JsfUtils.addErrorMessage("[大綱]欄位輸入長度過長!(最多500個字)");
        if (StringUtils.length(editVO.getSummary()) > 1500) {
            JsfUtils.addErrorMessage("[大綱]欄位輸入長度過長!(最多1500個字)");
            hasErr = true;
        }
        if (StringUtils.length(editVO.getCode()) > 20) {
            JsfUtils.addErrorMessage("[編號]欄位輸入長度過長!(最多20個英數字)");
            hasErr = true;
        }
        
        if ((fileController.getFiles() == null || fileController.getFiles().isEmpty())
                && (editVO.getDocs() == null || editVO.getDocs().isEmpty())) {
//            if (type != PublicationEnum.NEWS) {// NEWS 可能無PDF檔
                JsfUtils.addErrorMessage("未上傳任何檔案!");
                hasErr = true;
//            }
        }
//        
        if (StringUtils.isBlank(editVO.getContents())) {// 必填
//            JsfUtils.addErrorMessage("未輸入[招標公告]!");
//            hasErr = true;
        }

        return !hasErr;
    }
    
    /**
     * 儲存
     */
    public void saveDoc() {
        logger.debug("saveDoc ...");
        ActivityLogEnum acEnum = (editVO.isTemp() ? ActivityLogEnum.A_TENDER : ActivityLogEnum.U_TENDER);

        logger.debug("saveDoc ...");
        try {
            // 輸入資料檢核
            if (!checkInputData()) {
                return;
            }
//            editVO.setType(type.getCode());
//            if (!checkBeforeSave(editVO)) {
//                JsfUtils.buildErrorCallback();
//                return;
//            }

            // 儲存上傳檔以外資訊
            if (StringUtils.isNotBlank(editVO.getContents())) {
//                logger.info("saveDoc Contents:"+editVO.getContents());
                etTenderFacade.saveAll(editVO, this.getLoginUser(), this.isSimulated());
            }else{
                etTenderFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());
            }

            // 儲存後處理(UI、Files、Solr)
            EtTender entity = etTenderFacade.find(editVO.getId());
            // 上傳檔異動處理
                processUploadFiles(entity);
//            if (editVO.getCoverImg() != null) {
//                saveCoverImage();// 儲存封面圖示
//            }

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getTitle(), null, true, this.getLoginUser(), this.isSimulated());

            // 編輯頁較複雜，維持在編輯頁
            // this.doQuery();
            this.prepareEdit(editVO.getId());

            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            JsfUtils.addErrorMessage("儲存發生錯誤!(可能資料過長或格式錯誤)");
            processUnknowException(this.getLoginUser(), "saveDoc", e, true);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getTitle(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     * 上傳檔異動處理
     *
     * @param entity
     * @throws java.lang.Exception
     */
    public void processUploadFiles(EtTender entity) throws Exception {
        logger.info("processUploadFiles getFiles()"+fileController.getFiles());
        if (fileController.getFiles() != null && !fileController.getFiles().isEmpty()) {
            fileController.saveUploadedFiles(entity, false, this.getLoginUser(), this.isSimulated());
        } else {
            // 取得並保留已存在 App Ids 至 existedAppIds，以便後續分辨哪些是本次上傳的
            fileController.findExistedAttachments(entity);
        }
        // 刪除註記刪除檔
        if (editVO.getRemovedDocs() != null && !editVO.getRemovedDocs().isEmpty()) {
            for (AttachmentVO vo : editVO.getRemovedDocs()) {
                fileController.deleteAttachment(vo, this.isSimulated());
            }
        }
    }
    
    /**
     * 模擬 Web Preview
     */
    public void webPreview() {
        logger.debug("webPreview id = " + this.editVO.getId());
        TenderVO tmpVO = saveDocTempForPreview();// 暫存
        // 回傳 html 呼叫 RESTful 需要參數
        RequestContext rc = JsfUtils.buildSuccessCallback();
//        rc.addCallbackParam("pubType", tmpVO.getType());
        rc.addCallbackParam("code", tmpVO.getCode());
//        rc.addCallbackParam("realType", this.type.getCode());
    }
    /**
     * 另存為一筆暫存資料 for Preview
     *
     * @return
     */
    public TenderVO saveDocTempForPreview() {
        logger.debug("saveDocTempForPreview ...");
        try {
            TenderVO tmpVO = new TenderVO();
            ExtBeanUtils.copyProperties(tmpVO, editVO);
            String code = UUID.randomUUID().toString();
            // 另存為一筆暫存資料
            tmpVO.setId(null);
//            tmpVO.setType(PublicationEnum.TEMP.getCode());
            tmpVO.setCode(code);
            tmpVO.setHtmlId(null);// 需新產生

            // 儲存上傳檔以外資訊
            etTenderFacade.saveAll(tmpVO, this.getLoginUser(), this.isSimulated());

            return tmpVO;
        } catch (Exception e) {
            JsfUtils.addErrorMessage("預覽暫存發生錯誤! (可能資料過長或格式錯誤)");
            processUnknowException(this.getLoginUser(), "saveDocTempForPreview", e, true);
        }
        return null;
    }

    /**
     * Web Preview 網頁 URL
     *
     * @return
     */
    public String getWebPreviewPage() {
        return sysResourcesFacade.getDocPreviewUrl();
    }

    /**
     * 選取廠別
     */
    public void selectFactory(){
        logger.debug("selectFactory ... factoryId = "+editVO.getFactoryId());
        
        if( editVO.getFactoryId()>0 ){
            List<OptionVO> result = etOptionFacade.findArea(editVO.getFactoryId(), null);
            editVO.setAreaId(result.get(0).getId());
        }else{
            editVO.setAreaId(null);
        }
    }
    
    
    /**
     * link 詢價單查詢
     *
     * @param vo
     */
    public void redirectRfq(TenderVO vo) {
        logger.debug("redirectRfq ...");
        try {
            redirect("../rfq/createRFQ.xhtml?tenderId="+vo.getId());
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "redirectRfq", e, false);
        }
    }
    
    /**
     * link 投標查詢
     *
     * @param vo
     */
    public void redirectTender(TenderVO vo) {
        logger.debug("redirectRfq ...");
        try {
            redirect("conformQuery.xhtml?tenderId="+vo.getId());
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "redirectRfq", e, false);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for tinymceDlg.xhtml only">
    
    // 上傳插入文件對話框初始動作
    private void initForInsertDoc() {
        if (this.editVO == null || this.editVO.getId() == null) {
            logger.error("initForInsertDoc error editVO==null");
        } else {
            // for 上傳插入檔關聯 KbPublication 
            EtTender tempEntity = etTenderFacade.find(this.editVO.getId());
            if (tempEntity == null) {
                logger.error("initForInsertDoc error tempEntity==null");
            } else {
                // 直接使用 KbPublication 做 ContentHolder
                // fileController 初始設定
                fileController.setContentHolderTemp(tempEntity);
                fileController.setOperator(this.getLoginUser());
            }
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Export">
    /**
     * 匯出
     */
    public void prepareExport(){
        logger.debug("prepareExport ...");
        if (CollectionUtils.isEmpty(filterResultList)) {
            // 無查詢結果
            JsfUtils.addErrorMessage(JsfUtils.getResourceTxt("common.no.result"));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    
    public TenderCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(TenderCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public List<SelectItem> getAreaOptions() {
        return areaOptions;
    }

    public void setAreaOptions(List<SelectItem> areaOptions) {
        this.areaOptions = areaOptions;
    }

    public List<SelectItem> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOptions(List<SelectItem> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }
    
    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public BaseLazyDataModel<TenderVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<TenderVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<TenderVO> getResultList() {
        return resultList;
    }

    public void setResultList(List<TenderVO> resultList) {
        this.resultList = resultList;
    }

    public List<TenderVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<TenderVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public TenderVO getEditVO() {
        return editVO;
    }

    public void setEditVO(TenderVO editVO) {
        this.editVO = editVO;
    }

    public List<SelectItem> getFactoryOptions() {
        return factoryOptions;
    }

    public List<SelectItem> getTypeOptions() {
        return typeOptions;
    }

    public void setTypeOptions(List<SelectItem> typeOptions) {
        this.typeOptions = typeOptions;
    }
    //</editor-fold>
}
