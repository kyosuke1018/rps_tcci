/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.facade.AttachmentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jimmy
 */

@ManagedBean(name = "attachmentController")
@ViewScoped
public class AttachmentController extends SessionAwareController implements Serializable {
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB
    private AttachmentFacade attachmentFacade;
    //</editor-fold>
    
    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
    private List<AttachmentVO> attachmentVOList;
    private AttachmentVO selectedAttachmentVO;
    private StreamedContent downloadFile;    
    private boolean readonly = true;
    private ContentHolder container;
    private boolean onlyUploadOnce = false;
    private boolean disabledUpload = false;
    

    
    public void init(ContentHolder container, boolean readonly) {
        this.container = container;
        attachmentVOList = null;
        if (container != null) {
            attachmentVOList = attachmentFacade.loadContent(container);
        }
        this.readonly = readonly;
    }
    public void init(ContentHolder container, boolean readonly, boolean onlyUploadOnce) {
        init(container, readonly);
        this.onlyUploadOnce = onlyUploadOnce;
        this.disabledUpload = false;
    }
    // action
    public void handleFileUpload(FileUploadEvent event) {
        if (readonly) {
            return;
        }
        
        if (attachmentVOList == null) {
            attachmentVOList = new ArrayList<AttachmentVO>();
        }
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        //上傳檔名不得重複!
        boolean isDuplicate = false;
        for (AttachmentVO attachmentVO : attachmentVOList) {
            String this_fileName = attachmentVO.getFileName();
            if(this_fileName.equals(fileName)){
                isDuplicate = true;
            }
        }        
        if(isDuplicate){
            String msg = "上傳檔名不得重複!";
            JsfUtils.addErrorMessage(msg);
            logger.debug(msg);
            return;
        }
        
        AttachmentVO attachment = new AttachmentVO();
        // UploadFile.getContents() not work in GlassFish4.1 (JSF2.3)
        // attachment.setContent(uploadedFile.getContents());
        try{
            InputStream inputstream = uploadedFile.getInputstream();
            byte[] content = IOUtils.toByteArray(inputstream);
            attachment.setContent(content);
        }catch(Exception e){
            logger.error("handleFileUpload exception:\n", e);
        }
        attachment.setFileName(fileName);
        attachment.setContentType(uploadedFile.getContentType());
        attachment.setSize(uploadedFile.getSize());
        attachment.setIndex(attachmentVOList.size());
        attachmentVOList.add(attachment);   
        
        logger.debug("|onlyUploadOnce="+onlyUploadOnce+"|disabledUpload="+disabledUpload);
        if(onlyUploadOnce){
            disabledUpload = true;
        }
        logger.debug("|onlyUploadOnce="+onlyUploadOnce+"|disabledUpload="+disabledUpload);
        logger.debug("attachmentVOList = "+((attachmentVOList==null)?0:attachmentVOList.size()));
    }

    public void downloadAction(AttachmentVO vo) {
        selectedAttachmentVO = vo;
        try {
            String fileName = URLEncoder.encode(selectedAttachmentVO.getFileName(), "UTF-8");
            InputStream in = attachmentFacade.getContentStream(selectedAttachmentVO);
            downloadFile = new DefaultStreamedContent(in, selectedAttachmentVO.getContentType(), fileName);
        } catch (Exception e) {
            JsfUtils.addErrorMessage(e.getMessage());
            throw new AbortProcessingException(); //避免 java.io.IOException: Bad file descriptor
        }
    }

    public StreamedContent getDownloadFile() {
        if (container != null && selectedAttachmentVO != null) {
            logger.info("file downloaded, container={}, id={}, loginAccount={}, fileName={}", 
                    new Object[] {
                        container.getClass().getName(),
                        container.getId(),
                        this.getLoginUserCode(),
                        selectedAttachmentVO.getFileName()}                
                    );
        }
        return downloadFile;
    }
    
    public void remove(AttachmentVO vo) {
        if (readonly) {
            return;
        }
        
        if (attachmentVOList != null) {
            attachmentVOList.remove(vo);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public List<AttachmentVO> getAttachmentVOList() {
        return attachmentVOList;
    }
    
    public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
        this.attachmentVOList = attachmentVOList;
    }
    
    public AttachmentVO getSelectedAttachmentVO() {
        return selectedAttachmentVO;
    }
    
    public void setSelectedAttachmentVO(AttachmentVO selectedAttachmentVO) {
        this.selectedAttachmentVO = selectedAttachmentVO;
    }
    
    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }
    
    public boolean isReadonly() {
        return readonly;
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
    public boolean isOnlyUploadOnce() {
        return onlyUploadOnce;
    }

    public void setOnlyUploadOnce(boolean onlyUploadOnce) {
        this.onlyUploadOnce = onlyUploadOnce;
    }    
    public boolean isDisabledUpload() {
        return disabledUpload;
    }

    public void setDisabledUpload(boolean disabledUpload) {
        this.disabledUpload = disabledUpload;
    }    
    //</editor-fold>

}
