/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller.content;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.et.model.criteria.MediaCriteriaVO;

/**
 *
 * @author peter.pan
 */
abstract public class MediaController extends SessionAwareController {

    // query
    protected MediaCriteriaVO criteriaVO;
    
    //<editor-fold defaultstate="collapsed" desc="getter and setter">
    public MediaCriteriaVO getCriteriaVO() {
        return criteriaVO;
    }

    public void setCriteriaVO(MediaCriteriaVO criteriaVO) {
        this.criteriaVO = criteriaVO;
    }
    //</editor-fold>
}
