package com.tcci.fc.controller.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Lynn.Huang
 */
public class MessageUtils {

    public static void addInfoMessage(String summary, String detail, String clientId) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }

    public static void addErrorMessage(String summary, String detail, String clientId) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }
    
    public static void addWarnMessage(String summary, String detail, String clientId) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
    }
    
    public static void addInfoMessage(String summary, String detail) {
        addInfoMessage(summary, detail, null);
    }
    
    public static void addErrorMessage(String summary, String detail) {
        addErrorMessage(summary, detail, null);
    }
    
    public static void addWarnMessage(String summary, String detail) {
        addWarnMessage(summary, detail, null);
    }
    
    public static void addInfoMessage(String summary) {
        addInfoMessage(summary, "");
    }
    
    public static void addErrorMessage(String summary) {
        addErrorMessage(summary, "");
    }
    
    public static void addSuccessMessage(String msg) {
        addSuccessMessage(msg, "");
    }
    
    public static void addSuccessMessage(String msg, String detailMsg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, detailMsg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);        
    }    
}
