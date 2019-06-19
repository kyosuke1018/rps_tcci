/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.cm.controller.global;

import com.tcci.cm.util.ExceptionHandlerUtils;
import com.tcci.cm.util.JsfUtils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
@ManagedBean(name = "exceptionController")
@ViewScoped
public class ExceptionController implements Serializable {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String title;
    private String subject;
    private String description;
    private String detail;
    private String redirectUrl;
    
    @PostConstruct
    public void init() {
        try{
            logger.debug("ExceptionController init ...");
            // 系統異常提醒
            subject = JsfUtils.getResourceTxt("alert.exception.title");
            // 請先檢查輸入網址是否正確；若無誤請Email提供您輸入的網址，與目前畫面給系統管理人員，以利查詢問題原因，謝謝。
            description = JsfUtils.getResourceTxt("alert.exception.subject");
            detail = "unknow exception !";
                    
            final ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
            // extContext.getSession(true);// PWC3999: Cannot create a session after the response has been committed
            
            Throwable ex = null;
            String exceptionDetail = null;
            String oriUrl = null;
            
            if( extContext!=null ){
                if( extContext.getRequestMap()!=null ){
                    ex = (Throwable)(extContext.getRequestMap().get("javax.servlet.error.exception"));
                    if( ex!=null ){
                        exceptionDetail = ExceptionHandlerUtils.getStackTrace(ex);
                        logger.debug("exceptionDetail = "+exceptionDetail);
                    }
                    oriUrl = (String)extContext.getRequestMap().get("javax.servlet.error.request_uri");
                }
            }
            
            title = JsfUtils.getResourceTxt("alert.header.title"); //.getResourceBundle().getString("alert.header.title");
            
            if(ex!=null && ex.toString().indexOf("javax.faces.application.ViewExpiredException")>=0 ){ // javax.faces.el.EvaluationException
                subject = JsfUtils.getResourceTxt("alert.view.expired");
                description = JsfUtils.getResourceTxt("alert.expired.reson");
            }else{
                detail = (ex==null || exceptionDetail==null)? detail: ex.toString() + ":" + exceptionDetail;
            }
            
            if( extContext!=null ){
                // redirectUrl = (oriUrl==null)? extContext.getRequestContextPath():oriUrl;
                redirectUrl = extContext.getRequestContextPath();
            }else{
                logger.error("extContext is null !");
            }
        }catch(Exception e){
            logger.error("ExceptionController init exception :\n", e);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
    //</editor-fold>
}
