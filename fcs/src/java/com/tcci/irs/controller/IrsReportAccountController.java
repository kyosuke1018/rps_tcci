/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.controller;

import com.tcci.fcs.facade.ZtfiAfcsAcmaFacade;
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
@ManagedBean(name="irsReportAccountController")
@ViewScoped
public class IrsReportAccountController {
    private final static Logger logger = LoggerFactory.getLogger(IrsReportAccountController.class);
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB
    private ZtfiAfcsAcmaFacade ztfiAfcsAcmaFacade;
    //</editor-fold>
    
    private String group = "TCC";//defalut TCC:台泥
    private List<ReportBaseVO> irsReportAccountList;
    
    @PostConstruct
    private void init() {
        this.loadData();
    }
    
    protected void loadData() {
        irsReportAccountList = ztfiAfcsAcmaFacade.findIrsReportAccount(group);
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

    public List<ReportBaseVO> getIrsReportAccountList() {
        return irsReportAccountList;
    }
    //</editor-fold>
}
