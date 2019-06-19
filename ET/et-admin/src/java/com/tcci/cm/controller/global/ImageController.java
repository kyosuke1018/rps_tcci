package com.tcci.cm.controller.global;

import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.util.WebUtils;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * User: peter.pan
 * Date: 9/5/12
 */
@ManagedBean(name = "imageController")
@ViewScoped
public class ImageController extends AbstractController implements Serializable {
    @EJB private ImageFacade imageFacade;
    @EJB private AttachmentFacade attachmentFacade;
    
    // 顯示圖片
    private Map<Long, String> imageSrcMap;// 存放每筆資料圖片顯示URL
    private String showImgKey;// 安全檢核碼
    private AttachmentVO imageVO;
    private List<AttachmentVO> images;
    private boolean multi = false;
    private String domainName;
    
    @PostConstruct
    public void init(){
        logger.debug("ImageController init ...");
        initShowImage();
    }
    
    /**
     * 初始準備顯示圖片作業
     */
    public void initShowImage(){
        // 顯示圖片URL
        imageSrcMap = new HashMap<Long, String>();
        showImgKey = UUID.randomUUID().toString();// 驗證碼
        JsfUtils.setSessionAttr(showImgKey, Boolean.TRUE, false);// 驗證碼放行
        imageVO = null;
        images = null;
    }
    
    /**
     * 準備顯示圖片 URL 資訊存放至 imageSrcMap
     * @param attachmentVO
     * @param isPublic
     * @return 
     */
    public String prepareImage(AttachmentVO attachmentVO, boolean isPublic){
        logger.info("prepareImage ... attachmentVO = "+attachmentVO);
        if( domainName==null ){
            logger.error("prepareImage domainName==null.");
            return null;
        }
        return prepareImageUrl(domainName, attachmentVO, isPublic, showImgKey, imageSrcMap);
    }
    
    /**
     * 準備顯示圖片 URL 資訊存放至 imageSrcMap
     * @param domainName
     * @param attachmentVO
     * @param isPublic
     * @param showImgKey
     * @param imageSrcMap
     * @return 
     */
    public String prepareImageUrl(String domainName, AttachmentVO attachmentVO, boolean isPublic, String showImgKey, Map<Long, String> imageSrcMap){
        logger.info("prepareImageUrl ...");
        // 附件資訊
        //editDocument = pmisDocumentFacade.find(projectDocumentVO.getId());
        //AttachmentVO attachmentVO = fileController.getLastAttachment(editDocument);
        // 產出完整圖片URL
        if( attachmentVO!=null ){
            // for PrivateUrl 將完整檔名儲存於 Session Map中，URL只傳對應KEY
            if( !isPublic ){
                String fullFileName = attachmentFacade.getFullFileName(attachmentVO);// 完整實體名稱
                String mappingKey = imageFacade.genMappingKey(attachmentVO);
                WebUtils.setSessionAttr(JsfUtils.getRequest(), mappingKey, fullFileName, true);// 傳對應KEY，不傳實體名稱 for ImageServlet
            }
            
            String contentType = attachmentVO.getContentType();
            logger.debug("prepareImageUrl contentType = "+contentType);
            if( contentType!=null ){ //&& contentType.toLowerCase().startsWith("image/") ){
                try{
                    // 圖片URL
                    String url = imageFacade.genImageUrl(domainName, attachmentVO, isPublic, showImgKey, null);                    
                    // 文件ID 與 圖片URL 對應表
                    logger.debug("prepareImageUrl id = "+attachmentVO.getApplicationdata().getId()+", url="+url);
                    imageSrcMap.put(attachmentVO.getApplicationdata().getId(), url);// for UI
                    return url;
                }catch(Exception e){
                    logger.error("prepareImage Exception ...\n", e);
                }
            }
        }
        return null;
    }
    
    /**
     * 產生圖片 URL 
     * @param attachmentVO
     * @param isPublic
     * @param widthContextPath
     * @return 
     */
    public String genImageUrl(AttachmentVO attachmentVO, boolean isPublic, boolean widthContextPath){
        logger.info("genImageUrl ... attachmentVO = "+attachmentVO);
        if( domainName==null ){
            logger.error("genImageUrl domainName==null.");
            return null;
        }
        String contextPath = widthContextPath? JsfUtils.getContextPath():null;
        return imageFacade.genImageUrl(domainName, attachmentVO, isPublic, showImgKey, contextPath);
    }
    
    /**
     * 取得指定圖片 URL
     * @param attachmentVO
     * @return 
     */
    public String getImageSrc(AttachmentVO attachmentVO){
        logger.debug("getImageSrc id="+attachmentVO.getApplicationdata().getId());
        if( imageSrcMap!=null ){
            for(Long key : imageSrcMap.keySet()){
                logger.debug("getImageSrc key = "+key+", value = "+imageSrcMap.get(key));
            }
        }
        return (imageSrcMap!=null)?imageSrcMap.get(attachmentVO.getApplicationdata().getId()):null;
    }
    
    /**
     * 選是指定圖片 (接著使用 getViewImageUrl )
     * @param attachmentVO 
     */
    public void viewImage(AttachmentVO attachmentVO){
        imageVO = attachmentVO;
    }
    
    /**
     * 至 map 存取圖片 URL
     * @return 
     */
    public String getViewImageUrl(){
        return getImageSrc(imageVO);
    }
    
    public boolean canShowImageDlg(){
        return (multi && images!=null) || (!multi && imageVO!=null);
    }
    
    @PreDestroy
    public void onDestroy(){
       JsfUtils.removeSessionAttr(showImgKey);// 移除驗證碼
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Map<Long, String> getImageSrcMap() {
        return imageSrcMap;
    }

    public void setImageSrcMap(Map<Long, String> imageSrcMap) {
        this.imageSrcMap = imageSrcMap;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getShowImgKey() {
        return showImgKey;
    }

    public void setShowImgKey(String showImgKey) {
        this.showImgKey = showImgKey;
    }

    public AttachmentVO getImageVO() {
        return imageVO;
    }

    public void setImageVO(AttachmentVO imageVO) {
        this.imageVO = imageVO;
    }

    public List<AttachmentVO> getImages() {
        return images;
    }

    public void setImages(List<AttachmentVO> images) {
        this.images = images;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }
    //</editor-fold>
}