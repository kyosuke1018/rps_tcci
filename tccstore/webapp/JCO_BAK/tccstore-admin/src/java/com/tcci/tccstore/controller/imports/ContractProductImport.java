/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcContract;
import com.tcci.tccstore.entity.EcContractProduct;
import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.contract.EcContractFacade;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import com.tcci.tccstore.facade.product.EcProductFacade;
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
public class ContractProductImport extends ImportExcelBase<ContractProductVO> {

    @EJB
    private EcContractFacade ecContractFacade;
    @EJB
    private EcProductFacade ecProductFacade;
    @EJB
    private EcSalesareaFacade ecSalesareaFacade;
    @EJB
    private EcPlantFacade ecPlantFacade;
    
    private Set<String> setPK;
    private Map<String, EcContract> mapContract;
    private Map<String, EcProduct> mapProduct;
    private Map<String, EcSalesarea> mapSalesarea;
    private Map<String, EcPlant> mapPlant;
    
    public ContractProductImport() {
        super(ContractProductVO.class);
        setPK = new HashSet<>();
        mapContract = new HashMap<>();
        mapProduct = new HashMap<>();
        mapSalesarea = new HashMap<>();
        mapPlant = new HashMap<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(ContractProductVO vo) {
        // String pk = vo.getContract() + ":" + vo.getProduct();
        String pk = vo.getContract() + ":" + vo.getPosnr();
        if (!setPK.add(pk)) {
            vo.setMessage("data duplicated!");
            return false;
        }
        EcContract ecContract = findContract(vo.getContract());
        if (null == ecContract) {
            vo.setMessage(vo.getContract() + " contract not found!");
            return false;
        }
        vo.setEcContract(ecContract);
        EcProduct ecProduct = findProduct(vo.getProduct());
        if (null == ecProduct) {
            vo.setMessage(vo.getProduct() + " product not found!");
            return false;
        }
        vo.setEcProduct(ecProduct);
        EcSalesarea ecSalesarea = findSalesarea(vo.getSalesarea());
        if (null == ecSalesarea) {
            vo.setMessage(vo.getSalesarea() + " salesarea not found!");
            return false;
        }
        vo.setEcSalesarea(ecSalesarea);
        EcPlant ecPlant = findPlant(vo.getPlant());
        if (null == ecPlant) {
            vo.setMessage(vo.getPlant() + " plant not found!");
            return false;
        }
        vo.setEcPlant(ecPlant);
        EcContractProduct entity = ecContractFacade.find(ecContract, vo.getPosnr());
        vo.setEntity(entity);
        String[] fields = {"unitPrice", "method", "ecProduct", "ecSalesarea", "ecPlant"};
        updateStatus(vo, entity, fields);
        return true;
    }

    @Override
    protected boolean insert(ContractProductVO vo) {
        EcContractProduct entity = new EcContractProduct();
        entity.setEcContract(vo.getEcContract());
        entity.setEcProduct(vo.getEcProduct());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(ContractProductVO vo) {
        return save(vo);
    }

    private EcContract findContract(String contract) {
        if (mapContract.containsKey(contract)) {
            return mapContract.get(contract);
        }
        EcContract ecContract = ecContractFacade.findByCode(contract);
        mapContract.put(contract, ecContract);
        return ecContract;
    }

    private EcProduct findProduct(String product) {
        if (mapProduct.containsKey(product)) {
            return mapProduct.get(product);
        }
        EcProduct ecProduct = ecProductFacade.findByCode(product);
        mapProduct.put(product, ecProduct);
        return ecProduct;
    }

    private EcSalesarea findSalesarea(String salesarea) {
        if (mapSalesarea.containsKey(salesarea)) {
            return mapSalesarea.get(salesarea);
        }
        EcSalesarea ecSalesarea = ecSalesareaFacade.findByCode(salesarea);
        mapSalesarea.put(salesarea, ecSalesarea);
        return ecSalesarea;
    }

    private EcPlant findPlant(String plant) {
        if (mapPlant.containsKey(plant)) {
            return mapPlant.get(plant);
        }
        EcPlant ecPlant = ecPlantFacade.findByCode(plant);
        mapPlant.put(plant, ecPlant);
        return ecPlant;
    }

    private boolean save(ContractProductVO vo) {
        EcContractProduct entity = vo.getEntity();
        entity.setUnitPrice(vo.getUnitPrice());
        entity.setMethod(vo.getMethod());
        entity.setEcSalesarea(vo.getEcSalesarea());
        entity.setEcPlant(vo.getEcPlant());
        entity.setEcProduct(vo.getEcProduct());
        try {
            ecContractFacade.save(entity, vo.getPosnr());
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }
    
}
