/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.solr.controller.global;

import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Peter.pan
 */
abstract public class SessionAwareController extends AbstractController {    
    @ManagedProperty(value="#{tcSessionController}")
    protected TcSessionController sessionController;
    
    public TcSessionController getSessionController() {
        return sessionController;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">    
    public void setSessionController(TcSessionController sessionController) {
        this.sessionController = sessionController;
    }
    //</editor-fold>
}
