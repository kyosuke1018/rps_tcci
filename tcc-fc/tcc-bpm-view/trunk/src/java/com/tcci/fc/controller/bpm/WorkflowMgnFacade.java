/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.controller.bpm;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Greg.Chou
 */
public interface WorkflowMgnFacade {
    //bpmEngineFacade.createProcess(sysId, sysId, requester, "com.tcci.hep.entity.overtime.HepOvertimeMaster", master.getId(), map);
    //public long createProcess(String processTemplateId, String requesterEmpId, Map executionContext) throws WorkflowException;
    
    public HumanTask getTaskById(long taskId);

    public List<String> getActivityRouteNames(long taskId) throws WorkflowException;

    public void completeTask(long taskId, String approverEmpId, String route, String comment) throws WorkflowException;

    public void approveTask(long taskId, String approverEmpId, String comment) throws WorkflowException;

    public void rejectTask(long taskId, String approverEmpId, String comment) throws WorkflowException;

    public void reassignTask(long taskId, String assignerEmpId, String assigneeEmpId, String comment) throws WorkflowException;

    public void recallProcessByTaskId(long taskId, String executorEmpId) throws WorkflowException;

    public void recallProcessByProcessId(long processId, String executorEmpId) throws WorkflowException;

    public List<HumanTask> listOwnedTasks(String ownerEmpId) throws WorkflowException;

    public List<HumanTaskLog> listTaskHistory(String processInstanceId) throws WorkflowException;

    public List<BpmActivity> listAbnormalActivities(String processDefId, Date processStart, Date processEnd, Long formId) throws WorkflowException;

    public void restartAbnormalActivity(BpmActivity activity) throws WorkflowException;
    
    public List<String> listAllProcessType() throws WorkflowException;
}
