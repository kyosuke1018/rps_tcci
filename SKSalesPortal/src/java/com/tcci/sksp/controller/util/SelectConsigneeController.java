package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkCustomerFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neo.Fu
 */
@ManagedBean
@ViewScoped
public class SelectConsigneeController {
    //private List<SkCustomer> customerList;

    protected final static Logger logger = LoggerFactory.getLogger(SelectConsigneeController.class);
    private CustomerDataModel customerDataModel;
    //private String sales = "";
    private String code;
    private String name;
    private String selectedConsignee;
    @EJB
    SkCustomerFacade customerFacade;

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

    public String getSelectedConsignee() {
        return selectedConsignee;
    }

    public void setSelectedConsignee(String selectedConsignee) {
        this.selectedConsignee = selectedConsignee;
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
            customerList = customerFacade.findConsigneeByCriteria(code, name);
            logger.debug("customerList.size()={}", customerList.size());
            customerDataModel = new CustomerDataModel(customerList);
        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
            logger.error(e.getMessage());
        }
    }

    public List<String> completeMethod(String code) {
        this.code = code;
        List<String> codes = new ArrayList<String>();
        List<SkCustomer> customers = customerFacade.findConsigneeByCriteria(code, null);
        for (SkCustomer customer : customers) {
            codes.add(customer.getSimpleCode());
        }
        return codes;
    }

    public void onRowSelect(SelectEvent event) {
        SkCustomer customer = (SkCustomer) event.getObject();
        setName(customer.getName());
        setSelectedConsignee(customer.getSimpleCode());
    }

    public void handleSelect(SelectEvent event) {
        logger.debug("handleSelect(), selection={}", event.getObject().toString());
        String code = (String) event.getObject();
        setName(customerFacade.findBySimpleCode(code).getName());
        setSelectedConsignee(code);
    }

    public String getConsigneeName(String code) {
        SkCustomer customer = customerFacade.findBySimpleCode(code);
        return (null == customer) ? null : customer.getName();
    }
}
