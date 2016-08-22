/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import java.security.Principal;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Jason.Yu
 */
public class FacesUtil {

    public static String getLoginId() {
        String name = null;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        Principal p = request.getUserPrincipal();
        if (p != null) {
            name = p.getName();
        }
        return name;
    }
    
    public static void addFacesMessage(Severity severity,String msg) {
        addFacesMessage(null, severity, msg);
    }
    
    public static void addFacesMessage(String compomentId, Severity severity, String msg) {
        FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        FacesContext.getCurrentInstance().addMessage(compomentId, facesMsg);        
    }
    
    public static void addFacesDetailMessage(Severity severity, String msg, String detailMsg) {
        addFacesDetailMessage(null, severity, msg, detailMsg);
    }    
    
    public static void addFacesDetailMessage(String compomentId, Severity severity, String msg, String detailMsg) {
        FacesMessage facesMsg = new FacesMessage(severity, msg, detailMsg);
        FacesContext.getCurrentInstance().addMessage(compomentId, facesMsg);
    }       
    
    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }
    
    public static void setRequestAttribute(String key, String value) {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.setAttribute(key, value);        
    }
    
    public static String getRequestAttribute(String key) {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return (String)request.getAttribute(key);
    }
    
    public static ResourceBundle getResourceBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        return context.getApplication().getResourceBundle(context, "msg");
    }
    
    public static String getMessage(String rcKey) {
        try {
            return getResourceBundle().getString(rcKey);
        }catch(MissingResourceException e) {
            return "invalid rc key: " + rcKey;
        }
    }    
}
