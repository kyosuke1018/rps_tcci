/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.master;

import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryPlaceFacade;
import com.tcci.tccstore.facade.deliveryplace.QueryFilter;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class DeliveryPlaceQuery {

    private QueryFilter queryFilter = new QueryFilter();
    private List<EcDeliveryPlace> result;
    private String salesareaCode;
    private List<String> provinces;

    @EJB
    private EcDeliveryPlaceFacade ejb;
    @EJB
    private EcSalesareaFacade ecSalesareaFacade;

    private List<EcSalesarea> ecSalesareas;

    @PostConstruct
    private void init() {
        ecSalesareas = ecSalesareaFacade.findAll();
        provinces = ejb.findProvinces();
    }

    // action
    public void query() {
        if (null == salesareaCode) {
            queryFilter.setEcSalesarea(null);
        } else {
            for (EcSalesarea ecSalesarea : ecSalesareas) {
                if (salesareaCode.equals(ecSalesarea.getCode())) {
                    queryFilter.setEcSalesarea(ecSalesarea);
                    break;
                }
            }
        }
        result = ejb.query(queryFilter);
    }
    
    public List<EcSalesarea> completeSalesarea(String input) {
        List<EcSalesarea> list = new ArrayList<>();
        for (EcSalesarea entity : ecSalesareas) {
            if (entity.getCode().contains(input) || entity.getName().contains(input)) {
                list.add(entity);
            }
        }
        return list;
    }

    // getter, setter
    public QueryFilter getQueryFilter() {
        return queryFilter;
    }

    public void setQueryFilter(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
    }

    public List<EcDeliveryPlace> getResult() {
        return result;
    }

    public void setResult(List<EcDeliveryPlace> result) {
        this.result = result;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

    public List<String> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<String> provinces) {
        this.provinces = provinces;
    }

}
