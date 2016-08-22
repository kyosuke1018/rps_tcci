/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcCustomerSales;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.sales.EcSalesFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class CustomerSalesImport extends ImportExcelBase<CustomerSalesVO> {

    @EJB
    private EcCustomerFacade ecCustomerFacade;
    @EJB
    private EcSalesFacade ecSalesFacade;
    
    private Set<String> setPK;
    private Map<String, EcCustomer> mapCustomer;
    private Map<String, EcSales> mapSales;
    
    public CustomerSalesImport() {
        super(CustomerSalesVO.class);
        setPK = new HashSet<>();
        mapCustomer = new HashMap<>();
        mapSales = new HashMap<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(CustomerSalesVO vo) {
        String pk = vo.getCustomer() + ":" + vo.getSales();
        if (!setPK.add(pk)) {
            vo.setMessage("data duplicated!");
            return false;
        }
        EcCustomer ecCustomer = findCustomer(vo.getCustomer());
        if (null == ecCustomer) {
            vo.setMessage(vo.getCustomer() + " customer not found!");
            return false;
        }
        vo.setEcCustomer(ecCustomer);
        if (vo.getSales() != null) {
            EcSales ecSales = findSales(vo.getSales());
            if (null == ecSales) {
                vo.setMessage(vo.getSales() + " sales not found!");
                return false;
            }
            vo.setEcSales(ecSales);
            if (!ecCustomerFacade.isCustomerSalesExist(ecCustomer, ecSales)) {
                vo.setStatus(ExcelVOBase.Status.ST_INSERT);
            }
        } else if (vo.getSalesList() != null) {
            String[] codeArray = StringUtils.split(vo.getSalesList(), ",; \t\r\n");
            List<EcSales> list = new ArrayList<>();
            for (String code : codeArray) {
                EcSales ecSales = findSales(code);
                if (null == ecSales) {
                    vo.setMessage(code + " sales not found!");
                    return false;
                }
                list.add(ecSales);
            }
            vo.setEcSalesList(list);
            List<EcCustomerSales> origList = ecCustomerFacade.findCustomerSales(ecCustomer);
            boolean salesChanged = list.size() != origList.size();
            if (!salesChanged) {
                for (EcCustomerSales cs : origList) {
                    if (!list.contains(cs.getEcSales())) {
                        salesChanged = true;
                        break;
                    }
                }
            }
            if (salesChanged) {
                vo.setStatus(ExcelVOBase.Status.ST_UPDATE);
            }
        } else {
            vo.setStatus(ExcelVOBase.Status.ST_UPDATE); // clean customer sales
        }
        return true;
    }

    @Override
    protected boolean insert(CustomerSalesVO vo) {
        try {
            ecCustomerFacade.insertCustomerSales(vo.getEcCustomer(), vo.getEcSales());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    protected boolean update(CustomerSalesVO vo) {
        try {
            ecCustomerFacade.updateCustomerSales(vo.getEcCustomer(), vo.getEcSalesList());
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    private EcCustomer findCustomer(String customer) {
        if (mapCustomer.containsKey(customer)) {
            return mapCustomer.get(customer);
        }
        EcCustomer ecCustomer = ecCustomerFacade.findByCode(customer);
        mapCustomer.put(customer, ecCustomer);
        return ecCustomer;
    }

    private EcSales findSales(String sales) {
        if (mapSales.containsKey(sales)) {
            return mapSales.get(sales);
        }
        EcSales ecSales = ecSalesFacade.findByCode(sales);
        mapSales.put(sales, ecSales);
        return ecSales;
    }
    
}
