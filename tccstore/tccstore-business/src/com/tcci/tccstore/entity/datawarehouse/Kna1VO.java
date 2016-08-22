/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.entity.datawarehouse;

/**
 *
 * @author Jimmy.Lee
 */
public class Kna1VO {

    private String code;
    private String name;

    public Kna1VO() {
    }
    
    public Kna1VO(String code, String name) {
        this.code = code;
        this.name = name;
    } 
    
    // getter, setter
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

}
