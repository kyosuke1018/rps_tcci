/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.solr.controller.global;

import com.tcci.solr.server.model.global.GlobalConstant;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AP Scoped 資訊管理
 * @author Peter
 */
@ManagedBean(name = "applicationScopeManager")
@ApplicationScoped
public class ApplicationScopeManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
        
    //@Resource(mappedName = "jndi/ics.config")
    //private Properties jndiConfig;
         
    @PostConstruct
    public void init() {
        logger.info("init ...");
    }

    public void preRenderView() {
        logger.info("preRenderView ...");
        HttpSession session = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession( true );
        //tune session params, eg. session.setMaxInactiveInterval(..);
        //perform other pre-render stuff, like setting user context...
    }
    
    /**
     * 避免 UI 不過期頻率
     * @return 
     */
    public int getAlivePollInterval(){
        return GlobalConstant.ALIVE_POLL_INTERVAL;
    }
    
    /**
     * 顯示部分字串
     * @param ori
     * @param len
     * @return 
     */
    public String showPartialStr(String ori, int len){
        if( ori==null || ori.length()<=len ){
            return ori;
        }
        
        return ori.substring(0, len) + "...";
    }
    
    /**
     * getHostURL
     * @return http://<serverName>:<ServerPort>
     */
    public String getHostURL() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        String url = request.getContextPath();
        return url;
    }        

    /**
     * 取得Server IP Address
     * @return 
     */
    public String getServerIpAddress(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getLocalAddr();
    }
    
    /**
     * 取得系統時間
     * @return 
     */
    public String getSystime(){
        DateFormat df = new SimpleDateFormat(GlobalConstant.FORMAT_DATETIME);
        Date now = new Date();        
        return df.format(now);
    }
    
    /**
     * Get system default format string
     * @return 
     */
    public String getDateFormat(){
        return GlobalConstant.FORMAT_DATE;
    }
    public String getDateTimeFormat(){
        return GlobalConstant.FORMAT_DATETIME;
    }
    public String getIntFormat(){
        return GlobalConstant.FORMAT_INTEGER;
    }
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    //</editor-fold>
}
