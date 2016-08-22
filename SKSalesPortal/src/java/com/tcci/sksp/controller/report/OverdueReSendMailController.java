/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.report;

import com.tcci.sksp.controller.util.FacesUtil;
import com.tcci.sksp.controller.util.QueryCriteriaController;
import com.tcci.sksp.entity.org.SkSalesMember;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import com.tcci.sksp.facade.ZrtBsegAragFacade;
import javax.ejb.EJB;

/**
 *
 * @author carl.lin
 */
@ManagedBean
@ViewScoped
public class OverdueReSendMailController {

    @ManagedProperty(value = "#{queryCriteriaController}")
    QueryCriteriaController queryCriteriaController;
    @EJB
    private ZrtBsegAragFacade aragFacade;

    public void setQueryCriteriaController(QueryCriteriaController queryCriteriaController) {
        this.queryCriteriaController = queryCriteriaController;
    }

    @PostConstruct
    public void init() {
        setQueryCriteriaController(queryCriteriaController);
    }

    public String doReSendMailAction() {
        SkSalesMember sales = queryCriteriaController.getFilter().getSales();
        try {
            // 單一sales 發 mail 
            if (sales != null) {
                aragFacade.sendOverdueAccount(sales);
            } else {
                // send all sales
                aragFacade.sendOverdueAccount();
            }

        } catch (Exception e) {
            FacesUtil.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
        }
        return null;
    }
}
