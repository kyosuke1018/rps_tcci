/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcSales;
import com.tcci.tccstore.facade.sales.EcSalesFacade;
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
public class SalesImport extends ImportExcelBase<SalesVO> {

    @EJB
    private EcSalesFacade entityFacade;

    private Set<String> setPK;

    public SalesImport() {
        super(SalesVO.class);
        setPK = new HashSet<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(SalesVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcSales entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        String[] fields = {"name", "active"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(SalesVO vo) {
        EcSales entity = new EcSales();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(SalesVO vo) {
        return save(vo);
    }

    private boolean save(SalesVO vo) {
        EcSales entity = vo.getEntity();
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
