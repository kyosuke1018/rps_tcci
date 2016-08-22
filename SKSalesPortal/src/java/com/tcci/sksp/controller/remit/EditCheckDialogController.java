package com.tcci.sksp.controller.remit;

import com.tcci.sksp.entity.SkBank;
import com.tcci.sksp.entity.ar.SkCheckMaster;
import com.tcci.sksp.facade.SkBankFacade;
import com.tcci.sksp.facade.SkCheckMasterFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author nEO.Fu
 */
@ManagedBean
@ViewScoped
public class EditCheckDialogController {

    //<editor-fold defaultstate="collapsed" desc="parameters">
    protected final static Logger logger = LoggerFactory.getLogger(EditCheckDialogController.class);
    private ResourceBundle rb = ResourceBundle.getBundle("messages");
    private SkCheckMaster check = new SkCheckMaster();
    List<String> errorMessages = new ArrayList<String>();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @EJB
    SkCheckMasterFacade checkFacade;
    @EJB
    SkBankFacade bankFacade;
    @ManagedProperty(value = "#{advancePaymentController}")
    private AdvancePaymentController advancePaymentController;

    public void setAdvancePaymentController(AdvancePaymentController advancePaymentController) {
        this.advancePaymentController = advancePaymentController;
    }
    @ManagedProperty(value = "#{checkMasterMaintenanceController}")
    CheckMasterMaintenanceController checkMasterMaintenanceController;

    public void setCheckMasterMaintenanceController(CheckMasterMaintenanceController checkMasterMaintenanceController) {
        this.checkMasterMaintenanceController = checkMasterMaintenanceController;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">

    public SkCheckMaster getCheck() {
        return check;
    }

    public void setCheck(SkCheckMaster check) {
        this.check = check;
    }
    //</editor-fold>

    /**
     * 
     * @param check null mean create.
     */
    public void edit(SkCheckMaster check) {
        logger.debug("edit(), check={}", check);
        if (check == null) {
            logger.debug("create");
            this.check = new SkCheckMaster();
            checkMasterMaintenanceController.setBankCode("");
            checkMasterMaintenanceController.setBankName("");
        } else {
            logger.debug("edit");
            this.check = check;
            checkMasterMaintenanceController.setBankCode(check.getBillingBank().getCode());
            checkMasterMaintenanceController.setBankName(check.getBillingBank().getName());
        }
    }
    
    public void goEdit(String checkNumber) {
        SkCheckMaster check = null;
        if (!StringUtils.isEmpty(checkNumber)) {
            check = checkFacade.findByCheckNumber(checkNumber);
        }
        edit(check);
    }

    public void returnFromDialog() {
        String bankCode = checkMasterMaintenanceController.getBankCode();
        if (!StringUtils.isEmpty(bankCode)) {
            SkBank bank = (SkBank) bankFacade.findByCode(bankCode);
            if (bank != null) {
                check.setBillingBank(bank);
            }
        }

        if (checkMasterMaintenanceController.valid(check)) {
            advancePaymentController.getChecks().put(System.identityHashCode(check), check);
            logger.debug("checks.size()={}", advancePaymentController.getChecks().size());
        } else {
            String errormsg = "";
            for (String errorMessage : checkMasterMaintenanceController.getErrorMessages()) {
                errormsg += errorMessage;
                errormsg += "\n";
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("errormsg", errormsg);
        }
    }

    public String getTitle() {
        if (check == null || StringUtils.isEmpty(check.getCheckNumber())) {
            return rb.getString("advancepayment.label.createcheck");
        } else {
            return rb.getString("advancepayment.label.editcheck");
        }
    }
}
