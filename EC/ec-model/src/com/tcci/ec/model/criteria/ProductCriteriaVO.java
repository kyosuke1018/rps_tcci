/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

/**
 *
 * @author Peter.pan
 */
public class ProductCriteriaVO extends BaseCriteriaVO {
    private Long typeL1;
    private Long typeL2;

    public Long getTypeL1() {
        return typeL1;
    }

    public void setTypeL1(Long typeL1) {
        this.typeL1 = typeL1;
    }

    public Long getTypeL2() {
        return typeL2;
    }

    public void setTypeL2(Long typeL2) {
        this.typeL2 = typeL2;
    }
}
