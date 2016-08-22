package com.tcci.sksp.controller.remit;

import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.controller.util.SelectCustomerController;
import com.tcci.sksp.facade.SkCustomerFacade;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
public class RemitMaintenanceStepOneController {
    private String from;
    
    @EJB
    SkCustomerFacade customerFacade;
    @ManagedProperty(value = "#{queryCriteriaController}")
    private QueryCriteriaController queryCriteriaController;
    
    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        from = (String)flash.get("from");
    }

    public String gotoStep2() {
        /*
         * String customerCode = "";
        if (selectCustomerController.getSelectedCustomer() != null) {
        customerCode = selectCustomerController.getSelectedCustomer();
        }
        if (!StringUtils.isEmpty(customerCode)) {
        SkCustomer customer = customerFacade.findBySimpleCode(customerCode);
        if (customer == null) {
        customer = new SkCustomer();
        customer.setName(rb.getString("customer.msg.notexists"));
        }
        if (customer.getId() != null) {
        queryCriteriaController.getFilter().setSkCustomer(customer);
        } else {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("customer.msg.notexists"), rb.getString("customer.msg.notexists"));
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null;
        }
        selectCustomerController.setName(customer.getName());
        } else {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, rb.getString("customer.msg.required"), rb.getString("customer.msg.required"));
        FacesContext.getCurrentInstance().addMessage(null, message);
        return null;
        }
         * 
         */
        boolean isCustomerRequire = true;
        boolean isOnlySalesCode = true;
        queryCriteriaController.latestStepToCheckCustomerCode(isCustomerRequire, isOnlySalesCode);
        if (isOnlySalesCode && queryCriteriaController.isInvalidCustomerRelation()) {
            return null;
        } else if (!queryCriteriaController.isWrongCustomerCode()
                && !queryCriteriaController.isCustomerNotExists()) {
            Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
            flash.put("skSales", queryCriteriaController.getFilter().getSales());
            flash.put("skCustomer", queryCriteriaController.getFilter().getSkCustomer());
            return "remit_maintenance_step2";
        }
        return null;
    }

    // setter & getter
    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
}
