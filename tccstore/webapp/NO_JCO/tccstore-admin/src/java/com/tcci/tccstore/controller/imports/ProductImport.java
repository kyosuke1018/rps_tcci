/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.facade.product.EcProductFacade;
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
public class ProductImport extends ImportExcelBase<ProductVO> {

    @EJB
    private EcProductFacade entityFacade;

    private Set<String> setPK;

    public ProductImport() {
        super(ProductVO.class);
        setPK = new HashSet<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(ProductVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcProduct entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        String[] fields = {"name", "description", "active"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(ProductVO vo) {
        EcProduct entity = new EcProduct();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(ProductVO vo) {
        return save(vo);
    }

    private boolean save(ProductVO vo) {
        EcProduct entity = vo.getEntity();
        entity.setName(vo.getName());
        entity.setDescription(vo.getDescription());
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
