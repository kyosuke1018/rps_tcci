/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.util;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcservice.controller.login.UserSession;
import com.tcci.fcservice.facade.attachment.AttachmentFacade;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AbortProcessingException;
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
public class AttachmentController {
    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
    
    private List<AttachmentVO> attachmentVOList;
    private AttachmentVO selectedAttachmentVO;
    private StreamedContent downloadFile;    
    private boolean readonly = true;
    
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    private TcUser loginUser;  // 登入者
    
    private AttachmentEventListener eventListener;
    
    private ContentHolder container;
    
    @EJB
    private AttachmentFacade attachmentFacade;
    
    public void init(ContentHolder container, boolean readonly) {
        loginUser = userSession.getTcUser();
        if (loginUser == null) {
            return;
        }
        this.container = container;
        attachmentVOList = null;
        if (container != null) {
            attachmentVOList = attachmentFacade.loadContent(container);
        }
        this.readonly = readonly;
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
        AttachmentVO attachment = new AttachmentVO();
        attachment.setContent(uploadedFile.getContents());
        attachment.setFileName(getFilename(uploadedFile.getFileName()));
        attachment.setContentType(uploadedFile.getContentType());
        attachment.setSize(uploadedFile.getSize());
        attachment.setIndex(attachmentVOList.size());
        attachmentVOList.add(attachment);               
    }

    public void downloadAction(AttachmentVO vo) {
        selectedAttachmentVO = vo;
        try {
            String fileName = URLEncoder.encode(selectedAttachmentVO.getFileName(), "UTF-8");
            InputStream in = attachmentFacade.getContentStream(selectedAttachmentVO);
            downloadFile = new DefaultStreamedContent(in, selectedAttachmentVO.getContentType(), fileName);
            if (eventListener != null)
                eventListener.downloadAction(vo);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getMessage());
            throw new AbortProcessingException("can not readfile:" + vo.getFileName());
        }
    }

    public StreamedContent getDownloadFile() {
        if (container != null && selectedAttachmentVO != null) {
            logger.info("file downloaded, container={}, id={}, loginAccount={}, fileName={}", 
                    new Object[] {
                        container.getClass().getName(),
                        container.getId(),
                        loginUser.getLoginAccount(),
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
    
    // helper
    private String getFilename(String pathname) {
        int index = pathname.lastIndexOf("\\");
        if (index < 0)
            index = pathname.lastIndexOf("/");
        return pathname.substring(index + 1);
    }
    
    // getter, setter
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

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public AttachmentEventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(AttachmentEventListener eventListener) {
        this.eventListener = eventListener;
    }
}
