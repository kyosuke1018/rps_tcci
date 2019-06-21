/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.content;

import com.tcci.cm.controller.global.FileController;
import com.tcci.cm.controller.global.ImageController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.facade.conf.SysResourcesFacade;
import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.model.global.BaseLazyDataModel;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.entity.KbPhotoGallery;
import com.tcci.et.entity.KbPublication;
import com.tcci.et.enums.ContentStatusEnum;
import com.tcci.et.enums.DataTypeEnum;
import com.tcci.et.enums.PhotoGalleryEnum;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.et.facade.KbPhotoGalleryFacade;
import com.tcci.et.facade.KbPublicationFacade;
import com.tcci.cm.model.global.ImageVO;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.util.DateUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.et.enums.ImageSizeEnum;
import com.tcci.et.enums.OpenMethodEnum;
import com.tcci.et.enums.VideoLibraryEnum;
import com.tcci.et.facade.KbVideoFacade;
import com.tcci.et.model.LinkVO;
import com.tcci.et.model.PhotoGalleryVO;
import com.tcci.et.model.PublicationVO;
import com.tcci.et.model.RichContentVO;
import com.tcci.et.model.VideoVO;
import com.tcci.et.model.criteria.PublicationCriteriaVO;
import com.tcci.cm.model.global.GlobalConstant;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * 植物分類管理
 *
 * @author Peter.pan
 */
@ManagedBean(name = "publication")
@ViewScoped
public class PublicationController extends SessionAwareController implements Serializable {

    // private static final long FUNC_OPTION = 0;
    private static final String DATATABLE_RESULT = "fmMain:dtResult";

    @EJB KbPublicationFacade publicationFacade;
    @EJB KbVideoFacade videoFacade;
    @EJB KbPhotoGalleryFacade photoGalleryFacade;
    @EJB ImageFacade imageFacade;
    @EJB SysResourcesFacade sysResourcesFacade;
    @EJB AttachmentFacade attachmentFacade;

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

    private PublicationEnum type;// 文章刊物類型 (注意: publication.xhtml: A、R、P、F; tinymceDlg.xhtml: I、D、V)
    private boolean showDetail = false;// 顯示詳細資料區塊

    private BaseLazyDataModel<PublicationVO> lazyModel; // LazyModel for primefaces datatable lazy loading
    private List<PublicationVO> resultList;

    private List<PublicationVO> filterResultList; // datatable filter 後的結果
    private PublicationCriteriaVO criteriaVO = new PublicationCriteriaVO();
    private List<PublicationVO> folders;

    private PublicationVO editVO;
    private RichContentVO richContentVO;
    private LinkVO linkVO;

    private KbPhotoGallery photoGallery;// for 直接插入上傳圖片
    private VideoVO videoVO;// for 直接插入上傳影片

    private String resultHtml;

    // for 文章封面圖示
    private UploadedFile imgFile;
    private boolean uploadCoverImage;
    
    private List<PhotoGalleryVO> albumList;// 相本列表
    private List<PublicationVO> reportList;// 媒體報導列表
    private List<String> selectedAlbums;// 關聯照片相本
    private List<String> selectedRpts;// 關聯照片相本
    private List<String> selectedVideos;// 關聯影片
    private List<SelectItem> albumOps;// 相本列表
    private List<SelectItem> reportOps;// 媒體報導列表
    private List<SelectItem> videoOps;// 影片列表

    @PostConstruct
    private void init() {
        logger.debug("PublicationController init functionDenied = "+ functionDenied);
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if("kyle.cheng".equals(this.getLoginUser().getLoginAccount())){
            functionDenied = true;
        }else{    
        if (functionDenied) {
            return;
        }
        }
        // Get view Id
        viewId = JsfUtils.getViewId();

        getInputParams();// 取得輸入參數
        if (viewId.endsWith("/tinymceDlg.xhtml")) {
            initForInsertUpload(type);// 上傳插入圖片/文件/影片對話框初始動作
            return;
        }
        // saveTemp();// 暫存取得ID for 插入上傳圖

        folders = publicationFacade.findFolders(type);
        doQuery();

        // 刪除兩小時前的暫存資料 (管理者全刪、使用者刪自己的)
        publicationFacade.deleteTemp(isAdministrators(getLoginUser()) ? null : getLoginUser());
    }

    /**
     * 取得輸入參數
     */
    private void getInputParams() {
        // 文章刊物類型 (注意: publication.xhtml: A、R、P、F; tinymceDlg.xhtml: I、D、V)
        String typeCode = JsfUtils.getRequestParameter("type");
        logger.debug("typeCode = " + typeCode);
        if (typeCode != null) {
            type = PublicationEnum.getFromCode(typeCode);
        }

        // for tinymceDlg.xhtml and link redirect
        String idStr = JsfUtils.getRequestParameter("id");
        if (idStr != null) {
            try {
                Long id = Long.parseLong(idStr);
                editVO = publicationFacade.findById(id, false);
                if (type == null) {// link redirect
                    type = editVO.getTypeEnum();
                }
                criteriaVO.setId(id);
            } catch (Exception e) {
                logger.error("getInputParams exception:\n", e);
            }
        }
    }

    /**
     * 查詢
     */
    public void doQuery() {
        logger.debug("doQuery ...");
        if (type == null || !type.isRichContent()) {
            JsfUtils.addErrorMessage("未指定刊登文章類別!");
            return;
        }
        if( this.criteriaVO!=null && this.criteriaVO.getStartDate()!=null && this.criteriaVO.getEndDate()!=null ){
            if( this.criteriaVO.getStartDate().compareTo(this.criteriaVO.getEndDate()) > 0 ){
                JsfUtils.addErrorMessage("輸入[發佈日期]錯誤!(起日需小於等於迄日)");
                return;
            }
        }
        this.showDetail = false;

        try {
            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);
            filterResultList = null; // filterValue 初始化

            criteriaVO.setType(type.getCode());
            resultList = publicationFacade.findByCriteria(this.criteriaVO);
            lazyModel = new BaseLazyDataModel<PublicationVO>(resultList);
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
            criteriaVO = new PublicationCriteriaVO();

            // 移除 datatable 目前排序、filter 效果
            JsfUtils.resetDataTable(DATATABLE_RESULT);

            filterResultList = null; // filterValue 初始化
            resultList = null;
            lazyModel = new BaseLazyDataModel<PublicationVO>();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "doReset", e, false);
        }
    }

    /**
     * 功能標題
     *
     * @return
     */
    @Override
    public String getFuncTitle() {
        if (null != type) {
            return sessionController.getFunctionTitle(type.getFuncId());
        }
        return "無此分類";
    }

    /**
     * 模擬 Web Preview
     */
    public void webPreview() {
        logger.debug("webPreview id = " + this.editVO.getId());
        PublicationVO tmpVO = saveDocTempForPreview();// 暫存
        // 回傳 html 呼叫 RESTful 需要參數
        RequestContext rc = JsfUtils.buildSuccessCallback();
        rc.addCallbackParam("pubType", tmpVO.getType());
        rc.addCallbackParam("code", tmpVO.getCode());
        rc.addCallbackParam("realType", this.type.getCode());
    }

    /**
     * Web Preview 網頁 URL
     *
     * @return
     */
    public String getWebPreviewPage() {
        return sysResourcesFacade.getDocPreviewUrl();
    }

    //<editor-fold defaultstate="collapsed" desc="for tinymceDlg.xhtml only">
    /**
     * 上傳插入圖片/文件對話框初始動作
     */
    private void initForInsertUpload(PublicationEnum type) {
        logger.debug("initForInsertUpload type=" + type);
        if (PublicationEnum.IMAGE == type) {
            initForInsertImage();// 上傳插入圖片對話框初始動作
        } else if (PublicationEnum.DOC == type) {
            initForInsertDoc();// 上傳插入文件對話框初始動作
        } else if (PublicationEnum.VIDEO == type) {
            initForInsertVideo();// 上傳插入影片對話框初始動作
        }
    }

    // 上傳插入圖片對話框初始動作
    private void initForInsertImage() {
        if (this.editVO == null || this.editVO.getId() == null) {
            logger.error("initForInsertImage error editVO==null");
        } else {
            // for 上傳插入檔關聯 KbPublication 
            KbPublication tempEntity = publicationFacade.find(this.editVO.getId());
            if (tempEntity == null) {
                logger.error("initForInsertImage error tempEntity==null");
            } else {
                // 建立此批上傳 ContentHolder
                photoGallery = new KbPhotoGallery();
                photoGallery.setPrimaryType(PhotoGalleryEnum.DOC.getCode());// 文章插圖
                photoGallery.setPrimaryId(tempEntity.getId());// 關聯 ID
                photoGallery.setStatus(ContentStatusEnum.PUBLISH.getCode());
                photoGalleryFacade.save(photoGallery, this.getLoginUser(), this.isSimulated());
                // fileController 初始設定
                fileController.setContentHolderTemp(photoGallery);
                fileController.setOperator(this.getLoginUser());
            }
        }
    }

    // 上傳插入文件對話框初始動作
    private void initForInsertDoc() {
        if (this.editVO == null || this.editVO.getId() == null) {
            logger.error("initForInsertDoc error editVO==null");
        } else {
            // for 上傳插入檔關聯 KbPublication 
            KbPublication tempEntity = publicationFacade.find(this.editVO.getId());
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

    // 上傳插入影片對話框初始動作
    private void initForInsertVideo() {
        if (this.editVO == null || this.editVO.getId() == null) {
            logger.error("initForInsertVideo error editVO==null");
        } else {
            // for 上傳插入檔關聯 KbPublication 
            KbPublication tempEntity = publicationFacade.find(this.editVO.getId());
            if (tempEntity == null) {
                logger.error("initForInsertVideo error tempEntity==null");
            } else {
                // 直接使用 KbPublication 做 PrimaryType
                // videoVO 初始設定
                videoVO = new VideoVO();
                videoVO.setPrimaryType(VideoLibraryEnum.DOC.getCode());
                videoVO.setPrimaryId(editVO.getId());
                videoVO.setWidth(GlobalConstant.WIDTH_SMALL_VIDEO);
                videoVO.setHeight(GlobalConstant.HEIGHT_SMALL_VIDEO);
                videoVO.setStatus(editVO.getStatus());
                videoVO.setOpenMethod(OpenMethodEnum.ORIGIN.getCode());
            }
        }
    }

    /**
     * 插入影片
     */
    public void insertVideo() {
        logger.debug("insertVideo videoVO=" + videoVO);
        try {
            // 輸入資料檢核
            boolean hasErr = false;
            if (StringUtils.isBlank(this.videoVO.getTitle())) {// 必填
                JsfUtils.addErrorMessage("未輸入[標題]!");
                hasErr = true;
            }
            if (StringUtils.isBlank(this.videoVO.getUrl())) {// 必填
                JsfUtils.addErrorMessage("未輸入[影片網址]!");
                hasErr = true;
            }
            if (StringUtils.length(this.videoVO.getTitle()) > 100) {
                JsfUtils.addErrorMessage("[標題]欄位輸入長度過長!(最多100個字)");
                hasErr = true;
            }
            if (StringUtils.length(this.videoVO.getDescription()) > 500) {
                JsfUtils.addErrorMessage("[簡介]欄位輸入長度過長!(最多500個字)");
                hasErr = true;
            }
            if (StringUtils.length(videoVO.getUrl()) > 1024) {
                JsfUtils.addErrorMessage("[影片網址]欄位輸入長度過長!(最多1024個字元)");
                hasErr = true;
            }

            if (!hasErr) {
                videoFacade.saveVideo(videoVO, this.getLoginUser(), this.isSimulated());
                JsfUtils.buildSuccessCallback();
            } else {
                JsfUtils.buildErrorCallback();
            }
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "insertVideo", e, false);
        }
    }

    /**
     * 產出 RichEditor 插入影片URL
     */
    public void genResultHtmlAfteSaveVideo() {
        logger.debug("genResultHtmlAfteSaveVideo videoVO=" + videoVO);
        try {
            // 產出 RichEditor 插入影片URL
            genVideoResultHtml();
        } catch (Exception e) {
            JsfUtils.addErrorMessage("插入影片失敗!!");
            logger.error("genResultHtmlAfteSaveVideo exception:\n", e);
        }
    }

    /**
     * 產出 RichEditor 插入URL for 直接上傳圖片/檔案
     */
    public void genResultHtmlAfterUpload() {
        logger.debug("genResultHtmlAfterUpload type=" + type);
        try {
            TcDomain domain = imageFacade.getDomain(PhotoGalleryEnum.DOC.getDomainName());
            // 儲存 for get URL
            if (PublicationEnum.IMAGE == type) {
                // 每次有不同的 KbPhotoGallery 為 ContentHolder
                fileController.saveUploadedFiles(photoGallery, false, domain, this.getLoginUser(), this.isSimulated());
                // 縮圖
                ImageVO retImgVO = new ImageVO();
                String retMsg = imageFacade.compressImageByHolder(photoGallery, retImgVO);
                if (!StringUtils.isBlank(retMsg)) {
                    JsfUtils.addErrorMessage(retMsg);
                }
                photoGalleryFacade.saveImageInfo(photoGallery, retImgVO, this.getLoginUser(), this.isSimulated());// 儲存圖片資訊
            } else if (PublicationEnum.DOC == type) {
                // 因共用同一 KbPublication 為 ContentHolder，所以需指定指處理本次上傳
                fileController.saveFiles(fileController.getContentHolderTemp(),
                        fileController.getAttachments(), false, this.getLoginUser(), this.isSimulated());
            }

            // 產出 RichEditor 插入URL
            genResultHtml();
        } catch (Exception e) {
            JsfUtils.addErrorMessage("檔案上傳失敗!!");
            logger.error("genResultHtmlAfterUpload exception:\n", e);
        }
    }

    /**
     * 產出 RichEditor 插入圖片URL
     *
     * @return
     */
    public boolean genResultHtml() {
        logger.debug("genResultHtml editVO = " + editVO);
        logger.debug("genResultHtml fileController.getContentHolderTemp() = " + fileController.getContentHolderTemp());

        StringBuilder sb = new StringBuilder();
        List<AttachmentVO> list = fileController.findUploadedFiles(fileController.getContentHolderTemp());
        logger.debug("genResultHtml list = " + (list == null ? 0 : list.size()));

        if (list == null) {
            logger.error("genResultHtml error list==null, editVO = " + editVO);
            return false;
        }

        if (PublicationEnum.IMAGE == type) {// 自圖庫選圖
            // 設定圖片存取位置
            imageController.setDomainName(PhotoGalleryEnum.DOC.getDomainName());
            for (AttachmentVO vo : list) {
                String url = imageController.genImageUrl(vo, true, false);
                url = sysResourcesFacade.genWebImgUrl(url, ImageSizeEnum.BIG.getCode());// 外網可存取完整網址
                String alt = vo.getDescription() != null ? vo.getDescription() : vo.getFileName();
                String width = Integer.toString(GlobalConstant.WIDTH_HTML_IMG);
                String height = Integer.toString(GlobalConstant.HEIGHT_HTML_IMG);

                sb.append("<img src=\"").append(url).append("\" alt=\"").append(alt)
                        .append("\" width=\"").append(width)
                        .append("\" height=\"").append(height).append("\" ></img>&nbsp;");
            }
        } else if (PublicationEnum.DOC == type) {// 直接上傳插圖
            List<Long> existedAppIds = fileController.getExistedAppIds();
            for (AttachmentVO vo : list) {
                if (!existedAppIds.contains(vo.getApplicationdata().getId())) {// 本次上傳的
                    String url = sysResourcesFacade.genDocUrl(vo, true, false);
                    url = sysResourcesFacade.genWebFileUrl(url);// 外網可存取完整網址
                    String alt = vo.getDescription() != null ? vo.getDescription() : vo.getFileName();
                    String txt = (vo.getApplicationdata().getDescription() == null) ? vo.getFileName() : vo.getApplicationdata().getDescription();
                    sb.append("<a href=\"").append(url).append("\" alt=\"")
                            .append(alt).append("\" >").append(txt).append("</a>&nbsp;");
                }
            }
        }

        this.resultHtml = sb.toString();

        logger.debug("genResultHtml resultHtml = \n" + resultHtml);
        return true;
    }

    /**
     * 產出 RichEditor 插入影片URL
     *
     * @return
     */
    public boolean genVideoResultHtml() {
        logger.debug("genVideoResultHtml videoVO = " + videoVO);
        StringBuilder sb = new StringBuilder();
        sb.append("<iframe width=\"").append(GlobalConstant.WIDTH_SMALL_VIDEO)
                .append("\" height=\"").append(GlobalConstant.WIDTH_SMALL_VIDEO).append("\" src=\"")
                .append(videoVO.getEmbedUrl()).append("\" frameborder=\"0\" allowfullscreen=\"true\"")
                .append(" title=\"").append(videoVO.getTitle()).append("\" ></iframe>");
        this.resultHtml = sb.toString();
        logger.debug("genVideoResultHtml resultHtml = \n" + resultHtml);
        return true;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for detail operation">
    /**
     * 新增植物
     */
    public void prepareCreate() {
        logger.debug("prepareCreate ...");
        try {
            this.showDetail = true;
            editVO = new PublicationVO();
            saveTemp();// 暫存取得ID for 插入上傳圖
            editVO.setDataType((type.getDefDataType() != null) ? type.getDefDataType().getCode() : DataTypeEnum.HTML.getCode());// 預設
            editVO.setNews(type.getNews());
            editVO.setTemp(true);
            editVO.setDataDate(DateUtils.getToday());
            editVO.setCoverImg("未上傳封面圖示!");

            fileController.init();
            imgFile = null;
            uploadCoverImage = false;

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
        return type != null && (type.getDefDataType() == DataTypeEnum.HTML);
    }

    /**
     * 暫存取得ID for 插入上傳圖
     */
    public void saveTemp() {
        editVO = new PublicationVO();
        editVO.setType(PublicationEnum.TEMP.getCode());
        publicationFacade.saveVO(editVO, this.getLoginUser(), this.isSimulated());
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
        editVO = new PublicationVO();

        JsfUtils.buildSuccessCallback();
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
            editVO = publicationFacade.findById(id, false);
            editVO.setTemp(false);

            fileController.init();
            // for 封面圖示
            if (type != null && type.getHasCoverImg()) {
                imgFile = null;
                uploadCoverImage = false;
                // 準備顯示封面圖片
                photoGalleryFacade.loadCoverImage(editVO);
                if (editVO.getCoverImgVO() != null) {
                    imageController.setDomainName(PhotoGalleryEnum.DOC.getDomainName());
                    imageController.prepareImage(editVO.getCoverImgVO(), true);
                    logger.debug("prepareEdit editVO.getCoverImgVO() = " + editVO.getCoverImgVO().getFileName());
                }
            }

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
    public boolean canEditDoc(PublicationVO vo) {
        return true;
    }

    /**
     * 刪除前檢查
     *
     * @param vo
     * @return
     */
    public boolean checkDelete(PublicationVO vo) {
        if (vo.getStatusEnum() == ContentStatusEnum.PUBLISH) {
            JsfUtils.addErrorMessage("已發佈文章不可刪除!(若確定要刪除，請先[下架]後，再刪除。)");
            return false;
        }
        if (vo.getDataTypeEnum() == DataTypeEnum.FOLDER) {// 資料夾
            // 包含子項目
            PublicationCriteriaVO criteria = new PublicationCriteriaVO();
            criteria.setParent(vo.getId());
            List<PublicationVO> list = publicationFacade.findByCriteria(criteria);
            if (list != null && !list.isEmpty()) {
                JsfUtils.addErrorMessage("有包含子項目(子資料夾或文章)不可刪除!");
                return false;
            }
        }
        return true;
    }

    /**
     * 刪除文章
     *
     * @param vo
     */
    public void deleteDoc(PublicationVO vo) {
        logger.debug("deleteDoc ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_DOC;
        try {
            if (!checkDelete(vo)) {// 刪除前檢查
                return;
            }

            publicationFacade.deleteDoc(vo, this.getLoginUser(), this.isSimulated());

            // 重載資料夾選單
            if (vo.getDataTypeEnum() == DataTypeEnum.FOLDER) {// 資料夾
                folders = publicationFacade.findFolders(type);
            }

            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), vo.getId(),
                    vo.getTitle(), null, true, this.getLoginUser(), this.isSimulated());

            editVO = new PublicationVO();
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
        ActivityLogEnum acEnum = ActivityLogEnum.D_DOC_FILE;
        try {
            if (editVO.getDocs() != null) {
                // 未建立關聯直接刪除
                if (attachmentVO.getApplicationdata() == null
                        || attachmentVO.getApplicationdata().getId() == null) {
                    fileController.deleteAttachment(attachmentVO, this.isSimulated());
                } else {
                    // 註記刪除，於儲存時處理。
                    if (editVO.getRemovedDocs() == null) {
                        editVO.setRemovedDocs(new ArrayList<AttachmentVO>());
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
     * 刪除封面圖示
     * @param attachmentVO 
     */
    public void deleteCoverImg(AttachmentVO attachmentVO){
        logger.info("deleteCoverImg ...");
        ActivityLogEnum acEnum = ActivityLogEnum.D_DOC_COVER;
        try {
            // 未建立關聯直接刪除
            fileController.deleteAttachment(attachmentVO, this.isSimulated());
            editVO.setCoverImg("未上傳封面圖示!");
            editVO.setCoverImgVO(null);
            uploadCoverImage = false;
            imgFile = null;
            
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getTitle(), null, true, this.getLoginUser(), this.isSimulated());

            JsfUtils.buildSuccessCallback();
        } catch (Exception e) {
            processUnknowException(this.getLoginUser(), "deleteCoverImg", e, true);
            cmActivityLogFacade.logActiveForSingleId(acEnum, viewId, JsfUtils.getClientIP(), editVO.getId(),
                    editVO.getTitle(), null, false, this.getLoginUser(), this.isSimulated());
        }
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
     * 可否刪除
     *
     * @param vo
     * @return
     */
    public boolean canDeleteDoc(PublicationVO vo) {
        // 固定網頁只有系統管理人員可刪除
        //return (type != PublicationEnum.FIXEDPAGE || this.isAdministrators(this.getLoginUser()))
        //        && (this.getLoginUser() != null && this.getLoginUser().equals(vo.getLastUpdateUser()));
        return (this.getLoginUser() != null && this.getLoginUser().equals(vo.getLastUpdateUser()));
    }

    /**
     * 輸入檢查
     *
     * @param vo
     * @return
     */
    public boolean checkBeforeSave(PublicationVO vo) {
        if (vo.getType() == null) {
            JsfUtils.addErrorMessage("未設定刊登文章類別!");
            return false;
        }
        return true;
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
        if (editVO.getDataDate() == null) {// 必填
            JsfUtils.addErrorMessage("未輸入[發佈日期]!");
            hasErr = true;
        }
        if (StringUtils.length(editVO.getTitle()) > 200) {
            JsfUtils.addErrorMessage("[標題]欄位輸入長度過長!(最多200個字)");
            hasErr = true;
        }
        if (StringUtils.length(editVO.getSummary()) > 500) {
            JsfUtils.addErrorMessage("[大綱]欄位輸入長度過長!(最多500個字)");
            hasErr = true;
        }
        if (StringUtils.length(editVO.getCode()) > 20) {
            JsfUtils.addErrorMessage("[編號]欄位輸入長度過長!(最多20個英數字)");
            hasErr = true;
        }

        if (editVO.getDataTypeEnum() == DataTypeEnum.LINK) {
            if (StringUtils.isBlank(editVO.getUrl())) {// 必填
                JsfUtils.addErrorMessage("未輸入[連結網址]!");
                hasErr = true;
            }
            if (StringUtils.isBlank(editVO.getOpenMethod())) {// 必填
                JsfUtils.addErrorMessage("未輸入[開啟方式]!");
                hasErr = true;
            }
            if (StringUtils.length(editVO.getUrl()) > 1024) {
                JsfUtils.addErrorMessage("[連結網址]欄位輸入長度過長!(最多1024個字元)");
                hasErr = true;
            }
        } else if (editVO.getDataTypeEnum() == DataTypeEnum.UPLOAD) {
            if ((fileController.getFiles() == null || fileController.getFiles().isEmpty())
                    && (editVO.getDocs() == null || editVO.getDocs().isEmpty())) {
                if (type != PublicationEnum.NEWS) {// NEWS 可能無PDF檔
                    JsfUtils.addErrorMessage("未上傳任何檔案!");
                    hasErr = true;
                }
            }
        } else if (editVO.getDataTypeEnum() == DataTypeEnum.HTML) {
            if (StringUtils.isBlank(editVO.getContents())) {// 必填
                JsfUtils.addErrorMessage("未輸入[自製網頁內容]!");
                hasErr = true;
            }
        }

        return !hasErr;
    }

    /**
     * 儲存
     */
    public void saveDoc() {
        logger.debug("saveDoc ...");
        ActivityLogEnum acEnum = (editVO.isTemp() ? ActivityLogEnum.A_DOC : ActivityLogEnum.U_DOC);

        logger.debug("saveDoc ...");
        try {
            // 輸入資料檢核
            if (!checkInputData()) {
                return;
            }
            editVO.setType(type.getCode());
            if (!checkBeforeSave(editVO)) {
                JsfUtils.buildErrorCallback();
                return;
            }

            // 儲存上傳檔以外資訊
            publicationFacade.saveAll(editVO, this.getLoginUser(), this.isSimulated());

            // 儲存後處理(UI、Files、Solr)
            KbPublication entity = null;
            if (DataTypeEnum.UPLOAD.getCode().equals(editVO.getDataType())) {
                entity = publicationFacade.find(editVO.getId());
                // 上傳檔異動處理
                processUploadFiles(entity);
            } else if (DataTypeEnum.FOLDER.getCode().equals(editVO.getDataType())) {
                // 重載資料夾選單
                folders = publicationFacade.findFolders(type);
            }
            // 封面圖示
            /*if (editVO.getTypeEnum() == PublicationEnum.EXPRESS) {// 出版品封面必要
                if (editVO.getCoverImg() == null) {
                    JsfUtils.addErrorMessage("未上傳封面圖示!");
                } else {
                    saveCoverImage();// 儲存封面圖示
                }
            } else if (editVO.getTypeEnum() == PublicationEnum.HUNDREDS) {// 百種興盛
                if (editVO.getCoverImg() != null) {
                    saveCoverImage();// 儲存封面圖示
                }
            } else if (editVO.getTypeEnum() == PublicationEnum.ARTICLE) {// 保種專案
                if (editVO.getCoverImg() != null) {
                    saveCoverImage();// 儲存封面圖示
                }
            }*/
            if (editVO.getCoverImg() != null) {
                saveCoverImage();// 儲存封面圖示
            }

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
     * 另存為一筆暫存資料 for Preview
     *
     * @return
     */
    public PublicationVO saveDocTempForPreview() {
        logger.debug("saveDocTempForPreview ...");
        try {
            PublicationVO tmpVO = new PublicationVO();
            ExtBeanUtils.copyProperties(tmpVO, editVO);
            String code = UUID.randomUUID().toString();
            // 另存為一筆暫存資料
            tmpVO.setId(null);
            tmpVO.setType(PublicationEnum.TEMP.getCode());
            tmpVO.setCode(code);
            tmpVO.setHtmlId(null);// 需新產生

            // 儲存上傳檔以外資訊
            publicationFacade.saveAll(tmpVO, this.getLoginUser(), this.isSimulated());

            return tmpVO;
        } catch (Exception e) {
            JsfUtils.addErrorMessage("預覽暫存發生錯誤! (可能資料過長或格式錯誤)");
            processUnknowException(this.getLoginUser(), "saveDocTempForPreview", e, true);
        }
        return null;
    }

    /**
     * 上傳檔異動處理
     *
     * @param entity
     * @throws java.lang.Exception
     */
    public void processUploadFiles(KbPublication entity) throws Exception {
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
     * 關閉明細
     */
    public void closeDocDetail() {
        logger.debug("closeDocDetail ...");
        // 編輯頁較複雜，儲存後維持在編輯頁，關閉才Refresh
        this.doQuery();// Refresh
    }

    /**
     * 變更內容類型
     */
    public void changeDataType() {
        logger.debug("changeDataType = " + this.editVO.getDataType());
    }

    /**
     * 變更連結開啟方式
     */
    public void changeOpenMethod() {
        logger.debug("changeOpenMethod = " + this.editVO.getOpenMethod());
    }

    /**
     * 上傳封面圖示
     *
     * @param event
     */
    public void uploadCoverImage(FileUploadEvent event) {
        logger.debug("uploadCoverImage event.getFile().getFileName()=" + event.getFile().getFileName());
        try {
            if (editVO == null) {
                logger.error("uploadCoverImage error editVO==null");
            } else {
                imgFile = event.getFile();// 暫存於 imgFile
                uploadCoverImage = true;
                String filename = fileController.getFileName(imgFile);
                editVO.setCoverImg(filename);// 封面圖示檔名
                logger.debug("uploadCoverImage imgFile=" + imgFile);
            }
        } catch (Exception e) {
            JsfUtils.addErrorMessage("檔案上傳失敗!");
            logger.error("uploadCoverImage Exception:\n", e);
        }
    }

    /**
     * 儲存封面圖示
     */
    public void saveCoverImage() {
        if (this.editVO == null || this.imgFile == null) {
            logger.debug("saveCoverImage this.editVO==null || this.imgFile==null.");
            return;
        }
        try {
            String filename = fileController.getFileName(imgFile);
            // 建立此批上傳 ContentHolder
            KbPhotoGallery pgEntity = photoGalleryFacade.prepareDocCoverImageHolder(editVO, this.getLoginUser(), this.isSimulated());

            TcDomain tcDomain = imageFacade.getDomain(GlobalConstant.DOMAIN_NAME_DOC_IMG);
            fileController.init();// 清空其他上傳檔資訊
            fileController.setFile(imgFile);
            boolean res = fileController.saveSingleUploadedFile(pgEntity, tcDomain, this.getLoginUser(), this.isSimulated());
            logger.debug("saveCoverImage ... filename=" + filename + ", res = " + res);
            if (!res) {
                JsfUtils.addErrorMessage("封面圖示儲存失敗!");
            } else {
                // 縮圖
                ImageVO retImgVO = new ImageVO();
                String retMsg = imageFacade.compressImageByHolder(pgEntity, retImgVO);
                if (!StringUtils.isBlank(retMsg)) {
                    JsfUtils.addErrorMessage(retMsg);
                }
                pgEntity.setCname(filename);// 儲存檔名
                photoGalleryFacade.saveImageInfo(pgEntity, retImgVO, this.getLoginUser(), this.isSimulated());// 儲存圖片資訊
            }
        } catch (Exception e) {
            logger.error("saveCoverImage exception:\n", e);
            JsfUtils.addErrorMessage("封面圖示儲存失敗!");
        }
    }

    /**
     * 是否顯示封面圖示
     *
     * @return
     */
    public boolean hasCoverImage() {
        logger.debug("hasCoverImage ...");

        return type != null && type.getHasCoverImg() && editVO != null;
    }

    public boolean showCoverImage() {
        logger.debug("showCoverImage ...");
        return !uploadCoverImage // 未重新上傳，才顯示之前上傳封面圖示
                && type != null && type.getHasCoverImg()
                && editVO != null
                && editVO.getCoverImgVO() != null
                && editVO.getCoverImgVO().getApplicationdata() != null
                && editVO.getCoverImgVO().getApplicationdata().getId() != null;
    }
    
    public boolean canDelCoverImage() {
        logger.debug("canDelCoverImage ...");
        //return showCoverImage() && type!=PublicationEnum.EXPRESS;// 出版品封面必填只可重傳
        return showCoverImage();
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
    public PublicationEnum getType() {
        return type;
    }

    public void setType(PublicationEnum type) {
        this.type = type;
    }

    public List<String> getSelectedVideos() {
        return selectedVideos;
    }

    public void setSelectedVideos(List<String> selectedVideos) {
        this.selectedVideos = selectedVideos;
    }

    public List<SelectItem> getVideoOps() {
        return videoOps;
    }

    public void setVideoOps(List<SelectItem> videoOps) {
        this.videoOps = videoOps;
    }

    public List<PhotoGalleryVO> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<PhotoGalleryVO> albumList) {
        this.albumList = albumList;
    }

    public List<PublicationVO> getReportList() {
        return reportList;
    }

    public void setReportList(List<PublicationVO> reportList) {
        this.reportList = reportList;
    }

    public List<String> getSelectedAlbums() {
        return selectedAlbums;
    }

    public void setSelectedAlbums(List<String> selectedAlbums) {
        this.selectedAlbums = selectedAlbums;
    }

    public List<String> getSelectedRpts() {
        return selectedRpts;
    }

    public void setSelectedRpts(List<String> selectedRpts) {
        this.selectedRpts = selectedRpts;
    }

    public UploadedFile getImgFile() {
        return imgFile;
    }

    public void setImgFile(UploadedFile imgFile) {
        this.imgFile = imgFile;
    }

    public VideoVO getVideoVO() {
        return videoVO;
    }

    public void setVideoVO(VideoVO videoVO) {
        this.videoVO = videoVO;
    }

    public KbPhotoGallery getPhotoGallery() {
        return photoGallery;
    }

    public void setPhotoGallery(KbPhotoGallery photoGallery) {
        this.photoGallery = photoGallery;
    }

    public String getResultHtml() {
        return resultHtml;
    }

    public void setResultHtml(String resultHtml) {
        this.resultHtml = resultHtml;
    }

    public RichContentVO getRichContentVO() {
        return richContentVO;
    }

    public void setRichContentVO(RichContentVO richContentVO) {
        this.richContentVO = richContentVO;
    }

    public LinkVO getLinkVO() {
        return linkVO;
    }

    public void setLinkVO(LinkVO linkVO) {
        this.linkVO = linkVO;
    }

    public BaseLazyDataModel<PublicationVO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(BaseLazyDataModel<PublicationVO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<PublicationVO> getResultList() {
        return resultList;
    }

    public void setResultList(List<PublicationVO> resultList) {
        this.resultList = resultList;
    }

    public List<PublicationVO> getFilterResultList() {
        return filterResultList;
    }

    public void setFilterResultList(List<PublicationVO> filterResultList) {
        this.filterResultList = filterResultList;
    }

    public List<PublicationVO> getFolders() {
        return folders;
    }

    public void setFolders(List<PublicationVO> folders) {
        this.folders = folders;
    }

    public PublicationCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(PublicationCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }

    public PublicationVO getEditVO() {
        return editVO;
    }

    public void setEditVO(PublicationVO editVO) {
        this.editVO = editVO;
    }

    public boolean isUploadCoverImage() {
        return uploadCoverImage;
    }

    public void setUploadCoverImage(boolean uploadCoverImage) {
        this.uploadCoverImage = uploadCoverImage;
    }

    public List<SelectItem> getAlbumOps() {
        return albumOps;
    }

    public void setAlbumOps(List<SelectItem> albumOps) {
        this.albumOps = albumOps;
    }

    public List<SelectItem> getReportOps() {
        return reportOps;
    }

    public void setReportOps(List<SelectItem> reportOps) {
        this.reportOps = reportOps;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }
    //</editor-fold>
}
