/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.rfq;

import com.tcci.cm.controller.global.SessionAwareController;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "rfqCompare")
@ViewScoped
public class RfqCompareController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 46;
    public static final String DATATABLE_RESULT = "fmMain:dtResult";
    
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    } 
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    //</editor-fold>
}
