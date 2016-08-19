/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcActivityroute;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.inject.Default;

/**
 *
 * @author Jimmy.Lee
 */
@Default
public class BPMEngineImpl implements IBPMEngine {

    @EJB
    private BPMEngineFacade engineFacade;

    @Override
    public TcProcess createProcess(TcUser creator, String processname, Map<String, Object> roleUsers, String subject, Persistable primaryObj) {
        return engineFacade.createProcess(creator, processname, roleUsers, subject, primaryObj);
    }

    @Override
    public void startProcess(TcProcess process) {
        engineFacade.startProcess(process);
    }

    @Override
    public void terminateProcess(TcProcess process, String comments, TcUser operator) {
        engineFacade.terminateProcess(process, comments, operator);
    }

    @Override
    public void startActivity(TcActivity activity) {
        engineFacade.startActivity(activity);
    }

    @Override
    public void terminateActivity(TcActivity activity) {
        engineFacade.terminateActivity(activity);
    }

    @Override
    public void completeWorkitem(TcWorkitem workitem, String ballot, String routeName, String comments, TcUser operator, boolean allowAgent) {
        engineFacade.completeWorkitem(workitem, ballot, routeName, comments, operator, allowAgent);
    }
    
    @Override
    public void reassign(TcWorkitem workitem, TcUser newOwner, String comments, TcUser operator, boolean allowAgent) {
        engineFacade.reassign(workitem, newOwner, comments, operator, allowAgent);
    }

    @Override
    public void addActivityWorkitem(TcActivity activity, TcUser user) {
        engineFacade.addActivityWorkitem(activity, user);
    }

    @Override
    public void addActivityWorkitems(TcActivity activity, List<TcUser> users) {
        engineFacade.addActivityWorkitems(activity, users);
    }

    @Override
    public List<TcWorkitem> myRunningWorkitems(TcUser owner) {
        return engineFacade.myRunningWorkitems(owner);
    }

    @Override
    public List<TcWorkitem> myCompletedWorkitems(TcUser owner) {
        return engineFacade.myCompletedWorkitems(owner);
    }

    @Override
    public List<TcWorkitem> allRunningWorkitems() {
        return engineFacade.allRunningWorkitems();
    }

    @Override
    public void onStartActivity(TcActivity activity) {
    }
    
    @Override
    public void onCompleteActivity(TcActivity activity, String nextRoute) {
    }

    @Override
    public String onNextActivtyRoute(TcActivity activity) {
        ActivityTypeEnum actType = activity.getActivitytype();
        if (ActivityTypeEnum.START==actType ||
                ActivityTypeEnum.AND_GATE==actType ||
                ActivityTypeEnum.OR_GATE==actType) {
            return "ALL";
        }
        // 如果 activity state 是 pass (不需人簽時), 走 approve route.
        if (ExecutionStateEnum.PASS==activity.getExecutionstate()) {
            return "approve";
        }
        // 選最多人的 route (回傳 "TERMINATE", "reject" 會terminateProcess, "ALL" 所有的route都啟動)
        String result = null;
        long maxTally = 0;
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection();
        for (TcActivityroute route : routes) {
            long tally = route.getTally();
            if (tally > maxTally) {
                maxTally = tally;
                result = route.getRoutename();
            }
        }
        return result;
    }

    // event 
    @Override
    public boolean isRequireNewTransaction(BpmEventEnum eventEnum) {
        // mail通知一律完成不拋出exception
        return BpmEventEnum.WorkitemStartNotification==eventEnum ? true : false;
    }
    
    @Override
    public void onCompleteProcess(TcProcess process) {
        System.out.println("process(" + process.getId() + ") " + " completed!");
    }

    @Override
    public void onTerminateProcess(TcProcess process) {
        System.out.println("process(" + process.getId() + ") " + " terminated!");
    }

    @Override
    public List<ProcessActivityVO> findProcessActivitiesFlow(TcProcess process, boolean taskOnly) {
        return engineFacade.findProcessActivitiesFlow(process, taskOnly);
    }

    @Override
    public ReassignVO autoReassign(TcWorkitem workitem) {
        return null;
    }
    
    @Override
    public void onWorkitemStartNotification(TcWorkitem workitem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String onExecuteExpressionRobot(TcActivity activity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onWaitingActivity(TcActivity activity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TcProcess findProcess(Long id) {
        return engineFacade.findProcess(id);
    }

    @Override
    public List<TcProcess> findProcess(ProcessFilter filter) {
        return engineFacade.findProcess(filter);
    }

    @Override
    public TcActivity findActivity(Long id) {
        return engineFacade.findActivity(id);
    }

    @Override
    public TcWorkitem findWorkitem(Long id) {
        return engineFacade.findWorkitem(id);
    }

    @Override
    public void updateActivityUsers(TcActivity activity, Object users) {
        engineFacade.updateActivityUsers(activity, users);
    }

    @Override
    public void removeActivityUsers(TcActivity activity) {
        engineFacade.removeActivityUsers(activity);
    }

    @Override
    public void addActivitiesBefore(TcWorkitem workitem, List<TcUser> newUsers) {
        engineFacade.addActivitiesBefore(workitem, newUsers, true);
    }

    @Override
    public void addActivitiesBefore(TcWorkitem workitem, List<TcUser> newUsers, boolean string) {
        engineFacade.addActivitiesBefore(workitem, newUsers, string);
    }

    @Override
    public void addActivitiesAfter(TcWorkitem workitem, List<TcUser> newUsers) {
        engineFacade.addActivitiesAfter(workitem, newUsers, true);
    }

    @Override
    public void addActivitiesAfter(TcWorkitem workitem, List<TcUser> newUsers, boolean string) {
        engineFacade.addActivitiesAfter(workitem, newUsers, string);
    }

    @Override
    public void returnToPrevious(TcWorkitem workitem, String comments, TcUser operator, boolean allowAgent) {
        engineFacade.returnToPrevious(workitem, comments, operator, allowAgent);
    }
    
    @Override
    public void batchNotifyRunningWorkitems() {
        List<TcWorkitem> workitems = allRunningWorkitems();
        Map<TcUser, List<TcWorkitem>> mapUserItems = new HashMap<TcUser, List<TcWorkitem>>();
        for (TcWorkitem workitem : workitems) {
            TcUser owner = workitem.getOwner();
            List<TcWorkitem> userItems = mapUserItems.get(owner);
            if (null==userItems) {
                userItems = new ArrayList<TcWorkitem>();
                mapUserItems.put(owner, userItems);
            }
            userItems.add(workitem);
        }
        for (Map.Entry<TcUser, List<TcWorkitem>> entry : mapUserItems.entrySet()) {
            try {
                onBatchNotifyRunningWorkitems(entry.getKey(), entry.getValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBatchNotifyRunningWorkitems(TcUser owner, List<TcWorkitem> workitems) {
        System.out.println(owner.getCname() + "有下列申請單待簽核:");
        for (TcWorkitem workitem : workitems) {
            StringBuilder sb = new StringBuilder();
            sb.append("id:").append(workitem.getId())
              .append(", ").append(workitem.getActivityname())
              .append(", ").append(workitem.getStarttimestamp());
            System.out.println(sb.toString());
        }
    }

}
