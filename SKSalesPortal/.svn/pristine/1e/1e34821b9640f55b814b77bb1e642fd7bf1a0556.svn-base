/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.controller.quotation.EditQuotationController;
import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkCustomerFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jason.Yu
 */
@ManagedBean
@ViewScoped
public class SelectCustomerController {
    //private List<SkCustomer> customerList;

    protected final static Logger logger = LoggerFactory.getLogger(SelectCustomerController.class);
    private CustomerDataModel customerDataModel;
    //private String sales = "";
    private SkSalesMember salesMember;
    private String code;
    private String name;
    private String selectedCustomer;
    @EJB
    SkCustomerFacade customerFacade;
    @ManagedProperty(value = "#{selectConsigneeController}")
    private SelectConsigneeController selectConsigneeController;

    public void setSelectConsigneeController(SelectConsigneeController selectConsigneeController) {
        this.selectConsigneeController = selectConsigneeController;
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

    public String getSelectedCustomer() {
        logger.debug("selectedCustomer={}", selectedCustomer);
        return selectedCustomer;
    }

    public void setSelectedCustomer(String selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public CustomerDataModel getCustomerDataModel() {
        return customerDataModel;
    }

    public void setCustomerDataModel(CustomerDataModel customerDataModel) {
        this.customerDataModel = customerDataModel;
    }
    /*
     public String getSales() {
     return sales;
     }

     public void setSales(String sales) {
     this.sales = sales;
     }
     */

    public SkSalesMember getSalesMember() {
        return salesMember;
    }

    public void setSalesMember(SkSalesMember salesMember) {
        this.salesMember = salesMember;
    }

    @PostConstruct
    public void init() {
        code = "";
        name = "";
        setCustomerDataModel(new CustomerDataModel(new ArrayList<SkCustomer>()));
    }

    public void doCustomerSearch() {
        try {
            List<SkCustomer> customerList = null;
            /*
             if (!StringUtils.isEmpty(sales))  {
             customerList = customerFacade.findByConditions(code, sales);
             logger.debug("query by sales + code, customerList.size()={}",customerList.size());
             } else {
             customerList = customerFacade.findByCriteria(null, name, code);
             logger.debug("query by name + code, customerList.size()={}",customerList.size());
             }
             */
            logger.debug("getSalesMember()={}", getSalesMember());
            customerList = customerFacade.findByCriteria(getSalesMember(), code, name, null, null, null, null);
            customerDataModel = new CustomerDataModel(customerList);
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            logger.error(e.getMessage());
        }
    }

    public List<String> completeMethod(String code) {
        this.code = code;
        List<String> codes = new ArrayList<String>();
        List<SkCustomer> customers = customerFacade.findByCriteria(null, null, code);
        for (SkCustomer customer : customers) {
            codes.add(customer.getSimpleCode());
        }
        return codes;
    }

    public void onRowSelect(SelectEvent event) {
        SkCustomer customer = (SkCustomer) event.getObject();
        setSelectedCustomer(customer.getSimpleCode());
        setName(customer.getName());
        selectConsigneeController.setSelectedConsignee(customer.getSimpleCode());
        selectConsigneeController.setName(customer.getName());
    }

    public void handleSelect(SelectEvent event) {
        logger.debug("handleSelect(), selection={}", event.getObject().toString());
        String code = (String) event.getObject();
        SkCustomer customer = customerFacade.findBySimpleCode(code);
        setSelectedCustomer(code);
        setName(customer.getName());
        selectConsigneeController.setSelectedConsignee(code);
        selectConsigneeController.setName(customer.getName());
    }

    public String getCustomerName(String code) {
        SkCustomer customer = customerFacade.findBySimpleCode(code);
        return (null == customer) ? null : customer.getName();
    }
}
