/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.fc.controller.util.JsfUtil;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.bpm.ProcessActivityVO;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "bpmProcessView")
@ViewScoped
public class BpmProcessView implements RoleUsersDialogListener {
    
    private String processId;
    private TcProcess process;
    private List<ProcessActivityVO> activities;

    @ManagedProperty(value = "#{roleUsersDialog}")
    private RoleUsersDialog roleUserDialog;
    
    @Inject
    private IBPMEngine bpmEngine;
    
    private ProcessActivityVO editVO;
    private boolean taskOnly = false;
    private transient ResourceBundle rb = ResourceBundle.getBundle("msgBpm",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    @PostConstruct
    private void init() {
        processId = JsfUtil.getRequestParameter("processId");
        try {
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "view process exception!");
        }
    }
    
    // action
    public void startActivity(ProcessActivityVO vo) {
        try {
            bpmEngine.startActivity(vo.getActivity());
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "startActivity exception!");
        }
    }
    
    public void terminateActivity(ProcessActivityVO vo) {
        try {
            bpmEngine.terminateActivity(vo.getActivity());
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "terminateActivity exception!");
        }
    }

    public void editRoleUsers(ProcessActivityVO vo) {
        editVO = vo;
        roleUserDialog.init(this, vo.getActivity().getRolename(), null);
    }
    
    public void removeRoleUsers(ProcessActivityVO vo) {
        bpmEngine.removeActivityUsers(vo.getActivity());
        activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
    }
    
    @Override
    public void roleUsersDialog_OK(Object result) {
        try {
            bpmEngine.updateActivityUsers(editVO.getActivity(), result);
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtil.addErrorMessage(ex, "updateActivityUser exception!");
        }
    }

    // helper
    public String getPageTitle() {
        return "檢視流程(" + processId + ")";
    }
    
    // getter, setter
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public TcProcess getProcess() {
        return process;
    }

    public void setProcess(TcProcess process) {
        this.process = process;
    }

    public List<ProcessActivityVO> getActivities() {
        return activities;
    }

    public void setActivities(List<ProcessActivityVO> activities) {
        this.activities = activities;
    }

    public RoleUsersDialog getRoleUserDialog() {
        return roleUserDialog;
    }

    public void setRoleUserDialog(RoleUsersDialog roleUserDialog) {
        this.roleUserDialog = roleUserDialog;
    }

}
