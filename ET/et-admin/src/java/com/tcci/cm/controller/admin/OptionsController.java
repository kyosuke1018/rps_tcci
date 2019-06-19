/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.controller.admin;

import com.tcci.cm.controller.global.GenericOptionsController;
import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.ec.enums.OptionEnum;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Peter.pan
 */
@ManagedBean(name = "options")
@ViewScoped
public class OptionsController extends SessionAwareController implements Serializable {
    private static final long FUNC_OPTION = 38;// 保種記錄關聯
    
    @ManagedProperty(value = "#{genericOptions}")
    private GenericOptionsController genericOptionsController;
    public void setGenericOptionsController(GenericOptionsController genericOptionsController) {
        this.genericOptionsController = genericOptionsController;
    }
    
    @PostConstruct
    private void init(){
        // SessionAwareController.checkAuthorizedByViewId 檢核未通過
        if( functionDenied ){ return; }
        // Get view Id
        viewId = JsfUtils.getViewId();
        logger.debug("viewId = "+viewId);
        
        if( viewId.endsWith("/options.xhtml") ){
            genericOptionsController.setCategory(1);
            // default
            genericOptionsController.getCriteriaVO().setCategoryId(1);
            genericOptionsController.getCriteriaVO().setType(OptionEnum.AREA.getCode());
            genericOptionsController.doQuery();
        }
    }
    
    /**
     * 功能標題
     * @return 
     */
    @Override
    public String getFuncTitle(){
        return sessionController.getFunctionTitle(FUNC_OPTION);
    }
}
