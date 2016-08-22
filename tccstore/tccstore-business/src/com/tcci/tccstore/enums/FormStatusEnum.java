/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.enums;

/**
 *
 * @author Jimmy.Lee
 */
public enum FormStatusEnum {

    APPLY(1),
    APPROVE(2),
    REJECT(3);

    private final int value;

    private FormStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
