/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcCustomer;
import com.tcci.tccstore.facade.customer.EcCustomerFacade;
import java.util.HashSet;
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
public class CustomerImport extends ImportExcelBase<CustomerVO> {

    @EJB
    private EcCustomerFacade entityFacade;

    private Set<String> setPK;

    public CustomerImport() {
        super(CustomerVO.class);
        setPK = new HashSet<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(CustomerVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcCustomer entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        String[] fields = {"name", "active"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(CustomerVO vo) {
        EcCustomer entity = new EcCustomer();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(CustomerVO vo) {
        return save(vo);
    }

    private boolean save(CustomerVO vo) {
        EcCustomer entity = vo.getEntity();
        entity.setName(vo.getName());
        entity.setActive(vo.isActive());
        try {
            entityFacade.save(entity);
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

}
