/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcPlantSalesarea;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
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
public class PlantSalesareaImport extends ImportExcelBase<PlantSalesareaVO> {

    @EJB
    private EcPlantFacade ecPlantFacade;
    @EJB
    private EcSalesareaFacade ecSalesareaFacade;

    private Set<String> setPK;
    private Map<String, EcPlant> mapPlant;
    private Map<String, EcSalesarea> mapSalesarea;

    public PlantSalesareaImport() {
        super(PlantSalesareaVO.class);
        setPK = new HashSet<>();
        mapPlant = new HashMap<>();
        mapSalesarea = new HashMap<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(PlantSalesareaVO vo) {
        String pk = vo.getPlant() + ":" + vo.getSalesarea();
        if (!setPK.add(pk)) {
            vo.setMessage("data duplicated!");
            return false;
        }
        EcPlant ecPlant = findPlant(vo.getPlant());
        if (null == ecPlant) {
            vo.setMessage(vo.getPlant() + " plant not found!");
            return false;
        }
        vo.setEcPlant(ecPlant);
        EcSalesarea ecSalesarea = findSalesarea(vo.getSalesarea());
        if (null == ecSalesarea) {
            vo.setMessage(vo.getSalesarea() + " salesarea not found!");
            return false;
        }
        vo.setEcSalesarea(ecSalesarea);
        EcPlantSalesarea entity = ecPlantFacade.findPlantSalesarea(ecPlant, ecSalesarea);
        if (null == entity) {
            vo.setStatus(ExcelVOBase.Status.ST_INSERT);
        }
        return true;
    }

    @Override
    protected boolean insert(PlantSalesareaVO vo) {
        try {
            EcPlantSalesarea entity = new EcPlantSalesarea();
            entity.setEcPlant(vo.getEcPlant());
            entity.setEcSalesarea(vo.getEcSalesarea());
            ecPlantFacade.savePlantSalesarea(entity);
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

    @Override
    protected boolean update(PlantSalesareaVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private EcPlant findPlant(String plant) {
        if (mapPlant.containsKey(plant)) {
            return mapPlant.get(plant);
        }
        EcPlant ecPlant = ecPlantFacade.findByCode(plant);
        mapPlant.put(plant, ecPlant);
        return ecPlant;
    }

    private EcSalesarea findSalesarea(String salesarea) {
        if (mapSalesarea.containsKey(salesarea)) {
            return mapSalesarea.get(salesarea);
        }
        EcSalesarea ecSalesarea = ecSalesareaFacade.findByCode(salesarea);
        mapSalesarea.put(salesarea, ecSalesarea);
        return ecSalesarea;
    }

}
