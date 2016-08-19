/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
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
public class SalesareaImport extends ImportExcelBase<SalesareaVO> {

    @EJB
    private EcSalesareaFacade entityFacade;

    private Set<String> setPK;

    public SalesareaImport() {
        super(SalesareaVO.class);
        setPK = new HashSet<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(SalesareaVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcSalesarea entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        String[] fields = {"name", "active"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(SalesareaVO vo) {
        EcSalesarea entity = new EcSalesarea();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(SalesareaVO vo) {
        return save(vo);
    }

    private boolean save(SalesareaVO vo) {
        EcSalesarea entity = vo.getEntity();
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
