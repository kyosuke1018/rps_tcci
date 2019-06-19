/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.model;

import com.tcci.ec.model.rs.BaseResponseVO;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Peter.pan
 */
@XmlRootElement
public class PrdTypeTreeVO extends BaseResponseVO implements Serializable {
    private List<PrdTypeTreeNodeVO> nodes;

    public PrdTypeTreeVO(){}
    
    public List<PrdTypeTreeNodeVO> getNodes() {
        return nodes;
    }

    public void setNodes(List<PrdTypeTreeNodeVO> nodes) {
        this.nodes = nodes;
    }
}
