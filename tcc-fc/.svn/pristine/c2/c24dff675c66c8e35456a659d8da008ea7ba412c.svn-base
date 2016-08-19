/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.controller.bpm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg.Chou
 */

@ManagedBean
@ViewScoped
public class AbnormalActivityMgnController implements Serializable{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String processDefId;
    private Long formId;
    private Date createDateBegin;
    private Date createDateEnd;

    private List<BpmActivity> abnormalActivities = new ArrayList<BpmActivity>();
    private BpmActivity[] selectedActivities;
    private BpmActivity selectedActivity;

    ResourceBundle message = ResourceBundle.getBundle("bpm_messages");
    
    @ManagedProperty(value="#{tcciWorkflowMgnFacade}")
    private WorkflowMgnFacade workflowFacade;
    public void setWorkflowFacade(WorkflowMgnFacade workflowFacade) {
        this.workflowFacade = workflowFacade;
    }        

    /** Creates a new instance of AbnormalActivityMgnManagedBean */
    public AbnormalActivityMgnController() {
    }

    @PostConstruct
    private void init() {
    }

    private List<BpmActivity> loadAbnormalActivities() {
        List<BpmActivity> results = new ArrayList<BpmActivity>();

        try {
            results = workflowFacade.listAbnormalActivities(processDefId, createDateBegin, createDateEnd==null?null:DateUtils.addDays(createDateEnd, 1), formId);
        } catch (WorkflowException ex) {
            logger.error("fail to load tasks remotely" , ex);
        }

        return results;
    }

    public String getProcessDefId() {
        return processDefId;
    }

    public void setProcessDefId(String processDefId) {
        this.processDefId = processDefId;
    }

    public List<BpmActivity> getAbnormalActivities() {
        return abnormalActivities;
    }

    public void setAbnormalActivities(List<BpmActivity> abnormalActivities) {
        this.abnormalActivities = abnormalActivities;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public BpmActivity[] getSelectedActivities() {
        return selectedActivities;
    }

    public void setSelectedActivities(BpmActivity[] selectedActivities) {
        this.selectedActivities = selectedActivities;
    }

    public BpmActivity getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(BpmActivity selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    public Date getCreateDateBegin() {
        return createDateBegin;
    }

    public void setCreateDateBegin(Date createDateBegin) {
        this.createDateBegin = createDateBegin;
    }

    public Date getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(Date createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public void resendTask() {
        processTask("resend");
    }

    public void cancelTask() {
        processTask("cancel");
    }

    public void forceCompleteTask() {
        processTask("forceComplete");
    }

    private void processTask(String actionType) {
        String actionRCKey = "";
        List<String> failList = new ArrayList<String>();

        //for (BpmActivity activity: selectedActivities) {
            try {
                if ("resend".equals(actionType)) {
                    actionRCKey = "taskmgn.abnormaltaskmgn.resend";
                    workflowFacade.restartAbnormalActivity(selectedActivity);
                }else {
//                    taskMgnFacade.forceCompleteFailedTask(failedTask.getWorkItemId());
//                    actionRCKey = "taskmgn.failedtaskmgn.batch_forcecomplete";
                }
            } catch (WorkflowException ex) {
                failList.add(String.valueOf(selectedActivity.getId()));
            }
        //}

        FacesMessage msg;
        if (failList.isEmpty()) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message.getString(actionRCKey) + message.getString("common.success"), "");
        }else {
            String errMsg = message.getString(actionRCKey) + message.getString("common.fail");
            errMsg += ". " + message.getString("common.fail") + ". ActivityId: ";
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errMsg + failList.toString(), "");
            
            logger.error("fail to {} task. Failed TaskId list: {}", new Object[]{actionType, failList.toString()});
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);

        reloadTasks();
    }

    public void reloadTasks() {
        //reload tasks
        abnormalActivities.clear();
        abnormalActivities = loadAbnormalActivities();
    }
}
