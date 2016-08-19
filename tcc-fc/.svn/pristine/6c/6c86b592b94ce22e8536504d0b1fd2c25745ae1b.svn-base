/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.test;

import com.tcci.fc.entity.essential.TcDomain;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.TcDomainFacade;
import com.tcci.fcservice.controller.util.FileDownloadController;
import com.tcci.fcservice.controller.util.FileUploadController;
import com.tcci.fcservice.controller.util.JsfUtil;
import com.tcci.fcservice.facade.FileuploadTestFacade;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jimmy
 */
@ManagedBean(name = "fileuploadtest")
@ViewScoped
public class FileUploadTestController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadTestController.class);
    @ManagedProperty(value = "#{fileUploadController}")
    private FileUploadController fileUploadController;
    @ManagedProperty(value = "#{fileDownloadController}")
    private FileDownloadController fileDownloadController;
    @EJB
    private TcDomainFacade domainFacade;
    @EJB
    private ContentFacade contentFacade;
    @EJB
    private FileuploadTestFacade fileuploadtestFacade;
    private FileuploadTest current;

    @PostConstruct
    public void init(){
        current = new  FileuploadTest();
    }
    
    public void upload() {
        TcDomain domain = domainFacade.getDefaultDomain();  
        try {
            fileuploadtestFacade.create(current);
            contentFacade.saveContent(domain, current, fileUploadController.getAttachmentVOList());
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getMessage());
        }
    }

    public FileDownloadController getFileDownloadController() {
        return fileDownloadController;
    }

    public void setFileDownloadController(FileDownloadController fileDownloadController) {
        this.fileDownloadController = fileDownloadController;
    }

    public FileUploadController getFileUploadController() {
        return fileUploadController;
    }

    public void setFileUploadController(FileUploadController fileUploadController) {
        this.fileUploadController = fileUploadController;
    }

    public FileuploadTest getCurrent() {
        return current;
    }

    public void setCurrent(FileuploadTest current) {
        this.current = current;
    }
}
