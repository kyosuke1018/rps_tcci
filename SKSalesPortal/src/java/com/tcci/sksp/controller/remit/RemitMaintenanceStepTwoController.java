package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.SelectOrderNumberController;
import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class RemitMaintenanceStepTwoController {
    private String from;
    
    @ManagedProperty(value = "#{selectOrderNumberController}")
    private SelectOrderNumberController selectOrderNumberController;
    
    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        from = (String)flash.get("from");
    }    

    public String gotoStep3() {
        SkAccountsReceivable[] selectedArList = selectOrderNumberController.getSelectedArList();
        if (selectedArList.length == 0) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, FacesUtil.getMessage("remitmaintenance.msg.at.least.one.ordernumber"));
            return null;            
        }
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("skSales", selectOrderNumberController.getSkSales());
        flash.put("skCustomer", selectOrderNumberController.getSkCustomer());
        flash.put("selectedArList", selectedArList);
        return "remit_maintenance_step3";
    }

    // setter & getter
    public void setSelectOrderNumberController(SelectOrderNumberController selectOrderNumberController) {
        this.selectOrderNumberController = selectOrderNumberController;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }        
    
}
