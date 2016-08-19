/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.controller;

import com.tcci.fcs.facade.FcCompanyFacade;
import com.tcci.fcs.model.reprot.ReportBaseVO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name="nonConsolidationController")
@ViewScoped
public class NonConsolidationController {
    private final static Logger logger = LoggerFactory.getLogger(NonConsolidationController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB
    private FcCompanyFacade fcCompanyFacade;
    //</editor-fold>
    
    private String group = "TCC";//defalut TCC:台泥
    private List<ReportBaseVO> nonConsolidationList;
    
    @PostConstruct
    private void init() {
        this.loadData();
    }
    
    protected void loadData() {
        nonConsolidationList = fcCompanyFacade.findNonConsolidation(group);
    }
    
    public void changeGroup() {
        logger.debug("changeGroup:" + this.group);
        this.loadData();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ReportBaseVO> getNonConsolidationList() {
        return nonConsolidationList;
    }
    //</editor-fold>
}
