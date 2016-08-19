/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpm;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jimmy.Lee
 */
@ManagedBean(name = "workflowHistory")
@ViewScoped
public class WorkflowHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowHistoryController.class);
    
    private List<HumanTaskLog> taskList = new ArrayList<HumanTaskLog>();

    @ManagedProperty(value="#{tcciWorkflowMgnFacade}")
    private WorkflowMgnFacade workflowFacade;
    public void setWorkflowFacade(WorkflowMgnFacade workflowFacade) {
        this.workflowFacade = workflowFacade;
    }        

    /*
     * Initialization for workflow history preparation
     * this method should be invoked at the beginning if you want to include 'workflowHistory.xhtml'
     */
    public void init(Long processId) {
        try {
            taskList = workflowFacade.listTaskHistory(String.valueOf(processId));
        } catch (Exception ex) {
            logger.error("fail to initialize history by process("+processId+")", ex);
        }
    }

    public List<HumanTaskLog> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<HumanTaskLog> taskList) {
        this.taskList = taskList;
    }
}
