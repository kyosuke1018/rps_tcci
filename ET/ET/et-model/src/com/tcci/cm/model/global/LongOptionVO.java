/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class LongOptionVO implements Serializable {
    private Long value;
    private String label;

    public LongOptionVO(){}
    
    public LongOptionVO(Long value, String label){
        this.value = value;
        this.label = label;
    }
    
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
}
