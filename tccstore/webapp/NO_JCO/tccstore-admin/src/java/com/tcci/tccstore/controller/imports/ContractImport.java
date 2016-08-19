/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.facade.contract.EcContractFacade;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class ContractImport extends ImportExcelBase<ContractVO> {

    @EJB
    private EcContractFacade entityFacade;
    @EJB
    private EcCustomerFacade ecCustomerFacade;

    private Set<String> setPK;
    private Map<String, EcCustomer> mapCustomer;

    public ContractImport() {
        super(ContractVO.class);
        setPK = new HashSet<>();
        mapCustomer = new HashMap<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(ContractVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcCustomer ecCustomer = findCustomer(vo.getCustomer());
        if (null == ecCustomer) {
            vo.setMessage(vo.getCustomer() + " customer not found!");
            return false;
        }
        if (vo.getName() == null) {
            vo.setName(vo.getCode());
        }
        vo.setEcCustomer(ecCustomer);
        EcContract entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        String[] fields = {"name", "active", "ecCustomer"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(ContractVO vo) {
        EcContract entity = new EcContract();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(ContractVO vo) {
        return save(vo);
    }

    private EcCustomer findCustomer(String customer) {
        if (mapCustomer.containsKey(customer)) {
            return mapCustomer.get(customer);
        }
        EcCustomer ecCustomer = ecCustomerFacade.findByCode(customer);
        mapCustomer.put(customer, ecCustomer);
        return ecCustomer;
    }

    private boolean save(ContractVO vo) {
        EcContract entity = vo.getEntity();
        entity.setName(vo.getName());
        entity.setActive(vo.isActive());
        entity.setEcCustomer(vo.getEcCustomer());
        try {
            entityFacade.save(entity);
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

}
