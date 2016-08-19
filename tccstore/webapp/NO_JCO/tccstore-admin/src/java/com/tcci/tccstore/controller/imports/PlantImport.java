/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
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
public class PlantImport extends ImportExcelBase<PlantVO> {

    @EJB
    private EcPlantFacade entityFacade;

    private Set<String> setPK;

    public PlantImport() {
        super(PlantVO.class);
        setPK = new HashSet<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(PlantVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcPlant entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        String[] fields = {"name", "incoFlag", "vkorg", "autoOrder", "active"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(PlantVO vo) {
        EcPlant entity = new EcPlant();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(PlantVO vo) {
        return save(vo);
    }

    private boolean save(PlantVO vo) {
        EcPlant entity = vo.getEntity();
        entity.setName(vo.getName());
        entity.setIncoFlag(vo.getIncoFlag());
        entity.setVkorg(vo.getVkorg());
        entity.setAutoOrder(vo.isAutoOrder());
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
