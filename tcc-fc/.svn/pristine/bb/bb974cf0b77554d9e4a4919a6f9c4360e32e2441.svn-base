package com.tcci.fcservice.controller.util;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcservice.controller.util.JsfUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class FileUploadController {  
    @EJB
    TcDomainFacade domainFacade;
    @EJB
    ContentFacade contentFacade;
    @EJB
    TcUserFacade userFacade;
    /*
    @EJB
    RepositoryFacade repositoryFacade;
    */
    private List<AttachmentVO> attachmentVOList = new ArrayList<AttachmentVO>();
    private AttachmentVO selectedAttachmentVO;
    private StreamedContent downloadFile;

    @PostConstruct
    public void init() {
        
    }

    
    public AttachmentVO getSelectedAttachmentVO() {
        return selectedAttachmentVO;
    }

    public void setSelectedAttachmentVO(AttachmentVO selectedAttachmentVO) {
        this.selectedAttachmentVO = selectedAttachmentVO;
    }

   
    
    public List<AttachmentVO> getAttachmentVOList() {
        return attachmentVOList;
    }

    public void setAttachmentVOList(List<AttachmentVO> attachmentVOList) {
        this.attachmentVOList = attachmentVOList;
    }  
    
    public String removeAttachmentVO() {      
        List<AttachmentVO> list = new ArrayList<AttachmentVO>();
        if (selectedAttachmentVO != null) {
            int index = 0;
            for (AttachmentVO uf : attachmentVOList) {
                if (uf.getIndex() != selectedAttachmentVO.getIndex()) {
                    uf.setIndex(index++);
                    list.add(uf);
                }
            }
        }      
        setAttachmentVOList(list);
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();     
        try {
            if (uploadedFile != null && uploadedFile.getInputstream() != null) {
                AttachmentVO attachment = new AttachmentVO();
                attachment.setContent(uploadedFile.getContents());
                attachment.setFileName(uploadedFile.getFileName());
                attachment.setContentType(uploadedFile.getContentType());
                attachment.setSize(uploadedFile.getSize());
                if (attachmentVOList == null) {
                    attachmentVOList = new ArrayList<AttachmentVO>();               
                }
                attachment.setIndex(attachmentVOList.size());
                attachmentVOList.add(attachment);               
            }
        } catch (Exception ex) {          
        }
    }

    public StreamedContent getDownloadFile() {      
        try {
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            AttachmentVO attachmentVO = (AttachmentVO) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "row");
            InputStream is = null;
            if (attachmentVO.getApplicationdata() != null) {
                is = (InputStream) contentFacade.findContentStream(attachmentVO.getApplicationdata().getFvitem());
            } else {
                is = new ByteArrayInputStream(attachmentVO.getContent());
            }
            String fileName = attachmentVO.getFileName();
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            downloadFile = new DefaultStreamedContent(is, attachmentVO.getContentType(), encodeFileName);
        } catch (Exception e) {            
            JsfUtil.addErrorMessage(e.getMessage());
        }       
        return downloadFile;
    }

    public void initAttachments(ContentHolder contentHolder)  {
        if (contentHolder != null) {            
            List<TcApplicationdata> list = new ArrayList<TcApplicationdata>();
            try {
                list = contentFacade.getApplicationdata(contentHolder);
            } catch (Exception ex) {
                Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
            }
            initAttachments(list);
        }    
    }   
 
    private void initAttachments(List<TcApplicationdata> applicationDataList)  {
        attachmentVOList = new ArrayList<AttachmentVO>();
        int index = 0;
        if( applicationDataList != null ){
            for (TcApplicationdata a : applicationDataList) {
                AttachmentVO vo = new AttachmentVO();               
                InputStream is;
                try {
                    is = (InputStream) contentFacade.findContentStream(a.getFvitem());                
                    byte[] contents = getBytesFromInputStream(is, a.getFvitem().getFilesize(), a.getFvitem().getName());
                    vo.setApplicationdata(a);
                    vo.setContent(contents);
                } catch (Exception ex) {
                    Logger.getLogger(FileUploadController.class.getName()).log(Level.SEVERE, null, ex);
                }
                vo.setContentType(a.getFvitem().getContenttype());
                vo.setFileName(a.getFvitem().getName());
                vo.setIndex(index++);
                attachmentVOList.add(vo);
            }
        }
    }    
    
    private byte[] getBytesFromInputStream(InputStream is, long length, String fileName) throws IOException {
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + fileName);
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }    
    
  
}