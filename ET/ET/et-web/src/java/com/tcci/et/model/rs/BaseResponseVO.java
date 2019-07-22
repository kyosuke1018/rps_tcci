/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.model.rs;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class BaseResponseVO {
    private ResponseVO res;
    
    public BaseResponseVO(){}

    public ResponseVO getRes() {
        return res;
    }

    public void setRes(ResponseVO res) {
        this.res = res;
    }
    
}
