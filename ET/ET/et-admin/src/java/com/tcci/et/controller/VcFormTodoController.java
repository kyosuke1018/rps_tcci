/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.controller;

import com.tcci.cm.controller.global.SessionAwareController;
import com.tcci.cm.util.JsfUtils;
import com.tcci.et.model.VenderVO;
import com.tcci.et.entity.EtVcForm;
import com.tcci.et.facade.EtVcFormFacade;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.facade.bpm.IBPMEngine;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 * @author Kyle.Cheng
 */
@ManagedBean(name = "vcFormTodo")
@ViewScoped
public class VcFormTodoController extends SessionAwareController implements Serializable {
    public static final long FUNC_OPTION = 59;
    
    @Inject
    protected IBPMEngine bpmEngine;
    @EJB
    private EtVcFormFacade formFacade;
    
    protected List<VenderVO> forms;
    private Map<VenderVO, TcWorkitem> formMap = new HashMap<>();
    
    @PostConstruct
    private void init() {
        try {
            initValidate();
            reload();
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex.getMessage());
        }
    }
    
    protected void initValidate() throws Exception {
        if(this.getLoginUser()==null){
//        if (!userSession.isValid()) {
            throw new Exception("無登入者資料，請聯絡系統管理員!");
        }
    }
    
    public void reload() {
        formMap.clear();
        forms = new ArrayList<>();
        List<TcWorkitem> workitems = bpmEngine.myRunningWorkitems(this.getLoginUser(), EtVcForm.class);
        logger.info("reload workitems:"+workitems.size());
        if (CollectionUtils.isNotEmpty(workitems)) {
            for (TcWorkitem wo : workitems) {
                VenderVO formVO = formFacade.findById(wo.getActivityid().getProcessid().getPrimaryobjectid(), Boolean.FALSE);
                if (formVO != null) {
                    formMap.put(formVO, wo);
                    forms.add(formVO);
                }
            }
        }
    }
    
    // helper
    public Long getWorkitemId(VenderVO row) {
        return formMap.get(row).getId();
    }
    
    public String formStatus(String status) {
        try {
            return null == status ? null : msgApp.getString("status." + status);
        } catch (Exception ex) {
            return status;
        }
    }

    public List<VenderVO> getForms() {
        return forms;
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
