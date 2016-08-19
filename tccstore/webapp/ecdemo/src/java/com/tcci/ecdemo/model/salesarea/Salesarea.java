/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.model.salesarea;

/**
 *
 * @author Jimmy.Lee
 */
public class Salesarea {

    private Long id;
    private String code;
    private String name;

    public Salesarea() {
    }

    public Salesarea(String code, String name) {
        this.code = code;
        this.name = name;
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

}
