/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcPlantProduct;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import com.tcci.tccstore.facade.product.EcProductFacade;
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
public class PlantProductImport extends ImportExcelBase<PlantProductVO> {

    @EJB
    private EcPlantFacade ecPlantFacade;
    @EJB
    private EcProductFacade ecProductFacade;

    private Set<String> setPK;
    private Map<String, EcPlant> mapPlant;
    private Map<String, EcProduct> mapProduct;

    public PlantProductImport() {
        super(PlantProductVO.class);
        setPK = new HashSet<>();
        mapPlant = new HashMap<>();
        mapProduct = new HashMap<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(PlantProductVO vo) {
        String pk = vo.getPlant() + ":" + vo.getProduct();
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
        EcProduct ecProduct = findProduct(vo.getProduct());
        if (null == ecProduct) {
            vo.setMessage(vo.getProduct() + " product not found!");
            return false;
        }
        vo.setEcProduct(ecProduct);
        EcPlantProduct entity = ecPlantFacade.findPlantProduct(ecPlant, ecProduct);
        vo.setEntity(entity);
        String[] fields = {"active"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(PlantProductVO vo) {
        EcPlantProduct entity = new EcPlantProduct();
        entity.setEcPlant(vo.getEcPlant());
        entity.setEcProduct(vo.getEcProduct());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(PlantProductVO vo) {
        return save(vo);
    }

    private EcPlant findPlant(String plant) {
        if (mapPlant.containsKey(plant)) {
            return mapPlant.get(plant);
        }
        EcPlant ecPlant = ecPlantFacade.findByCode(plant);
        mapPlant.put(plant, ecPlant);
        return ecPlant;
    }

    private EcProduct findProduct(String product) {
        if (mapProduct.containsKey(product)) {
            return mapProduct.get(product);
        }
        EcProduct ecProduct = ecProductFacade.findByCode(product);
        mapProduct.put(product, ecProduct);
        return ecProduct;
    }

    private boolean save(PlantProductVO vo) {
        EcPlantProduct entity = vo.getEntity();
        entity.setActive(vo.isActive());
        try {
            ecPlantFacade.savePlantProduct(entity);
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }

}
