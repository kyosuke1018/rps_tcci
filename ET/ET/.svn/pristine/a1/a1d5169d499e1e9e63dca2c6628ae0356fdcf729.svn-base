/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.content;

import com.tcci.cm.controller.global.FileController;
import com.tcci.cm.controller.global.ImageController;
import com.tcci.cm.enums.ActionEnum;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.et.entity.KbPhotoGallery;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.facade.KbPhotoGalleryFacade;
import com.tcci.cm.model.global.ImageVO;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.model.PhotoGalleryVO;
import com.tcci.et.model.PublicationVO;
import com.tcci.et.model.criteria.MediaCriteriaVO;
import com.tcci.et.model.criteria.PublicationCriteriaVO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * 圖庫管理
 * @author Peter.pan
 */
@ManagedBean(name = "photos")
@ViewScoped
public class PhotosController extends MediaController implements Serializable {
    private static final long FUNC_OPTION = 25;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    @EJB KbPhotoGalleryFacade photoGalleryFacade;
    @EJB AttachmentFacade attachmentFacade;
    @EJB ImageFacade imageFacade;
    
    @ManagedProperty(value = "#{fileController}")
    private FileController fileController;
    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }
    @ManagedProperty(value = "#{imageController}")
    private ImageController imageController;
    public void setImageController(ImageController imageController) {
        this.imageController = imageController;
    }
    
    // tree
    private TreeNode root;
    private TreeNode selectedNode;
    private boolean showTree = true;
    private Map<Long, Boolean> expandMap;
    private boolean first = true;
    
    // query
    private List<PhotoGalleryVO> imgList;
    private BaseLazyDataModel<PhotoGalleryVO> lazyModel; // LazyModel for primefaces datatable lazy loading
            
    // edit
    private String mode;
    // private ActionEnum editMode = ActionEnum.MODIFY;
    private PhotoGalleryVO editVO;
    private PhotoGalleryVO editSubVO;
    private PublicationVO editPubVO;

    private List<PhotoGalleryVO> allList;
    private List<PhotoGalleryVO> resList;
    
    // 顯示圖片
    private Map<Long, String> imageSrcMap;// 存放每筆資料圖片顯示URL
    private String showImgKey;// 安全檢核碼
    
    // for 選取文章插圖
    private Long docId;
    
    @PostConstruct
    private void init(){
        logger.debug("PhotosController init ...");
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if("kyle.cheng".equals(this.getLoginUser().getLoginAccount())){
            functionDenied = true;
        }else{
        if( functionDenied ){ return; }
        }
        // Get view Id
        viewId = JsfUtils.getViewId();
        
        if( viewId.endsWith("/photosSelect.xhtml") ){// 自圖庫選圖
            mode = ActionEnum.CHOICE.getCode();
            getInputParams();
        }else{
            mode = ActionEnum.MODIFY.getCode();
        }
        
        expandMap = new HashMap<Long, Boolean>();// 記錄樹節點展開狀況
        rebuildTree();// 建立樹狀圖
        // prepareShowImage();// 初始準備顯示圖片作業
        criteriaVO = new MediaCriteriaVO();
    }
    
    /**
     * 取得輸入參數
     */
    private void getInputParams(){
        // for 選取文章插圖
        String idStr = JsfUtils.getRequestParameter("docId");
        if( idStr!=null ){
            try{
                docId = Long.parseLong(idStr);
                logger.info("getInputParams docId="+docId);
            }catch(Exception e){
                logger.error("getInputParams exception:\n", e);
            }
        }
    }
    
    /**
     * 資料夾
     * @return 
     */
    public List<SelectItem> getCustomParentOptions(Long excludeId){
        return getParentFolderOptions(PhotoGalleryEnum.CUSTOM, excludeId);
    }
    public List<SelectItem> getParentFolderOptions(PhotoGalleryEnum typeEnum, Long excludeId){
        //List<PhotoGalleryVO> list = photoGalleryFacade.findEmptyFolders(typeEnum);// 不包含圖的資料夾
        List<SelectItem> ops = new ArrayList<SelectItem>();
        ops.add(new SelectItem("0", typeEnum.getName()));// 自訂相簿/圖庫
        
        if( allList!=null ){
            // 找出 excludeId 所有下層 ID，皆不可設定
            List<Long> excludeList = new ArrayList<Long>();
            findSubFolder(allList, excludeId, excludeList);
            
            for(PhotoGalleryVO vo : allList){
                if( PhotoGalleryEnum.CUSTOM == vo.getPrimaryTypeEnum() ){
                    if( !excludeList.contains(vo.getId()) ){// 過濾不可設定節點
                        String label = (vo.getParentName()!=null)? vo.getParentName()+"/"+vo.getCname():vo.getCname();
                        ops.add(new SelectItem(vo.getId().toString(), label));
                    }
                }
            }
        }
        return ops;
    }
    
    /**
     * 遞迴找出下層節點
     * @param allList
     * @param keyId
     * @param resList 
     */
    public void findSubFolder(List<PhotoGalleryVO> allList, Long keyId, List<Long> resList){
        if( keyId==null || allList==null || allList.isEmpty() ){
            return;
        }
        resList.add(keyId);
        
        for(PhotoGalleryVO vo : allList){
            if( keyId.equals(vo.getParent()) ){
                findSubFolder(allList, vo.getId(), resList);
            }
        }
    }
    
    public void doQueryDoc(){
        logger.debug("doQueryDoc ...");
        findDocImages(PublicationEnum.getFromCode(this.editVO.getType()));// 查詢文章插圖
        //prepareImagesUrl(imgList);// 準備 image URL (顯示該頁才執行提升校能)
        lazyModel = new BaseLazyDataModel<PhotoGalleryVO>(imgList);
    }

    public void doResetDoc(){
        logger.debug("doResetDoc ...");
        PublicationCriteriaVO docCriteriaVO = new PublicationCriteriaVO();
        this.criteriaVO.setDocCriteriaVO(docCriteriaVO);
    }

    /**
     * 編輯操作
     */
    public void changeEditMode(){
        logger.debug("changeEditMode editMode = "+this.mode);
        if( ActionEnum.MODIFY.getCode().equals(mode) ){
            prepareEditNode();// 編輯基本資料
        }else if( ActionEnum.ADDSUB.getCode().equals(mode) ){
            prepareAddSubNode();// 新增子資料夾/相簿
        }else if( ActionEnum.UPLOAD.getCode().equals(mode) ){
            prepareUpload();// 新增包含圖片
        }
    }
    
    /**
     * 準備顯示圖片列表
     * @param vo 
     */
    public void prepareIncludeImages(PhotoGalleryVO vo){
        logger.debug("prepareIncludeImages "+vo.getPrimaryTypeEnum()+ ", vo = "+vo);
        imgList = new ArrayList<PhotoGalleryVO>();
        
        if( PhotoGalleryEnum.CUSTOM == vo.getPrimaryTypeEnum() ){
            imgList = photoGalleryFacade.findImagesByParent(vo.getId());// 關連 ContentHolder
            // imgList = photoGalleryFacade.findCustomImages(list);
        }else if( PhotoGalleryEnum.DOC == vo.getPrimaryTypeEnum() ){
            findDocImages(PublicationEnum.getFromCode(vo.getType()));
        }
        logger.debug("prepareIncludeImages imgList="+((imgList!=null)?imgList.size():0));
        
        //prepareImagesUrl(imgList);// 準備 image URL (顯示該頁才執行提升校能)
        lazyModel = new BaseLazyDataModel<PhotoGalleryVO>(imgList);
    }

    /**
     * 查詢文章插圖
     * @param docType
     */ 
    public void findDocImages(PublicationEnum docType){
        logger.debug("findDocImages ...");
        criteriaVO.setType((docType!=null)?docType.getCode():null);// 文章大分類
        criteriaVO.setGetFvInfo(true);// 效能考量-直接取得 attachmentVO 資訊 (1對1適用)
        imgList = photoGalleryFacade.findImagesByDoc(criteriaVO);// 關連 ContentHolder
    }
    
    /**
     * 準備 image URL
     * imgList 須已包含 AttachmentVO 資訊
     * @param imgList 
     */
    public void prepareImagesUrl(List<PhotoGalleryVO> imgList){
        if( imgList!=null ){
            for(PhotoGalleryVO pgVO : imgList){
                // 設定圖片存取位置
                prepareImageUrl(pgVO);
            }
        }
    }
    public void prepareImageUrl(PhotoGalleryVO pgVO){
        // 設定圖片存取位置
        imageController.setDomainName(pgVO.getPrimaryTypeEnum().getDomainName());
        boolean isPublic = true;// 有用於文章插圖需為 Public URL
        String url = imageController.prepareImage(pgVO.getAttachmentVO(), isPublic);

        if( url!=null ){
            logger.debug("prepareImageUrl url = "+url);
            pgVO.setUrl(url);
            pgVO.setDescription(pgVO.getAttachmentVO().getDescription());// for UI顯示
            pgVO.setDescription(pgVO.getDescription()==null? pgVO.getAttachmentVO().getFileName():null);
        }else{
            logger.error("prepareImageUrl error: not image url with pgVO = "+pgVO);
        }
    }
    
    /**
     * for 多圖展示 primefaces galleria
     * @param idx 
     */
    public void prepareGalleriaImages(int idx){
        logger.debug("prepareGalleriaImages ... idx = "+idx);
        if( imgList!=null ){
            List<AttachmentVO> images = new ArrayList<AttachmentVO>();
            if( idx<imgList.size() ){
                for(int i=idx; i<imgList.size(); i++){
                    images.add(imgList.get(i).getAttachmentVO());
                }
            }
            if( idx>0 ){
                for(int i=0; i<idx; i++){
                    images.add(imgList.get(i).getAttachmentVO());
                }
            }
            imageController.setMulti(true);
            imageController.setImages(images);
        }
    }
    
    /**
     * 準備編輯目錄
     */
    public void prepareEditNode(){
        // editMode = ActionEnum.MODIFY;
        if( !this.isChoiceOnly() ){
            mode = ActionEnum.MODIFY.getCode();
        }
        logger.debug("prepareEditNode editVO = "+editVO);
    }
    
    /**
     * 輸入資料檢核
     * 
     * @param vo
     * @return 
     */
    public boolean checkInputDataBasic(PhotoGalleryVO vo){
        logger.debug("checkInputDataBasic vo = "+vo);
        boolean hasErr = false;

        if( StringUtils.length(vo.getCname())>40 ){
            JsfUtils.addErrorMessage("[名稱]欄位輸入長度過長!(最多40個字)");
            hasErr = true;
        }
        if( StringUtils.length(vo.getSubject())>100 ){
            JsfUtils.addErrorMessage("[標題]欄位輸入長度過長!(最多100個字)");
            hasErr = true;
        }
        if( StringUtils.length(vo.getDescription())>500 ){
            JsfUtils.addErrorMessage("[簡介]欄位輸入長度過長!(最多500個字)");
            hasErr = true;
        }
        
        return !hasErr;
    }
    public boolean checkInputData(){
        logger.debug("checkInputData editMode = "+this.mode);
        if( ActionEnum.MODIFY.getCode().equals(mode) ){ // 編輯基本資料
            if( StringUtils.isBlank(editVO.getCname()) ){// 必填
                JsfUtils.addErrorMessage("未輸入[名稱]!");
                return false;
            }
            if( editVO.getDataDate()==null ){// 必填
                JsfUtils.addErrorMessage("未輸入[資料日期]!");
                return false;
            }
            return checkInputDataBasic(this.editVO);
        }else if( ActionEnum.ADDSUB.getCode().equals(mode) ){ // 新增子資料夾/相簿
            if( StringUtils.isBlank(editSubVO.getCname()) ){// 必填
                JsfUtils.addErrorMessage("未輸入[名稱]!");
                return false;
            }
            if( editSubVO.getDataDate()==null ){// 必填
                JsfUtils.addErrorMessage("未輸入[資料日期]!");
                return false;
            }
            return checkInputDataBasic(this.editSubVO);
        }else if( ActionEnum.UPLOAD.getCode().equals(mode) ){ // 新增包含圖片
            if( this.editSubVO.getId()==null ){
                if( fileController.getFiles()==null || fileController.getFiles().isEmpty()  ){
                    JsfUtils.addErrorMessage("未上傳任何檔案!");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean canSaveUpload(){
        if( ActionEnum.UPLOAD.getCode().equals(mode) ){ // 新增包含圖片 
            if( this.editSubVO.getId()==null ){
                return (fileController.getFiles()!=null && !fileController.getFiles().isEmpty());
            }else{// 編輯圖片
                return true;
            }
        }
        return false;
    }
    
    /**
     * 基本資料儲存
     */
    public void saveBasic(){
        logger.debug("saveBasic ...");
        ActivityLogEnum acEnum = ActivityLogEnum.U_PHOTO_FOLDER;
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }

            photoGalleryFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(), 
                    editVO.getCname(), null, true, this.getLoginUser(), this.isSimulated());
            
            rebuildTree();// TODO
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "saveBasic", e, false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(), 
                    editVO.getCname(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
       
    /**
     * 準備新增子目錄
     */
    public void prepareAddSubNode(){
        //editMode = ActionEnum.ADDSUB;
        if( !this.isChoiceOnly() ){
            mode = ActionEnum.ADDSUB.getCode();
        }
        editSubVO = new PhotoGalleryVO();
        editSubVO.setParent(editVO.getId());// 父目錄
        editSubVO.setPrimaryType(editVO.getPrimaryType());
    }

    /**
     * 子資料夾/相簿儲存
     */
    public void saveFolder(){
        logger.debug("saveFolder ...");
        ActivityLogEnum acEnum = ActivityLogEnum.A_PHOTO_FOLDER;
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }
            photoGalleryFacade.saveVO(editSubVO, this.getLoginUser(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editSubVO.getId(),
                    editSubVO.getCname(), editSubVO.getDescription(), true, this.getLoginUser(), this.isSimulated());
            
            rebuildTree();// TODO
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "saveFolder", e, false);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editSubVO.getId(),
                    editSubVO.getCname(), editSubVO.getDescription(), false, this.getLoginUser(), this.isSimulated());
        }
    }
       
    /**
     * 準備上傳圖片
     */
    public void prepareUpload(){
        //editMode = ActionEnum.UPLOAD;
        if( !this.isChoiceOnly() ){
            mode = ActionEnum.UPLOAD.getCode();
        }
        // editPubVO = new PublicationVO();
        // for 上傳圖片預設值
        editSubVO = new PhotoGalleryVO();
        editSubVO.setParent(editVO.getId());// 父目錄
        editSubVO.setPrimaryType(PhotoGalleryEnum.IMAGE.getCode());
        editSubVO.setStatus(ContentStatusEnum.DRAFT.getCode());

        fileController.init();
    }

    /**
     * 儲存圖片
     */
    public void saveImages(){
        logger.debug("saveImages ... mode = "+mode);
        ActivityLogEnum acEnum = ActivityLogEnum.A_PHOTO;
        try{
            // 輸入資料檢核
            if( !checkInputData() ){
                return;
            }

            if( this.editSubVO.getId()==null ){
                // 儲存上傳檔
                // 每筆 PhotoGalleryVO 只會關聯一個圖片 (for 控制每個圖片是否可發佈)
                if( fileController.getFiles()!=null && !fileController.getFiles().isEmpty() ){
                    List<KbPhotoGallery> pgList = photoGalleryFacade.prepareImageHolders(
                            editVO.getId(), 
                            editSubVO.getPrimaryType(), null, 
                            editSubVO.getSubject(), 
                            editSubVO.getDescription(),
                            editSubVO.getStatus(),
                            null,
                            fileController.getFiles().size(), this.getLoginUser(), this.isSimulated());

                    TcDomain domain = imageFacade.getDomain(editSubVO.getPrimaryTypeEnum().getDomainName());
                    this.fileController.saveUploadedFilesByMultiHolder(pgList, domain, this.getLoginUser(), this.isSimulated());
                    // 縮圖
                    if( pgList!=null ){
                        for(KbPhotoGallery pgVO : pgList){
                            ImageVO retImgVO = new ImageVO();
                            String retMsg = imageFacade.compressImageByHolder(pgVO, retImgVO);
                            if( !StringUtils.isBlank(retMsg) ){
                                JsfUtils.addErrorMessage(retMsg);
                            }
                            photoGalleryFacade.saveImageInfo(pgVO, retImgVO, this.getLoginUser(), this.isSimulated());// 儲存圖片資訊
                        }
                    }
                }
                fileController.init();// 回復上傳初始狀態
            }else{// 編輯圖片
                logger.debug("saveImages editSubVO = "+editSubVO);
                photoGalleryFacade.saveVO(editSubVO, this.getLoginUser(), this.isSimulated());
            }
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editSubVO.getSubject(), null, true, this.getLoginUser(), this.isSimulated());
            
            prepareIncludeImages(editVO);// reload include images
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "saveImages", e, false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editSubVO.getSubject(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
            
    /**
     * 變更照片狀態
     * @param pgVO 
     */
    public void changeImgStatus(PhotoGalleryVO pgVO){
        logger.debug("changeImgStatus ... pgVO.getStatus() = "+pgVO.getStatus());
        ActivityLogEnum acEnum = ActivityLogEnum.U_PHOTO;
        try{
            photoGalleryFacade.saveVO(pgVO, this.getLoginUser(), this.isSimulated());

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), pgVO.getId(),
                    pgVO.getSubject(), null, true, this.getLoginUser(), this.isSimulated());

            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "changeImgStatus", e, false);

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), pgVO.getId(),
                    pgVO.getSubject(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    public boolean canUploadImage(){
        return (ActionEnum.UPLOAD.getCode().equals(mode) && editSubVO!=null && editSubVO.getId()==null);
    }
    
    /**
     * 刪除前檢查
     * @param vo
     * @return 
     */
    public boolean checkDelete(PhotoGalleryVO vo){
        if( vo.getPrimaryTypeEnum() == PhotoGalleryEnum.CUSTOM ){// 資料夾
            // 包含子項目
            PhotoGalleryVO theVO = photoGalleryFacade.findById(vo.getId(), false);
            if( theVO.getSubCounts()>0 ){
                JsfUtils.addErrorMessage("有包含子項目(子資料夾或圖片)不可刪除!");
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 刪除資料夾/相簿
     * @param vo
     */
    public void deleteFolder(PhotoGalleryVO vo){
        logger.debug("deleteFolder ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_PHOTO_FOLDER;
        try{
            if( !checkDelete(vo) ){
                return;
            }
            photoGalleryFacade.remove(vo.getId(), this.isSimulated());
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getCname(), null, true, this.getLoginUser(), this.isSimulated());
            // this.selectedNode = node.getParent();
            first = true;// 變更選取至預設
            rebuildTree();// TODO
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "deleteFolder", e, true);
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getCname(), null, false, this.getLoginUser(), this.isSimulated());
        }
    }
    
    /**
     * 刪除圖片 
     * @param vo
     */
    public void deleteImage(PhotoGalleryVO vo){
        ActivityLogEnum acEnum = ActivityLogEnum.D_PHOTO;
        try{
            logger.debug("deleteImage vo.getPrimaryType() = "+vo.getPrimaryType());

            if( PhotoGalleryEnum.IMAGE.getCode().equals(vo.getPrimaryType()) ){// 安全檢核
                // 檢查是否是文章插圖
                if( !photoGalleryFacade.isDocImage(vo.getId()) ){
                    photoGalleryFacade.removePhotoById(vo.getId(), this.getLoginUser(), this.isSimulated());

                    imgList.remove(vo);// for UI

                    cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                            vo.getSubject(), null, true, this.getLoginUser(), this.isSimulated());

                    JsfUtils.buildSuccessCallback();
                }else{
                    JsfUtils.addErrorMessage("有被文章引用為插圖，不可刪除!(建議狀態改為[下架]即可)");
                }
                return;
            }else{
                JsfUtils.addErrorMessage("無法刪除!");
            }
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "deleteImage", e, false);
        }
        
        cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                vo.getSubject(), null, false, this.getLoginUser(), this.isSimulated());
    }
    
    /**
     * 編輯圖片 
     * @param vo
     */
    public void editImage(PhotoGalleryVO vo){
        try{
            logger.debug("editImage vo.getPrimaryType() = "+vo.getPrimaryType());
            if( PhotoGalleryEnum.IMAGE.getCode().equals(vo.getPrimaryType()) ){// 安全檢核
                KbPhotoGallery entity = photoGalleryFacade.find(vo.getId());
                logger.debug("editImage id = "+vo.getId()+", entity = "+entity);
                if( entity!=null ){
                    this.editSubVO = new PhotoGalleryVO();
                    ExtBeanUtils.copyProperties(this.editSubVO, vo);
                    this.mode = ActionEnum.UPLOAD.getCode();
                }
                JsfUtils.buildSuccessCallback();
            }else{
                JsfUtils.addErrorMessage("無法編輯!");
            }
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "editImage", e, false);
        }
    }
    
    /**
     * 照片可否編輯
     * @param vo
     * @return 
     */
    public boolean canEditImage(PhotoGalleryVO vo){
        logger.debug("canEditImage ...");
        
        return (!isChoiceOnly()) && PhotoGalleryEnum.IMAGE.getCode().equals(vo.getPrimaryType());
    }
    
    /**
     * 照片可否刪除
     * @param vo
     * @return 
     */
    public boolean canDeleteImage(PhotoGalleryVO vo){
        logger.debug("canDeleteImage ...");
        
        return (!isChoiceOnly()) && PhotoGalleryEnum.IMAGE.getCode().equals(vo.getPrimaryType());
    }
    
    /**
     * 相簿可否刪除
     * @param vo
     * @return 
     */
    public boolean canDeleteFolder(PhotoGalleryVO vo){
        logger.debug("canDeleteFolder ...");
        
        return (!isChoiceOnly()) && (!vo.getId().equals(0L)) && (vo.getSubCounts()==0 && StringUtils.isBlank(vo.getCode()));
    }
    
    /**
     * 自圖庫選取文章插圖 (加一筆KbPhotoGallery，用來判斷圖庫圖片是否被文章引用)
     * @param vo 
     */
    public void selectDocImage(PhotoGalleryVO vo){
        logger.debug("selectDocImage docId="+docId+"; vo ="+vo);
        
        try{
            KbPhotoGallery photoGallery = new KbPhotoGallery();
            photoGallery.setPrimaryType(PhotoGalleryEnum.DOC.getCode());// 文章插圖
            photoGallery.setPrimaryId(this.docId);// 記錄關聯 KbPublication ID 
            photoGallery.setParent(vo.getId());// 記錄關聯 KbPhotoGallery ID 
            photoGallery.setStatus(ContentStatusEnum.PUBLISH.getCode());
            
            // 複製文字註記相關欄位
            photoGallery.setCname(vo.getCname());
            photoGallery.setEname(vo.getEname());
            photoGallery.setSubject(vo.getSubject());
            photoGallery.setDescription(vo.getDescription());
            
            photoGalleryFacade.save(photoGallery, this.getLoginUser(), this.isSimulated());
            
            JsfUtils.buildSuccessCallback();
        }catch(Exception e){
            processUnknowException(this.getLoginUser(), "selectDocImage", e, true);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="for control show block">
    public boolean isChoiceOnly(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.CHOICE);
    }

    public boolean showEditNodeBlock(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.MODIFY);
    }
    public boolean showAddSubNodeBlock(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.ADDSUB);
    }
    public boolean showUploadBlock(){
        return (ActionEnum.getFromCode(mode) == ActionEnum.UPLOAD) ;
    }
    
    public boolean showCustomAlbum(){
        return (PhotoGalleryEnum.CUSTOM == PhotoGalleryEnum.getFromCode(editVO.getPrimaryType()));
    }
    public boolean showIllustration(){
        return (PhotoGalleryEnum.DOC == PhotoGalleryEnum.getFromCode(editVO.getPrimaryType()));
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for tree">
    /**
     * 隱藏顯示 Tree
     */
    public void displayTree(){
        showTree = !showTree;
        logger.debug("displayTree showTree = "+showTree);
    }
    
    /**
     * 建立樹狀圖
     */
    public void rebuildTree(){
        allList = photoGalleryFacade.findAllFolder();
        
        buildTree(allList);// 建立樹狀圖
    }
    
    public void chooseEditVO(TreeNode node){
        editVO = (PhotoGalleryVO)node.getData();
        logger.debug("chooseEditVO editVO.getId()="+editVO.getId());
        logger.debug("chooseEditVO editVO.getPrimaryType()="+editVO.getPrimaryType());
        logger.debug("chooseEditVO editVO.getParent()="+editVO.getParent());
        logger.debug("chooseEditVO editVO.isCanEdit()="+editVO.isCanEdit());
        
        if( !this.isChoiceOnly() ){
            this.mode = ActionEnum.MODIFY.getCode();// default action
        }
        prepareIncludeImages(editVO);// 該節點包含圖片
        
        if( !this.isChoiceOnly() && !editVO.isCanEdit() ){
            this.mode = ActionEnum.ADDSUB.getCode();// default action
            //prepareAddSubNode();// 預設增加節點
            logger.debug("chooseEditVO this.mode="+this.mode);
        }
        
        changeEditMode();
    }
    
    public void onNodeSelect(NodeSelectEvent event) {
        logger.debug("onNodeSelect selectedNode = "+this.selectedNode);
        chooseEditVO(selectedNode);
        
        if( !this.isChoiceOnly() ){
            this.mode = ActionEnum.MODIFY.getCode();// default action
        }
    }
    
    public void onNodeExpand(NodeExpandEvent event) {
        logger.debug("onNodeExpand = "+event.getTreeNode().toString());
        this.expandMap.put(((PhotoGalleryVO)event.getTreeNode().getData()).getId(), Boolean.TRUE);
    }
 
    public void onNodeCollapse(NodeCollapseEvent event) {
        logger.debug("onNodeCollapse = "+event.getTreeNode().toString());
        this.expandMap.put(((PhotoGalleryVO)event.getTreeNode().getData()).getId(), Boolean.FALSE);
    }
    
    public void checkSelectedNode(TreeNode node){
        if( selectedNode!=null ){
            PhotoGalleryVO voS = ((PhotoGalleryVO)selectedNode.getData());
            PhotoGalleryVO vo = ((PhotoGalleryVO)node.getData());
            if( voS.getPrimaryType().equals(vo.getPrimaryType()) && voS.getId().equals(vo.getId()) ){
                node.setSelected(true);
            }
        }
    }
    
    public void checkExpandNode(TreeNode node){
        Long id = ((PhotoGalleryVO)node.getData()).getId();
        if( expandMap.get(id)!=null && expandMap.get(id) ){
            node.setExpanded(true);
            logger.debug("checkExpandNode "+id+"="+node.isExpanded());
        }
    }
    
    /**
     * 建立樹狀圖
     * @param rootVO 
     */
    private void buildTree(List<PhotoGalleryVO> allList){
        PhotoGalleryVO rootVO = new PhotoGalleryVO();
        rootVO.setId(0L);
        root = new DefaultTreeNode(rootVO, null);
        root.setExpanded(true);

        // 目錄
        List<PhotoGalleryEnum> enumsD = PhotoGalleryEnum.getDirectories(true);
        for(PhotoGalleryEnum enumD : enumsD){
            PhotoGalleryVO vo = new PhotoGalleryVO();
            vo.setId(0L);
            vo.setCname(enumD.getDisplayName());
            vo.setPrimaryType(enumD.getCode());
            vo.setCanEdit(false);
            vo.setCanAddSub(PhotoGalleryEnum.CUSTOM == enumD);
            vo.setCanUpload(PhotoGalleryEnum.CUSTOM == enumD);
            
            TreeNode nodeD = new DefaultTreeNode(enumD.getCode(), vo, root);
            
            if( enumD==PhotoGalleryEnum.CUSTOM ){
                nodeD.setExpanded(true);// 預設展開
                // 自訂相本圖庫子樹
                buildSubTree(allList, nodeD, PhotoGalleryEnum.CUSTOM);

                if( first ){// 預設選取
                    nodeD.setSelected(true);
                    selectedNode = nodeD;
                    chooseEditVO(selectedNode);
                    first = !first;
                }else{
                    checkSelectedNode(nodeD);
                }
            }else if( enumD==PhotoGalleryEnum.DOC ){
                // 以文章大項目為資料夾
                for(PublicationEnum enumP : PublicationEnum.richContentList()){
                    PhotoGalleryVO vo1 = new PhotoGalleryVO();
                    vo1.setId(0L);
                    vo1.setPrimaryType(enumD.getCode());// 文章插圖
                    vo1.setType(enumP.getCode());// 文章大項目
                    vo1.setCname(enumP.getDisplayName());
                    vo1.setParent(0L);

                    TreeNode nodeP = new DefaultTreeNode(enumD.getCode(), vo1, nodeD);
                }
            }
        }
    }
    
    /**
     * 遞迴建立子樹
     * @param allList
     * @param parent 
     */
    private void buildSubTree(List<PhotoGalleryVO> allList, TreeNode parent, PhotoGalleryEnum typeEnum){
        if( allList==null || parent==null ){
            return;
        }
        
        // 子節點
        List<PhotoGalleryVO> subList = photoGalleryFacade.findSubItems(allList, ((PhotoGalleryVO)parent.getData()).getId(), typeEnum);
        if( subList!=null ){
            for(PhotoGalleryVO vo : subList){
                vo.setCanEdit(PhotoGalleryEnum.CUSTOM == typeEnum);
                vo.setCanAddSub(PhotoGalleryEnum.CUSTOM == typeEnum);
                vo.setCanUpload(PhotoGalleryEnum.CUSTOM == typeEnum);
                
                TreeNode subNode = new DefaultTreeNode(vo.getPrimaryType(), vo, parent);
                
                checkSelectedNode(subNode);// 是否選取
                checkExpandNode(subNode);// 是否展開
                
                buildSubTree(allList, subNode, typeEnum);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }  

    public BaseLazyDataModel<PhotoGalleryVO> getLazyModel() {
        return lazyModel;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public void setLazyModel(BaseLazyDataModel<PhotoGalleryVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public KbPhotoGalleryFacade getPhotoGalleryFacade() {
        return photoGalleryFacade;
    }

    public void setPhotoGalleryFacade(KbPhotoGalleryFacade photoGalleryFacade) {
        this.photoGalleryFacade = photoGalleryFacade;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isShowTree() {
        return showTree;
    }

    public void setShowTree(boolean showTree) {
        this.showTree = showTree;
    }

    public Map<Long, String> getImageSrcMap() {
        return imageSrcMap;
    }

    public void setImageSrcMap(Map<Long, String> imageSrcMap) {
        this.imageSrcMap = imageSrcMap;
    }

    public String getShowImgKey() {
        return showImgKey;
    }

    public void setShowImgKey(String showImgKey) {
        this.showImgKey = showImgKey;
    }

    public PublicationVO getEditPubVO() {
        return editPubVO;
    }

    public void setEditPubVO(PublicationVO editPubVO) {
        this.editPubVO = editPubVO;
    }

    public PhotoGalleryVO getEditSubVO() {
        return editSubVO;
    }

    public void setEditSubVO(PhotoGalleryVO editSubVO) {
        this.editSubVO = editSubVO;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public PhotoGalleryVO getEditVO() {
        return editVO;
    }

    public void setEditVO(PhotoGalleryVO editVO) {
        this.editVO = editVO;
    }

    public List<PhotoGalleryVO> getAllList() {
        return allList;
    }

    public void setAllList(List<PhotoGalleryVO> allList) {
        this.allList = allList;
    }

    public List<PhotoGalleryVO> getImgList() {
        return imgList;
    }

    public void setImgList(List<PhotoGalleryVO> imgList) {
        this.imgList = imgList;
    }

    public List<PhotoGalleryVO> getResList() {
        return resList;
    }

    public void setResList(List<PhotoGalleryVO> resList) {
        this.resList = resList;
    }
    //</editor-fold>
}
