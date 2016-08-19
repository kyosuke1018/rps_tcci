/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.controller.master;

import com.tcci.tccstore.entity.EcPlant;
import com.tcci.tccstore.entity.EcPlantSalesarea;
import com.tcci.tccstore.entity.EcSalesarea;
import com.tcci.tccstore.facade.plant.EcPlantFacade;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean
@ViewScoped
public class PlantSalesareaQuery {

    private List<EcPlantSalesarea> result = new ArrayList<>();
    private String plantCode;
    private String salesareaCode;

    @EJB
    private EcPlantFacade ecPlantFacade;

    private List<EcPlantSalesarea> all;
    private Set<EcPlant> ecPlants;
    private Set<EcSalesarea> ecSalesareas;

    @PostConstruct
    private void init() {
        result = new ArrayList<>();
        ecPlants = new TreeSet<>(new Comparator<EcPlant>() {
            @Override
            public int compare(EcPlant o1, EcPlant o2) { return o1.getCode().compareTo(o2.getCode()); }
        });
        ecSalesareas = new TreeSet<>(new Comparator<EcSalesarea>() {
            @Override
            public int compare(EcSalesarea o1, EcSalesarea o2) { return o1.getCode().compareTo(o2.getCode()); }
        });
        all = ecPlantFacade.findAllPlantSalesarea();
        for (EcPlantSalesarea entity : all) {
            ecPlants.add(entity.getEcPlant());
            ecSalesareas.add(entity.getEcSalesarea());
        }
    }

    public void query() {
        result.clear();
        for (EcPlantSalesarea entity : all) {
            if (plantCode != null && !plantCode.equals(entity.getEcPlant().getCode())) {
                continue;
            }
            if (salesareaCode != null && !salesareaCode.equals(entity.getEcSalesarea().getCode())) {
                continue;
            }
            result.add(entity);
        }
    }

    public List<EcPlant> completePlant(String input) {
        List<EcPlant> list = new ArrayList<>();
        for (EcPlant entity : ecPlants) {
            if (entity.getCode().contains(input) || entity.getName().contains(input)) {
                list.add(entity);
            }
        }
        return list;
    }

    public List<EcSalesarea> completeSalesarea(String input) {
        List<EcSalesarea> list = new ArrayList<>();
        for (EcSalesarea entity : ecSalesareas) {
            if (entity.getCode().contains(input) || entity.getName().contains(input)) {
                list.add(entity);
            }
        }
        return list;
    }

    // getter, setter
    public List<EcPlantSalesarea> getResult() {
        return result;
    }

    public void setResult(List<EcPlantSalesarea> result) {
        this.result = result;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getSalesareaCode() {
        return salesareaCode;
    }

    public void setSalesareaCode(String salesareaCode) {
        this.salesareaCode = salesareaCode;
    }

}
