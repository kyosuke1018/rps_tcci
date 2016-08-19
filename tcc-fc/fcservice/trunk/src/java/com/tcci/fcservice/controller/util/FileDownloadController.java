/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcservice.controller.util;

import com.tcci.fc.entity.content.ContentHolder;
import com.tcci.fc.entity.content.TcApplicationdata;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.facade.essential.EssentialFacade;
import com.tcci.fcservice.controller.util.JsfUtil;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author neo
 */
@ManagedBean
@ViewScoped
public class FileDownloadController {

    @EJB
    ContentFacade contentFacade;
    @EJB
    EssentialFacade essentialFacade;
    private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
    private List<TcApplicationdata> applicationdataList;
    private ContentHolder current;
    
    public List<TcApplicationdata> getApplicationDataList() {
        System.out.println("current="+current);
        if (getApplicationDataList(current) != null) {
            System.out.println("size="+getApplicationDataList(current).size());
        }
        return getApplicationDataList(current);
    }

    public List<TcApplicationdata> getApplicationDataList(ContentHolder contentHolder) {
        try {
            if (contentHolder != null) {
                return contentFacade.getApplicationdata(contentHolder);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public StreamedContent getDownloadFile() {
        logger.info("getDownloadFile start!");
        System.out.println("DownloadFile start!");
        StreamedContent downloadFile = null;
        try {
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            TcApplicationdata applicationdata = (TcApplicationdata) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "row");
            InputStream is = (InputStream) contentFacade.findContentStream(applicationdata.getFvitem());
            String fileName = applicationdata.getFvitem().getName();            
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            downloadFile = new DefaultStreamedContent(is, applicationdata.getFvitem().getContenttype(), encodeFileName);
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e.getMessage());
        }
        System.out.println("DownloadFile end!");
        logger.info("getDownloadFile end!");
        return downloadFile;
    }

    /**
     * @return the current
     */
    public ContentHolder getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(ContentHolder current) {
        this.current = current;
    }
}
