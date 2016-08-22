/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.util;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.ContentRole;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.worklist.controller.util.JsfUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
    private String fileDesc; // 檔案備註
    private ContentHolder container;
    private ContentRole contentRole;
    @EJB
    private ContentFacade contentFacade;
    @EJB
    private TcDomainFacade domainFacade;

    public void init(ContentHolder container, ContentRole contentRole) {
        logger.debug("init(),container={},contentRole={}", new Object[]{container, contentRole.getDisplayName()});
        this.container = container;
        this.contentRole = contentRole;
        this.attachmentVOList = new ArrayList();
        try {

            for (TcApplicationdata applicationData : contentFacade.getApplicationdata(container, contentRole)) {
                AttachmentVO vo = new AttachmentVO();
                vo.setApplicationdata(applicationData);
                vo.setFileName(applicationData.getFvitem().getName());
                vo.setSize(applicationData.getFvitem().getFilesize());
                this.attachmentVOList.add(vo);
            }
        } catch (Exception e) {
            logger.error("e={}", e);
        }
        this.readonly = false;
    }

    // action
    public void handleFileUpload(FileUploadEvent event) {
        logger.debug("handleFileUpload");
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
        attachment.setContentRole(contentRole);
        logger.debug("uploadedFile.getSize()={}", uploadedFile.getSize());
        attachment.setSize(uploadedFile.getSize());
        attachment.setIndex(attachmentVOList.size());
        attachmentVOList.add(attachment);
        logger.debug("attachmentVOList.size()={}", attachmentVOList.size());
    }

    public void save() {
        try {
            for(TcApplicationdata applicationdata: contentFacade.getApplicationdata(container)) {
                if(applicationdata.getContentrole().equals(this.contentRole.toCharacter())) {
                    continue;
                }
                AttachmentVO vo = new AttachmentVO();
                vo.setApplicationdata(applicationdata);
                attachmentVOList.add(vo);
            }
            contentFacade.saveContent(container, attachmentVOList);
            JsfUtils.addSuccessMessage(((SkProductMaster)container).getDisplayIdentifier()+" 的 " + contentRole.getDisplayName() + " 已修改!");
        } catch (Exception e) {
            logger.debug("e={}", e);
        }
    }

    public void downloadAction(AttachmentVO vo) {
        selectedAttachmentVO = vo;
        try {
            String fileName = URLEncoder.encode(selectedAttachmentVO.getFileName(), "UTF-8");
            if (vo.getApplicationdata() != null) {
                InputStream in = contentFacade.findContentStream(vo.getApplicationdata());
                downloadFile = new DefaultStreamedContent(in, selectedAttachmentVO.getContentType(), fileName);
            } else {
                downloadFile = new DefaultStreamedContent(new ByteArrayInputStream(selectedAttachmentVO.getContent()), selectedAttachmentVO.getContentType(), fileName);
            }
        } catch (Exception e) {
            JsfUtils.addErrorMessage(e.getMessage());
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

    public ContentHolder getContainer() {
        return container;
    }

    public void setContainer(ContentHolder container) {
        this.container = container;
    }

    public ContentRole getContentRole() {
        return contentRole;
    }

    public void setContentRole(ContentRole contentRole) {
        this.contentRole = contentRole;
    }

}
