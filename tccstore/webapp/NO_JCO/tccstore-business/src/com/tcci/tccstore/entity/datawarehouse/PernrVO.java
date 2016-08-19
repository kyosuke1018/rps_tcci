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
public class PernrVO {

    private String code;
    private String name;
    private boolean active;

    public PernrVO() {
    }
    
    public PernrVO(String code, String name, boolean active) {
        this.code = code;
        this.name = name;
        this.active = active;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
