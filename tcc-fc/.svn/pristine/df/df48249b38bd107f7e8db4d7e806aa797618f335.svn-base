/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.controller.bpm;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.org.TcUserFacade;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg.Chou
 */
@ManagedBean
@ViewScoped
public class WorklistController implements Serializable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private HumanTask[] selectedTasks;
    private List<HumanTask> ownedTasks;
    private List<HumanTask> filteredTasks = new ArrayList<HumanTask>();
    private String currentUserEmpId;
    
    @EJB private TcUserFacade tcUserFacade;

    ResourceBundle message = ResourceBundle.getBundle("bpm_messages");

    private String selectedReassignUserId;

    private String[] sysTypes = new String[0];
    private String sysType;

    private String comment;
    
    private String pickUserId;
    private String pickUserInfo;
    
    @ManagedProperty(value="#{tcciWorkflowMgnFacade}")
    private WorkflowMgnFacade workflowFacade;
    public void setWorkflowFacade(WorkflowMgnFacade workflowFacade) {
        this.workflowFacade = workflowFacade;
    }        
    
    /** Creates a new instance of WorklistManagedBean */
    public WorklistController() {
    }

    @PostConstruct
    private void init() {
        sysTypes = loadProcessTypes();
        
        currentUserEmpId = getCurrentUserEmpId();

        ownedTasks = loadOwnedTasks();
        filteredTasks.addAll(ownedTasks);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String getCurrentUserEmpId() {
        String user = "";
        
//        String owner = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("owner");
//        if (owner != null) {
//            user = owner;
//        }
        
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Please implement 'getCurrentUserEmpId()' in 'WorklistController.java'", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        
        return user;
    }

    public List<HumanTask> getOwnedTasks() {
        return ownedTasks;
    }

    public void setOwnedTasks(List<HumanTask> ownedTasks) {
        this.ownedTasks = ownedTasks;
    }

    public HumanTask[] getSelectedTasks() {
        return selectedTasks;
    }

    public void setSelectedTasks(HumanTask[] selectedTasks) {
        this.selectedTasks = selectedTasks;
    }

    public String getSelectedReassignUserId() {
        return selectedReassignUserId;
    }

    public void setSelectedReassignUserId(String selectedUserId) {
        this.selectedReassignUserId = selectedUserId;
    }

    public String getPickUserId() {
        return pickUserId;
    }

    public void setPickUserId(String pickUserId) {
        this.pickUserId = pickUserId;
    }

    public String getPickUserInfo() {
        return pickUserInfo;
    }

    public void setPickUserInfo(String pickUserInfo) {
        this.pickUserInfo = pickUserInfo;
    }

    public String[] getSysTypes() {
        return sysTypes;
    }

    public void setSysTypes(String[] sysTypes) {
        this.sysTypes = sysTypes;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }

    public List<HumanTask> getFilteredTasks() {
        return filteredTasks;
    }

    public void setFilteredTasks(List<HumanTask> filteredTasks) {
        this.filteredTasks = filteredTasks;
    }

    private List<HumanTask> loadOwnedTasks() {
        List<HumanTask> results = new ArrayList<HumanTask>();
        try {
            results = workflowFacade.listOwnedTasks(currentUserEmpId);

            for (HumanTask task: results) {
                String processTimeDisplay = task.getProcessTimeDisplay();
                task.setProcessTimeDisplay(MessageFormat.format(processTimeDisplay, new Object[]{message.getString("common.duration.day"), message.getString("common.hour")}));
            }
        } catch (WorkflowException ex) {
            logger.error("fail to load tasks remotely" , ex);
        }

        return results;
    }

    public void approveTask() {
        processTask("approve");
    }
    public void rejectTask() {
        processTask("reject");
    }

    public void processTask(String action) {
        if (selectedTasks.length == 0) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message.getString("taskmgn.worklist.no_task_selected"), ""));
            return;
        }

        List<String> failList = new ArrayList<String>();
        String actionRCKey = "";

        for (HumanTask task: selectedTasks) {
            try {
                if ("approve".equals(action)) {
                    actionRCKey = "taskmgn.worklist.approve";
                    workflowFacade.approveTask(task.getId(), currentUserEmpId, "");
                }else { // reject
                    actionRCKey = "taskmgn.worklist.reject";
                    workflowFacade.rejectTask(task.getId(), currentUserEmpId, "");
                }

            }catch(WorkflowException e) {
                failList.add(String.valueOf(task.getId()));
            }
        }

        FacesMessage msg;
        if (failList.isEmpty()) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message.getString(actionRCKey) + message.getString("common.success"), "");
        }else {
            String errMsg = message.getString(actionRCKey) + message.getString("common.fail");
            errMsg += ". " + message.getString("common.fail") + message.getString("taskmgn.worklist.taskId") +  ": ";
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errMsg + failList.toString(), "");
            logger.error("fail to {} task. Failed TaskId list: {}", new Object[] {action, failList.toString()});
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        reloadTasks();
    }

    public void reassignTask() {
        if (selectedTasks.length == 0) {
            final FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message.getString("taskmgn.worklist.no_task_selected"), ""));
            return;
        }

        List<String> failList = new ArrayList<String>();

        for (HumanTask task: selectedTasks) {
            try {
                workflowFacade.reassignTask(task.getId(), currentUserEmpId, selectedReassignUserId, "");
            } catch (WorkflowException ex) {
                failList.add(String.valueOf(task.getId()));
            }
        }

        FacesMessage msg;
        if (failList.isEmpty()) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message.getString("taskmgn.worklist.reassign") + message.getString("common.success"), "");
        }else {
            String errMsg = message.getString("taskmgn.worklist.reassign") + message.getString("common.fail");
            errMsg += ". " + message.getString("common.fail") + message.getString("taskmgn.worklist.taskId") +  ": ";
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errMsg + failList.toString(), "");
            logger.error("fail to reassign task. Failed TaskId list: {}", failList.toString());
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        reloadTasks();
    }

    private void reloadTasks() {
        //reload tasks
        ownedTasks.clear();
        ownedTasks = loadOwnedTasks();

        filteredTasks.clear();
        filteredTasks = filterWorklistBySysId(ownedTasks);
    }

    public void filterWorklist(AjaxBehaviorEvent event) {
        filteredTasks.clear();
        filteredTasks = filterWorklistBySysId(ownedTasks);
    }

    private List<HumanTask> filterWorklistBySysId(List<HumanTask> srcTasks) {
        List<HumanTask> results = new ArrayList<HumanTask>();

        if (StringUtils.isEmpty(sysType)) {
            results.addAll(srcTasks);
        }else {
            for (HumanTask task : srcTasks) {
                if (sysType.equals(task.getApplicationId())) {
                    results.add(task);
                }
            }
        }

        return results;
    }
    
    public void checkReassigner() {
        TcUser reassigner = tcUserFacade.findUserByEmpId(this.pickUserId);
        
        if (reassigner == null) {
            this.pickUserInfo = message.getString("taskmgn.worklist.verify_no_employee");
        }else {
            this.pickUserInfo = reassigner.getDisplayIdentifier();
        }       
    }
    
    public void setReassigner() {
        if (!this.pickUserId.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("empCode", this.pickUserId);        
        }
        
        //reset pick-user dialog
        this.pickUserId = "";
        this.pickUserInfo = "";
    }
    
    private String[] loadProcessTypes() {
        try {
            return workflowFacade.listAllProcessType().toArray(new String[0]);
        } catch (WorkflowException ex) {
            logger.error("fail to load processTypes remotely" , ex);
            return new String[0];
        }
    }    
    
    public int getSysTypeLength() {
        return sysTypes.length;
    }
}
