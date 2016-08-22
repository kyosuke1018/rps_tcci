package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.SkCustomer;
import com.tcci.sksp.entity.ar.SkAccountsReceivable;
import com.tcci.sksp.entity.ar.SkArRemitItem;
import com.tcci.sksp.entity.ar.SkArRemitMaster;
import com.tcci.sksp.entity.org.SkSalesMember;
import com.tcci.sksp.facade.SkAccountsReceivableFacade;
import com.tcci.sksp.facade.SkSalesMemberFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lynn.Huang
 */
@ManagedBean
@ViewScoped
public class SelectOrderNumberController {
    Logger logger = LoggerFactory.getLogger(SelectOrderNumberController.class);
    private SkSalesMember skSales;
    private SkCustomer skCustomer;
    private SkArRemitMaster skArRemitMaster;
    private SkAccountsReceivable[] selectedArList;
    private List<SkAccountsReceivable> allArList;
    private Map<Long,SkAccountsReceivable> allArMap;
    private ArDataModel arDataModel;
    private SkAccountsReceivable removedAR;
    
    @EJB
    private SkAccountsReceivableFacade skAccountsReceivableFacade;     
    @EJB
    private SkSalesMemberFacade skSalesMemberFacade;
    
    @PostConstruct
    public void init() {        
        logger.debug("init()");
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        skArRemitMaster = (SkArRemitMaster) flash.get("skArRemitMaster");
        if (skArRemitMaster != null) {
           skSales = skSalesMemberFacade.findByCode(skArRemitMaster.getSapid());
           skCustomer = skArRemitMaster.getCustomer();
           selectedArList = getSelectedArListByRemitMaster();
        } else {
            skSales = (SkSalesMember) flash.get("skSales");
            skCustomer = (SkCustomer) flash.get("skCustomer");  
            selectedArList = (SkAccountsReceivable[]) flash.get("selectedArList");
        }
        initAllAR();
        arDataModel = new ArDataModel(getArList());
    }

    public List<SkAccountsReceivable> getAllArList() {
        return allArList;
    }        
    
    public List<SkAccountsReceivable> getArList() {   
        if (!allArList.isEmpty() && selectedArList != null && selectedArList.length > 0) {
            List<SkAccountsReceivable> tempList = new ArrayList<SkAccountsReceivable>(allArList);
            Collections.copy(tempList, allArList);
            return filterSelectedArList(tempList);
        }
        return allArList;        
    }
    
    private void initAllAR() {
        allArList = new ArrayList<SkAccountsReceivable>();
        if (skCustomer != null) {
            allArList = skAccountsReceivableFacade.findARByCriteria(skCustomer);
        }
        initAllARMap();
    }
    
    private void initAllARMap() {
        allArMap = new HashMap<Long,SkAccountsReceivable>();
        for (SkAccountsReceivable ar : allArList) {
            allArMap.put(ar.getId(), ar);
        }        
    }             
    
    private List<SkAccountsReceivable> filterSelectedArList(List<SkAccountsReceivable> list) {
        for (SkAccountsReceivable ar : selectedArList) {
            SkAccountsReceivable tempAR = allArMap.get(ar.getId());
            if (tempAR != null) {
                list.remove(ar);
            }
        }
        return list;
    }            
    
    private SkAccountsReceivable[] getSelectedArListByRemitMaster() {
        List<SkAccountsReceivable> tempSelectedArList = new ArrayList<SkAccountsReceivable>();
        for (SkArRemitItem item : skArRemitMaster.getSkArRemitItemCollection()) {
            List<SkAccountsReceivable> arlist = skAccountsReceivableFacade.findARByCriteria(skCustomer, item.getInvoiceNumber());               
            if (arlist != null && arlist.size() > 0) {
                 // ar should be unique by query criteria : customer and invoiceNumber
                 tempSelectedArList.add(arlist.get(0)); 
            } 
        }
        return (SkAccountsReceivable[])tempSelectedArList.toArray(new SkAccountsReceivable[tempSelectedArList.size()]);        
    }    
    
    // setter & getter
    public SkCustomer getSkCustomer() {
        return skCustomer;
    }

    public void setSkCustomer(SkCustomer skCustomer) {
        this.skCustomer = skCustomer;
    }

    public SkAccountsReceivable[] getSelectedArList() {
        return selectedArList;
    }

    public void setSelectedArList(SkAccountsReceivable[] selectedArList) {
        this.selectedArList = selectedArList;
    }

    public void setSkSales(SkSalesMember skSales) {
        this.skSales = skSales;
    }
    
    public SkSalesMember getSkSales() {
        return skSales;
    }

    public ArDataModel getArDataModel() {                      
        if (removedAR != null) {                       
            List<SkAccountsReceivable> data = (List<SkAccountsReceivable>) arDataModel.getWrappedData();
            data.add(0, removedAR);
            removedAR = null;            
            arDataModel.setWrappedData(data);
        }        
        return arDataModel;
    }

    public void setArDataModel(ArDataModel arDataModel) {
        this.arDataModel = arDataModel;
    }

    public SkAccountsReceivable getRemovedAR() {
        return removedAR;
    }

    public void setRemovedAR(SkAccountsReceivable removedAR) {
        this.removedAR = removedAR;
    }
    
}
