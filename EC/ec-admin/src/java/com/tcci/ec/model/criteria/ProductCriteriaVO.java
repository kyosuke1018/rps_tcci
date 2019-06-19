/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.criteria;

import java.util.List;

/**
 *
 * @author Peter.pan
 */
public class ProductCriteriaVO extends BaseCriteriaVO {
    private Long typeL1;
    private Long typeL2;
    private Long areaId;
    private List<Long> areaList;
    private Boolean tccPrd;// 台泥轉入商品
    private Boolean noPicOnly;

    public List<Long> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Long> areaList) {
        this.areaList = areaList;
    }

    public Boolean getNoPicOnly() {
        return noPicOnly;
    }

    public void setNoPicOnly(Boolean noPicOnly) {
        this.noPicOnly = noPicOnly;
    }

    public Boolean getTccPrd() {
        return tccPrd;
    }

    public void setTccPrd(Boolean tccPrd) {
        this.tccPrd = tccPrd;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

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
