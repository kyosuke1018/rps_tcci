/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.attachment;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.inject.Inject;
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
    private String fileDesc; // 檔案備註
    private ContentHolder container;
    @Inject
    private ContentFacade contentFacade;

    public void init(ContentHolder container, boolean readonly) {
        this.container = container;
        attachmentVOList = (null == container) ? null : contentFacade.findAttachment(container);
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
        logger.debug("uploadedFile.getContents()={}", uploadedFile.getContents());
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
            InputStream in = contentFacade.findContentStream(vo);
            downloadFile = new DefaultStreamedContent(in, selectedAttachmentVO.getContentType(), fileName);
        } catch (Exception e) {
            logger.error("e={}", e);
            JsfUtil.addErrorMessage(e.getMessage());
            throw new AbortProcessingException("can not readfile:" + vo.getFileName());
        }
    }

    public StreamedContent getDownloadFile() {
        if (container != null && selectedAttachmentVO != null) {
            Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
            String loginAccount = null == principal ? "" : principal.getName();
            logger.warn("file downloaded, container={}, id={}, loginAccount={}, fileName={}",
                    new Object[]{
                        container.getClass().getName(),
                        container.getId(),
                        loginAccount,
                        selectedAttachmentVO.getFileName()});
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

    public void editFileDesc(AttachmentVO vo) {
        selectedAttachmentVO = vo;
        fileDesc = vo.getDescription();
    }

    public void editFileDescOK() {
        selectedAttachmentVO.setDescription(fileDesc);
    }

    // helper
    private String getFilename(String pathname) {
        int index = pathname.lastIndexOf('\\');
        if (index < 0) {
            index = pathname.lastIndexOf('/');
        }
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

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

}
