/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model.e10;

/**
 *
 * @author Peter.pan
 */
public class PlantE10VO {
    private Long id; // PK
    private String code; // SAP代碼
    private String name; // 廠名稱
    private Integer incoFlag; // bit0:廠交自提(EXW), bit1:工地交自提(FCA)
    private Boolean active; // 1:啟用, 0:停用
    private String vkorg; // 銷售組織
    private Boolean autoOrder; // 1:自動核單, 0:人工核單

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIncoFlag() {
        return incoFlag;
    }

    public void setIncoFlag(Integer incoFlag) {
        this.incoFlag = incoFlag;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public Boolean getAutoOrder() {
        return autoOrder;
    }

    public void setAutoOrder(Boolean autoOrder) {
        this.autoOrder = autoOrder;
    }

    
}
