/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcActivityroute;
import com.tcci.fc.entity.bpm.TcActivityroutetemplate;
import com.tcci.fc.entity.bpm.TcActivitytemplate;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcSignature;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.role.TcRole;
import com.tcci.fc.entity.role.TcRoleholderrolemap;
import com.tcci.fc.entity.role.TcRoleprincipallink;
import com.tcci.fc.util.BPMException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
@Named
public class TcActivityFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    private ExpressionFacade expressionFacade;
    @EJB
    private TcWorkitemFacade workitemFacade;
    @EJB
    private BPMFacade bpmFacade;

    /*
     * 建立 process activities (包含 activityroutes)
     */
    public void createProcessActivities(TcProcess process) {
        Collection<TcActivitytemplate> acts = process.getProcesstemplateid().getTcActivitytemplateCollection();
        if (acts != null && !acts.isEmpty()) {
            // Generate Process activities
            Map<Long, TcActivity> actHT = new HashMap<Long, TcActivity>(); // mapping activityTemplate.id 到實際 activity
            List<TcActivity> activities = new ArrayList<TcActivity>();
            for (TcActivitytemplate activityTemplate : acts) {
                TcActivity activity = createTcActivity(process, activityTemplate);
                em.persist(activity);
                activities.add(activity);
                actHT.put(activity.getActivitytemplateid().getId(), activity);
            }
            process.setTcActivityCollection(activities);

            // Generate Process activities' routes
            for (TcActivity activity : activities) {
                // 取得ActivityTemplate的TcActivityroutetemplate(fromActivity)
                Collection<TcActivityroutetemplate> actRoutes = activity.getActivitytemplateid().getTcActivityroutetemplateCollection();
                for (TcActivityroutetemplate activityRouteTemplate : actRoutes) {
                    // 取得實際的 fromActivity, toActivity
                    TcActivity fromActivity = actHT.get(activityRouteTemplate.getFromactivity().getId());
                    TcActivity toActivity = actHT.get(activityRouteTemplate.getToactivity().getId());
                    TcActivityroute route = createTcActivityroute(activityRouteTemplate, fromActivity, toActivity);
                    em.persist(route);
                }
            }
            for (TcActivity activity : activities) {
                em.merge(activity); // route已經改變, 重新merge
            }
            em.merge(process);
        } else {
            // TODO: activitytemplate是空的應該warning
            process.setTcActivityCollection(Collections.EMPTY_LIST); // to avoid NPE
            em.merge(process);
        }
    }

    /*
     * 啟動 activity
     */
    public void startActivity(TcActivity activity) {
        ActivityTypeEnum activityType = activity.getActivitytype();
        // AND Gate and OR Gate may be started by other branch
        if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())
                && !ActivityTypeEnum.AND_GATE.equals(activityType)
                && !ActivityTypeEnum.OR_GATE.equals(activityType)) {
            throw new EJBException("BPM engine is unable to start activity, because Bpm activity has been started. Activity("
                    + activity.getActivityname() + ") status=" + activity.getExecutionstate());
        }

        Calendar calNow = Calendar.getInstance();
        Date dateNow = calNow.getTime();
        // =========================================================
        // Update Duration and Deadline
        // If there is a pre-defined Deadline in process template, it has higher priority to use
        // =========================================================
        if (activity.getDuration() != null) {
            Calendar deadline = (Calendar) calNow.clone();
            deadline.add(Calendar.DATE, activity.getDuration().intValue());
            activity.setDeadline(dateNow);
        } else {
            activity.setDeadline(null);
        }
        if (ActivityTypeEnum.START.equals(activityType)) {
            activity.setStarttimestamp(dateNow);
            activity.setEndtimestamp(dateNow);
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            em.merge(activity);
            startNextActivities(activity);
        } else if (ActivityTypeEnum.END.equals(activityType)) {
            activity.setStarttimestamp(dateNow);
            activity.setEndtimestamp(dateNow);
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            em.merge(activity);
            TcProcess process = activity.getProcessid();
            process.setExecutionstate(ExecutionStateEnum.COMPLETED);
            process.setEndtimestamp(dateNow);
            em.merge(process);
            // TODO: 要停掉其它running的activities, workitems嗎?
        } else if (ActivityTypeEnum.TASK.equals(activityType)) {
            activity.setExecutionstate(ExecutionStateEnum.RUNNING);
            activity.setStarttimestamp(dateNow);
            activity.setEndtimestamp(null);
            resetActivity(activity); // 可能重複執行，清掉route tally
            em.merge(activity);
            // 建立workitems, 沒有簽核人時啟動下個activity
            if (workitemFacade.startBpmworkitems(activity) == 0) {
                activity.setExecutionstate(ExecutionStateEnum.PASS);
                em.merge(activity);
                startNextActivities(activity);
            }
        } else if (ActivityTypeEnum.AND_GATE.equals(activityType)) {
            if (!ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
                activity.setExecutionstate(ExecutionStateEnum.RUNNING);
                activity.setStarttimestamp(dateNow);
                activity.setEndtimestamp(null);
            }
            if (areAllActivitiesCompleted(activity)) {
                activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
                activity.setEndtimestamp(dateNow);
            }
            em.merge(activity);
            startNextActivities(activity);
        } else if (ActivityTypeEnum.OR_GATE.equals(activityType)) {
            if (ExecutionStateEnum.NOT_START.equals(activity.getExecutionstate())) {
                activity.setExecutionstate(ExecutionStateEnum.RUNNING);
                activity.setStarttimestamp(dateNow);
                activity.setEndtimestamp(null);
                em.merge(activity);
            }
            if (activityCompletedCount(activity) > 0) {
                activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
                activity.setEndtimestamp(dateNow);
                em.merge(activity);
                terminateRunningFromActivities(activity);
                startNextActivities(activity);
            }
        } else if (ActivityTypeEnum.CONDITION.equals(activityType)) {
            // TODO: 處理 ActivityTypeEnum.CONDITION
        } else if (ActivityTypeEnum.EXPRESSION_ROBOT.equals(activityType)) {
            // TODO: 處理 ActivityTypeEnum.EXPRESSION_ROBOT
            throw new EJBException("TODO: 處理 ActivityTypeEnum.EXPRESSION_ROBOT");
        } else {
            throw new EJBException("unsupported activity type:" + activityType);
        }
    }

    /*
     * 停止 activity (process terminated or OR gateway completed)
     */
    public void terminateActivity(TcActivity activity) {
        if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
            // 停止簽核中的 workitem
            Collection<TcWorkitem> col = activity.getTcWorkitemCollection();
            if (col != null && !col.isEmpty()) {
                for (TcWorkitem workitem : col) {
                    if (ExecutionStateEnum.RUNNING==workitem.getExecutionstate()) {
                        // workitem.setBallot("reject");
                        // workitem.setEndtimestamp(new Date());
                        workitem.setExecutionstate(ExecutionStateEnum.TERMINATED);
                        em.merge(workitem);
                    }
                }
            }
            activity.setExecutionstate(ExecutionStateEnum.TERMINATED);
            activity.setEndtimestamp(new Date());
            activity = em.merge(activity);
        }
    }

    // private helper
    private TcActivity createTcActivity(TcProcess process, TcActivitytemplate activityTemplate) {
        TcActivity activity = new TcActivity();
        activity.setProcessid(process);
        activity.setActivityname(activityTemplate.getActivityname());
        activity.setActivitytemplateid(activityTemplate);
        activity.setDeadline(activityTemplate.getDeadline());
        activity.setDuration(activityTemplate.getDuration());
        activity.setActivitytype(activityTemplate.getActivitytype());
        activity.setExpression(activityTemplate.getExpression());
        activity.setRolename(activityTemplate.getRolename());
        activity.setExecutionstate(ExecutionStateEnum.NOT_START);
        // 先產生空的activityroute, 後面再補上去
        activity.setTcActivityrouteCollection(new ArrayList<TcActivityroute>());
        activity.setTcActivityrouteCollection1(new ArrayList<TcActivityroute>());
        return activity;
    }

    private TcActivityroute createTcActivityroute(TcActivityroutetemplate activityRouteTemplate,
            TcActivity fromActivity, TcActivity toActivity) {
        TcActivityroute route = new TcActivityroute();
        route.setFromactivity(fromActivity);
        route.setToactivity(toActivity);
        route.setRoutename(activityRouteTemplate.getRoutename());
        route.setTally(0L);
        fromActivity.getTcActivityrouteCollection().add(route);
        toActivity.getTcActivityrouteCollection1().add(route);
        return route;
    }

    public void startNextActivities(TcActivity activity) {
//        System.out.println("***** startNextActivities");
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();
        if (ars != null && !ars.isEmpty()) {
            for (TcActivityroute ar : ars) {
                startActivity(ar.getToactivity());
            }
        }
    }

    private void resetActivity(TcActivity activity) {
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection();
        if (routes != null) {
            for (TcActivityroute route : routes) {
                route.setTally(0L);
                em.merge(route);
            }
        }
//        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
//        if (workitems != null) {
//            for (TcWorkitem workitem : workitems) {
//                if (!ExecutionStateEnum.ARCHIVED.equals(workitem.getExecutionstate())) {
//                    workitem.setExecutionstate(ExecutionStateEnum.ARCHIVED);
//                    em.merge(workitem);
//                }
//            }
//        }
    }

    private boolean areAllActivitiesCompleted(TcActivity activity) {
        Collection<TcActivityroute> fromActivities = activity.getTcActivityrouteCollection1();
        if (fromActivities != null && !fromActivities.isEmpty()) {
            for (TcActivityroute route : fromActivities) {
                TcActivity fromActivity = route.getFromactivity();
                // TODO: 為什麼不是檢查 COMPLETED 即可?
                if (ExecutionStateEnum.RUNNING == fromActivity.getExecutionstate()
                        || ExecutionStateEnum.NOT_START == fromActivity.getExecutionstate()
                        || ExecutionStateEnum.HOLD == fromActivity.getExecutionstate()
                        || ExecutionStateEnum.WAITING == fromActivity.getExecutionstate()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int activityCompletedCount(TcActivity activity) {
        int completedCount = 0;
        Collection<TcActivityroute> fromActivities = activity.getTcActivityrouteCollection1();
        if (fromActivities != null && !fromActivities.isEmpty()) {
            for (TcActivityroute route : fromActivities) {
                TcActivity fromActivity = route.getFromactivity();
                if (ExecutionStateEnum.COMPLETED == fromActivity.getExecutionstate()) {
                    completedCount++;
                }
            }
        }
        return completedCount;
    }

    // for OR gateway, terminate other running activities
    // TODO: 有可能再往上層嗎(recursive)?
    private void terminateRunningFromActivities(TcActivity activity) {
        Collection<TcActivityroute> fromRoutes = activity.getTcActivityrouteCollection();
        for (TcActivityroute route : fromRoutes) {
            terminateActivity(route.getFromactivity());
        }
    }

    public List<TcActivitytemplate> getActivityRoleByProcessname(String processname) {
        List<TcActivitytemplate> activitytemplate = new ArrayList<TcActivitytemplate>();
        String sql = "SELECT a FROM TcActivitytemplate a Where a.processtemplateid.processname = :processname"
                + " AND a.activitytype = :activitytype ORDER BY a.id";
        try {
            Query query = em.createQuery(sql);
            query.setParameter("processname", processname);
            query.setParameter("activitytype", ActivityTypeEnum.TASK);
            activitytemplate = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activitytemplate;
    }

    /**
     * 加簽_串簽
     *
     * @param fromActivity
     * @param user
     * @param cosignMethod
     * @return
     */
    public void adhocString(TcActivity fromActivity, List<TcUser> participants, String cosignMethod) throws BPMException {
        //1.create role
        String activityname = "";
        String rolename = "";
        if (cosignMethod.equals("before")) {
            activityname = fromActivity.getActivityname() + "_先會";
            rolename = fromActivity.getRolename() + "_Cosign_Before";
        } else {
            activityname = fromActivity.getActivityname() + "_後會";
            rolename = fromActivity.getRolename() + "_Cosign_After";
        }
        Collection<TcActivity> activities = fromActivity.getProcessid().getTcActivityCollection();
        // 用來避免rolename重複
        Set<String> rolenameSet = new HashSet<String>();
        for (TcActivity act : activities) {
            rolenameSet.add(act.getRolename());
        }
        int idx = 1;
        TcActivity startActivity = null; // 先會的話, 要啟動activity
        for (int i = 0; i < participants.size(); i++) {
            TcUser user = (TcUser) participants.get(i);
            TcRole role = new TcRole();
            while (true) {
                String newRolename = rolename + idx;
                if (rolenameSet.add(newRolename)) {
                    role.setName(newRolename);
                    break;
                }
                idx++;
            }
            TcRoleholderrolemap holder = new TcRoleholderrolemap();
            holder.setHolderclassname(fromActivity.getProcessid().getClass().getCanonicalName());
            holder.setHolderid(fromActivity.getProcessid().getId());
            holder.setRole(role);
            ArrayList<TcRoleholderrolemap> holders = new ArrayList<TcRoleholderrolemap>();
            holders.add(holder);
            role.setTcRoleholderrolemapCollection(holders);
            ArrayList<TcRoleprincipallink> principals = new ArrayList<TcRoleprincipallink>();
            TcRoleprincipallink principallink = new TcRoleprincipallink();
            principallink.setPrincipalclassname(user.getClass().getCanonicalName());
            principallink.setPrincipalid(user.getId());
            principallink.setRole(role);
            principals.add(principallink);

            //4.create tcActivity
            TcActivity toActivity = new TcActivity();
            toActivity.setProcessid(fromActivity.getProcessid());
            // 避免先出現2, 再出現1的情形
            // toActivity.setActivityname(activityname + idx);
            toActivity.setActivityname(activityname);
            toActivity.setRolename(role.getName());
            toActivity.setActivitytype(ActivityTypeEnum.TASK);
            toActivity.setExecutionstate(ExecutionStateEnum.NOT_START);
            toActivity.setDuration(2);
            em.persist(toActivity);
            //5.create tcActivityroute
//            Collection<TcActivityroute> routes = new ArrayList<TcActivityroute>(fromActivity.getTcActivityrouteCollection().size());
            if ("before".equals(cosignMethod) && i == 0) {
                this.addhoc_before(toActivity, fromActivity);
            } else {
                this.addhoc_after(toActivity, fromActivity);
            }

            role.setTcRoleprincipallinkCollection(principals);
            em.persist(role);
            if ("before".equals(cosignMethod) && i == 0) {
                startActivity = toActivity;
            }
            fromActivity = toActivity;
            activities.add(toActivity);
        }
        fromActivity.getProcessid().setTcActivityCollection(activities);
        em.merge(fromActivity.getProcessid());
        if (startActivity != null) {
            startActivity(startActivity);
        }
    }

    /**
     * 加簽_並簽
     *
     * @param fromActivity
     * @param user
     * @return
     */
    public void adhocVertical(TcActivity fromActivity, List<TcUser> participants, String cosignMethod) throws BPMException {
        //1.create role
        String activityname = "";
        String rolename = "";
        if (cosignMethod.equals("before")) {
            activityname = fromActivity.getActivityname() + "_先會";
            rolename = fromActivity.getRolename() + "_Cosign_Before";
        } else {
            activityname = fromActivity.getActivityname() + "_後會";
            rolename = fromActivity.getRolename() + "_Cosign_After";
        }
        Collection<TcActivity> activities = fromActivity.getProcessid().getTcActivityCollection();
        TcRole role = new TcRole();
        role.setName(rolename);
        TcRoleholderrolemap holder = new TcRoleholderrolemap();
        holder.setHolderclassname(fromActivity.getProcessid().getClass().getCanonicalName());
        holder.setHolderid(fromActivity.getProcessid().getId());
        holder.setRole(role);
        ArrayList<TcRoleholderrolemap> holders = new ArrayList<TcRoleholderrolemap>();
        holders.add(holder);
        role.setTcRoleholderrolemapCollection(holders);
        ArrayList<TcRoleprincipallink> principals = new ArrayList<TcRoleprincipallink>();
        for (int i = 0; i < participants.size(); i++) {
            TcUser user = (TcUser) participants.get(i);
            TcRoleprincipallink principallink = new TcRoleprincipallink();
            principallink.setPrincipalclassname(user.getClass().getCanonicalName());
            principallink.setPrincipalid(user.getId());
            principallink.setRole(role);
            principals.add(principallink);
        }
        //4.create tcActivity
        TcActivity toActivity = new TcActivity();
        toActivity.setProcessid(fromActivity.getProcessid());
        toActivity.setActivityname(activityname);
        toActivity.setRolename(rolename);
        toActivity.setActivitytype(ActivityTypeEnum.TASK);
        toActivity.setExecutionstate(ExecutionStateEnum.NOT_START);
        toActivity.setDuration(2);
        em.persist(toActivity);
        //5.create tcActivityroute
//            Collection<TcActivityroute> routes = new ArrayList<TcActivityroute>(fromActivity.getTcActivityrouteCollection().size());
        if ("before".equals(cosignMethod)) {
            this.addhoc_before(toActivity, fromActivity);
        } else {
            this.addhoc_after(toActivity, fromActivity);
        }
//            toActivity.setTcActivityrouteCollection(routes);
//            em.merge(toActivity);
        role.setTcRoleprincipallinkCollection(principals);
        em.persist(role);
        if ("before".equals(cosignMethod)) {
            toActivity.setStarttimestamp(new Date());
            toActivity.setDeadline(new Date());
            em.merge(toActivity);
            workitemFacade.startBpmworkitems(toActivity);
        }
        fromActivity = toActivity;
        activities.add(toActivity);
        fromActivity.getProcessid().setTcActivityCollection(activities);
        em.merge(fromActivity.getProcessid());
//        em.flush();
//        em.refresh(fromActivity.getProcessid());
//        return toActivity;
    }

    /**
     * 用processid 撈取相關的activity
     *
     * @param processid
     * @return
     */
    /*
     public List<TcActivity> findByProcess(Long processid){
     List<TcActivity> activities = new ArrayList<TcActivity>();
     String sql = "SELECT a FROM TcActivity a Where a.processid.id = :processid"
     + " ORDER BY a.id";
     try{
     Query query = em.createQuery(sql);
     query.setParameter("processid", processid);
     activities = query.getResultList();
     }catch(Exception e){
     e.printStackTrace();
     }
     return activities;
     }*/
    public TcActivity findByProcessRolename(Long processid, String rolename) {
        TcActivity activity = new TcActivity();
        String sql = "SELECT a FROM TcActivity a Where a.processid.id = :processid"
                + " AND a.rolename = :rolename ";
        try {
            Query query = em.createQuery(sql);
            query.setParameter("processid", processid);
            query.setParameter("rolename", rolename);
            activity = (TcActivity) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
    }

    public List<TcActivity> findCompletedRole(Long processid) {
        List<TcActivity> activityList = new ArrayList<TcActivity>();
        try {
            Query query = em.createQuery("SELECT a FROM TcActivity a WHERE a.processid.id = :processid and a.participant is not null and a.executionstate = :executionstate order by a.id");
            query.setParameter("processid", processid);
            query.setParameter("executionstate", ExecutionStateEnum.COMPLETED);
            activityList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityList;
    }

    /**
     * 退回至指定步驟
     *
     * @param activity
     */
    public void returnActivity(Long activityid) throws Exception {
        try {
            //1. update tc_activityroute (tally 1→0)
            //2. update tc_activity (executionstate → NOT_START , 指定的activity executionstate →RUNNING)
            //3. update or delete tc_workitem (指定:update , else:delete)

            // update TcActivityroute
            Query query = em.createQuery("SELECT a FROM TcActivityroute a WHERE a.fromactivity.id = :fromactivity ");
            query.setParameter("fromactivity", activityid);
            List routeList = query.getResultList();
//            TcProcess process = new TcProcess();
            if (routeList != null && !routeList.isEmpty()) {
                for (int i = 0; i < routeList.size(); i++) {
                    TcActivityroute activityroute = (TcActivityroute) routeList.get(i);
//                    process = activityroute.getFromactivity().getProcessid();
//                    System.out.println("returnActivity  activityroute.getFromactivity().getId()="+activityroute.getFromactivity().getId());
                    activityroute.setTally(Long.parseLong("0"));
                    activityroute = (TcActivityroute) em.merge(activityroute);
                    em.flush();
                    em.refresh(activityroute);
                    //update TcActivity
                    Query activityQry = em.createQuery("SELECT a FROM TcActivity a WHERE a.id = :activity ");
                    activityQry.setParameter("activity", activityroute.getFromactivity().getId());
                    List activityList = activityQry.getResultList();
                    TcActivity activityTmp = (TcActivity) activityList.get(0);
                    activityTmp.setExecutionstate(ExecutionStateEnum.RUNNING);
                    activityTmp.setDeadline(new Date());
                    activityTmp.setStarttimestamp(new Date());
                    activityTmp.setEndtimestamp(null);
                    activityTmp = (TcActivity) em.merge(activityTmp);
                    em.flush();
                    em.refresh(activityTmp);
                    //delete tc_signature
                    Query itemQry = em.createQuery("SELECT a FROM TcWorkitem a WHERE a.activityid = :activity ");
                    itemQry.setParameter("activity", activityTmp);
                    List itemList = itemQry.getResultList();
                    TcWorkitem workitem = (TcWorkitem) itemList.get(0);
                    Query signatureQry = em.createQuery("SELECT a FROM TcSignature a WHERE a.workitem = :workitem ");
                    signatureQry.setParameter("workitem", workitem);
                    List signatureList = signatureQry.getResultList();
                    if (signatureList != null && !signatureList.isEmpty()) {
                        TcSignature signature = (TcSignature) signatureList.get(0);
                        em.remove(signature);
                        em.flush();
                    }
                    // update TcWorkitem
                    workitem.setTcSignatureCollection(null);
                    workitem.setExecutionstate(ExecutionStateEnum.RUNNING);
                    workitem.setStarttimestamp(new Date());
                    workitem.setEndtimestamp(null);
                    workitem.setBallot(null);
                    workitem = (TcWorkitem) em.merge(workitem);
                    em.flush();
                    em.refresh(workitem);
                    checkRelation(activityroute.getToactivity());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error: ");
        }
    }

    private void checkRelation(TcActivity activity) {
//            System.out.println("---- enter checkRelation activity.id = "+activity.getId());
        Query query = em.createQuery("SELECT a FROM TcActivityroute a WHERE a.fromactivity = :fromactivity ");
        query.setParameter("fromactivity", activity);
        List routeList = query.getResultList();
        if (routeList != null && !routeList.isEmpty()) {
            for (int i = 0; i < routeList.size(); i++) {
                TcActivityroute activityroute = (TcActivityroute) routeList.get(i);
                updateRelation(activityroute);
//                System.out.println("---- enter checkRelation enter for activityroute.getToactivity() = "+activityroute.getToactivity().getId());
                checkRelation(activityroute.getToactivity());
            }
        }
    }

    private void updateRelation(TcActivityroute activityroute) {
        activityroute.setTally(Long.parseLong("0"));
        activityroute = (TcActivityroute) em.merge(activityroute);
        em.flush();
        em.refresh(activityroute);
        //update TcActivity
        Query activityQry = em.createQuery("SELECT a FROM TcActivity a WHERE a.id = :activity ");
        activityQry.setParameter("activity", activityroute.getFromactivity().getId());
        List activityList = activityQry.getResultList();
        TcActivity activityTmp = (TcActivity) activityList.get(0);
        activityTmp.setExecutionstate(ExecutionStateEnum.NOT_START);
        activityTmp.setDeadline(null);
        activityTmp.setStarttimestamp(null);
        activityTmp.setEndtimestamp(null);
        activityTmp = (TcActivity) em.merge(activityTmp);
        em.flush();
        em.refresh(activityTmp);

        // delete TcWorkitem
        Query itemQry = em.createQuery("SELECT a FROM TcWorkitem a WHERE a.activityid = :activity ");
        itemQry.setParameter("activity", activityTmp);
        List itemList = itemQry.getResultList();
        if (itemList != null && !itemList.isEmpty()) {
            TcWorkitem workitem = (TcWorkitem) itemList.get(0);
            //delete tc_signature
            Query signatureQry = em.createQuery("SELECT a FROM TcSignature a WHERE a.workitem = :workitem ");
            signatureQry.setParameter("workitem", workitem);
            List signatureList = signatureQry.getResultList();
            if (signatureList != null && !signatureList.isEmpty()) {
                TcSignature signature = (TcSignature) signatureList.get(0);
                em.remove(signature);
                em.flush();
            }
            em.remove(workitem);
            em.flush();
        }
    }

    public List<TcActivity> findSignList(TcActivity activity) {
        List<TcActivity> activities = new ArrayList<TcActivity>();
        activities = this.findRouteByToActivity(activities, activity);
        return activities;
    }

    public List<TcActivity> findRouteByToActivity(List<TcActivity> activities, TcActivity activity) {
        Query activityrouteQry = em.createQuery("SELECT a FROM TcActivityroute a WHERE a.fromactivity = :activity ");
        activityrouteQry.setParameter("activity", activity);
        List itemList = activityrouteQry.getResultList();
        if (itemList != null && !itemList.isEmpty()) {
            TcActivity toActivity = ((TcActivityroute) itemList.get(0)).getToactivity();
            if (!toActivity.getActivitytype().equals(ActivityTypeEnum.END)) {
                activities.add(toActivity);
                activities = this.findRouteByToActivity(activities, toActivity);
            }
        }
        return activities;

    }

    /*
    public void addhoc_before(TcActivity toActivity, TcActivity fromActivity) {
        toActivity.setExecutionstate(ExecutionStateEnum.RUNNING);
        toActivity = (TcActivity)em.merge(toActivity);
        fromActivity.setDeadline(null);
        fromActivity.setStarttimestamp(null);
        fromActivity.setExecutionstate(ExecutionStateEnum.NOT_START);
        fromActivity = (TcActivity)em.merge(fromActivity);
        for (TcActivityroute route : fromActivity.getTcActivityrouteCollection1()) {
            System.out.println("---- adhocVertical setToactivity= " + route.getToactivity().getId());
            TcActivityroute activityroute = new TcActivityroute();
            activityroute.setFromactivity(toActivity);
            activityroute.setToactivity(route.getToactivity());
            activityroute.setRoutename("approve");
            activityroute.setTally(route.getTally());
            toActivity.getTcActivityrouteCollection().add(activityroute);
            for (TcActivityroute fromroute : fromActivity.getTcActivityrouteCollection()) {
                if (!"approve".equals(fromroute.getRoutename())) {
                    TcActivityroute activityFromroute = new TcActivityroute();
                    activityFromroute.setFromactivity(toActivity);
                    activityFromroute.setToactivity(fromroute.getToactivity());
                    activityFromroute.setRoutename(fromroute.getRoutename());
                    activityFromroute.setTally(fromroute.getTally());
                    toActivity.getTcActivityrouteCollection().add(activityFromroute);
                    em.merge(toActivity);
                    if (fromroute.getRoutename().equals("approve")) {
                        fromroute.setToactivity(toActivity);
                    }
                    em.merge(route);
                }
            }
            em.merge(toActivity);
            System.out.println("---- adhocVertical toActivity= " + toActivity.getId());
            route.setToactivity(toActivity);
            em.merge(route);
        }
    }

    public void addhoc_after(TcActivity toActivity, TcActivity fromActivity) {
        for (TcActivityroute route : fromActivity.getTcActivityrouteCollection()) {
            TcActivityroute activityroute = new TcActivityroute();
            activityroute.setFromactivity(toActivity);
            activityroute.setToactivity(route.getToactivity());
            activityroute.setRoutename(route.getRoutename());
            activityroute.setTally(route.getTally());
            toActivity.getTcActivityrouteCollection().add(activityroute);
            em.merge(toActivity);
//                em.persist(activityroute);
//                routes.add(activityroute);
            if (route.getRoutename().equals("approve")) {
                route.setToactivity(toActivity);
            }
            em.merge(route);
        }
    }
    */

    // fromActivity: 目前的 activity
    // toActivity:   新的 activity
    private void addhoc_before(TcActivity toActivity, TcActivity fromActivity) throws BPMException {
        // 若已有人簽核，不允許先會。
        for (TcWorkitem workitem : fromActivity.getTcWorkitemCollection()) {
            if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
                throw new BPMException("此關已有人簽核，不可執行先會");
            }
        }
        // 修改目前acitvity為 NOT_START
        fromActivity.setDeadline(null);
        fromActivity.setStarttimestamp(null);
        fromActivity.setExecutionstate(ExecutionStateEnum.NOT_START);
        em.merge(fromActivity);
        for (TcWorkitem workitem : fromActivity.getTcWorkitemCollection()) {
            workitem.setExecutionstate(ExecutionStateEnum.NOT_START);
            workitem.setStarttimestamp(null);
            em.merge(workitem);
        }
        // 新增 route (fromActivity -> toActivity)
        TcActivityroute newRoute = new TcActivityroute();
        newRoute.setFromactivity(toActivity);
        newRoute.setToactivity(fromActivity);
        newRoute.setRoutename("approve");
        newRoute.setTally(0L);
        em.persist(newRoute);
        // 原有上層的 route 改指向 toActivity
        Collection<TcActivityroute> origRoutes = fromActivity.getTcActivityrouteCollection1();
        for (TcActivityroute origRoute : origRoutes) {
            origRoute.setToactivity(toActivity);
            em.merge(origRoute);
        }
        Collection<TcActivityroute> coll1 = new ArrayList<TcActivityroute>(origRoutes);
        Collection<TcActivityroute> coll = new ArrayList<TcActivityroute>();
        coll.add(newRoute);
        toActivity.setTcActivityrouteCollection(coll);
        toActivity.setTcActivityrouteCollection1(coll1);
        em.merge(toActivity);
        origRoutes.clear();
        origRoutes.add(newRoute);
        em.merge(fromActivity);
    }

    // fromActivity: 目前的 activity
    // toActivity:   新的 activity
    private void addhoc_after(TcActivity toActivity, TcActivity fromActivity) {
        // 新增 route (fromActivity -> toActivity)
        TcActivityroute newRoute = new TcActivityroute();
        newRoute.setFromactivity(fromActivity);
        newRoute.setToactivity(toActivity);
        newRoute.setRoutename("approve");
        newRoute.setTally(0L);
        em.persist(newRoute);
        // 原有下層的 route 改指向 toActivity
        Collection<TcActivityroute> origRoutes = fromActivity.getTcActivityrouteCollection();
        for (TcActivityroute origRoute : origRoutes) {
            origRoute.setFromactivity(toActivity);
            em.merge(origRoute);
        }
        Collection<TcActivityroute> coll = new ArrayList<TcActivityroute>(origRoutes);
        Collection<TcActivityroute> coll1 = new ArrayList<TcActivityroute>();
        coll1.add(newRoute);
        toActivity.setTcActivityrouteCollection(coll);
        toActivity.setTcActivityrouteCollection1(coll1);
        em.merge(toActivity);
        origRoutes.clear();
        origRoutes.add(newRoute);
        em.merge(fromActivity);
    }
    
}
