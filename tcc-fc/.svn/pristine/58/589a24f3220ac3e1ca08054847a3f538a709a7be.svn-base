/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.doc;

import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.entity.repository.TcDocument;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.repository.TcDocumentFacade;
import com.tcci.fc.util.ConstantUtil;
import com.tcci.fc.util.FacesUtil;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class ViewDocumentController implements Serializable  {
    Logger logger = Logger.getLogger( this.getClass().getCanonicalName() );
    @EJB
    TcDocumentFacade documentFacade;
    @EJB
    ContentFacade contentFacade;
    
    private TcDocument document;
    private StreamedContent downloadFile;
    private List<TcApplicationdata> applicationDataList;
    private String oid;
    public TcDocument getDocument() {
        return document;
    }

    public void setDocument(TcDocument document) {
        this.document = document;
    }

    public StreamedContent getDownloadFile() {
        logger.info("getDownloadFile start!");
        try {
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            TcApplicationdata applicationdata = (TcApplicationdata) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "row");
            InputStream is = (InputStream) contentFacade.findContentStream(applicationdata.getFvitem());
            String fileName =  applicationdata.getFvitem().getFilename();
            String encodeFileName = URLEncoder.encode(fileName,"UTF-8");
            downloadFile = new DefaultStreamedContent(is, applicationdata.getFvitem().getContenttype(), encodeFileName);
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage());
        }
        logger.info("getDownloadFile end!");
        return downloadFile;
    }

    public List<TcApplicationdata> getApplicationDataList() {
        return applicationDataList;
    }

    public void setApplicationDataList(List<TcApplicationdata> applicationDataList) {
        this.applicationDataList = applicationDataList;
    }
    
    
    @PostConstruct
    public void init() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        logger.info("ViewDocumentController init keset=" + params.keySet() );
	oid = params.get(ConstantUtil.OID);
        logger.info("ViewDocumentController init oid=" + oid );
        try {
            document = (TcDocument)documentFacade.getObject(oid);
            logger.info("ViewDocumentController init document=" + document );
            applicationDataList = contentFacade.getApplicationdata(document);
            logger.info("ViewDocumentController init applicationDataList=" + applicationDataList.size() );
        } catch (Exception ex) {
            Logger.getLogger(ViewDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage());
        }
    }
}
