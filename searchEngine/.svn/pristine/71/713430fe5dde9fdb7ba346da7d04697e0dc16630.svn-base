/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.solr.controller.global;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * @author Peter
 */
@ManagedBean(name = "indexController")
@ViewScoped
public class IndexController extends SessionAwareController implements Serializable {
    private Date entryPageTime;
    
    @PostConstruct
    public void init(){
        logger.info("init ...");
        entryPageTime = new Date();
        
        // *.xhtml 有 SSO 轉址保護, 才能確保首頁存取 session 不會出現 [pwc3999: Cannot create a session after the response has been committed] 錯誤
        String loginAccount = (sessionController!=null)? sessionController.getLoginAccount():"null";
        logger.info(loginAccount+"登入 Solr Admmin Console，現在時間："+ entryPageTime);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public Date getEntryPageTime() {
        return entryPageTime;
    }

    public void setEntryPageTime(Date entryPageTime) {
        this.entryPageTime = entryPageTime;
    }
    //</editor-fold>

}
