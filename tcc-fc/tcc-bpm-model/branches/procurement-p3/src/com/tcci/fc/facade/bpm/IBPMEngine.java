/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jimmy.Lee
 */
public interface IBPMEngine {
    // flow action
    public TcProcess createProcess(TcUser creator, String processname, Map<String, Object> roleUsers, String subject, Persistable primaryObj);
    public void startProcess(TcProcess process);
    public void terminateProcess(TcProcess process, String comments, TcUser operator);
    public void startActivity(TcActivity activity);
    public void terminateActivity(TcActivity activity);
    public void completeWorkitem(TcWorkitem workitem, String ballot, String routeName, String comments, TcUser operator, boolean allowAgent);
    public void reassign(TcWorkitem workitem, TcUser newOwner, String comments, TcUser operator, boolean allowAgent);
    public void addActivitiesBefore(TcWorkitem workitem, List<TcUser> newUsers); // 先會,串簽
    public void addActivitiesBefore(TcWorkitem workitem, List<TcUser> newUsers, boolean string); // 先會, string true:串簽, false:並簽
    public void addActivitiesAfter(TcWorkitem workitem, List<TcUser> newUsers);
    public void addActivitiesAfter(TcWorkitem workitem, List<TcUser> newUsers, boolean string);
    public void returnToPrevious(TcWorkitem workitem, String comments, TcUser operator, boolean allowAgent);
    // 簽核中的activity增加簽核人
    public void addActivityWorkitem(TcActivity activity, TcUser user);
    public void addActivityWorkitems(TcActivity activity, List<TcUser> users);
    
    // event
    public void onCompleteProcess(TcProcess process);
    public void onTerminateProcess(TcProcess process);
    public void onWorkitemStartNotification(TcWorkitem workitem);
    public void onWaitingActivity(TcActivity activity);
    public void onStartActivity(TcActivity activity);
    public void onCompleteActivity(TcActivity activity, String nextRoute);
    public String onNextActivtyRoute(TcActivity activity); // 回傳下個activity route
    public String onExecuteExpressionRobot(TcActivity activity); // 回傳下個activity route
    
    // helper
    public boolean isRequireNewTransaction(BpmEventEnum eventEnum);
    public List<ProcessActivityVO> findProcessActivitiesFlow(TcProcess process, boolean taskOnly);
    public List<TcWorkitem> myRunningWorkitems(TcUser owner);
    public List<TcWorkitem> myCompletedWorkitems(TcUser owner);
    public List<TcWorkitem> allRunningWorkitems();
    public TcProcess findProcess(Long id);
    public List<TcProcess> findProcess(ProcessFilter filter);
    public TcActivity findActivity(Long id);
    public TcWorkitem findWorkitem(Long id);
    public void updateActivityUsers(TcActivity activity, Object users);
    public void removeActivityUsers(TcActivity activity);
    public ReassignVO autoReassign(TcWorkitem workitem);
    
    // 批次通知running workitems
    public void batchNotifyRunningWorkitems();
    public void onBatchNotifyRunningWorkitems(TcUser owner, List<TcWorkitem> workitems);
    
}
