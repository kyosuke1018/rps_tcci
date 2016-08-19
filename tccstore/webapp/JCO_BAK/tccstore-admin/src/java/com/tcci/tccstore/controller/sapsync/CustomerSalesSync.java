/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.sapsync;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.tccstore.controller.imports.CustomerSalesVO;
import com.tcci.tccstore.controller.imports.CustomerVO;
import com.tcci.tccstore.controller.imports.SalesVO;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.entity.EcCustomerSales;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.entity.datawarehouse.Kna1VO;
import com.tcci.tccstore.entity.datawarehouse.PernrVO;
import com.tcci.tccstore.entity.datawarehouse.ZperCnVO;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import com.tcci.tccstore.facade.datawarehouse.ZperCnFacade;
import com.tcci.tccstore.facade.sales.EcSalesFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class CustomerSalesSync {

    private List<CustomerVO> customerVOs;
    private List<SalesVO> salesVOs;
    private List<CustomerSalesVO> customerSalesVOs;

    @EJB
    transient private EcCustomerFacade ecCustomerFacade;
    @EJB
    transient private EcSalesFacade ecSalesFacade;
    @EJB
    transient private ZperCnFacade zperCnFacade;

    private Set<String> setCustomerPK;
    private Map<String, EcCustomer> mapEcCustomer;
    private Map<String, Kna1VO> mapKna1VO;

    private Set<String> setSalesPK;
    private Map<String, EcSales> mapEcSales;
    private Map<String, PernrVO> mapPernrVO;

    private Set<String> setCustomerSalesPK;
    private Map<String, EcCustomerSales> mapEcCustomerSales;
    private Map<String, ZperCnVO> mapZperCnVO;

    @PostConstruct
    private void init() {
    }

    public void sync() {
        customerLoad();
        customerCompare();

        salesLoad();
        salesCompare();
        
        customerSalesLoad();
        customerSalesCompare();
    }

    public void save() {
        boolean error = false;
        StringBuilder errMsg = new StringBuilder();
        if (!customerSave()) {
            errMsg.append("customer save error!");
            error = true;
        }
        if (!salesSave()) {
            if (errMsg.length() > 0) {
                errMsg.append('\n');
            }
            errMsg.append("sales save error!");
            error = true;
        }
        if (!customerSalesSave()) {
            if (errMsg.length() > 0) {
                errMsg.append('\n');
            }
            errMsg.append("CustomerSales save error!");
            error = true;
        }
        if (error) {
            JsfUtil.addErrorMessage(errMsg.toString());
        } else {
            JsfUtil.addSuccessMessage("save success!");
        }
    }

    // helper
    private void customerLoad() {
        setCustomerPK = new TreeSet<>();
        mapEcCustomer = new HashMap<>();
        mapKna1VO = new HashMap<>();

        List<EcCustomer> ecCustomers = ecCustomerFacade.findAll();
        for (EcCustomer ecCustomer : ecCustomers) {
            String pk = ecCustomer.getCode();
            setCustomerPK.add(pk);
            mapEcCustomer.put(pk, ecCustomer);
        }

        List<Kna1VO> kna1VOs = zperCnFacade.findAllCustomer();
        for (Kna1VO vo : kna1VOs) {
            String pk = vo.getCode();
            setCustomerPK.add(pk);
            mapKna1VO.put(pk, vo);
        }
    }

    private void customerCompare() {
        customerVOs = new ArrayList<>();
        for (String pk : setCustomerPK) {
            EcCustomer ecCustomer = mapEcCustomer.get(pk);
            Kna1VO vo = mapKna1VO.get(pk);
            if (ecCustomer != null && vo != null) {
                if (!StringUtils.equals(ecCustomer.getName(), vo.getName())) { // 只異動 name
                    CustomerVO cvo = new CustomerVO();
                    cvo.setActive(ecCustomer.isActive());
                    cvo.setCode(pk);
                    cvo.setName(vo.getName());
                    cvo.setEntity(ecCustomer);
                    cvo.setStatus(ExcelVOBase.Status.ST_UPDATE);
                    cvo.setMessage("update");
                    cvo.setValid(true);
                    customerVOs.add(cvo);
                }
            } else if (ecCustomer != null) { // 刪除 -> 停用
                if (ecCustomer.isActive()) {
                    CustomerVO cvo = new CustomerVO();
                    cvo.setActive(false);
                    cvo.setCode(pk);
                    cvo.setName(ecCustomer.getName());
                    cvo.setEntity(ecCustomer);
                    cvo.setStatus(ExcelVOBase.Status.ST_UPDATE);
                    cvo.setMessage("disable");
                    cvo.setValid(true);
                    customerVOs.add(cvo);
                }
            } else if (vo != null) { // 新增
                CustomerVO cvo = new CustomerVO();
                cvo.setActive(true);
                cvo.setCode(pk);
                cvo.setName(vo.getName());
                cvo.setStatus(ExcelVOBase.Status.ST_INSERT);
                cvo.setMessage("insert");
                cvo.setValid(true);
                customerVOs.add(cvo);
            }
        }
    }

    private boolean customerSave() {
        boolean success = true;
        for (CustomerVO cvo : customerVOs) {
            if (!cvo.isValid() || cvo.getStatus() == ExcelVOBase.Status.ST_NOCHANGE) {
                continue;
            }
            boolean insert = false;
            // only insert/update
            EcCustomer entity = cvo.getEntity();
            if (null == entity) {
                entity = new EcCustomer();
                entity.setCode(cvo.getCode());
                cvo.setEntity(entity);
                insert = true;
            }
            entity.setName(cvo.getName());
            entity.setActive(cvo.isActive());
            try {
                ecCustomerFacade.save(entity);
                cvo.setMessage(cvo.getMessage() + " success");
                cvo.setStatus(ExcelVOBase.Status.ST_NOCHANGE);
                if (insert) {
                    mapEcCustomer.put(cvo.getCode(), entity);
                }
            } catch (Exception ex) {
                cvo.setMessage(ex.getMessage());
                cvo.setValid(false);
                success = false;
            }
        }
        return success;
    }

    private void salesLoad() {
        setSalesPK = new TreeSet<>();
        mapEcSales = new HashMap<>();
        mapPernrVO = new HashMap<>();

        List<EcSales> entities = ecSalesFacade.findAll();
        for (EcSales entity : entities) {
            String pk = entity.getCode();
            setSalesPK.add(pk);
            mapEcSales.put(pk, entity);
        }

        List<PernrVO> voes = zperCnFacade.findAllSales();
        for (PernrVO vo : voes) {
            String pk = vo.getCode();
            setSalesPK.add(pk);
            mapPernrVO.put(pk, vo);
        }
    }

    private void salesCompare() {
        salesVOs = new ArrayList<>();
        for (String pk : setSalesPK) {
            EcSales entity = mapEcSales.get(pk);
            PernrVO vo = mapPernrVO.get(pk);
            if (entity != null && vo != null) { // update
                SalesVO svo = new SalesVO();
                svo.setCode(vo.getCode());
                svo.setName(vo.getName());
                svo.setActive(vo.isActive());
                svo.setEntity(entity);
                String[] fields = {"name", "active"};
                if (updateStatus(svo, entity, fields) != ExcelVOBase.Status.ST_NOCHANGE) {
                    svo.setMessage("update");
                    salesVOs.add(svo);
                }
            } else if (entity != null) { // delete -> 停用
                if (entity.isActive()) {
                    SalesVO svo = new SalesVO();
                    svo.setEntity(entity);
                    svo.setCode(entity.getCode());
                    svo.setName(entity.getName());
                    svo.setActive(false);
                    svo.setStatus(ExcelVOBase.Status.ST_UPDATE);
                    svo.setMessage("disable");
                    salesVOs.add(svo);
                }
            } else if (vo != null) { // insert
                SalesVO svo = new SalesVO();
                svo.setCode(vo.getCode());
                svo.setName(vo.getName());
                svo.setActive(vo.isActive());
                svo.setStatus(ExcelVOBase.Status.ST_INSERT);
                svo.setMessage("insert");
                salesVOs.add(svo);
            }
        }
    }

    private boolean salesSave() {
        boolean success = true;
        for (SalesVO svo : salesVOs) {
            if (!svo.isValid() || svo.getStatus() == ExcelVOBase.Status.ST_NOCHANGE) {
                continue;
            }
            // only insert/update
            EcSales entity = svo.getEntity();
            boolean insert = false;
            if (null == entity) {
                entity = new EcSales();
                entity.setCode(svo.getCode());
                svo.setEntity(entity);
                insert = true;
            }
            entity.setName(svo.getName());
            entity.setActive(svo.isActive());
            try {
                ecSalesFacade.save(entity);
                svo.setMessage(svo.getMessage() + " success");
                svo.setStatus(ExcelVOBase.Status.ST_NOCHANGE);
                if (insert) {
                    mapEcSales.put(svo.getCode(), entity);
                }
            } catch (Exception ex) {
                svo.setMessage(ex.getMessage());
                svo.setValid(false);
                success = false;
            }
        }
        return success;
    }
    
    private void customerSalesLoad() {
        setCustomerSalesPK = new TreeSet<>();
        mapEcCustomerSales = new HashMap<>();
        mapZperCnVO = new HashMap<>();
        
        List<EcCustomerSales> enties = ecCustomerFacade.findAllCustomerSales();
        for (EcCustomerSales entity : enties) {
            String pk = entity.getEcCustomer().getCode() + ":" + entity.getEcSales().getCode();
            setCustomerSalesPK.add(pk);
            mapEcCustomerSales.put(pk, entity);
        }
        
        List<ZperCnVO> voes = zperCnFacade.findAll();
        for (ZperCnVO vo : voes) {
            String pk = vo.getCustomer() + ":" + vo.getSales();
            setCustomerSalesPK.add(pk);
            mapZperCnVO.put(pk, vo);
        }
    }
    
    private void customerSalesCompare() {
        customerSalesVOs = new ArrayList<>();
        for (String pk : setCustomerSalesPK) {
            EcCustomerSales entity = mapEcCustomerSales.get(pk);
            ZperCnVO vo = mapZperCnVO.get(pk);
            if (entity != null && vo != null) { // no change
                continue;
            }
            // insert or delete
            CustomerSalesVO csvo = new CustomerSalesVO();
            String[] array = pk.split(":");
            csvo.setCustomer(array[0]);
            csvo.setSales(array[1]);
            if (entity != null) { // delete -> 停用
                csvo.setEntity(entity);
                csvo.setStatus(ExcelVOBase.Status.ST_DELETE);
                csvo.setMessage("delete");
            } else {
                csvo.setStatus(ExcelVOBase.Status.ST_INSERT);
                csvo.setMessage("insert");
            }
            customerSalesVOs.add(csvo);
        }
    }
    
    private boolean customerSalesSave() {
        boolean success = true;
        for (CustomerSalesVO csvo : customerSalesVOs) {
            if (!csvo.isValid() || csvo.getStatus() == ExcelVOBase.Status.ST_NOCHANGE) {
                continue;
            }
            if (csvo.getStatus() == ExcelVOBase.Status.ST_INSERT) {
                EcCustomer ecCustomer = mapEcCustomer.get(csvo.getCustomer());
                EcSales ecSales = mapEcSales.get(csvo.getSales());
                if (ecCustomer == null) {
                    csvo.setMessage(csvo.getCustomer() + " customer not found!");
                    csvo.setValid(false);
                    success = false;
                } else if (null == ecSales) {
                    csvo.setMessage(csvo.getSales()+ " sales not found!");
                    csvo.setValid(false);
                    success = false;
                } else {
                    try {
                        ecCustomerFacade.insertCustomerSales(ecCustomer, ecSales);
                        csvo.setMessage(csvo.getMessage() + " success");
                        csvo.setStatus(ExcelVOBase.Status.ST_NOCHANGE);
                    } catch (Exception ex) {
                        csvo.setMessage(ex.getMessage());
                        csvo.setValid(false);
                        success = false;
                    }
                }
            } else if (csvo.getStatus() == ExcelVOBase.Status.ST_DELETE) {
                try {
                    ecCustomerFacade.removeCustomerSales(csvo.getEntity());
                    csvo.setMessage(csvo.getMessage() + " success");
                    csvo.setStatus(ExcelVOBase.Status.ST_NOCHANGE);
                } catch (Exception ex) {
                    csvo.setMessage(ex.getMessage());
                    csvo.setValid(false);
                    success = false;
                }
            }
        }
        return success;
    }

    private ExcelVOBase.Status updateStatus(ExcelVOBase vo, Object entity, String[] fields) {
        ExcelVOBase.Status status;
        if (null == entity) {
            status = ExcelVOBase.Status.ST_INSERT;
        } else {
            status = isFieldModified(vo, entity, fields) ? ExcelVOBase.Status.ST_UPDATE : ExcelVOBase.Status.ST_NOCHANGE;
        }
        vo.setStatus(status);
        return status;
    }

    private boolean isFieldModified(ExcelVOBase vo, Object entity, String[] fields) {
        if (null == fields) {
            return false;
        }
        try {
            for (String field : fields) {
                Object f1 = PropertyUtils.getSimpleProperty(vo, field);
                Object f2 = PropertyUtils.getSimpleProperty(entity, field);
                if (f1 != null && f2 != null) {
                    if (!f1.equals(f2)) {
                        return true;
                    }
                } else if (f1 != null || f2 != null) {
                    return true;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }

    // getter, setter
    public List<CustomerVO> getCustomerVOs() {
        return customerVOs;
    }

    public void setCustomerVOs(List<CustomerVO> customerVOs) {
        this.customerVOs = customerVOs;
    }

    public List<SalesVO> getSalesVOs() {
        return salesVOs;
    }

    public void setSalesVOs(List<SalesVO> salesVOs) {
        this.salesVOs = salesVOs;
    }

    public List<CustomerSalesVO> getCustomerSalesVOs() {
        return customerSalesVOs;
    }

    public void setCustomerSalesVOs(List<CustomerSalesVO> customerSalesVOs) {
        this.customerSalesVOs = customerSalesVOs;
    }

}
