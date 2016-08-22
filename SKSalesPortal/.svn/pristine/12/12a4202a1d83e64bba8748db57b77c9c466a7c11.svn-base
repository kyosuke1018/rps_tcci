/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.controller.remit.CheckMasterMaintenanceController;
import com.tcci.sksp.entity.SkBank;
import com.tcci.sksp.facade.SkBankFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class SelectBankController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @EJB
    SkBankFacade bankFacade;
    @ManagedProperty(value = "#{checkMasterMaintenanceController}")
    CheckMasterMaintenanceController checkMasterMaintenanceController;

    public void setCheckMasterMaintenanceController(CheckMasterMaintenanceController checkMasterMaintenanceController) {
        this.checkMasterMaintenanceController = checkMasterMaintenanceController;
    }
    //private List<SkBank> bankList;
    private BankDataModel bankDataModel;
    private String code;
    private String name;

    public BankDataModel getBankDataModel() {
        return bankDataModel;
    }

    public void setBankDataModel(BankDataModel bankDataModel) {
        this.bankDataModel = bankDataModel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void init() {
        name = "";
        code = "";
        bankDataModel = new BankDataModel(new ArrayList<SkBank>());
    }

    public String doBankSearch() {
        try {
            List<SkBank> bankList = bankFacade.findByCriteria(code, name);
            bankDataModel = new BankDataModel(bankList);
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            logger.error("e={}", e);
        }
        return null;
    }

    public void onRowSelect(SelectEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        SkBank bank = (SkBank) event.getObject();
        checkMasterMaintenanceController.setBankCode(bank.getCode());
        context.addCallbackParam("bankCode", bank.getCode());
        context.addCallbackParam("bankName",bank.getName());
        context.addCallbackParam("bankId",bank.getId());
        
    }
}
