/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.controller.bpm;

import com.tcci.fc.entity.bpm.PrimaryBpmObject;
import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcActivityroute;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.entity.bpm.TcSignature;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.bpm.BPMEngineFacade;
import com.tcci.fc.facade.bpm.BPMFacade;
import com.tcci.fc.facade.bpm.VariableFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Greg.Chou
 */
@ManagedBean(name = "tcciWorkflowMgnFacade")
@ApplicationScoped
public class TcciWorkflowMgnFacade implements WorkflowMgnFacade {

    public static final String DEFAULT_ROUTE_NAME_APPROVE = "approve";
    public static final String DEFAULT_ROUTE_NAME_REJECT = "reject";
    public static final String MESSAGE_PREFIX_BALLOT = "ballot_";
    private static final BigDecimal HOUR_UNIT = new BigDecimal(String.valueOf(1000 * 60 * 60));
    @EJB
    private BPMEngineFacade bpmEngineFacade;
    @EJB
    private BPMFacade bpmFacade;
    @EJB
    private VariableFacade variableFacade;
    @EJB
    private TcUserFacade tcUserFacade;
    private ResourceBundle msg = ResourceBundle.getBundle("bpm_messages");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public HumanTask getTaskById(long taskId) {
        try {
            TcWorkitem workItem = bpmFacade.getWorkitemById(String.valueOf(taskId));
            HumanTask task = new HumanTask();
            task.setStatus(TcciWorkflowStatusMapper.mapStatus(workItem.getExecutionstate()));
            task.setApprover(workItem.getOwner().getEmpId());
            return task;
        } catch (Exception ex) {
            logger.error("[getTaskById]: fail to get task(" + taskId + ")", ex);
            return null;
        }
    }

    @Override
    public void approveTask(long taskId, String approverEmpId, String comment) throws WorkflowException {
        completeTask(taskId, approverEmpId, DEFAULT_ROUTE_NAME_APPROVE, comment);
    }

    @Override
    public void rejectTask(long taskId, String approverEmpId, String comment) throws WorkflowException {
        completeTask(taskId, approverEmpId, DEFAULT_ROUTE_NAME_REJECT, comment);
    }

    @Override
    public void completeTask(long taskId, String approverEmpId, String route, String comment) throws WorkflowException {
        try {
            final TcWorkitem workItem = bpmFacade.getWorkitemById(String.valueOf(taskId));

            putOperatorIntoContext(approverEmpId, workItem);

            bpmEngineFacade.completeTask(workItem, route, comment);
        } catch (Exception ex) {
            logger.error("[completeTask]: fail to '" + route + "' task(" + taskId + ") by '" + approverEmpId + "'", ex);
            throw new WorkflowException(route + " fail");
        }
    }

    @Override
    public void reassignTask(long taskId, String assignerEmpId, String assigneeEmpId, String comment) throws WorkflowException {
        try {
            bpmEngineFacade.reassign(bpmFacade.getWorkitemById(String.valueOf(taskId)), tcUserFacade.findUserByEmpId(assigneeEmpId), comment);
        } catch (Exception ex) {
            logger.error("[reassignTask]: fail to reassign task(" + taskId + ") from '" + assignerEmpId + "' to '" + assigneeEmpId + "'", ex);
            throw new WorkflowException("reassign fail");
        }
    }

    @Override
    public void recallProcessByProcessId(long processId, String executorEmpId) throws WorkflowException {
        try {
            bpmEngineFacade.terminateBpmprocess(bpmFacade.getProcessById(String.valueOf(processId)));
        } catch (Exception ex) {
            logger.error("[recallProcessByProcessId]: fail to recall process(" + processId + ") by '" + executorEmpId + "'", ex);
            throw new WorkflowException("reassign fail");
        }
    }

    @Override
    public void recallProcessByTaskId(long taskId, String executorEmpId) throws WorkflowException {
        try {
            long processId = bpmFacade.getWorkitemById(String.valueOf(taskId)).getActivityid().getProcessid().getId();
            recallProcessByProcessId(processId, executorEmpId);
        } catch (Exception ex) {
            logger.error("[recallProcessByTaskId]: fail to recall process by task(" + taskId + ") by '" + executorEmpId + "'", ex);
            throw new WorkflowException("reassign fail");
        }
    }

    @Override
    public List<HumanTask> listOwnedTasks(String ownerUId) throws WorkflowException {
        List<HumanTask> tasks = new ArrayList<HumanTask>();

        Calendar c = Calendar.getInstance();

        TcUser tcUser = tcUserFacade.findUserByEmpId(ownerUId);
        List<TcWorkitem> workItems = bpmFacade.getRunningWorkitems(tcUser);

        for (TcWorkitem task : workItems) {
            HumanTask wTask = new HumanTask();
            long taskId = task.getId();

            try {
                PrimaryBpmObject obj = (PrimaryBpmObject) bpmFacade.getPrimaryObject(task);
                String formId = String.valueOf(obj.getId());

                String subject = obj.getFormSubject();
                String formURL = obj.getApprovalURL(String.valueOf(taskId));

                wTask.setTaskName(task.getActivityname());
                wTask.setId(taskId);
                wTask.setSubject(subject);
                String sysId = task.getActivityid().getProcessid().getProcessname();
                String sysName = msg.getString(sysId);

                wTask.setApplicationName(sysName);
                wTask.setApplicationId(sysId);
                wTask.setRequester(task.getActivityid().getProcessid().getCreator().getDisplayIdentifier());
                wTask.setFormId(formId);
                wTask.setFormURL(formURL);

                final Date processStart = task.getStarttimestamp();

                wTask.setProcessStart(processStart);

                BigDecimal processHour = calculateProcessHour(processStart);
                wTask.setProcessHour(processHour);

                String duration = calculateDuration(processStart);
                wTask.setProcessTimeDisplay(duration);
                tasks.add(wTask);
            } catch (Exception e) {
                logger.error("fail to load task(" + taskId + ")", e);
            }
        }

        return tasks;
    }

    @Override
    public List<HumanTaskLog> listTaskHistory(String processInstanceId) throws WorkflowException {
        List<HumanTaskLog> tasks = new ArrayList<HumanTaskLog>();

        try {
            //List<TcActivity> activityList = new ArrayList<TcActivity>(bpmFacade.getProcessById(processInstanceId).getTcActivityCollection());
            List<TcActivity> activityList = bpmFacade.getActivityByProcess(bpmFacade.getProcessById(processInstanceId));

//            System.out.println("==========Before Sort==========");
//            for (TcActivity activity: activityList) {
//                System.out.println("("+activity.getId()+")"+activity.getActivityname());
//            }

//            Collections.sort(activityList,
//                           new Comparator<TcActivity>() {
//                                @Override
//                                public int compare(TcActivity o1, TcActivity o2) {
//                                    final Date startTime1 = o1.getStarttimestamp();
//                                    final Date startTime2 = o2.getStarttimestamp();
//
//                                    if (startTime1 == null) return 1;
//
//                                    if (startTime2 == null) return -1;
//
//                                    return startTime1.compareTo(startTime2);
//                                }
//                            });

//            System.out.println("==========After Sort==========");
//            for (TcActivity activity: activityList) {
//                System.out.println("("+activity.getId()+")"+activity.getActivityname());
//            }

            for (TcActivity activity : activityList) {
                if (activity.getActivitytype() == ActivityTypeEnum.TASK
                        && (activity.getExecutionstate() == ExecutionStateEnum.RUNNING
                        || activity.getExecutionstate() == ExecutionStateEnum.COMPLETED
                        || activity.getExecutionstate() == ExecutionStateEnum.TERMINATED)) {
                    HumanTaskLog task = new HumanTaskLog();
                    task.setTaskName(activity.getActivityname());
                    task.setCreator(activity.getParticipant());
                    task.setCreateDatetime(activity.getStarttimestamp());
                    task.setProcessDatetime(activity.getEndtimestamp());
                    task.setStatus(TcciWorkflowStatusMapper.mapStatus(activity.getExecutionstate()));

                    String comment = "";
                    TcUser owner = activity.getParticipant();
                    for (TcWorkitem workitem : activity.getTcWorkitemCollection()) {

                        //get owner from last workitem
                        owner = workitem.getOwner();

                        for (TcSignature signature : workitem.getTcSignatureCollection()) {
                            comment += "[" + msg.getString(MESSAGE_PREFIX_BALLOT + signature.getBallot()) + "] "
                                    + (signature.getComments() == null ? "" : StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(signature.getComments())))
                                    + "<br/>";
                        }
                    }

                    task.setOwner(owner);
                    task.setComment(comment);

                    tasks.add(task);
                }
            }
        } catch (Exception ex) {
            logger.error("fail to load history", ex);
            throw new WorkflowException("fail to load history", ex);
        }

        return tasks;
    }

    @Override
    public List<String> getActivityRouteNames(long taskId) throws WorkflowException {
        try {
            TcWorkitem workItem = bpmFacade.getWorkitemById(String.valueOf(taskId));
            TcActivity tcActivity = workItem.getActivityid();
            Collection<TcActivityroute> tcRoutes = tcActivity.getTcActivityrouteCollection();
            List<String> names = new ArrayList<String>();
            for (TcActivityroute route : tcRoutes) {
                names.add(route.getRoutename());
            }
            return names;            
        } catch (Exception ex) {
            logger.error("[getActivityRoute]: fail to get activity routes by task(" + taskId + ")");
            throw new WorkflowException("fail to get activity routes", ex);
        }
    }

    @Override
    public List<BpmActivity> listAbnormalActivities(String processDefId, Date processCreateStart, Date processCreateEnd, Long formId) throws WorkflowException {
        List<BpmActivity> abnormalActivities = new ArrayList<BpmActivity>();

        for (TcActivity abnormal : getAbnormalActivity(processDefId, processCreateStart, processCreateEnd, formId)) {
            BpmActivity activity = new BpmActivity();
            activity.setId(abnormal.getId());
            activity.setProcessId(abnormal.getProcessid().getId());
            activity.setProcessDefId(abnormal.getProcessid().getProcessname());
            activity.setName(abnormal.getActivityname());
            activity.setType(abnormal.getActivitytype().toString());
            activity.setCreateDate(abnormal.getProcessid().getStarttimestamp());

            final Persistable primaryObject = bpmFacade.getPrimaryObject(abnormal.getProcessid());

            if (primaryObject != null) {
                String formid = String.valueOf(primaryObject.getId());

                String formURL = ((PrimaryBpmObject) primaryObject).getViewURL();

                activity.setFormId(formid);
                activity.setFormURL(formURL);
            } else {
                System.out.println("===No primary object in process - " + abnormal.getProcessid().getId() + "===");
            }

            activity.setStatus(abnormal.getExecutionstate().toString());

            abnormalActivities.add(activity);
        }

        return abnormalActivities;
    }

    @Override
    public void restartAbnormalActivity(BpmActivity activity) throws WorkflowException {
        try {
            TcActivity tcActivity = bpmFacade.getTcActivityById(String.valueOf(activity.getId()));
            bpmEngineFacade.triggerActivity(tcActivity);
        } catch (Exception ex) {
            throw new WorkflowException("fail to restart activity", ex);
        }
    }

    private BigDecimal calculateProcessHour(final Date processStart) {
        Calendar today = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(processStart);

        BigDecimal totalMillis = new BigDecimal(String.valueOf(today.getTimeInMillis() - startDate.getTimeInMillis()));
        final BigDecimal processHour = totalMillis.divide(HOUR_UNIT, 1, RoundingMode.HALF_UP);
        return processHour;
    }

    private String calculateDuration(Date processStart) {
        Calendar today = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(processStart);

        String duration = DurationFormatUtils.formatDuration(today.getTimeInMillis() - startDate.getTimeInMillis(), "d{0} HH{1}");

        return duration;
    }

    private void putOperatorIntoContext(String approverEmpId, final TcWorkitem workItem) {
        HashMap executionContext = new HashMap();
        executionContext.put("operator", tcUserFacade.findUserByEmpId(approverEmpId));
        variableFacade.createVariables(workItem.getActivityid().getProcessid(), executionContext);
    }

    private List<TcActivity> getAbnormalActivity(String processName, Date processCreateStart, Date processCreateEnd, Long objId) {
        final List<TcProcess> processes = bpmFacade.getProcesses(processName, ExecutionStateEnum.RUNNING.toString(), processCreateStart, processCreateEnd, objId);

        return getAbnormalActivityByProcess(processes);
    }

    private List<TcActivity> getAbnormalActivityByProcess(final List<TcProcess> abnormalProcesses) {
        List<TcActivity> results = new ArrayList<TcActivity>();
        for (TcProcess process : abnormalProcesses) {
            int numOfRunningActivity = 0;

            List<TcActivity> noneRunActivity = new ArrayList<TcActivity>();
            for (TcActivity activity : bpmFacade.getActivityByProcess(process)) {
                if (activity.getExecutionstate() == ExecutionStateEnum.RUNNING) {
                    numOfRunningActivity++;
                }
                if (activity.getExecutionstate() == ExecutionStateEnum.NOT_START
                        || activity.getExecutionstate() == ExecutionStateEnum.HOLD) {
                    noneRunActivity.add(activity);
                }
            }

            if (numOfRunningActivity == 0) {
                //abnormal process is found, add none-running activity to list
                results.addAll(noneRunActivity);
            }
        }
        return results;
    }
    
    @Override
    public List<String> listAllProcessType() throws WorkflowException {
        List<String> processTypes = new ArrayList<String>();
        
        for (TcProcesstemplate p: bpmFacade.getProcessTemplates()) {
            processTypes.add(p.getProcessname());
        }
        
        return processTypes;
    }    
}
