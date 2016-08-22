/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.sksp.controller.util;

import com.tcci.sksp.entity.ar.SkProductMaster;
import com.tcci.sksp.facade.SkProductMasterFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "selectOneProduct")
@ViewScoped
public class SelectOneProduct {
    
    private String[] selectedCategories;
    private String filter;
    private List<SkProductMaster> filtedProducts;
    private String prodCode;
    private String prodName;
    
    private List<SkProductMaster> products;

    @EJB
    private SkProductMasterFacade productMasterFacade;

    @PostConstruct
    private void init() {
        filtedProducts = new ArrayList<SkProductMaster>();
    }

    // action
    public void categoriesChange() {
        products = productMasterFacade.findByCategories(selectedCategories);
        filterChange();
    }

    public void filterChange() {
        filtedProducts.clear();
        if (null==filter || filter.isEmpty()) {
            filtedProducts.addAll(products);
        } else {
            String str = filter.trim();
            for (SkProductMaster product : products) {
                if (StringUtils.containsIgnoreCase(product.getCode(), str) ||
                    StringUtils.containsIgnoreCase(product.getName(), str)) {
                    filtedProducts.add(product);
                }
            }
        }
    }
    
    public void productSelected(SkProductMaster product) {
        prodCode = product.getCode();
        prodName = product.getName();
    }

    // getter, setter
    public String[] getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(String[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<SkProductMaster> getFiltedProducts() {
        return filtedProducts;
    }

    public void setFiltedProducts(List<SkProductMaster> filtedProducts) {
        this.filtedProducts = filtedProducts;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
}
