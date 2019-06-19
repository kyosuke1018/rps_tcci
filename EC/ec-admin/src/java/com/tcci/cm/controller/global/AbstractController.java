/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.controller.global;

import com.tcci.cm.util.JsfUtils;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter
 */
abstract public class AbstractController implements Serializable {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected String viewId;
    protected String globalMessage;
    
    public void showErrorMessage(){
        JsfUtils.addErrorMessage(globalMessage);
        globalMessage = "";// clear after show
    }
    
    public String getFuncTitle(){
        return "Show function title there ...";
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getGlobalMessage() {
        return globalMessage;
    }

    public void setGlobalMessage(String globalMessage) {
        this.globalMessage = globalMessage;
    }
    //</editor-fold>
    
}
