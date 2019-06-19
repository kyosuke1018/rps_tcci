/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.vo;

import java.util.List;

/**
 *
 * @author Kyle.Cheng
 */
public class ProductQuery {
    private List<Product> productList;
    private List<PrdType> nextTypeList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<PrdType> getNextTypeList() {
        return nextTypeList;
    }

    public void setNextTypeList(List<PrdType> nextTypeList) {
        this.nextTypeList = nextTypeList;
    }
}
