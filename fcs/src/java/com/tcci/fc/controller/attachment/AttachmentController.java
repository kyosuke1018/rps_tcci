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
import javax.faces.event.PhaseId;
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
public class AttachmentController {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
    private List<AttachmentVO> attachmentVOList;
    private AttachmentVO selectedAttachmentVO;
    private StreamedContent downloadFile;
    private boolean readonly = true;
    private int maxFiles = 1;
    @ManagedProperty(value = "#{userSession}")
    private UserSession userSession;
    private ContentHolder container;
    
    private AttachmentEventListener eventListener;
    
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
    public void handleFileUpload(FileUploadEvent event, boolean singleFile) {
        if (readonly) {
            return;
        }
        // 等form資料更新後再處理upload, 參考底下網址
        // http://stackoverflow.com/questions/11008943/using-pfileupload-with-the-hinputtext-doesnt-get-the-inputext-mapping-va
        if (!PhaseId.INVOKE_APPLICATION.equals(event.getPhaseId())) {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            event.queue();
            return;
        }
        if (attachmentVOList == null) {
            attachmentVOList = new ArrayList<AttachmentVO>();
        }
        /* 僅保留一筆
        if (attachmentVOList.size() >= maxFiles) {
            JsfUtil.addErrorMessage("超過上傳上限!");
            return;
        }
        */

        if(singleFile){
            attachmentVOList.clear();
        }
        UploadedFile uploadedFile = event.getFile();
        // 驗證檔案
        if (eventListener != null && !eventListener.uploadVerify(uploadedFile)) {
            return;
        }
        AttachmentVO attachment = new AttachmentVO();
        // UploadFile.getContents() not work in GlassFish4.1 (JSF2.3)
//         attachment.setContent(uploadedFile.getContents());
        try{
            InputStream inputstream = uploadedFile.getInputstream();
            byte[] content = IOUtils.toByteArray(inputstream);
            attachment.setContent(content);
        }catch(Exception e){
            logger.error("handleFileUpload exception:\n", e);
        }
        attachment.setFileName(getFilename(uploadedFile.getFileName()));
        attachment.setContentType(uploadedFile.getContentType());
        attachment.setSize(uploadedFile.getSize());
        attachment.setIndex(attachmentVOList.size());
        attachmentVOList.add(attachment);
    }
    
    //20151112 中橡可能要上傳多筆 template
    public void handleFileUploadMulti(FileUploadEvent event) {
        this.handleFileUpload(event, false);
    }
    public void handleFileUploadSingle(FileUploadEvent event) {
        this.handleFileUpload(event, true);
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

    public int getMaxFiles() {
        return maxFiles;
    }

    public void setMaxFiles(int maxFiles) {
        this.maxFiles = maxFiles;
    }

    public AttachmentEventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(AttachmentEventListener eventListener) {
        this.eventListener = eventListener;
    }
}
