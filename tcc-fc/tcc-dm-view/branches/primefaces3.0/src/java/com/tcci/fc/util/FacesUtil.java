/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.util;

import java.security.Principal;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Jason.Yu
 */
public class FacesUtil {
    private static Logger logger = Logger.getLogger(FacesUtil.class .getCanonicalName());
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
        FacesMessage facesMsg = new FacesMessage(severity, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static String getClientCountry(){
        return getClientLocale().getCountry();
    }
    
    public static Locale getClientLocale(){
        return FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }
    
    public static Object getManagedBean(String beanName){
        try{
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            Object object = (Object) FacesContext.getCurrentInstance().getApplication()
            .getELResolver().getValue(elContext, null, beanName);
             return object;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Object getValueExpression(String el, Class cls){
        //createValueExpression
        try{
            Application app = FacesContext.getCurrentInstance().getApplication();
            ExpressionFactory exprFactory = app.getExpressionFactory();
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            ValueExpression valExpr = exprFactory.createValueExpression(elContext, el, cls);
            return valExpr.getValue(elContext);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
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
