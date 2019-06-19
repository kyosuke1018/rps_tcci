/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpmadmin;

import com.tcci.cm.util.JsfUtils;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.IBPMEngine;
import com.tcci.fc.facade.bpm.ProcessActivityVO;
import com.tcci.fc.facade.org.TcUserFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;

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
    private String users;

    @ManagedProperty(value = "#{roleUsersDialog}")
    private RoleUsersDialog roleUserDialog;
    
    @Inject
    private IBPMEngine bpmEngine;
    @EJB
    private TcUserFacade userFacade;
    
    private ProcessActivityVO editVO;
    private boolean taskOnly = false;
    private transient ResourceBundle rb = ResourceBundle.getBundle("msgBpm",
            FacesContext.getCurrentInstance().getViewRoot().getLocale());
    
    @PostConstruct
    private void init() {
        processId = JsfUtils.getRequestParameter("processId");
        try {
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex, "view process exception!");
        }
    }
    
    // action
    public void startActivity(ProcessActivityVO vo) {
        try {
            bpmEngine.startActivity(vo.getActivity());
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex, "startActivity exception!");
        }
    }
    
    public void terminateActivity(ProcessActivityVO vo) {
        try {
            bpmEngine.terminateActivity(vo.getActivity());
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex, "terminateActivity exception!");
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
            JsfUtils.addErrorMessage(ex, "updateActivityUser exception!");
        }
    }
    
    public void addUsers(ProcessActivityVO vo) {
        editVO = vo;
        users = null;
    }
    
    public void addUsers_OK() {
        try {
            List<TcUser> tcUsers = findUsers();
            bpmEngine.addActivityWorkitems(editVO.getActivity(), tcUsers);
            process = bpmEngine.findProcess(Long.valueOf(processId));
            activities = bpmEngine.findProcessActivitiesFlow(process, taskOnly);
        } catch (Exception ex) {
            JsfUtils.addErrorMessage(ex, "exception");
        }
    }

    // helper
    public String getPageTitle() {
        return "檢視流程(" + processId + ")";
    }
    
    private TcUser findUser(String account) throws Exception {
        String str = StringUtils.trimToEmpty(account).toLowerCase();
        if (str.isEmpty()) {
            throw new Exception("帳號是空的!");
        }
        TcUser user = userFacade.findUserByLoginAccount(str);
        if (null == user) {
            throw new Exception("帳號:" + account + "不存在!");
        }
        return user;
    }

    private List<TcUser> findUsers() throws Exception {
        String[] aryUser = StringUtils.split(users, ',');
        if (aryUser == null || aryUser.length == 0) {
            throw new Exception("帳號是空的!");
        }
        List<TcUser> listUser = new ArrayList<TcUser>();
        for (String str : aryUser) {
            listUser.add(findUser(str));
        }
        return listUser;
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

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
    
    public RoleUsersDialog getRoleUserDialog() {
        return roleUserDialog;
    }

    public void setRoleUserDialog(RoleUsersDialog roleUserDialog) {
        this.roleUserDialog = roleUserDialog;
    }

    public ProcessActivityVO getEditVO() {
        return editVO;
    }

    public void setEditVO(ProcessActivityVO editVO) {
        this.editVO = editVO;
    }

}
