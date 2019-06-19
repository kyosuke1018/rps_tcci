/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class PrdTypeTreeNodeVO implements Serializable {
    private PrdTypeVO data;
    private List<PrdTypeTreeNodeVO> children;

    public PrdTypeTreeNodeVO(){}
    
    public PrdTypeVO getData() {
        return data;
    }

    public void setData(PrdTypeVO data) {
        this.data = data;
    }

    public List<PrdTypeTreeNodeVO> getChildren() {
        return children;
    }

    public void setChildren(List<PrdTypeTreeNodeVO> children) {
        this.children = children;
    }

}
