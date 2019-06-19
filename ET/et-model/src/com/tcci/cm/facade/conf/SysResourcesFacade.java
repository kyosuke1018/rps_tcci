/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.conf;

import com.tcci.cm.facade.global.ImageFacade;
import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.util.NetworkUtils;
import com.tcci.cm.util.NotificationUtils;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.et.enums.PublicationEnum;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.model.global.ImageVO;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.model.FileVO;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server Resources (JNDI, ...)
 * @author peter.pan
 */
@Stateless
public class SysResourcesFacade {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected @EJB ImageFacade img;
    
    @Resource(mappedName = "jndi/et.config")
    protected Properties jndiConfig;
    
    @Resource(mappedName = "jndi/global.config")
    private Properties jndiGlobalConfig;
    
    public boolean isTrue(Boolean flag){
        return (flag!=null && flag);
    }
    
    public boolean isFalse(Boolean flag){
        return (flag!=null && !flag);
    }
    
    public boolean isEmpty(List list){
        return (list==null || list.isEmpty());
    }
    
    //<editor-fold defaultstate="collapsed" desc="for JNDI Setting">
    public String getStringProp(String propName, String def){
        String res = def;
        try{
            res = jndiConfig.getProperty(propName);
            logger.debug("getStringProp propName="+propName+", res = "+res);
        }catch(Exception e){
            logger.error("getStringProp propName="+propName+" exception:\n", e);
        }
        return (res!=null)?res:def;
    }

    public boolean getBooleanProp(String propName, boolean def){
        try{
            return "Y".equals(getStringProp(propName, null));
        }catch(Exception e){
            logger.error("getBooleanProp propName="+propName+" exception:\n", e);
        }
        return def;
    }

    public int getIntProp(String propName, int def){
        try{
            return Integer.parseInt(getStringProp(propName, null));
        }catch(Exception e){
            logger.error("getIntProp propName="+propName+" exception:\n", e);
        }
        return def;
    }
    //</editor-fold>
    
    // 系統管理員通知 EMAIL
    public String getNotifyAdmins(){
        return jndiConfig.getProperty(GlobalConstant.JNDI_ADMIN_EMAIL);
    }
    
    public String getRestUrlPrefix(){
//        String txt = getJndiValue("restUrlPrefix");
        String txt = jndiConfig.getProperty("restUrlPrefix");
        return txt;
    }
    
    // 保種紀錄上傳檔路徑
    public String getRecordUploadDir(){
        return jndiConfig.getProperty(GlobalConstant.JNDI_RECORD_DIR);
    }
    
    // 圖檔實際路徑
    public String getBigImgaeDir(){
        return jndiConfig.getProperty(GlobalConstant.JNDI_PNAME_DIR_BIMAGE);
    }
    public String getSmallImgaeDir(){
        return jndiConfig.getProperty(GlobalConstant.JNDI_PNAME_DIR_SIMAGE);
    }
    public String getServiceUrlPrefix(){// 前端網頁連結的 Services 使用的 URL 的前置詞
        return jndiConfig.getProperty(GlobalConstant.JNDI_URL_SRV_PREFIX);
    }
    public String getWebUrlPrefix(){// 前端網頁連結使用URL的前置詞
        return jndiConfig.getProperty(GlobalConstant.JNDI_URL_WEB_PREFIX);
    }
    public String getDocPreviewUrl(){// 線上編輯文章，模擬真實網頁預覽
        return jndiConfig.getProperty(GlobalConstant.JNDI_URL_DOC_PREVIEW);
    }
    
    // 圖檔 URL
    /**
     * 
     * @param relUrl
     * @param imgType (S,B,O)
     * @return 
     */
    public String genWebImgUrl(String relUrl, String imgType){// 產生外網圖片連結URL
        StringBuilder sb = new StringBuilder();
        String webUrlPrefix = getServiceUrlPrefix();
        logger.debug("genWebImgUrl webUrlPrefix = "+webUrlPrefix);
        sb.append(webUrlPrefix).append(relUrl);
        
        if( imgType!=null && !imgType.isEmpty() ){
            sb.append("&imgType=").append(imgType);
        }
        return sb.toString();
    }
    public String genAdminImgUrl(String relUrl, String imgType){// 產生管理介面圖片連結URL
        StringBuilder sb = new StringBuilder();
        sb.append(JsfUtils.getContextPath()).append(relUrl);
        
        if( imgType!=null && !imgType.isEmpty() ){
            sb.append("&imgType=").append(imgType);
        }
        return sb.toString();
    }
    public String genJsfImgUrl(String relUrl, String imgType){// 產生Jsf TAG圖片連結URL
        StringBuilder sb = new StringBuilder();
        sb.append(relUrl);
        
        if( imgType!=null && !imgType.isEmpty() ){
            sb.append("&imgType=").append(imgType);
        }
        return sb.toString();
    }
    
    // 檔案 URL
    public String genWebFileUrl(String relUrl){// 產生外網檔案連結URL
        StringBuilder sb = new StringBuilder();
        sb.append(getServiceUrlPrefix()).append(relUrl);
        return sb.toString();
    }
    public String genAdminFileUrl(String relUrl){// 產生管理介面檔案連結URL
        StringBuilder sb = new StringBuilder();
        sb.append(JsfUtils.getContextPath()).append(relUrl);
        return sb.toString();
    }
    
    // 文管系統
    public String getDocPubUrl(){
        String url = "";
        try{
            url = jndiGlobalConfig.getProperty(GlobalConstant.JNDI_DOCPUB_URL);
        }catch(Exception e){
            logger.error("getDocPubUrl exception:\n", e);
        }
        return url;
    }
    
    //<editor-fold defaultstate="collapsed" desc="for download">
    /**
     * 產生非 JSF 環境，檔案下載 URL
     * @param vo
     * @return 
     */
    public String preparePublicDownloadUrl(AttachmentVO vo){
        if( vo==null || vo.getApplicationdata()==null || vo.getApplicationdata().getFvitem()==null ){
            logger.error("preparePublicDownloadUrl empty error vo = "+vo);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 只以實體檔名名稱部分做之後 FileDownloadREST 比對的 KEY
        String filekey = vo.getApplicationdata().getFvitem().getFilename();
        int i = filekey.lastIndexOf(".");
        filekey = (i>0)? filekey.substring(0, i):filekey;
        
        sb.append(GlobalConstant.URL_GET_FILE)
                .append("/").append(vo.getApplicationdata().getContainerid()) // cid
                .append("/").append(PublicationEnum.DOC.getCode()) // ctype
                .append("/").append(vo.getApplicationdata().getId()) // appid
                .append("/").append(filekey); // fn
        
        return sb.toString();
    }
    public String preparePrivateDownloadUrl(AttachmentVO vo){
        return preparePublicDownloadUrl(vo);
    }
    public String genDocUrl(AttachmentVO vo, boolean isPublic, boolean widthContextPath){
        try{
            String url = (isPublic)?preparePublicDownloadUrl(vo):preparePrivateDownloadUrl(vo);
            url = (widthContextPath)? JsfUtils.getContextPath()+url:url;
            
            logger.debug("genDocUrl url = " + url);
            return url;
        }catch(Exception e){
            logger.error("genDocUrl Exception:\n", e);
        }
        return "";
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Exception">
    /**
     * 未知例外處理
     * @param operator
     * @param methodName
     * @param e 
     */
    public void processUnknowException(TcUser operator, String methodName, Exception e){
        UUID uuid = UUID.randomUUID();
        String constraintViolations = "";
        if(e instanceof EJBException){
            constraintViolations = ExceptionHandlerUtils.printConstraintViolationException((EJBException)e);
        }
        processUnknowException(operator, methodName, e, uuid.toString(), constraintViolations);
    }
    public void processUnknowException(TcUser operator, String methodName, Exception e, String errorCode, String constraintViolations){
        String hostname = NetworkUtils.getHostIP(); // WebUtils.getHostName();
        String datetime = DateUtils.format(new Date());
        logger.error("processUnknowException ["+datetime+"]["+hostname+"]["+methodName+"] ("+errorCode+") Exception:\n", e);

        NotificationUtils sender = new NotificationUtils();
        sender.notifyOnException(getNotifyAdmins(), operator, methodName, e, errorCode, constraintViolations);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for Ec File Process">
    /**
     * 寫入實體檔案
     * @param fileEnum
     * @param storeId
     * @param content
     * @param fext
     * @return
     * @throws IOException 
     */
    public FileVO writeRealFile(FileEnum fileEnum, String oriFileName, byte[] content, boolean isImage, Long keyId) throws IOException{
        FileVO fileVO = new FileVO();
        // 取附檔名
        String[] fs = FileUtils.getFileExtension(oriFileName);
        String name = fs[0];
        String fext = fs[1];
        // 儲存實體檔案
        String root = fileEnum.getRootDir();
        String dir = root + (keyId!=null?keyId:"");
        File dirfile = new File(dir);
        dirfile.mkdirs(); //for several levels, without the "s" for one level
        String saveFileName = UUID.randomUUID().toString() + "." +fext;
        String saveFileNameFull = dir + GlobalConstant.FILE_SEPARATOR + saveFileName;
        File file = new File(saveFileNameFull);
        FileUtils.writeByteArrayToFile(file, content);
        logger.info("writeRealFile save file finish. oriFileName="+oriFileName+", saveFileNameFull = \n"+saveFileNameFull);
        
        if( isImage ){
            // 縮圖
            ImageVO retImgVO = new ImageVO();
            // 取得Image寬高
            String errMsg1 = img.getImageInfo(saveFileNameFull, retImgVO);
            if( errMsg1!=null ){
                logger.error("writeRealFile getImageInfo ... "+errMsg1);
            }else{
                // 縮圖 (縮圖需寬高資訊)
                String errMsg2 = img.compressImageFile(dir, saveFileName, saveFileNameFull, true, retImgVO);
                if( errMsg2!=null ){
                    logger.error("writeRealFile compressImageFile ... "+errMsg1);
                }
            }
            logger.info("writeRealFile compress image finish.");
        }
        
        fileVO.setFileSize(content.length);
        fileVO.setFilename(oriFileName);
        fileVO.setName(name);
        fileVO.setSavedir(dir);
        fileVO.setSavename(saveFileName);
        fileVO.setSaveFileNameFull(saveFileNameFull);
        return fileVO;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public Properties getJndiGlobalConfig() {
        return jndiGlobalConfig;
    }

    public void setJndiGlobalConfig(Properties jndiGlobalConfig) {
        this.jndiGlobalConfig = jndiGlobalConfig;
    }

    public Properties getJndiConfig() {
        return jndiConfig;
    }

    public void setJndiConfig(Properties jndiConfig) {
        this.jndiConfig = jndiConfig;
    }
    //</editor-fold>
}
