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
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.entity.bpm.TcSignature;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.essential.Persistable;
import com.tcci.fc.entity.org.TcPrincipal;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.role.TcRole;
import com.tcci.fc.facade.role.RoleFacade;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
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
public class BPMEngineFacade {
    private final static ResourceBundle rb = ResourceBundle.getBundle("com.tcci.fc.facade.bpm.messages");
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Resource
    private SessionContext ctx;
    @Inject
    private IBPMEngine bpmEngine;
    @EJB
    private RoleFacade roleFacade;
    
    // 為了支援 transaction annotation
    // 請參考 http://www.adam-bien.com/roller/abien/entry/how_to_self_invoke_ejb
    private BPMEngineFacade self;
    
    @PostConstruct
    private void init() {
        self = ctx.getBusinessObject(BPMEngineFacade.class);
    }
    
    // process APIs
    public TcProcess createProcess(TcUser creator, String processname, Map<String, Object> roleUsers, String subject, Persistable primaryObj) {
        return createProcess(creator, processname, roleUsers, subject, primaryObj, true);
    }
    
    public TcProcess createProcess(TcUser creator, String processname, Map<String, Object> roleUsers, String subject, Persistable primaryObj, boolean startProcess) {
        // 建立 process instance
        TcProcess process = createProcessInstance(creator, processname, primaryObj);
        // 建立 activities, routes
        createActivities(process);
        // 建立 roles 及對應的 users
        createRoleUsers(process, roleUsers);
        // 啟動 process
        if (startProcess) {
            bpmEngine.startProcess(process);
        }
        return process;
    }
    
    public void startProcess(TcProcess process) {
        process = em.find(TcProcess.class, process.getId()); // 確認是最新的資料
        ExecutionStateEnum state = process.getExecutionstate();
        // 只有 NOT_START狀態才可以啟動process
        if (ExecutionStateEnum.NOT_START != state) {
            throw new BPMException(rb.getString("error.processisrunning"));
        }
        // 如果有其它執行中的process也不能啟動
        if (hasProcessRunning(process.getProcessname(), process.getPrimaryobjectclassname(), process.getId())) {
            throw new BPMException(rb.getString("error.anotherprocessisrunning"));
        }
        TcActivity startActivity = findStartActivity(process);
        if (null==startActivity) {
            throw new BPMException(MessageFormat.format(rb.getString("error.nostartactivity"), process.getProcessname()));
        }
        process.setStarttimestamp(new Date());
        process.setExecutionstate(ExecutionStateEnum.RUNNING);
        em.merge(process);
        bpmEngine.startActivity(startActivity);
    }
    
    public void terminateProcess(TcProcess process, String comments, TcUser operator) {
        process = em.find(TcProcess.class, process.getId()); // 確認是最新的資料
        ExecutionStateEnum state = process.getExecutionstate();
        if (ExecutionStateEnum.RUNNING != state) {
            throw new BPMException(rb.getString("error.processisnotrunning"));
        }
        terminateAllActivities(process);
        process.setExecutionstate(ExecutionStateEnum.TERMINATED);
        process.setEndtimestamp(new Date());
        process.setTerminator(operator);
        process.setTerminateReason(comments);
        em.merge(process);
        em.flush(); // 儘早發生 db exception
        engineOnTerminateProcess(process);
    }
    
    public TcProcess findProcess(Long id) {
        return em.find(TcProcess.class, id);
    }
    
    public List<TcProcess> findProcess(ProcessFilter filter) {
        StringBuilder sb = new StringBuilder("SELECT p FROM TcProcess p WHERE 1=1");
        Map<String, Object> params = new HashMap<String, Object>();
        if (filter.getProcessid() != null) {
            sb.append(" AND p.id=:id");
            params.put("id", filter.getProcessid());
        }
        if (filter.getExecutionstate() != null) {
            sb.append(" AND p.executionstate=:executionstate");
            params.put("executionstate", filter.getExecutionstate());
        }
        if (filter.getPrimaryobjectclassname() != null) {
            String str = filter.getPrimaryobjectclassname().trim();
            if (!str.isEmpty()) {
                sb.append(" AND p.primaryobjectclassname=:primaryobjectclassname");
                params.put("primaryobjectclassname", filter.getPrimaryobjectclassname());
            }
        }
        if (filter.getPrimaryobjectid() != null) {
            sb.append(" AND p.primaryobjectid=:primaryobjectid");
            params.put("primaryobjectid", filter.getPrimaryobjectid());
        }
        if (filter.getCreator() != null) {
            sb.append(" AND p.creator=:creator");
            params.put("creator", filter.getCreator());
        }
        if (filter.getDateStart() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(filter.getDateStart());
            truncateTime(cal);
            sb.append(" AND p.starttimestamp>=:dateStart");
            params.put("dateStart", cal.getTime());
        }
        if (filter.getDateEnd()!= null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(filter.getDateEnd());
            cal.add(Calendar.DATE, 1);
            truncateTime(cal);
            sb.append(" AND p.starttimestamp<:dateEnd");
            params.put("dateEnd", cal.getTime());
        }
        sb.append(" ORDER BY p.starttimestamp DESC");
        Query q = em.createQuery(sb.toString());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        return q.getResultList();
    }
    
    public List<ProcessActivityVO> findProcessActivitiesFlow(TcProcess process, boolean taskOnly) {
        // 簡單的排序 (start -> end)
        TcActivity startActivity = findStartActivity(process);
        List<TcActivity> sortedActivities = new ArrayList<TcActivity>();
        sortedActivities.add(startActivity);
        addChildActivities(startActivity.getTcActivityrouteCollection(), sortedActivities);

        List<ProcessActivityVO> listVO = new ArrayList<ProcessActivityVO>();
        for (TcActivity activity : sortedActivities) {
            Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
            if (taskOnly && ActivityTypeEnum.TASK != activity.getActivitytype()) {
                continue;
            }
            // 沒有workitem則顯示role users
            if (workitems.isEmpty()) {
                ProcessActivityVO vo = new ProcessActivityVO(activity);
                Collection<TcUser> roleUsers = roleFacade.getParticipants(TcProcess.class.getCanonicalName(),
                        process.getId(), activity.getRolename());
                if (!roleUsers.isEmpty()) {
                    vo.setOwner(new ArrayList<TcUser>(roleUsers));
                }
                listVO.add(vo);
            } else {
                for (TcWorkitem workitem : workitems) {
                    ProcessActivityVO vo = new ProcessActivityVO(activity);
                    List<TcUser> owner = new ArrayList<TcUser>();
                    owner.add(workitem.getOwner());
                    vo.setOwner(owner);
                    vo.setWorkitem(workitem);
                    listVO.add(vo);
                }
            }
        }
        return listVO;
    }
    
    // activity APIs
    public void startActivity(TcActivity activity) {
        activity = findActivity(activity.getId());
        ActivityTypeEnum activityType = activity.getActivitytype();
        ExecutionStateEnum state = activity.getExecutionstate();
        // AND_GATE, OR_GATE才有可能同時running
        if (ExecutionStateEnum.RUNNING==state
                && ActivityTypeEnum.AND_GATE != activityType
                && ActivityTypeEnum.OR_GATE != activityType ) {
            throw new BPMException(MessageFormat.format(rb.getString("error.activityisrunning"), activity.getActivityname()));
        }
        engineOnStartActivity(activity);
        if (ActivityTypeEnum.START==activityType) {
            startActivity_START(activity);
        } else if (ActivityTypeEnum.END==activityType) {
            startActivity_END(activity);
        } else if (ActivityTypeEnum.TASK==activityType) {
            startActivity_TASK(activity);
        } else if (ActivityTypeEnum.AND_GATE==activityType) {
            startActivity_ANDGATE(activity);
        } else if (ActivityTypeEnum.OR_GATE==activityType) {
            startActivity_ORGATE(activity);
        } else if (ActivityTypeEnum.EXPRESSION_ROBOT==activityType) {
            startActivity_ROBOT(activity);
        } else {
            throw new BPMException(MessageFormat.format(rb.getString("error.activitytypenotsupport"), activityType));
        }
    }
    
    public void terminateActivity(TcActivity activity) {
        activity = findActivity(activity.getId());
        // 停止簽核中的 workitem
        ExecutionStateEnum state = activity.getExecutionstate();
        if (ExecutionStateEnum.RUNNING!=state && ExecutionStateEnum.WAITING!=state) {
            throw new BPMException(MessageFormat.format(rb.getString("error.activityisnotrunning"), activity.getActivityname()));
        }
        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
        if (workitems != null && !workitems.isEmpty()) {
            for (TcWorkitem workitem : workitems) {
                if (ExecutionStateEnum.RUNNING==workitem.getExecutionstate()) {
                    workitem.setExecutionstate(ExecutionStateEnum.TERMINATED);
                    // 被結束, 不要設定結束時間
                    // workitem.setEndtimestamp(new Date());
                    em.merge(workitem);
                }
            }
        }
        activity.setExecutionstate(ExecutionStateEnum.TERMINATED);
        // 要設定結束時間嗎?
        activity.setEndtimestamp(new Date());
        em.merge(activity);
    }

    public void save(TcActivity activity) {
        em.merge(activity);
    }
    
    public TcActivity findActivity(Long id) {
        return em.find(TcActivity.class, id);
    }
    
    // workitem APIs
    public void completeWorkitem(TcWorkitem workitem, String ballot, String routeName, String comments, TcUser operator, boolean allowAgent) {
        workitem = em.find(TcWorkitem.class, workitem.getId());
        if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.workitemisnotrunning"), workitem.getActivityname()));
        }
        if (!allowAgent && !operator.equals(workitem.getOwner())) {
            throw new BPMException(MessageFormat.format(rb.getString("error.notworkitemowner"), workitem.getActivityname()));
        }
        // 記錄簽核
        saveSignature(workitem, ballot, operator, comments);
        // 更新 workitem
        workitem.setBallot(ballot);
        workitem.setEndtimestamp(new Date());
        workitem.setExecutionstate(ExecutionStateEnum.COMPLETED);
        em.merge(workitem);
        em.flush(); // 儘早發生 db exception
        // 更新 route tally
        TcActivity activity = workitem.getActivityid();
        int count = updateTally(activity, routeName);
        // 如果routeName不存在, 且routeName叫"TERMINATE", 結束 process
        if (0==count && ("TERMINATE".equals(routeName) || "reject".equals(routeName))) {
            bpmEngine.terminateProcess(activity.getProcessid(), comments, operator);
        } else if (activity.isOptionRequireAny() || !hasRunningWorkitem(activity)) {
            passRunningWorkitem(activity);
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity.setEndtimestamp(new Date());
            em.merge(activity);
            startNextActivities(activity);
        }
    }

    public void reassign(TcWorkitem workitem, TcUser newOwner, String comments, TcUser operator, boolean allowAgent) {
        workitem = em.find(TcWorkitem.class, workitem.getId());
        if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.workitemisnotrunning"), workitem.getActivityname()));
        }
        if (!allowAgent && !operator.equals(workitem.getOwner())) {
            throw new BPMException(MessageFormat.format(rb.getString("error.notworkitemowner"), workitem.getActivityname()));
        }
        reassign_internal(workitem, newOwner, comments, operator);
    }

    // 簽核中增加簽核人
    public void addActivityWorkitems(TcActivity activity, List<TcUser> users) {
        for (TcUser user : users) {
            addActivityWorkitem(activity, user);
        }
    }
    
    public void addActivityWorkitem(TcActivity activity, TcUser user) {
        ExecutionStateEnum state = activity.getExecutionstate();
        if (ExecutionStateEnum.RUNNING!=state && ExecutionStateEnum.WAITING!=state) {
            throw new BPMException(MessageFormat.format(rb.getString("error.activityisnotrunning"), activity.getActivityname()));
        }
        for (TcWorkitem w : activity.getTcWorkitemCollection()) {
            if (w.getOwner().equals(user)) {
                return;
            }
        }
        TcWorkitem workitem = createWorkitem(activity, user, ExecutionStateEnum.RUNNING);
        em.persist(workitem);
        activity.getTcWorkitemCollection().add(workitem);
        activity.setExecutionstate(ExecutionStateEnum.RUNNING);
        em.merge(activity);
        
        ReassignVO reassign = bpmEngine.autoReassign(workitem);
        if (reassign != null) {
            reassign_internal(workitem, reassign.getNewOwner(), reassign.getComments(), workitem.getOwner());
        } else {
            engineOnWorkitemStartNotification(workitem);
        }
    }
    
    public void addActivitiesBefore(TcWorkitem workitem, List<TcUser> newUsers, boolean string) {
        workitem = em.find(TcWorkitem.class, workitem.getId());
        if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.workitemisnotrunning"), workitem.getActivityname()));
        }
        // 如果已有人簽核，不允許先會
        TcActivity activity = workitem.getActivityid();
        if (hasCompletedWorkitem(activity)) {
            throw new BPMException(MessageFormat.format(rb.getString("error.otherworkitemiscompleted"), activity.getActivityname()));
        }
        //重設 activity, workitem為 NOT_START
        resetActivity(activity);
        
        // 避免roleName重複
        Collection<TcActivity> activities = activity.getProcessid().getTcActivityCollection();
        Set<String> rolenameSet = new HashSet<String>();
        for (TcActivity act : activities) {
            rolenameSet.add(act.getRolename());
        }
        
        TcProcess process = activity.getProcessid();
        TcActivitytemplate template = activity.getActivitytemplateid();
        String newActivityName = activity.getActivityname() + "_先會";
        String rolePrefix = activity.getRolename() + "_Before";
        int idx = 0;
        TcActivity firstAcivity = null; // 第一位是 addActivityBefore, 之後用 addActivityAfter, 要start firstAcivity
        if (string) { // 串簽
            for (TcUser user : newUsers) {
                // 新的roleName
                String newRolename;
                while (true) {
                    idx++;
                    newRolename = rolePrefix + idx;
                    if (rolenameSet.add(newRolename)) {
                        break;
                    }
                }
                // 新增 role
                roleFacade.addRolePrincipal(process, newRolename, user);
                // 新增 activity
                TcActivity newActivity = createTcActivity(process, template);
                newActivity.setOptions(0); // 所有人都要簽，簽過不會pass
                newActivity.setActivityname(newActivityName);
                newActivity.setRolename(newRolename);
                em.persist(newActivity);
                activities.add(newActivity);
                // 修正 activity route
                if (null == firstAcivity) {
                    firstAcivity = newActivity;
                    addActivityBefore(activity, newActivity);
                } else {
                    addActivityAfter(activity, newActivity);
                }
                activity = newActivity;
            }
        } else { // 並簽
            // 新的roleName
            String newRolename;
            while (true) {
                idx++;
                newRolename = rolePrefix + idx;
                if (rolenameSet.add(newRolename)) {
                    break;
                }
            }
            // 新增 role
            roleFacade.addRolePrincipals(process, newRolename, new HashSet<TcPrincipal>(newUsers));
            // 新增 activity
            TcActivity newActivity = createTcActivity(process, template);
            newActivity.setOptions(0); // 所有人都要簽，簽過不會pass
            newActivity.setActivityname(newActivityName);
            newActivity.setRolename(newRolename);
            em.persist(newActivity);
            activities.add(newActivity);
            // 修正 activity route
            if (null == firstAcivity) {
                firstAcivity = newActivity;
                addActivityBefore(activity, newActivity);
            } else {
                addActivityAfter(activity, newActivity);
            }
        }
        em.merge(process); // activities有異動
        bpmEngine.startActivity(firstAcivity);
    }
    
    public void addActivitiesAfter(TcWorkitem workitem, List<TcUser> newUsers, boolean string) {
        workitem = em.find(TcWorkitem.class, workitem.getId());
        if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.workitemisnotrunning"), workitem.getActivityname()));
        }
        TcActivity activity = workitem.getActivityid();
        // 避免roleName重複
        Collection<TcActivity> activities = activity.getProcessid().getTcActivityCollection();
        Set<String> rolenameSet = new HashSet<String>();
        for (TcActivity act : activities) {
            rolenameSet.add(act.getRolename());
        }
        
        TcProcess process = activity.getProcessid();
        TcActivitytemplate template = activity.getActivitytemplateid();
        String newActivityName = activity.getActivityname() + "_後會";
        String rolePrefix = activity.getRolename() + "_After";
        int idx = 0;
        if (string) { // 串簽
            for (TcUser user : newUsers) {
                // 新的roleName
                String newRolename;
                while (true) {
                    idx++;
                    newRolename = rolePrefix + idx;
                    if (rolenameSet.add(newRolename)) {
                        break;
                    }
                }
                // 新增 role
                roleFacade.addRolePrincipal(process, newRolename, user);
                // 新增 activity
                TcActivity newActivity = createTcActivity(process, template);
                newActivity.setOptions(0); // 所有人都要簽，簽過不會pass
                newActivity.setActivityname(newActivityName);
                newActivity.setRolename(newRolename);
                em.persist(newActivity);
                activities.add(newActivity);
                // 修正 activity route
                addActivityAfter(activity, newActivity);
                activity = newActivity;
            }
        } else { // 並簽
            // 新的roleName
            String newRolename;
            while (true) {
                idx++;
                newRolename = rolePrefix + idx;
                if (rolenameSet.add(newRolename)) {
                    break;
                }
            }
            // 新增 role
            roleFacade.addRolePrincipals(process, newRolename, new HashSet<TcPrincipal>(newUsers));
            // 新增 activity
            TcActivity newActivity = createTcActivity(process, template);
            newActivity.setOptions(0); // 所有人都要簽，簽過不會pass
            newActivity.setActivityname(newActivityName);
            newActivity.setRolename(newRolename);
            em.persist(newActivity);
            activities.add(newActivity);
            // 修正 activity route
            addActivityAfter(activity, newActivity);
        }
        em.merge(process); // activities有異動
    }
    
    public void returnToPrevious(TcWorkitem workitem, String comments, TcUser operator, boolean allowAgent) {
        workitem = em.find(TcWorkitem.class, workitem.getId());
        if (ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.workitemisnotrunning"), workitem.getActivityname()));
        }
        if (!allowAgent && !operator.equals(workitem.getOwner())) {
            throw new BPMException(MessageFormat.format(rb.getString("error.notworkitemowner"), workitem.getActivityname()));
        }
        TcActivity currentAct = workitem.getActivityid();
        // 其它 running 改成 not_start (保留其他已簽核資訊)
        for (TcWorkitem wi : currentAct.getTcWorkitemCollection()) {
            if (!wi.equals(workitem) && wi.getExecutionstate()==ExecutionStateEnum.RUNNING) {
                wi.setStarttimestamp(null);
                wi.setExecutionstate(ExecutionStateEnum.NOT_START);
                em.merge(wi);
            }
        }
        //1.save signature
        saveSignature(workitem, "return", operator, comments);
        //2.update workitem
        workitem.setBallot("return");
        workitem.setExecutionstate(ExecutionStateEnum.RETURN);
        workitem.setEndtimestamp(new Date()); // 記錄送出時間
        em.merge(workitem);
        em.flush();
        //3.update tc_activity
        currentAct.setExecutionstate(ExecutionStateEnum.RETURN);
        currentAct.setEndtimestamp(new Date());
        currentAct.setDeadline(null);
        em.merge(currentAct);
        //4.退回到上一層
        returnToPrevious(currentAct);
    }
    
    public List<TcWorkitem> myRunningWorkitems(TcUser owner) {
        String sql="SELECT w FROM TcWorkitem w WHERE w.executionstate=:state AND w.owner=:owner ORDER BY w.starttimestamp DESC";
        Query q = em.createQuery(sql);
        q.setParameter("state", ExecutionStateEnum.RUNNING);
        q.setParameter("owner", owner);
        return q.getResultList();
    }
    
    public List<TcWorkitem> myCompletedWorkitems(TcUser owner) {
        String sql="SELECT w FROM TcWorkitem w WHERE w.executionstate=:state AND w.owner=:owner ORDER BY w.endtimestamp DESC";
        Query q = em.createQuery(sql);
        q.setParameter("state", ExecutionStateEnum.COMPLETED);
        q.setParameter("owner", owner);
        return q.getResultList();
    }
    
    public List<TcWorkitem> allRunningWorkitems() {
        String sql="SELECT w FROM TcWorkitem w WHERE w.executionstate=:state ORDER BY w.starttimestamp DESC";
        Query q = em.createQuery(sql);
        q.setParameter("state", ExecutionStateEnum.RUNNING);
        return q.getResultList();
    }

    public TcWorkitem findWorkitem(Long id) {
        return em.find(TcWorkitem.class, id);
    }
    
    public void updateActivityUsers(TcActivity activity, Object users) {
        TcActivity origAct = activity;
        activity = findActivity(activity.getId());
        validUpdateActivityUser(activity);
        //<< 重設activity狀態, 因為workflow可能做退回, activity狀態可能是PASS
        // 新增user後會造成workflow顯示問題
        if (ExecutionStateEnum.NOT_START != activity.getExecutionstate()) {
            resetActivity(activity);
            origAct.setExecutionstate(ExecutionStateEnum.NOT_START);
            origAct.setStarttimestamp(null);
            origAct.setEndtimestamp(null);
        }
        //>>
        if (users instanceof TcPrincipal) {   // TcUser, TcGroup
            TcPrincipal principal = (TcPrincipal) users;
            roleFacade.updateParticipant(activity.getProcessid(), activity.getRolename(), principal);
        } else if (users instanceof Collection<?>) { // Collection<TcUser>
            Collection<TcPrincipal> principals = (Collection<TcPrincipal>) users;
            roleFacade.updateParticipants(activity.getProcessid(), activity.getRolename(), principals);
        } else {
            throw new BPMException(MessageFormat.format(rb.getString("error.roletypenotsupport"), users.getClass().getCanonicalName()));
        }
    }
    
    public void removeActivityUsers(TcActivity activity) {
        activity = findActivity(activity.getId());
        validUpdateActivityUser(activity);
        roleFacade.removeParticipants(activity.getProcessid(), activity.getRolename());
    }

    // helpers
    private TcProcess createProcessInstance(TcUser creator, String processname, Persistable primaryObj) {
        if (hasProcessRunning(processname, primaryObj.getClass().getCanonicalName(), primaryObj.getId())) {
            throw new BPMException(rb.getString("error.anotherprocessisrunning"));
        }
        Query query = em.createNamedQuery("TcProcesstemplate.findByName");
        query.setParameter("processname", processname);
        List<TcProcesstemplate> procTempList = query.getResultList();
        if (procTempList.isEmpty()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.processtemplatenotfound"), processname));
        } else if (procTempList.size() > 1) {
            throw new BPMException(MessageFormat.format(rb.getString("error.processtemplateduplicate"), processname));
        }
        TcProcesstemplate processTemplate = (TcProcesstemplate) procTempList.get(0);
        TcProcess process = new TcProcess();
        process.setProcesstemplateid(processTemplate);
        process.setProcessname(processname);
        process.setStarttimestamp(new Date());
        process.setCreator(creator);
        process.setPrimaryobjectclassname(primaryObj.getClass().getCanonicalName());
        process.setPrimaryobjectid(primaryObj.getId());
        process.setExecutionstate(ExecutionStateEnum.NOT_START);
        em.persist(process);
        return process;
    }

    private void createActivities(TcProcess process) {
        Collection<TcActivitytemplate> acts = process.getProcesstemplateid().getTcActivitytemplateCollection();
        if (acts == null || acts.isEmpty()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.processtemplateempty"), process.getProcessname()));
        }
        // 建立 activities
        Map<Long, TcActivity> actHT = new HashMap<Long, TcActivity>(); // mapping activityTemplate.id 到實際 activity
        List<TcActivity> activities = new ArrayList<TcActivity>();
        for (TcActivitytemplate activityTemplate : acts) {
            TcActivity activity = createTcActivity(process, activityTemplate);
            activities.add(activity);
            actHT.put(activityTemplate.getId(), activity);
        }
        // 建立 routes
        for (TcActivitytemplate activityTemplate : acts) {
            // 只取 from 的 collection即可
            Collection<TcActivityroutetemplate> actRoutes = activityTemplate.getTcActivityroutetemplateCollection();
            for (TcActivityroutetemplate activityRouteTemplate : actRoutes) {
                // 取得實際的 activity
                TcActivity fromActivity = actHT.get(activityRouteTemplate.getFromactivity().getId());
                TcActivity toActivity = actHT.get(activityRouteTemplate.getToactivity().getId());
                createTcActivityroute(activityRouteTemplate.getRoutename(), fromActivity, toActivity);
            }
        }
        // 更新 process
        process.setTcActivityCollection(activities);
        em.merge(process);
    }

    private void createRoleUsers(TcProcess process, Map<String, Object> roleUsers) {
        for (Map.Entry<String, Object> entry : roleUsers.entrySet()) {
            String roleName = entry.getKey();
            Object users = entry.getValue();
            if (users instanceof TcPrincipal) {   // TcUser, TcGroup
                TcPrincipal principal = (TcPrincipal) users;
                roleFacade.addRolePrincipal(process, roleName, principal);
            } else if (users instanceof Collection<?>) { // Collection<TcUser>
                Collection<TcPrincipal> principals = (Collection<TcPrincipal>) users;
                roleFacade.addRolePrincipals(process, roleName, principals);
            } else if (users instanceof TcRole) { // 已存在的 role
                TcRole tcRole = (TcRole) users;
                roleFacade.addRoleholderrolemap(process, tcRole);
            } else {
                throw new BPMException(MessageFormat.format(rb.getString("error.roletypenotsupport"), users.getClass().getCanonicalName()));
            }
        }
    }

    private TcActivity findStartActivity(TcProcess process) {
        for (TcActivity activity : process.getTcActivityCollection()) {
            if (ActivityTypeEnum.START.equals(activity.getActivitytype())) {
                return activity;
            }
        }
        return null;
    }

    private boolean hasProcessRunning(String processname, String primaryobjectclassname, Long id) {
        String sql = "SELECT p FROM TcProcess p"
                + " WHERE p.executionstate=:executionstate"
                + " AND p.processname=:processname"
                + " AND p.primaryobjectclassname=:primaryobjectclassname"
                + " AND p.primaryobjectid=:primaryobjectid";
        Query q = em.createQuery(sql);
        q.setParameter("executionstate", ExecutionStateEnum.RUNNING);
        q.setParameter("processname", processname);
        q.setParameter("primaryobjectclassname", primaryobjectclassname);
        q.setParameter("primaryobjectid", id);
        q.setMaxResults(1);
        List<TcProcess> list = q.getResultList();
        return !list.isEmpty();
    }

    private void terminateAllActivities(TcProcess process) {
        Collection<TcActivity> acts = process.getTcActivityCollection();
        if (acts != null && !acts.isEmpty()) {
            for (TcActivity activity : acts) {
                ExecutionStateEnum state = activity.getExecutionstate();
                if (ExecutionStateEnum.RUNNING==state || ExecutionStateEnum.WAITING==state) {
                    bpmEngine.terminateActivity(activity);
                }
            }
        }
    }

    private void startActivity_START(TcActivity activity) {
        Date now = new Date();
        activity.setStarttimestamp(now);
        activity.setEndtimestamp(now);
        activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
        em.merge(activity);
        startNextActivities(activity);
    }

    private void startActivity_END(TcActivity activity) {
        Date now = new Date();
        activity.setStarttimestamp(now);
        activity.setEndtimestamp(now);
        activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
        em.merge(activity);
        TcProcess process = activity.getProcessid();
        process.setExecutionstate(ExecutionStateEnum.COMPLETED);
        process.setEndtimestamp(now);
        em.merge(process);
        terminateAllActivities(process);
        engineOnCompleteProcess(process);
    }

    private void startActivity_TASK(TcActivity activity) {
        Date now = new Date();
        activity.setExecutionstate(ExecutionStateEnum.RUNNING);
        activity.setStarttimestamp(now);
        activity.setEndtimestamp(null);
        // 重算 tally
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection();
        if (routes != null) {
            for (TcActivityroute route : routes) {
                if (route.getTally() != 0) {
                    route.setTally(0L);
                    em.merge(route);
                }
            }
        }
        em.merge(activity);
        // 如果沒有workitem待簽核, pass這個activity
        if (!startWorkitem(activity)) {
            if (activity.isOptionRequired() && activity.getTcWorkitemCollection().isEmpty()) {
                activity.setExecutionstate(ExecutionStateEnum.WAITING);
                em.merge(activity);
                engineOnWaitingActivity(activity);
            } else {
                activity.setExecutionstate(ExecutionStateEnum.PASS);
                em.merge(activity);
                startNextActivities(activity);
            }
        }
    }

    private void startActivity_ANDGATE(TcActivity activity) {
        Date now = new Date();
        if (ExecutionStateEnum.RUNNING != activity.getExecutionstate()) {
            activity.setExecutionstate(ExecutionStateEnum.RUNNING);
            activity.setStarttimestamp(now);
            activity.setEndtimestamp(null);
            em.merge(activity);
        }
        // 檢查上層是否全completed
        boolean allCompleted = true;
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection1();
        for (TcActivityroute route : routes) {
            ExecutionStateEnum state = route.getFromactivity().getExecutionstate();
            if (ExecutionStateEnum.RUNNING==state || ExecutionStateEnum.WAITING==state) {
                allCompleted = false;
                break;
            }
        }
        if (allCompleted) {
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity.setEndtimestamp(now);
            em.merge(activity);
            startNextActivities(activity);
        }
    }

    private void startActivity_ORGATE(TcActivity activity) {
        Date now = new Date();
        activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
        activity.setStarttimestamp(now);
        activity.setEndtimestamp(now);
        em.merge(activity);
        // 停掉上層其它執行中的activities
        // TODO: 上層如果也是 AND/OR?
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection1();
        for (TcActivityroute route : routes) {
            ExecutionStateEnum state = route.getFromactivity().getExecutionstate();
            if (ExecutionStateEnum.RUNNING==state || ExecutionStateEnum.WAITING==state) {
                terminateActivity(route.getFromactivity());
            }
        }
        startNextActivities(activity);
    }

    private void startActivity_ROBOT(TcActivity activity) {
        Date now = new Date();
        activity.setStarttimestamp(now);
        activity.setEndtimestamp(null);
        activity.setExecutionstate(ExecutionStateEnum.RUNNING);
        em.merge(activity);
        String route = engineOnExecuteExpressionRobot(activity);
        updateTally(activity, route);
        activity.setEndtimestamp(new Date());
        activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
        em.merge(activity);
        startNextActivities(activity, route);
    }

    private TcActivity createTcActivity(TcProcess process, TcActivitytemplate activityTemplate) {
        TcActivity activity = new TcActivity();
        activity.setProcessid(process);
        activity.setActivityname(activityTemplate.getActivityname());
        activity.setActivitytemplateid(activityTemplate);
        activity.setDuration(activityTemplate.getDuration());
        activity.setActivitytype(activityTemplate.getActivitytype());
        activity.setExpression(activityTemplate.getExpression());
        activity.setOptions(activityTemplate.getOptions());
        activity.setRolename(activityTemplate.getRolename());
        activity.setExecutionstate(ExecutionStateEnum.NOT_START);
        // 先產生空的activityroute, 後面再補上去
        activity.setTcActivityrouteCollection(new ArrayList<TcActivityroute>());
        activity.setTcActivityrouteCollection1(new ArrayList<TcActivityroute>());
        activity.setTcWorkitemCollection(new ArrayList<TcWorkitem>());
        em.persist(activity);
        return activity;
    }

    private void createTcActivityroute(String routeName, TcActivity fromActivity, TcActivity toActivity) {
        TcActivityroute route = new TcActivityroute();
        route.setFromactivity(fromActivity);
        route.setToactivity(toActivity);
        route.setRoutename(routeName);
        route.setTally(0L);
        fromActivity.getTcActivityrouteCollection().add(route);
        toActivity.getTcActivityrouteCollection1().add(route);
        em.persist(route);
        em.merge(fromActivity);
        em.merge(toActivity);
    }

    private void startNextActivities(TcActivity activity) {
        String route = engineOnNextActivtyRoute(activity);
        if ("TERMINATE".equals(route) || "reject".equals(route)) {
            engineOnCompleteActivity(activity, route);
            bpmEngine.terminateProcess(activity.getProcessid(), null, null);
        } else  {
            startNextActivities(activity, route);
        }
    }
    
    private void startNextActivities(TcActivity activity, String route) {
        engineOnCompleteActivity(activity, route);
        int count = 0;
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();
        for (TcActivityroute ar : ars) {
            if ("ALL".equals(route) || ar.getRoutename().equals(route)) {
                count++;
                bpmEngine.startActivity(ar.getToactivity());
            }
        }
        if (0==count) {
            activity.setExecutionstate(ExecutionStateEnum.WAITING);
            em.merge(activity);
            engineOnWaitingActivity(activity);
        }
    }

    private boolean startWorkitem(TcActivity activity) {
        // 如果已有簽核記錄(退回重簽?)，一律重跑簽核
        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
        if (!workitems.isEmpty()) {
            Date now = new Date();
            for (TcWorkitem workitem : workitems) {
                workitem.setBallot(null);
                workitem.setEndtimestamp(null);
                workitem.setExecutionstate(ExecutionStateEnum.RUNNING);
                workitem.setStarttimestamp(now);
                em.merge(workitem);
                ReassignVO reassign = bpmEngine.autoReassign(workitem);
                if (reassign != null) {
                    reassign_internal(workitem, reassign.getNewOwner(), reassign.getComments(), workitem.getOwner());
                } else {
                    engineOnWorkitemStartNotification(workitem);
                }
            }
            return true;
        }
        // 如果有設 autopass, 找出已簽核過的users, 直接PASS
        Set<TcUser> signedUsers = new HashSet<TcUser>();
        if (activity.isOptionAutoPass()) {
            for (TcActivity act : activity.getProcessid().getTcActivityCollection()) {
                for (TcWorkitem workitem : act.getTcWorkitemCollection()) {
                    if (ExecutionStateEnum.COMPLETED==workitem.getExecutionstate()) {
                        signedUsers.add(workitem.getOwner());
                    }
                }
            }
        }
        // 取得目前簽核人員清單
        Collection<TcUser> roleUsers = roleFacade.getParticipants(TcProcess.class.getCanonicalName(),
                activity.getProcessid().getId(), activity.getRolename());
        int workItemCount = 0;
        for (TcUser user : roleUsers) {
            boolean pass = activity.isOptionAutoPass() && signedUsers.contains(user);
            TcWorkitem workitem = pass ?
                    createWorkitem(activity, user, ExecutionStateEnum.PASS) :
                    createWorkitem(activity, user, ExecutionStateEnum.RUNNING);
            em.persist(workitem);
            activity.getTcWorkitemCollection().add(workitem);
            em.merge(activity);
            if (!pass) {
                ReassignVO reassign = bpmEngine.autoReassign(workitem);
                if (reassign != null) {
                    reassign_internal(workitem, reassign.getNewOwner(), reassign.getComments(), workitem.getOwner());
                } else {
                    engineOnWorkitemStartNotification(workitem);
                }
                workItemCount++;
            }
        }
        return workItemCount>0;
    }

    private TcWorkitem createWorkitem(TcActivity activity, TcUser user, ExecutionStateEnum executionStateEnum) {
        TcWorkitem workitem = new TcWorkitem();
        workitem.setActivityname(activity.getActivityname());
        workitem.setActivityid(activity);
        workitem.setOwner(user);
        workitem.setExecutionstate(executionStateEnum);
        workitem.setStarttimestamp(new Date());
        em.persist(workitem);
        return workitem;
    }

    private void saveSignature(TcWorkitem workitem, String ballot, TcUser operator, String comments) {
        TcSignature sigature = new TcSignature();
        sigature.setBallot(ballot);
        if (!operator.equals(workitem.getOwner())) {
            if (null == comments) {
                comments = "(" + operator.getCname() + "代執行)";
            } else {
                comments += "(" + operator.getCname() + "代執行)";
            }
        }
        sigature.setComments(comments);
        sigature.setCreatetimestamp(new Date());
        sigature.setCreator(operator);
        sigature.setPrimaryobjectclassname(workitem.getActivityid().getProcessid().getPrimaryobjectclassname());
        sigature.setPrimaryobjectid(workitem.getActivityid().getProcessid().getPrimaryobjectid());
        sigature.setWorkitem(workitem);
        em.persist(sigature);
        em.flush();
        workitem.getTcSignatureCollection().add(sigature);
        em.merge(workitem);
    }

    private int updateTally(TcActivity activity, String routeName) {
        int count = 0;
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection();
        for (TcActivityroute route : routes) {
            if (route.getRoutename().equals(routeName)) {
                long tally = route.getTally() + 1;
                route.setTally(tally);
                em.merge(route);
                count++;
            }
        }
        return count;
    }

    private boolean hasRunningWorkitem(TcActivity activity) {
        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
        for (TcWorkitem workitem : workitems) {
            if (ExecutionStateEnum.RUNNING==workitem.getExecutionstate()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasCompletedWorkitem(TcActivity activity) {
        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
        for (TcWorkitem workitem : workitems) {
            if (ExecutionStateEnum.COMPLETED==workitem.getExecutionstate()) {
                return true;
            }
        }
        return false;
    }

    private void passRunningWorkitem(TcActivity activity) {
        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
        for (TcWorkitem workitem : workitems) {
            if (ExecutionStateEnum.RUNNING==workitem.getExecutionstate()) {
                workitem.setExecutionstate(ExecutionStateEnum.PASS);
                em.merge(workitem);
            }
        }
    }

    private void addChildActivities(Collection<TcActivityroute> toRoutes, List<TcActivity> sortedActivities) {
        if (null==toRoutes || toRoutes.isEmpty()) {
            return;
        }
        for (TcActivityroute route : toRoutes) {
            TcActivity toActivity = route.getToactivity();
            if (!sortedActivities.contains(toActivity)) {
                sortedActivities.add(toActivity);
                addChildActivities(toActivity.getTcActivityrouteCollection(), sortedActivities);
            }
        }
    }

    private void truncateTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private void addActivityBefore(TcActivity activity, TcActivity newActivity) {
        // 原有上層的 route 改指向 newActivity
        Collection<TcActivityroute> origRoutes = activity.getTcActivityrouteCollection1();
        for (TcActivityroute origRoute : origRoutes) {
            origRoute.setToactivity(newActivity);
            em.merge(origRoute);
            newActivity.getTcActivityrouteCollection1().add(origRoute);
        }
        origRoutes.clear();
        // 補上 newActivity -> activity
        createTcActivityroute("approve", newActivity, activity);
    }

    private void addActivityAfter(TcActivity activity, TcActivity newActivity) {
        // 原有下層的 route 改指向 newActivity
        Collection<TcActivityroute> origRoutes = activity.getTcActivityrouteCollection();
        for (TcActivityroute origRoute : origRoutes) {
            origRoute.setFromactivity(newActivity);
            em.merge(origRoute);
            newActivity.getTcActivityrouteCollection().add(origRoute);
        }
        origRoutes.clear();
        // 補上 activity -> newActivity
        createTcActivityroute("approve", activity, newActivity);
    }

    private void resetActivity(TcActivity activity) {
        activity.setExecutionstate(ExecutionStateEnum.NOT_START);
        activity.setStarttimestamp(null);
        activity.setEndtimestamp(null);
        activity.setDeadline(null);
        em.merge(activity);
        for (TcWorkitem workitem : activity.getTcWorkitemCollection()) {
            workitem.setExecutionstate(ExecutionStateEnum.NOT_START);
            workitem.setStarttimestamp(null);
            workitem.setEndtimestamp(null);
            em.merge(workitem);
        }
    }
    
    private void validUpdateActivityUser(TcActivity activity) {
        ActivityTypeEnum activityType = activity.getActivitytype();
        if (activityType != ActivityTypeEnum.TASK) {
            throw new BPMException(MessageFormat.format(rb.getString("error.activityisnottask"), activity.getActivityname()));
        }
        if (!activity.getTcWorkitemCollection().isEmpty()) {
            throw new BPMException(MessageFormat.format(rb.getString("error.activityisrunning"), activity.getActivityname()));
        }
    }
    
    // 避免 bpmEngine.onXXX 發生 rollback exception, 整個簽核會 rollback
    private void engineOnWorkitemStartNotification(TcWorkitem workitem) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.WorkitemStartNotification)) {
            self.newTxOnWorkitemStartNotification(workitem);
        } else {
            bpmEngine.onWorkitemStartNotification(workitem);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void newTxOnWorkitemStartNotification(TcWorkitem workitem) {
        try {
            bpmEngine.onWorkitemStartNotification(workitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void engineOnCompleteProcess(TcProcess process) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.CompleteProcess)) {
            self.newTxOnCompleteProcess(process);
        } else {
            bpmEngine.onCompleteProcess(process);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void newTxOnCompleteProcess(TcProcess process) {
        try {
            bpmEngine.onCompleteProcess(process);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void engineOnTerminateProcess(TcProcess process) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.TerminateProcess)) {
            self.newTxOnTerminateProcess(process);
        } else {
            bpmEngine.onTerminateProcess(process);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void newTxOnTerminateProcess(TcProcess process) {
        try {
            bpmEngine.onTerminateProcess(process);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void engineOnWaitingActivity(TcActivity activity) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.TerminateProcess)) {
            self.newTxOnWaitingActivity(activity);
        } else {
            bpmEngine.onWaitingActivity(activity);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void newTxOnWaitingActivity(TcActivity activity) {
        try {
            bpmEngine.onWaitingActivity(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String engineOnExecuteExpressionRobot(TcActivity activity) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.TerminateProcess)) {
            return self.newTxOnExecuteExpressionRobot(activity);
        } else {
            return bpmEngine.onExecuteExpressionRobot(activity);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String newTxOnExecuteExpressionRobot(TcActivity activity) {
        try {
            return bpmEngine.onExecuteExpressionRobot(activity);
        } catch (Exception ex) {
            return null;
        }
    }

    private void engineOnStartActivity(TcActivity activity) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.StartActivity)) {
            self.newTxOnStartActivity(activity);
        } else {
            bpmEngine.onStartActivity(activity);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void newTxOnStartActivity(TcActivity activity) {
        try {
            bpmEngine.onStartActivity(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void engineOnCompleteActivity(TcActivity activity, String nextRoute) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.StartActivity)) {
            self.newTxOnCompleteActivity(activity, nextRoute);
        } else {
            bpmEngine.onCompleteActivity(activity, nextRoute);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void newTxOnCompleteActivity(TcActivity activity, String nextRoute) {
        try {
            bpmEngine.onCompleteActivity(activity, nextRoute);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private String engineOnNextActivtyRoute(TcActivity activity) {
        if (bpmEngine.isRequireNewTransaction(BpmEventEnum.TerminateProcess)) {
            return self.newTxOnNextActivtyRoute(activity);
        } else {
            return bpmEngine.onNextActivtyRoute(activity);
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public String newTxOnNextActivtyRoute(TcActivity activity) {
        try {
            return bpmEngine.onNextActivtyRoute(activity);
        } catch (Exception ex) {
            return null;
        }
    }

    private void returnToPrevious(TcActivity currentAct) {
        for (TcActivityroute activityroute : currentAct.getTcActivityrouteCollection1()) {
            TcActivity fromAct = activityroute.getFromactivity(); // 上層activity
            // 如果該層activity沒有workitem(不需簽核或非TASK)則再往上
            if (fromAct.getTcWorkitemCollection().isEmpty()) {
                returnToPrevious(fromAct);
            } else {
                startActivity(fromAct);
            }
        }
    }

    private void reassign_internal(TcWorkitem workitem, TcUser newOwner, String comments, TcUser operator) {
        StringBuilder sb = new StringBuilder();
        sb.append(workitem.getOwner().getCname()).append('→').append(newOwner.getCname());
        if (comments != null && !comments.isEmpty()) {
            sb.append(',').append(comments);
        }
        // 記錄簽核
        saveSignature(workitem, "reassign", operator, sb.toString());
        // 更新 workitem
        workitem.setOwner(newOwner);
        workitem.setStarttimestamp(new Date());
        em.merge(workitem);
        em.flush();
        engineOnWorkitemStartNotification(workitem);
    }

}