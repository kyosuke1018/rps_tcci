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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "selectProductsController")
@ViewScoped
public class SelectProductsController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private List<SkProductMaster> products;
    private List<SkProductMaster> selectedProducts;
    private String[] selectedCategories;
    private boolean checkAll;

    @EJB
    private SkProductMasterFacade productMasterFacade;

    @PostConstruct
    private void init() {
        selectedProducts = new ArrayList<SkProductMaster>();
    }

    public String getProductFilter() {
        if (selectedProducts.isEmpty()) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (SkProductMaster prod : selectedProducts) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(prod.getCode());
            }
            return sb.toString();
        }
    }

    // action
    public void categoriesChange() {
        products = productMasterFacade.findByCategories(selectedCategories);
        for (int i = selectedProducts.size() - 1; i >= 0; i--) {
            SkProductMaster prod = selectedProducts.get(i);
            if (!products.contains(prod)) {
                selectedProducts.remove(i);
            }
        }
    }

    public void toggle(SkProductMaster product) {
        logger.debug("product={}", product);
        logger.debug("selectedProducts={}", selectedProducts);
        if (selectedProducts.contains(product)) {
            selectedProducts.remove(product);
        } else {
            selectedProducts.add(product);
        }
    }

    public void selectAll() {
        selectedProducts.clear();
        if (products != null && !products.isEmpty()) {
            selectedProducts.addAll(products);
        }
    }

    public void removeAll() {
        selectedProducts.clear();
    }

    // helper
    public boolean isProductSelected(SkProductMaster product) {
        logger.debug("product={}", product);
        logger.debug("selectedProducts.contains(product)={}", selectedProducts.contains(product));
        return selectedProducts.contains(product);
    }

    // getter, setter
    public List<SkProductMaster> getProducts() {
        return products;
    }

    public void setProducts(List<SkProductMaster> products) {
        this.products = products;
    }

    public List<SkProductMaster> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(List<SkProductMaster> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public String[] getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(String[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public boolean isCheckAll() {
        return checkAll;
    }

    public void setCheckAll(boolean checkAll) {
        this.checkAll = checkAll;
    }

}
