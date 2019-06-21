package com.tcci.cm.controller.global;

import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.util.FileUtils;
import com.tcci.fc.vo.AttachmentVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * User: peter.pan
 * Date: 9/5/12
 */
@ManagedBean(name = "fileController")
@ViewScoped
public class FileController extends AbstractController implements Serializable {
    @EJB private TcDomainFacade domainFacade;
    @EJB private ContentFacade contentFacade;
    @EJB private AttachmentFacade attachmentFacade;

    private String root;
    private StreamedContent downloadFile;
    private UploadedFile file;
    private List<UploadedFile> files;
    private List<AttachmentVO> attachments;
    private List<Long> existedAppIds;
    private ContentHolder contentHolderTemp;
    private TcUser operator;
    private TcDomain defDomain;
    
    @PostConstruct
    public void init(){
        logger.debug("FileController init ...");
        file = null;
        files = new ArrayList<UploadedFile>();
        attachments = new ArrayList<AttachmentVO>();
        downloadFile = null;
        defDomain = domainFacade.getDefaultDomain();
    }
    
    public void handleSingleFileUpload(FileUploadEvent event) {
        try{
            logger.debug("handleSingleFileUpload event.getFile().getFileName()="+ event.getFile().getFileName());
            file = event.getFile();
            files.add(file);
        }catch(Exception e){
            JsfUtils.addErrorMessage("檔案上傳失敗!");
            logger.error("handleFileUploadAutoSave exception:\n", e);
        }
    }
    
    /**
     * 增加上傳檔
     * @param event 
     */
    public void handleFileUpload(FileUploadEvent event) {
        try{
            logger.debug("handleFileUpload event.getFile().getFileName()="+ event.getFile().getFileName());
            files.add(event.getFile());
        }catch(Exception e){
            JsfUtils.addErrorMessage("檔案上傳失敗!");
            logger.error("handleFileUploadAutoSave exception:\n", e);
        }
    }
    
    /**
     * 上傳直接儲存，並關聯至 contentHolderTemp
     */
    /*public void handleMultiFileUploadAutoSave() {
        logger.debug("handleMultiFileUploadAutoSave contentHolderTemp="+ contentHolderTemp);
        try{
            // 儲存 for get URL
            saveUploadedFiles(contentHolderTemp, false, this.operator);
        }catch(Exception e){
            JsfUtils.addErrorMessage("檔案上傳失敗!");
            logger.error("handleFileUploadAutoSave exception:\n", e);
        }
    }*/
    
    /**
     * 上傳檔包裝至 AttachmentVO for 編輯 description
     */
    public void handleFileUploadToAttachment() {
        logger.debug("handleMultiFileUploadToAttachment contentHolderTemp="+ contentHolderTemp);
        try{
            // 儲存 for get URL
            attachments = prepareUploadToAttachment(this.files);
        }catch(Exception e){
            JsfUtils.addErrorMessage("檔案上傳失敗!");
            logger.error("handleFileUploadAutoSave exception:\n", e);
        }
    }
    
    /**
     * 儲存上傳文件與 contain holder 1 對 1 (多筆)
     * @param holderList
     * @param domain
     * @param tcUser
     * @param simulated
     * @return
     * @throws Exception 
     */
    public boolean saveUploadedFilesByMultiHolder(List holderList, TcDomain domain, TcUser tcUser, boolean simulated) throws Exception{
        if( files==null || holderList==null 
                || files.isEmpty() ||  holderList.isEmpty() 
                || files.size()!=holderList.size() ){
            logger.error("saveUploadedFilesByMultiHolder error init check ");
            return false;
        }
        
        boolean result = true;
        for(int i=0; i<files.size(); i++){
            // 每個 Content Holder 只關聯一個檔
            List<UploadedFile> fileList = new ArrayList<UploadedFile>();
            fileList.add(files.get(i));
            List<AttachmentVO> newAttachments = prepareUploadToAttachment(fileList);
            
            existedAppIds = new ArrayList<Long>();
            result = result && attachmentFacade.saveFiles((ContentHolder)holderList.get(i), newAttachments, existedAppIds, true, domain, tcUser, simulated);
        }
        
        return result;
    }
    public boolean saveSingleUploadedFile(ContentHolder holder, TcDomain domain, TcUser tcUser, boolean simulated) throws Exception{
        if( file==null ){
            logger.error("saveSingleUploadedFile file==null");
            return false;
        }
        
        files = new ArrayList<UploadedFile>();
        files.add(file);
        return saveUploadedFiles(holder, true, domain, tcUser, simulated);
    }
    
    /**
     * 儲存上傳文件與 contain holder 1 對 多
     * @param holder
     * @param removeOri 刪除原關聯檔
     * @param tcUser
     * @param simulated
     * @return
     * @throws Exception
     */
    public boolean saveUploadedFiles(ContentHolder holder, boolean removeOri, TcUser tcUser, boolean simulated) throws Exception{
        return saveUploadedFiles(holder, removeOri, this.defDomain, tcUser, simulated);
    }
    public boolean saveUploadedFiles(ContentHolder holder, boolean removeOri, TcDomain domain, TcUser tcUser, boolean simulated) throws Exception{
        if( files==null || files.isEmpty() ){
            logger.error("saveUploadedFiles error files==null");
            return false;
        }
        if( existedAppIds==null ){
            this.existedAppIds = new ArrayList<Long>();
        }
        List<AttachmentVO> newAttachments = prepareUploadToAttachment(this.files);
        return attachmentFacade.saveFiles(holder, newAttachments, existedAppIds, removeOri, domain, tcUser, simulated);
    }
    public boolean saveFiles(ContentHolder holder, List<AttachmentVO> newAttachments,  
            boolean removeOri, TcUser tcUser, boolean simulated) throws Exception{
        this.existedAppIds = new ArrayList<Long>();
        return attachmentFacade.saveFiles(holder, newAttachments, existedAppIds, removeOri, this.defDomain, tcUser, simulated);
    }
  
    /**
     * 取得並保留已存在 App Ids 至 existedAppIds，以便後續分辨哪些是本次上傳的
     * @param holder
     * @return 
     */
    public List<AttachmentVO> findExistedAttachments(ContentHolder holder){
        this.existedAppIds = new ArrayList<Long>();
        return attachmentFacade.findExistedAttachments(holder, existedAppIds);
    }
  
    /**
     * List UploadedFile to List AttachmentVO
     * @param fileList
     * @return 
     */
    public List<AttachmentVO> prepareUploadToAttachment(List<UploadedFile> fileList){
        if( fileList==null ){
            logger.error("prepareUploadToAttachment error fileList==null");
            return null;
        }
        List<AttachmentVO> attachmentList = new ArrayList<AttachmentVO>();

        int index = 0;
        for(UploadedFile fi : fileList){
            String srcFile = getFileName(fi); //file.getFileName(); IE10以前會含path, IE11及Chrome只會有檔名
            logger.debug("prepareUploadToAttachment srcFile = "+srcFile);
            
            try{
                // UploadFile.getContents() not work in GlassFish4.1 (JSF2.3)
                // byte[] content = file.getContents();
                InputStream inputstream = fi.getInputstream();
                byte[] content = IOUtils.toByteArray(inputstream);
                logger.debug("inputStreamToAttachmentVO content size = "+((content!=null)?content.length:0));
                
                String contentType = fi.getContentType();            
                AttachmentVO attachmentVO = attachmentFacade.genAttachmentVO(srcFile, contentType, content, index);

                if( attachmentVO.getContent()==null ){
                    logger.error("prepareUploadToAttachment content = null !");
                }else{
                    attachmentList.add(attachmentVO);
                    index++;
                }
            }catch(IOException e){
                logger.error("prepareUploadToAttachment IOException "+srcFile+":\n", e);
            }
        }
        
        return attachmentList;
    }
    
    public String getFileName(UploadedFile uploadedFile){
        String uploadFileName = uploadedFile.getFileName();// IE10以前會含path, IE11及Chrome只會有檔名
        try{
            if( uploadFileName.lastIndexOf("\\")>-1 ){// windows file separator
                uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
            }else if( uploadFileName.lastIndexOf(File.separator)>-1 ){
                uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf(File.separator) + 1);
            }
        }catch(Exception e){
            logger.error("getFileName error parse filename: " + uploadFileName + "\n", e);
        }
        return uploadFileName;
    }
    
    /**
     * 取得完整檔名
     * @param attachmentVO
     * @return 
     */
    public String getFullFileName(AttachmentVO attachmentVO){
        return attachmentFacade.getFullFileName(attachmentVO);
    }
    
    /**
     * 取得關聯上傳檔資訊 (不含檔案內容)
     * @param holder
     * @return 
     */
    public List<AttachmentVO> findUploadedFiles(ContentHolder holder){
        return attachmentFacade.loadContent(holder);
    }
    
    /**
     * 刪除上傳檔
     * @param attachmentVO 
     * @param simulated 
     */
    public void deleteAttachment(AttachmentVO attachmentVO, boolean simulated){
        attachmentFacade.remove(attachmentVO.getApplicationdata(), simulated);
    }
    
    /**
     * 依 ContentHolder  抓取檔案
     * @param holder
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public boolean fetchDocFileByHolder(ContentHolder holder) throws FileNotFoundException, UnsupportedEncodingException{
        if( holder!=null ){
            logger.info("fetchDocFileByHolder holder.id = "+holder.getId());
            AttachmentVO attachmentVO = getLastAttachment(holder); // 取最新版本即可
            return fetchDocFile(attachmentVO);
        }else{
            logger.error("fetchDocFileByHolder holder=null");
        }
        return false;
    }
    public boolean fetchDocFile(AttachmentVO attachmentVO) throws FileNotFoundException, UnsupportedEncodingException{
        if( attachmentVO!=null ){
            //使用 <p:fileDownload> filename不需再用URLEncoder.encode(5.3 -> 6.x)
//            String fileName = URLEncoder.encode(attachmentVO.getFileName(), GlobalConstant.ENCODING_DEF);
            String fileName = attachmentVO.getFileName();
            downloadFile = new DefaultStreamedContent(
                    attachmentFacade.getContentStream(attachmentVO),
                    attachmentVO.getContentType(),
                    fileName);
            return true;
        }else{
            logger.error("fetchDocFile error : attachmentVO=null");
        }
        
        return false;
    }
    /**
     *  依 CententHolder 取最新檔案
     * @param contentHolder
     * @return
     */
    public AttachmentVO getLastAttachment(ContentHolder contentHolder){
        return attachmentFacade.getLastAttachment(contentHolder);
    }
        
    /**
     * 移除檔案
     * @param filename
     * @return 
     */
    public boolean removeFile(String filename){
        try{
            if( filename==null ){
                logger.error("removeFile filename==null ...");
                return false;
            }
            return FileUtils.deleteQuietly(new File(filename));
        }catch(Exception e){
            logger.error("removeFile exception :\n", e);
        }
        return false;
    }
    
    /**
     * File to Stream
     * @param srcFile
     * @param outFileName
     * @param fileType
     * @return
     */
    public boolean prepareDownloadFileStream(String srcFile, String outFileName, String fileType){
        File theFile = new File(srcFile);
        if( theFile.exists() && theFile.isFile() ){
            try{
                outFileName = URLEncoder.encode(outFileName, GlobalConstant.ENCODING_DEF); //匯出中文檔名編碼處理
                
                downloadFile = new DefaultStreamedContent(new FileInputStream(theFile), fileType, outFileName);
                return true;
            }catch(FileNotFoundException e){
                logger.error("prepareDownloadFileStream FileNotFoundException:\n", e);
            } catch (UnsupportedEncodingException e) {
                logger.error("prepareDownloadFileStream UnsupportedEncodingException:\n", e);
            }
        }else{
            logger.error("prepareDownloadFileStream file not exists : "+ srcFile);
        }
        return false;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public ContentHolder getContentHolderTemp() {
        return contentHolderTemp;
    }

    public void setContentHolderTemp(ContentHolder contentHolderTemp) {
        this.contentHolderTemp = contentHolderTemp;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public TcDomain getDefDomain() {
        return defDomain;
    }

    public void setDefDomain(TcDomain defDomain) {
        this.defDomain = defDomain;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<Long> getExistedAppIds() {
        return existedAppIds;
    }

    public void setExistedAppIds(List<Long> existedAppIds) {
        this.existedAppIds = existedAppIds;
    }

    public List<AttachmentVO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentVO> attachments) {
        this.attachments = attachments;
    }

    public TcUser getOperator() {
        return operator;
    }

    public void setOperator(TcUser operator) {
        this.operator = operator;
    }
    
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }
    //</editor-fold>

}