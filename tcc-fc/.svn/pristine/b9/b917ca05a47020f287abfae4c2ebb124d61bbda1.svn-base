/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcActivityroute;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcSignature;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.role.TcRole;
import com.tcci.fc.entity.role.TcRoleprincipallink;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.role.RoleFacade;
import com.tcci.fc.util.BPMException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sam.Lin
 */
@Stateless
@Named
public class TcWorkitemFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @EJB
    private RoleFacade roleFacade;
    @EJB
    private TcActivityFacade activityFacade;
    @EJB
    private TcProcessFacade processFacade;
    @EJB
    private TcUserFacade userFacade;
    @Inject
    private Event<TcWorkitem> workitemEvent;

    public TcWorkitemFacade() {
    }

    /**
     * create new workitem
     *
     * @param activity
     * @return
     * @throws Exception
     */
    public int startBpmworkitems(TcActivity activity) {
        // 如果已有簽核記錄(退回重簽?)，一律重跑簽核
        Collection<TcWorkitem> workitems =activity.getTcWorkitemCollection();
        if (workitems != null & !workitems.isEmpty()) {
            for (TcWorkitem workitem : workitems) {
                workitem.setBallot(null);
                workitem.setEndtimestamp(null);
                workitem.setExecutionstate(ExecutionStateEnum.RUNNING);
                workitem.setStarttimestamp(new Date());
                em.merge(workitem);
                sendWorkitemNotification(workitem);
            }
            return workitems.size();
        }

        // 找出已簽核過的users, 此次簽核直接PASS
        Set<TcUser> signedUsers = new HashSet<TcUser>();
        for (TcActivity act : activity.getProcessid().getTcActivityCollection()) {
            for (TcWorkitem workitem : act.getTcWorkitemCollection()) {
                if (ExecutionStateEnum.COMPLETED==workitem.getExecutionstate()) {
                    signedUsers.add(workitem.getOwner());
                }
            }
        }

        // 取得目前簽核人員清單
        Collection<TcUser> roleUsers = roleFacade.getParticipants(TcProcess.class.getCanonicalName(),
                activity.getProcessid().getId(), activity.getRolename());
        int workItemCount = 0;
        for (TcUser user : roleUsers) {
            boolean signed = signedUsers.contains(user);
            TcWorkitem workitem = signed ?
                    newBpmworkitem(activity, user, ExecutionStateEnum.PASS) :
                    newBpmworkitem(activity, user, ExecutionStateEnum.RUNNING);
            em.persist(workitem);
            activity.getTcWorkitemCollection().add(workitem);
            em.merge(activity);
            if (!signed) {
                sendWorkitemNotification(workitem);
                workItemCount++;
            }
        }
        return workItemCount;
    }

    private TcWorkitem newBpmworkitem(TcActivity activity, TcUser participant, ExecutionStateEnum status) {
        TcWorkitem wi = new TcWorkitem();
        wi.setActivityname(activity.getActivityname());
        wi.setActivityid(activity);
        wi.setOwner(participant);
        wi.setExecutionstate(status);
        wi.setStarttimestamp(new Date());
        /* 不用特別處理
        if (status.equals(ExecutionStateEnum.PASS)) {
            wi.setEndtimestamp(wi.getStarttimestamp());
            wi.setBallot("pass");
        }
        */
        return wi;
    }

    /**
     * To send notification for a workitem. The method can be re-used for
     * repeatly reminding email
     *
     * @param wi Bpmworkitem The notification for the workitem
     * @return return void
     */
    public void sendWorkitemNotification(TcWorkitem wi) {
        /*
        try {
            TcUser tcUser = wi.getOwner();
            TcActivity tcActivity = wi.getActivityid();
            TcProcess tcProcess = tcActivity.getProcessid();
            HashMap map = variableFacade.getVariables(tcProcess);
            MailBean bean = new MailBean();
            bean.setTemplateName("approval.html");
            bean.setActorId(tcUser.getDisplayIdentifier());
            String reason = (String) map.get("reason");
            if (reason != null) {
                reason = reason.replaceAll(System.getProperty("line.separator"), "<br>");
            }
            bean.setReason(reason);
            bean.setRequester(tcProcess.getCreator().getDisplayIdentifier());

            bean.setSubject("表單簽核");

            bean.setEmailAddress(tcUser.getEmail());
            bean.setHostId(InetAddress.getLocalHost().getHostAddress());
            bean.setOid(String.valueOf(tcProcess.getPrimaryobjectid()));
            bean.setTaskId(wi.getId());
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            MimeMessage msg = new MimeMessage(mailSession);
            try {
                mbp.setContent(VelocityTemplateUtils.getContent(bean, bean.getTemplateName()), "text/html;charset=utf-8");
                mp.addBodyPart(mbp);
                msg.setContent(mp);
                msg.setSubject(bean.getSubject(), "UTF-8");
                msg.setRecipient(Message.RecipientType.TO, new javax.mail.internet.InternetAddress(bean.getEmailAddress()));
                Transport.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(BPMEngineFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        workitemEvent.fire(wi);
    }

    /**
     * To complete a workitem with signature
     *
     * @param WorkItem
     * @param routeName String The route which participant seleted. If there is
     * only one route, route name is ignored.
     * @param vars Hashtable The variables which participant want to update
     * @return return true, if the workitem is completed.
     */
    public boolean completeTask(TcWorkitem workitem, String routeName, String comments, boolean all) throws BPMException {
        boolean result = false;
        try {
            saveSignature(workitem, routeName, comments);
            _workComplete(workitem, routeName, all);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BPMException("Unable to complete work item", e);
        }
        return result;
    }

    public TcSignature saveSignature(TcWorkitem workitem, String ballot, String comment) {
        TcSignature sigature = new TcSignature();
        sigature.setBallot(ballot);
        sigature.setComments(comment);
        sigature.setCreatetimestamp(new Date());
        sigature.setCreator(workitem.getOwner());
        sigature.setPrimaryobjectclassname(workitem.getActivityid().getProcessid().getPrimaryobjectclassname());
        sigature.setPrimaryobjectid(workitem.getActivityid().getProcessid().getPrimaryobjectid());
        sigature.setWorkitem(workitem);

        em.persist(sigature);
        workitem.getTcSignatureCollection().add(sigature);
        em.merge(workitem);
        return sigature;
    }

    /*
     * all: true 全部簽核完才繼續
     *   全部是rejct時 -> terminate process
     *   繼續下個 activity
     * all: false 立即繼續
     *   其它workitem -> pass
     *   reject -> terminate process
     *   繼續下個 activity
     */
    private void _workComplete(TcWorkitem workitem, String routeName, boolean all) throws BPMException {
        if (!addTally(workitem, routeName) && !"reject".equals(routeName)) {
            throw new BPMException("Route Name: " + routeName + " is invalid");
        }
        boolean terminated = false;
        boolean goNext = false;
        workitem.setEndtimestamp(Calendar.getInstance().getTime());
        workitem.setExecutionstate(ExecutionStateEnum.COMPLETED);
        workitem.setBallot(routeName);
        TcActivity activity = workitem.getActivityid();
        if (all) {
            boolean hasRunning = false;
            boolean anyReject = false;
            for (TcWorkitem wi : activity.getTcWorkitemCollection()) {
                if (wi.getExecutionstate()==ExecutionStateEnum.RUNNING) {
                    hasRunning = true;
                }
                if ("reject".equals(wi.getBallot())) {
                    anyReject = true;
                }
            }
            if (anyReject) {
                terminated = true;
            } else if (!hasRunning) {
                goNext = true;
            }
        } else {
            // 其它running workitem設為PASS(n/a)
            for (TcWorkitem wi : activity.getTcWorkitemCollection()) {
                if (wi.getExecutionstate()==ExecutionStateEnum.RUNNING) {
                    wi.setExecutionstate(ExecutionStateEnum.PASS);
                    em.merge(wi);
                }
            }
            if ("reject".equals(routeName)) {
                terminated = true;
            } else {
                goNext = true;
            }
        }
        if (terminated) {
            // 簽核人仍是 complete, 其他人是TERMINATED
            // workitem.setExecutionstate(ExecutionStateEnum.TERMINATED);
            // em.merge(workitem);
            processFacade.terminateProcess(activity.getProcessid());
        } else if (goNext) {
            em.merge(workitem);
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            em.merge(activity);
            activityFacade.startNextActivities(activity);
        } else {
            em.merge(workitem);
        }
    }

    private TcWorkitem _mergeAndValidateWorkitem(TcWorkitem workitem) throws BPMException {
        workitem = em.merge(workitem);
        em.refresh(workitem);

        if (ExecutionStateEnum.WAITING.equals(workitem.getExecutionstate())) {
            throw new BPMException("BPM Engine is unable to complete task. The workitem state is " + workitem.getExecutionstate()
                    + ". It may have running Ad Hoc tasks.");
        } else if (!ExecutionStateEnum.RUNNING.equals(workitem.getExecutionstate())) {
            throw new BPMException("BPM Engine is unable to complete task, because workitem state is " + workitem.getExecutionstate());
        }
        return workitem;
    }

    /**
     * To check whether all workitems of the activity have been completed or
     * terminated
     *
     * @param activity Bpmactivity The activity which is trying to be completed
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its
     * rate of the activity
     * @return return boolean Return true, if all workitems have been completed.
     * Return false, if there is any RUNNING workitem(s).
     */
    private boolean addTally(TcWorkitem workitem, String routeName) throws BPMException {
        boolean isValid = false;

        if (workitem == null) {
            throw new BPMException("BPM Engine is unable to check route name, because Workitem is null");
        }
        if (routeName.equals("partApprove")) {
            routeName = "approve";
        }
        Collection<TcActivityroute> routes = workitem.getActivityid().getTcActivityrouteCollection();
        if (routes != null) {
            for (TcActivityroute route : routes) {
                if (route.getRoutename().equals(routeName)) {
                    isValid = true;
                    long tally = route.getTally() + 1;
                    route.setTally(tally);
                    em.merge(route);
                }
            }
        }
        return isValid;
    }

    public TcWorkitem getWorkitemById(Long workItemId) throws BPMException {
        if (workItemId == null) {
            throw new BPMException("Error: BPM Engine is unable to find the workitem, because workitem id is null!");
        }
        return em.find(TcWorkitem.class, workItemId);
    }

    public TcWorkitem findByActivityOwner(TcActivity activity, TcUser owner) {
        TcWorkitem workitem = null;
        Query query = em.createQuery("select b from TcWorkitem b where b.activityid =:activityid and b.owner = :owner"); // .setHint("refresh", "true");
        query.setParameter("activityid", activity);
        query.setParameter("owner", owner);
        List workitemList = query.getResultList();
        if (workitemList != null && !workitemList.isEmpty()) {
            workitem = (TcWorkitem) workitemList.get(0);
        }
        return workitem;
    }

    public Map<TcUser, String> findExistWorkitemOwner(TcProcess process) {
        Map<TcUser, String> workitemMap = new HashMap<TcUser, String>();
        for (TcActivity activity : process.getTcActivityCollection()) {
            Query query = em.createQuery("select b from TcWorkitem b where b.activityid =:activityid and b.executionstate =:executionstate "); 
            query.setParameter("activityid", activity);
            query.setParameter("executionstate", ExecutionStateEnum.COMPLETED);
            List<TcWorkitem> workitemList = query.getResultList();
            if (workitemList != null && !workitemList.isEmpty()) {
                for (TcWorkitem workitem : workitemList) {
                    workitemMap.put(workitem.getOwner(), "Exist");
                }
            }
        }
        return workitemMap;
    }

    public void reassign(Long workitemId, TcUser newOwner) throws BPMException {
        reassign(workitemId, newOwner, null);
    }
    
    public void reassign(Long workitemId, TcUser newOwner, String comment) throws BPMException {
        TcWorkitem workitem = em.find(TcWorkitem.class, workitemId);
        if (null==workitem || ExecutionStateEnum.RUNNING != workitem.getExecutionstate()) {
            throw new BPMException("reassign failed!");
        }
        // Jimmy, 改派應該不用改roleprincipallink
        //1.更改roleprincipallink 
//        TcRole role = roleFacade.getRoleHolderRole(workitem.getActivityid().getProcessid(), workitem.getActivityid().getRolename());
//        for (TcRoleprincipallink principal : role.getTcRoleprincipallinkCollection()) {
//            if (principal.getPrincipalid().equals(workitem.getOwner().getId())) {
//                principal.setPrincipalid(newOwner.getId());
//                principal = (TcRoleprincipallink) em.merge(principal);
//            }
//        }
        StringBuilder sb = new StringBuilder();
        sb.append(workitem.getOwner().getCname()).append('→').append(newOwner.getCname());
        if (comment != null) {
            sb.append(',').append(comment);
        }
        //2.更改TcWorkitem owner及開始日期
        workitem.setOwner(newOwner);
        workitem.setStarttimestamp(new Date());
        //3.create signature
        saveSignature(workitem, "ReassignTask", sb.toString());
        sendWorkitemNotification(workitem);
    }

    /**
     * 退回前activities(可能一個或多個)
     * note: 若目前activity已有人核准則不可以退回
     *       若前一個
     * @param workitem
     */
    public void RoutePrevious(TcWorkitem workitem, String comments) throws BPMException {
        TcActivity currentAct = workitem.getActivityid();
        for (TcWorkitem wi : currentAct.getTcWorkitemCollection()) {
            if (!wi.equals(workitem) && ExecutionStateEnum.COMPLETED==wi.getExecutionstate() && "approve".equals(wi.getBallot())) {
                throw new BPMException("some one already approved!");
            }
        }
        // 其它 running 改成 not_start
        for (TcWorkitem wi : currentAct.getTcWorkitemCollection()) {
            if (!wi.equals(workitem) && wi.getExecutionstate()==ExecutionStateEnum.RUNNING) {
                wi.setStarttimestamp(null);
                wi.setExecutionstate(ExecutionStateEnum.NOT_START);
                em.merge(wi);
            }
        }
        //1.save signature
        saveSignature(workitem, ExecutionStateEnum.RETURN.getName().toLowerCase(), comments);
        //2.update workitem
        workitem.setExecutionstate(ExecutionStateEnum.RETURN);
        workitem.setEndtimestamp(new Date()); // 保留送出時間
        em.merge(workitem);
        //3.update tc_activity
        currentAct.setExecutionstate(ExecutionStateEnum.RETURN);
        currentAct.setStarttimestamp(null);
        currentAct.setDeadline(null);
        em.merge(currentAct);

        //4.更新上層activities的狀態
        RoutePrevious(currentAct);
    }

    private void RoutePrevious(TcActivity currentAct) throws BPMException {
        for (TcActivityroute activityroute : currentAct.getTcActivityrouteCollection1()) {
            TcActivity fromAct = activityroute.getFromactivity(); // 上層activity
            ActivityTypeEnum fromType = fromAct.getActivitytype();
            // AND_GATE, OR_GATE, PASS的case要再往上一關
            if (ActivityTypeEnum.AND_GATE==fromType || ActivityTypeEnum.OR_GATE==fromType
                    || ExecutionStateEnum.PASS==fromAct.getExecutionstate()) {
                currentAct.setExecutionstate(ExecutionStateEnum.NOT_START);
                RoutePrevious(fromAct);
            } else if (ActivityTypeEnum.TASK==fromType) {
                fromAct.setStarttimestamp(new Date());
                fromAct.setEndtimestamp(null);
                fromAct.setExecutionstate(ExecutionStateEnum.RUNNING);
                activityroute.setTally(0L);
                // 上層activity的workitem重新啟動
                for (TcWorkitem fromWorkitem : fromAct.getTcWorkitemCollection()) {
                    fromWorkitem.setExecutionstate(ExecutionStateEnum.RUNNING);
                    fromWorkitem.setStarttimestamp(new Date());
                    fromWorkitem.setEndtimestamp(null);
                    em.merge(fromWorkitem);
                    sendWorkitemNotification(fromWorkitem);
                }
            } else {
                throw new BPMException("RoutePrevious exception, type:" + fromType);
            }
            em.merge(fromAct);
            em.merge(activityroute);
        }
    }

}
