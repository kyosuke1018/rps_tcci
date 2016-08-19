/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.imports;

import com.tcci.fc.controller.dataimport.ExcelVOBase;
import com.tcci.fc.controller.dataimport.ImportExcelBase;
import com.tcci.tccstore.entity.EcDeliveryPlace;
import com.tcci.tccstore.entity.EcDeliveryVkorg;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryPlaceFacade;
import com.tcci.tccstore.facade.deliveryplace.EcDeliveryVkorgFacade;
import com.tcci.tccstore.facade.salesarea.EcSalesareaFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
public class DeliveryImport extends ImportExcelBase<DeliveryVO> {

    @EJB
    private EcDeliveryPlaceFacade entityFacade;
    @EJB
    private EcDeliveryVkorgFacade ecDeliveryVkorgFacade;
    @EJB
    private EcSalesareaFacade ecSalesareaFacade;
    
    private Set<String> setPK;
    private Map<String, EcSalesarea> mapSalesarea;

    public DeliveryImport() {
        super(DeliveryVO.class);
        setPK = new HashSet<>();
        mapSalesarea = new HashMap<>();
    }

    @Override
    protected void verify() {
        setPK.clear();
        super.verify();
    }

    @Override
    protected boolean postInit(DeliveryVO vo) {
        if (!setPK.add(vo.getCode())) {
            vo.setMessage(vo.getCode() + " code duplicated!");
            return false;
        }
        EcSalesarea ecSalesarea = findSalesare(vo.getSalesarea());
        if (null == ecSalesarea) {
            vo.setMessage(vo.getSalesarea() + " salesarea not found!");
            return false;
        }
        vo.setEcSalesarea(ecSalesarea);
        EcDeliveryPlace entity = entityFacade.findByCode(vo.getCode());
        vo.setEntity(entity);
        if (entity != null) {
            if (vo.getProvince()==null) {
                vo.setProvince(entity.getProvince());
            }
            if (vo.getCity()==null) {
                vo.setCity(entity.getCity());
            }
            if (vo.getDistrict()==null) {
                vo.setDistrict(entity.getDistrict());
            }
            if (vo.getTown()==null) {
                vo.setTown(entity.getTown());
            }
            List<EcDeliveryVkorg> ecVksas = ecDeliveryVkorgFacade.findByDelivery(entity);
            vo.setEcVksas(ecVksas);
        }
        String[] fields = {"name", "province", "city", "district", "town", "active", "ecSalesarea"};
        updateStatus(vo, entity, fields);
        compareVksas(vo);
        return true;
    }

    @Override
    protected boolean insert(DeliveryVO vo) {
        EcDeliveryPlace entity = new EcDeliveryPlace();
        entity.setCode(vo.getCode());
        vo.setEntity(entity);
        return save(vo);
    }

    @Override
    protected boolean update(DeliveryVO vo) {
        return save(vo);
    }

    private EcSalesarea findSalesare(String salesarea) {
        if (mapSalesarea.containsKey(salesarea)) {
            return mapSalesarea.get(salesarea);
        }
        EcSalesarea ecSalesarea = ecSalesareaFacade.findByCode(salesarea);
        mapSalesarea.put(salesarea, ecSalesarea);
        return ecSalesarea;
    }

    private boolean save(DeliveryVO vo) {
        EcDeliveryPlace entity = vo.getEntity();
        entity.setName(vo.getName());
        entity.setProvince(vo.getProvince());
        entity.setCity(vo.getCity());
        entity.setDistrict(vo.getDistrict());
        entity.setTown(vo.getTown());
        entity.setActive(vo.isActive());
        entity.setEcSalesarea(vo.getEcSalesarea());
        try {
            entityFacade.save(entity);
            saveVksas(vo);
            return true;
        } catch (Exception ex) {
            vo.setMessage(ex.getMessage());
            return false;
        }
    }
    
    private void compareVksas(DeliveryVO vo) {
        Set<String> setVkorg = new HashSet<>();
        Map<String, String> map1 = new HashMap<>();
        String vksas = vo.getVksas();
        if (vksas != null) {
            for (String vksa : vksas.split(",")) {
                String[] array = vksa.split(":");
                String vkorg = array[0].trim();
                String salesarea = array[1].trim();
                EcSalesarea ecSalesarea = findSalesare(salesarea);
                if (null == ecSalesarea) {
                    vo.setValid(false);
                    vo.setMessage(salesarea + " salesarea not found!");
                    return;
                }
                setVkorg.add(vkorg);
                map1.put(vkorg, salesarea);
            }
        }
        
        Map<String, EcDeliveryVkorg> map2 = new HashMap<>();
        List<EcDeliveryVkorg> ecVksas = vo.getEcVksas();
        if (ecVksas != null && !ecVksas.isEmpty()) {
            for (EcDeliveryVkorg e : ecVksas) {
                String vkorg = e.getPk().getVkorg();
                setVkorg.add(vkorg);
                map2.put(vkorg, e);
            }
        }
        
        boolean changed = false;
        List<EcDeliveryVkorg> delVkorgs = new ArrayList<>();
        List<EcDeliveryVkorg> updVkorgs = new ArrayList<>();
        Map<String, String> insVkorgs = new HashMap<>();
        for (String vkorg : setVkorg) {
            String salesarea = map1.get(vkorg);
            EcDeliveryVkorg ecVkorg = map2.get(vkorg);
            if (salesarea != null && ecVkorg != null) {
                if (!salesarea.equals(ecVkorg.getEcSalesarea().getCode())) {
                    EcSalesarea ecSalesarea = findSalesare(salesarea);
                    ecVkorg.setEcSalesarea(ecSalesarea);
                    updVkorgs.add(ecVkorg);
                    changed = true;
                }
            } else if (salesarea != null) { // insert
                insVkorgs.put(vkorg, salesarea);
                changed = true;
            } else {
                delVkorgs.add(ecVkorg);
                changed = true;
            }
        }
        vo.setDelVkorgs(delVkorgs);
        vo.setUpdVkorgs(updVkorgs);
        vo.setInsVkorgs(insVkorgs);
        if (changed && vo.getStatus() != ExcelVOBase.Status.ST_INSERT) {
            vo.setStatus(ExcelVOBase.Status.ST_UPDATE);
        }
    }

    private void saveVksas(DeliveryVO vo) {
        // delete
        for (EcDeliveryVkorg ecVkorg : vo.getDelVkorgs()) {
            ecDeliveryVkorgFacade.remove(ecVkorg);
        }
        // update
        for (EcDeliveryVkorg ecVkorg : vo.getUpdVkorgs()) {
            ecDeliveryVkorgFacade.edit(ecVkorg);
        }
        // insert
        for (Map.Entry<String, String> entry : vo.getInsVkorgs().entrySet()) {
            String vkorg = entry.getKey();
            String salesarea = entry.getValue();
            EcSalesarea ecSalesarea = findSalesare(salesarea);
            ecDeliveryVkorgFacade.insert(vo.getEntity(), vkorg, ecSalesarea);
        }
    }

}
