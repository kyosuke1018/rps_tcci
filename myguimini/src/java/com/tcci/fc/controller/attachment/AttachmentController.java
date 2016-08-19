/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.attachment;

import com.tcci.fc.controller.login.UserSession;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.content.TcFvitem;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.ByteArrayInputStream;
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
    private ContentHolder container;
    @EJB
    private ContentFacade contentFacade;

    public void init(ContentHolder container, boolean readonly) {
        if (!userSession.isValid()) {
            return;
        }

        this.container = container;
        attachmentVOList = null;
        if (container != null) {
            attachmentVOList = loadContent(container);
        }
        this.readonly = readonly;
    }

    public List<AttachmentVO> loadContent(ContentHolder container) {
        List<TcApplicationdata> applicationDataList = contentFacade.getApplicationdata(container);
        List<AttachmentVO> result = new ArrayList<AttachmentVO>();
        if (applicationDataList != null) {
            int index = 0;
            for (TcApplicationdata a : applicationDataList) {
                AttachmentVO vo = new AttachmentVO();
                vo.setApplicationdata(a);
                TcFvitem fvItem = a.getFvitem();
                if (fvItem == null) {
                    continue;
                }
                // vo.setContent(content); // 下載時再讀取檔案
                vo.setContentType(fvItem.getContenttype());
                vo.setFileName(fvItem.getName());
                vo.setSize(fvItem.getFilesize());
                vo.setIndex(index++);
                result.add(vo);
            }
        }
        return result;
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
            InputStream in;
            if (selectedAttachmentVO.getApplicationdata() == null) {
                in = new ByteArrayInputStream(vo.getContent());
            } else {
                in = contentFacade.findContentStream(selectedAttachmentVO.getApplicationdata());
            }
            downloadFile = new DefaultStreamedContent(in, selectedAttachmentVO.getContentType(), fileName);
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getMessage());
            throw new AbortProcessingException("can not readfile:" + vo.getFileName());
        }
    }

    public StreamedContent getDownloadFile() {
        if (container != null && selectedAttachmentVO != null) {
            logger.info("file downloaded, container={}, id={}, loginAccount={}, fileName={}",
                    new Object[]{
                        container.getClass().getName(),
                        container.getId(),
                        userSession.getAdaccount(),
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

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
