/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.doc;

import com.tcci.fc.controller.util.MessageUtils;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.repository.TcDocument;
import com.tcci.fc.entity.repository.TcDocumentMaster;
import com.tcci.fc.entity.repository.TcFolder;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.repository.RepositoryFacade;
import com.tcci.fc.facade.repository.TcDocumentFacade;
import com.tcci.fc.facade.repository.TcFolderFacade;
import com.tcci.fc.util.ConstantUtil;
import com.tcci.fc.util.FacesUtil;
import com.tcci.fc.util.SequenceGenerator;
import com.tcci.fc.util.SequenceGeneratorFactory;
import com.tcci.fc.vo.AttachmentVO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class EditDocumentController implements Serializable {

    Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    @ManagedProperty(value = "#{folderTreeController}")
    private FolderTreeController folderTreeController;

    public void setFolderTreeController(FolderTreeController folderTreeController) {
        this.folderTreeController = folderTreeController;
    }
    @EJB
    TcDomainFacade domainFacade;
    @EJB
    TcDocumentFacade documentFacade;
    @EJB
    TcFolderFacade folderFacade;
    @EJB
    ContentFacade contentFacade;
    @EJB
    TcUserFacade userFacade;
    @EJB
    RepositoryFacade repositoryFacade;
    private TcFolder selectedFolder;
    private List<AttachmentVO> attachmentVOList;
    private TcDocument document;
    private AttachmentVO selectedAttachmentVO;
    private StreamedContent downloadFile;
    private String oid;
    //private List<TcApplicationdata> applicationDataList;

    public TcFolder getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(TcFolder selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public TcDocument getDocument() {
        return document;
    }

    public void setDocument(TcDocument document) {
        this.document = document;
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
    /*
    public List<TcApplicationdata> getApplicationDataList() {
        return applicationDataList;
    }

    public void setApplicationDataList(List<TcApplicationdata> applicationDataList) {
        this.applicationDataList = applicationDataList;
    }
    */
    
    @PostConstruct
    public void init() {
        setSelectedFolder(folderTreeController.getSelectedFolder());

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        oid = params.get(ConstantUtil.OID);

        if (StringUtils.isEmpty(oid)) {
            document = new TcDocument();
        } else {
            try {
                document = (TcDocument) documentFacade.getObject(oid);
                List<TcApplicationdata> applicationDataList = contentFacade.getApplicationdata(document);
                initAttachments( applicationDataList  );
            } catch (Exception ex) {
                Logger.getLogger(ViewDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage());
            }
        }
    }

    private void initAttachments(List<TcApplicationdata> applicationDataList) throws Exception {
        attachmentVOList = new ArrayList<AttachmentVO>();
        int index = 0;
        if( applicationDataList != null ){
            for (TcApplicationdata a : applicationDataList) {
                AttachmentVO vo = new AttachmentVO();
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                InputStream is = (InputStream) contentFacade.findContentStream(a.getFvitem());
                byte[] contents = getBytesFromInputStream(is, a.getFvitem().getFilesize(), a.getFvitem().getName());
                vo.setApplicationdata(a);
                vo.setContent(contents);
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
    
    private TcUser getCreateor() {
        TcUser tcUser = null;
        String loginAccount = FacesUtil.getLoginId();
        if (loginAccount != null) {
            tcUser = userFacade.findUserByLoginAccount(loginAccount);
        }
        return tcUser;
    }

    public boolean isEditMode() {
        return (!StringUtils.isEmpty(oid));
    }

    public String editDocument() {
        TcDocumentMaster master = null;
        TcUser user = getCreateor();
        if (document != null && document.getId() == null) {
            //TODO
            /*
            Long folderID = Long.valueOf(5);
            selectedFolder = folderFacade.find(folderID);
             */
            Long domainID = Long.valueOf(1);
            TcDomain domain = domainFacade.find(domainID);
            //End TODO

            SequenceGeneratorFactory sgf = new SequenceGeneratorFactory(document);
            SequenceGenerator sg = sgf.createSequenceGenerator();
            master = new TcDocumentMaster();
            master.setName("NA");
            master.setDocnumber("NA");

            document.setCreator(user);
            document.setCreatetimestamp(new Date());
            document.setFolder(selectedFolder);
            document.setDomain(domain);
            document.setIterationnumber(sg.getStartValue());
            document.setVersionnumber(sg.getStartValue());
            document.setIslatestiteration(Boolean.TRUE);
            document.setIslatestversion(Boolean.TRUE);
            document.setIsremoved(Boolean.FALSE);
        } else {
            master = (TcDocumentMaster) document.getMaster();
            document.setModifier(user);
            document.setModifytimestamp(new Date());
        }
        try {
            documentFacade.editDocument(master, document, attachmentVOList);
            List<TcApplicationdata> applicationDataList = contentFacade.getApplicationdata(document);
            initAttachments(applicationDataList);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, "Successful to create/save document");
        } catch (Exception ex) {
            Logger.getLogger(EditDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
        return null;
    }

    public String cancel() {
        return null;
    }
    /*
    public String removeAttachment(TcApplicationdata application) {
        try {
            contentFacade.removeContentItem(application);
            applicationDataList = contentFacade.getApplicationdata(document);
            MessageUtils.addSuccessMessage("Successfully remove the attachement!");
        } catch (Exception e) {
            MessageUtils.addErrorMessage("Remove Attachement Failed!", e.getLocalizedMessage());
        }
        return null;
    }
    */

    public String removeAttachmentVO() {
        logger.info("removeAttachmentVO file=" + selectedAttachmentVO.getFileName());
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
        logger.info("removeAttachmentVO list.size=" + list.size());
        setAttachmentVOList(list);
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        logger.info("handleFileUpload : uploadedFile=" + uploadedFile.getFileName());
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
                logger.info("handleFileUpload : add a file:" + attachment.getFileName());
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }

    }

    public StreamedContent getDownloadFile() {
        //logger.info("getDownloadFile start!");
        try {
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            AttachmentVO attachmentVO = (AttachmentVO) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "row");
            //logger.info("getDownloadFile attachmentVO index=" + attachmentVO.getIndex() );
            //logger.info("getDownloadFile attachmentVO filename=" + attachmentVO.getFileName() );
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
            e.printStackTrace();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        //logger.info("getDownloadFile end!");
        return downloadFile;
    }

    public String removeDocument() {
        try {
            repositoryFacade.removeDocument(document, true);
        } catch (Exception e) {
            MessageUtils.addErrorMessage("Remove Document Failed!", e.getLocalizedMessage());
        }
        return null;
    }
}
