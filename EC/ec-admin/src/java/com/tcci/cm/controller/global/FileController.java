package com.tcci.cm.controller.global;

import com.tcci.cm.entity.doc.CmDocument;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.util.FileUtils;
import com.tcci.fc.vo.AttachmentVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
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
    @EJB private ContentFacade contentFacade;
    @EJB private AttachmentFacade attachmentFacade;

    private String root;
    private UploadedFile file;
    private StreamedContent downloadFile;
    
    public FileController() {
    }
    
    public UploadedFile getFile() {
        return file;
    }
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        logger.debug("handleFileUpload ... event.getFile()="+event.getFile());
        file = event.getFile();
    }
    /**
     * 儲存上傳文件 (只用於一個ContentHolder連結一個檔)
     * @param cmDocument
     * @param tcUser
     * @return
     * @throws Exception
     */
    public boolean saveDocument(CmDocument cmDocument, TcUser tcUser) throws Exception{
        String srcFile = file.getFileName();
        byte[] content;
        String contentType;
        
        // UploadFile.getContents() not work in GlassFish4.1 (JSF2.3)
        // byte[] content = file.getContents();
        InputStream inputstream = file.getInputstream();
        content = IOUtils.toByteArray(inputstream);
        contentType = file.getContentType();
        logger.debug("saveDocument srcFile = "+srcFile);
        logger.debug("saveDocument content size = "+((content!=null)?content.length:0));
        
        // 讀取實體檔案
        if( content==null ){
            logger.error("saveDocument content = null !");
            return false;
        }else{
            List<AttachmentVO> attachments = attachmentFacade.loadContent(cmDocument);
            if( attachments==null ){
                attachments = new ArrayList<AttachmentVO>();
            }
            // 上傳 FVVAULT
            AttachmentVO attachmentVO = new AttachmentVO();
            attachmentVO.setSize(content.length);
            attachmentVO.setContent(content);
            attachmentVO.setContentType(contentType);
            attachmentVO.setFileName(srcFile);
            attachments.add(attachmentVO);
            
            logger.debug("saveDocument this.getLoginUser() = "+tcUser.getLoginAccount());
            
            // for本機連62
            //contentFacade.saveContent(cmDocument, attachments, tcUser);
            TcDomain domain = new TcDomain();
            domain.setId(GlobalConstant.getDomain());
            contentFacade.saveContent(domain, cmDocument, attachments, tcUser);
        }
        
        return true;
    }
    
    /**
     * 依 ContentHolder  抓取檔案
     * @param cmDocument
     * @return
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public boolean fetchDocFileByHolder(CmDocument cmDocument) throws FileNotFoundException, UnsupportedEncodingException{
        AttachmentVO attachmentVO = getLastAttachment(cmDocument); // 取最新版本即可
        if( attachmentVO!=null ){
            String fileName = URLEncoder.encode(attachmentVO.getFileName(), GlobalConstant.FILE_ENCODING);
            downloadFile = new DefaultStreamedContent(
                    attachmentFacade.getContentStream(attachmentVO),
                    attachmentVO.getContentType(),
                    fileName);
            return true;
        }else{
            logger.error("fetchDocFileByHolder error : attachmentVO=null, cmDocument.id = "+cmDocument.getId());
        }
        
        return false;
    }
    
    /**
     *  依 CententHolder 取最新檔案
     * @param icsDocument
     * @return
     */
    public AttachmentVO getLastAttachment(ContentHolder contentHolder){
        List<AttachmentVO> attachments = attachmentFacade.loadContent(contentHolder);
        AttachmentVO attachmentVO = null;
        if( attachments!=null && !attachments.isEmpty() ){
            // 取最新版本即可
            attachmentVO = attachments.get(attachments.size()-1);
        }
        
        return attachmentVO;
    }
    
    public StreamedContent getDownloadFile() {
        return downloadFile;
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
                outFileName = URLEncoder.encode(outFileName, "UTF-8"); //匯出中文檔名編碼處理
                
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
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
    //</editor-fold>

}