/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.rs;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement(name = "account")
public class CombinedAnnotationBean {

    @JsonProperty("value")
    int x;

    public CombinedAnnotationBean(int x) {
        this.x = x;
    }

    public CombinedAnnotationBean() {
        this(15);
    }
}
