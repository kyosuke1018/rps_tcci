/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.model.plant;

import com.tcci.tccstore.model.ModelConstant;

/**
 *
 * @author Jimmy.Lee
 */
public class Plant {

    private Long id;
    private String code;
    private String name;
    private int incoFlag; // 國貿條件控制

    public Plant() {
    }

    public Plant(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
    
    public boolean incoEXW() {
        return (incoFlag & ModelConstant.INCO_FLAG_EXW) != 0;
    }
    
    public boolean incoFCA() {
        return (incoFlag & ModelConstant.INCO_FLAG_FCA) != 0;
    }

    // getter, setter
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

    public int getIncoFlag() {
        return incoFlag;
    }

    public void setIncoFlag(int incoFlag) {
        this.incoFlag = incoFlag;
    }

}
