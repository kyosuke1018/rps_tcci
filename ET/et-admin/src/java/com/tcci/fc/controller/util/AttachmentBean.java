/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.util;

import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
public class AttachmentBean {

    private final static Logger logger = LoggerFactory.getLogger(AttachmentBean.class);

    private StreamedContent streamedContent; // p:fileDownload
    private String allowTypes = "/.*/"; // p:fileUpload allowTypes
    private Integer sizeLimit; // p:fileUpload sizeLimit (each file), null for unlimited
    private int fileLimit = 0; // 0 for unlimited
    private boolean readOnly;
    private AttachmentEventListener eventListener;

    private final ContentFacade contentFacade;
    private final Map<Integer, List<AttachmentVO>> map = new HashMap<>();

    // ctor
    public AttachmentBean(ContentFacade contentFacade) {
        this.contentFacade = contentFacade;
    }

    // fluent API
    public AttachmentBean allowTypes(String allowTypes) {
        this.allowTypes = allowTypes;
        return this;
    }

    public AttachmentBean sizeLimit(Integer sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    public AttachmentBean fileLimit(int fileLimit) {
        this.fileLimit = fileLimit;
        return this;
    }

    public AttachmentBean readOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    // action
    public void fileUpload(FileUploadEvent event) {
        ContentHolder contentHolder = (ContentHolder) event.getComponent().getAttributes().get("contentHolder");
        List<AttachmentVO> attachmentList = getAttachmentList(contentHolder);
        if (fileLimit > 0 && attachmentList.size() >= fileLimit) {
            JsfUtils.addErrorMessage("超過可允許上傳的檔案數目!");
            return;
        }
        UploadedFile uploadedFile = event.getFile();
        // 驗證檔案
        if (eventListener != null && !eventListener.uploadVerify(uploadedFile)) {
            return;
        }
        AttachmentVO attachment = new AttachmentVO();
        attachment.setContent(uploadedFile.getContents());
        String fileName = uploadedFile.getFileName();
        int index = fileName.lastIndexOf('\\');
        if (index < 0) {
            index = fileName.lastIndexOf('/');
        }
        if (index > 0) {
            fileName = fileName.substring(index + 1);
        }
        attachment.setFileName(fileName);
        attachment.setContentType(uploadedFile.getContentType());
        attachment.setSize(uploadedFile.getSize());
        attachment.setIndex(attachmentList.size());
        attachmentList.add(attachment);
    }

    public void fileDownload(AttachmentVO vo) {
        try {
            //使用 <p:fileDownload> filename不需再用URLEncoder.encode(5.3 -> 6.x)
//            String fileName = URLEncoder.encode(vo.getFileName(), "UTF-8");
            String fileName = vo.getFileName();
            InputStream in = (vo.getContent() != null)
                    ? new ByteArrayInputStream(vo.getContent())
                    : contentFacade.findContentStream(vo);
            streamedContent = new DefaultStreamedContent(in, vo.getContentType(), fileName);
//        } catch (UnsupportedEncodingException | FileNotFoundException e) {    
        } catch (FileNotFoundException e) {
            logger.error("fileDownload exception!", e);
            JsfUtils.addErrorMessage(e.getMessage());
        }
    }

    public void fileRemove(ContentHolder contentHolder, AttachmentVO vo) {
        getAttachmentList(contentHolder).remove(vo);
    }

    // helper
    public String getIdentity(ContentHolder contentHolder) {
        return String.valueOf(System.identityHashCode(contentHolder));
    }

    public List<AttachmentVO> getAttachmentList(ContentHolder contentHolder) {
        if (null == contentHolder) {
            // throw new IllegalArgumentException("contentHolder can not be null!");
            return null;
        }
        int key = System.identityHashCode(contentHolder);
        List<AttachmentVO> result = map.get(key);
        if (null == result) {
            if (contentHolder.getId() == null) {
                result = new ArrayList<>();
            } else {
                result = contentFacade.findAttachment(contentHolder);
                Collections.sort(result, new Comparator<AttachmentVO>() {
                    @Override
                    public int compare(AttachmentVO o1, AttachmentVO o2) {
                        Date time1 = o1.getApplicationdata().getFvitem().getCreatetimestamp();
                        Date time2 = o2.getApplicationdata().getFvitem().getCreatetimestamp();
                        return time1.compareTo(time2);
                    }
                });
            }
            map.put(key, result);
        }
        return result;
    }

    // getter, setter
    public StreamedContent getStreamedContent() {
        return streamedContent;
    }

    public String getAllowTypes() {
        return allowTypes;
    }

    public void setAllowTypes(String allowTypes) {
        this.allowTypes = allowTypes;
    }

    public Integer getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(Integer sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public int getFileLimit() {
        return fileLimit;
    }

    public void setFileLimit(int fileLimit) {
        this.fileLimit = fileLimit;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public AttachmentEventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(AttachmentEventListener eventListener) {
        this.eventListener = eventListener;
    }

}
