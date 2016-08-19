/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.controller;

import com.tcci.fc.controller.BaseController;
import com.tcci.fcs.entity.ZtfiAfcsAcma;
import com.tcci.fcs.facade.ZtfiAfcsAcmaFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author kyle.cheng
 */
@ManagedBean(name="accountsController")
@ViewScoped
public class AccountsController extends BaseController {
    
    //<editor-fold defaultstate="collapsed" desc="Injects">
    @EJB
    private ZtfiAfcsAcmaFacade ztfiAfcsAcmaFacade;
    //</editor-fold>
    
    private String group = "A";//defalut A:台泥
    private List<ZtfiAfcsAcma> accMasterList;
    
    @PostConstruct
    @Override
    protected void init() {
        this.loadData();
    }
    
    @Override
    protected boolean loadData() {
        accMasterList = ztfiAfcsAcmaFacade.findAll(group);
        return super.loadData();
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

    public List<ZtfiAfcsAcma> getAccMasterList() {
        return accMasterList;
    }
    //</editor-fold>
}
