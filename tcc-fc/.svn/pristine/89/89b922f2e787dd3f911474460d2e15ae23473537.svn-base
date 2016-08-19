/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.doc;

import com.tcci.fc.controller.attachment.AttachmentController;
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
import com.tcci.fc.facade.vc.VersionControlFacade;
import com.tcci.fc.util.ConstantUtil;
import com.tcci.fc.util.FacesUtil;
import com.tcci.fc.util.SequenceGenerator;
import com.tcci.fc.util.SequenceGeneratorFactory;
import com.tcci.fc.vo.AttachmentVO;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class EditDocumentController implements Serializable {
    Logger logger = LoggerFactory.getLogger(EditDocumentController.class);
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
    @EJB
    VersionControlFacade versionControlFacade;

    @ManagedProperty(value = "#{attachmentController}")
    private AttachmentController attachmentController;

    public void setAttachmentController(AttachmentController attachmentController) {
        this.attachmentController = attachmentController;
    }

    private TcFolder selectedFolder;
    private TcDocument document;
    private AttachmentVO selectedAttachmentVO;
    private StreamedContent downloadFile;
    private String oid;
    private ResourceBundle messages = ResourceBundle.getBundle("messages");
    private String action;
    private String updateVersions;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUpdateVersions() {
        return updateVersions;
    }

    public void setUpdateVersions(String updateVersions) {
        this.updateVersions = updateVersions;
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
            action = messages.getString("document_create");
        } else {
            action = messages.getString("document_update");
            updateVersions = "0";
            try {
                document = (TcDocument) documentFacade.getObject(oid);
            } catch (Exception ex) {
                logger.error("ex={}", ex);
                FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage());
            }
        }
        attachmentController.init(document, false);
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
        try {
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
                document.setIterationnumber(sg.getStartValue());
                document.setVersionnumber(sg.getStartValue());
                document.setIslatestiteration(Boolean.TRUE);
                document.setIslatestversion(Boolean.TRUE);
                document.setIsremoved(Boolean.FALSE);
                documentFacade.editDocument(master, document, attachmentController.getAttachmentVOList());
            } else {
                master = (TcDocumentMaster) document.getMaster();
                if (updateVersions.equals("0")) {
                    document.setModifier(user);
                    document.setModifytimestamp(new Date());
                    documentFacade.editDocument(master, document, attachmentController.getAttachmentVOList());
                } else if (updateVersions.equals("1")) {
                    //update minor version
                    document = (TcDocument) versionControlFacade.newIteration(document);
                    documentFacade.createDocument(master, document, attachmentController.getAttachmentVOList());
                } else if (updateVersions.equals("2")) {
                    //update major version
                    document = (TcDocument) versionControlFacade.newVersion(document);
                    documentFacade.createDocument(master, document, attachmentController.getAttachmentVOList());
                }
            }
            List<TcApplicationdata> applicationDataList = contentFacade.getApplicationdata(document);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, "Successful to create/save document");
        } catch (Exception ex) {
            logger.error("ex={}",ex);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
        }
        return "listDocument.xhtml";
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

    public String removeDocument() {
        try {
            repositoryFacade.removeDocument(document, true);
        } catch (Exception e) {
            MessageUtils.addErrorMessage("Remove Document Failed!", e.getLocalizedMessage());
        }
        return null;
    }
}
