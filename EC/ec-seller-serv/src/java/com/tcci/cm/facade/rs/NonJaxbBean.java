/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs;

/**
 *
 * @author Peter.pan
 */
public class NonJaxbBean {
    private String name = "non-JAXB-bean";
    private String description = "I am not a JAXB bean, just an unannotated POJO";
    private int[] array = {1, 1, 2, 3, 5, 8, 13, 21};

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}