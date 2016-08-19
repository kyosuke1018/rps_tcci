/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.bpm.*;
import com.tcci.fc.entity.bpm.enumeration.*;

import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.team.TeamFacade;
import com.tcci.fc.util.DateUtils;
import com.tcci.fc.util.VelocityTemplateUtils;
import com.tcci.mail.template.MailBean;
import com.tcci.proxy.service.rs.client.AgentRsClient;
import com.tcci.proxy.service.rs.model.AgentDto;
import com.tcci.proxy.service.rs.model.AgentListDto;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wayne
 */
@Stateless
@Named
public class BPMEngineFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
//    @EJB
//    private EssentialFacade essentialFacade;
    @EJB
    private TcUserFacade userFacade;
//    @EJB
    private ExpressionFacade expressionFacade;
    @EJB
    private TeamFacade teamFacade;
    @EJB
    private VariableFacade variableFacade;
    @EJB
    private BPMFacade bpmFacade;
    @Resource(name = "mail/automail")
    private Session mailSession;
    @Resource(name = "jndi/workflow.config")
    private Properties bpmConfig;

//    private AgentRsClient client ;
    public BPMEngineFacade() {
    }

    public static void main(String[] args) {

        try {
            // Bpmprocess process = BPMEngineFacade.createProcess("PMD Process", "PMD Process Instance Test", "105538", "ext.richard", "" roleParticipants);
            // System.out.println("process=" + process.getProcessid());
            //
            // BPMEngineFacade.startProcess(process);
            // BPMEngineFacade.callbackProcess(processx);
            // BPMEngineFacade.addBpmroleparticipant("14", "Manager1", "99999");
            // BPMEngineFacade.completeTask("82", "Submit");
            // BPMEngineFacade.completeTask("24", "Reject To Submitter");
            // BPMEngineFacade.completeTask("82", "Approve");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * To instantiate a process.
     * 
     * @param processTemplateName String The process template that want to instantiate
     * @param processName String To name a name for the process instance
     * @param creatorId String The process instance creator Id
     * @param roleParticipants Vector <Bpmroleparticipant> The participants of roles for the process
     * @return return The instantiate process
     * 
     */
    public TcProcess createProcess(String processTemplateName, String processName, TcUser creator, PrimaryBpmObject bpmObject, HashMap map) throws Exception {
        return createProcess(processTemplateName, processName, creator, bpmObject.getClass().getCanonicalName(), bpmObject.getId(), map);
    }

    public TcProcess createProcess(String processTemplateName, String processName, TcUser creator, String pboClassName, long pboId, HashMap map)
            throws Exception {
        TcProcess process = null;

        // To get Process, Activities, Roles and Activity Route
        if (processName == null) {
            throw new Exception("Error: BPM Engine is unable to initialize the process, because Process name is null");
        }

//        TeamManaged tm = null;
//        try {
//            tm = (TeamManaged) essentialFacade.getObject(pboClassName + ":" + pboId);
//        } catch (Exception e) {
//            throw new Exception("Error: BPM Engine is unable to initialize the process, because " + e.getLocalizedMessage());
//        }

        // ======================================================================================
        // To get process template
        // ======================================================================================
        Query query = em.createQuery("SELECT b FROM TcProcesstemplate b WHERE b.processname = :processname and b.disabled<>1 order by b.id desc");
        query.setParameter("processname", processTemplateName);
        List procTempList = query.getResultList();
        if (procTempList.size() == 0) {
            throw new Exception("Start process error: Process " + processTemplateName + " not found!");
        } else {
            TcProcesstemplate processTemplate = (TcProcesstemplate) procTempList.get(0);
            // System.out.println("processtemplate=" + processTemplate);
            try {
                // =================================
                // Generate Process instance
                // =================================
                process = new TcProcess();
                process.setProcesstemplateid(processTemplate);
                process.setProcessname(processName);
                process.setStarttimestamp(Calendar.getInstance().getTime());
                process.setCreator(creator);
                process.setPrimaryobjectclassname(pboClassName);
                process.setPrimaryobjectid(pboId);
                process.setExecutionstate(ExecutionStateEnum.NOT_START);
//                process.setTeam(tm.getTeam());
//                System.out.println(teamFacade.getTeams().size());
//                 process.setTeam(teamFacade.getTeams().get(0));
                em.persist(process);

                // =================================
                // Generate Process role participants instances
                // =================================
                // List rps = new ArrayList();
				/*
                 * Unncessary to create Role any more. It will be created by createTeam()
                 * if (roleParticipants!=null) {
                 * for (int i=0; i<roleParticipants.size(); i++) {
                 * Role rp = (Role)roleParticipants.elementAt(i);
                 * rp.setProcessid(process);
                 * em.persist(rp);
                 * //rps.add(rp);
                 * }
                 * }
                 */

                // =================================
                // Generate Process activities
                // =================================
                Collection<TcActivitytemplate> acts = processTemplate.getTcActivitytemplateCollection();
                Hashtable actHT = new Hashtable();
                // List activities = new ArrayList();
//                Collection<TcRole> roles = teamFacade.getRoleHolderRoles(process.getTeam());
                if (acts != null) {
                    List<TcActivity> activities = new LinkedList<TcActivity>();
                    for (TcActivitytemplate activityTemplate : acts) {
                        TcUser tcUser = (TcUser) map.get(activityTemplate.getRolename());
//                        if (activityTemplate.getRolename() != null) {
//                            System.out.println("Role Name=" + activityTemplate.getRolename() + " User=" + (String) map.get(activityTemplate.getRolename()));
//                            tcUser = userFacade.findUserByEmpId((String) map.get(activityTemplate.getRolename()));
//                        }
                        TcActivity activity = newBpmactivity(process, activityTemplate, tcUser);

                        em.persist(activity);
                        activities.add(activity);
                        actHT.put(activity.getActivitytemplateid().getId().toString(), activity);
                    }
                    process.setTcActivityCollection(activities);
                }

                // =================================
                // Generate Process activities' routes
                // =================================
                for (Enumeration enumeration = actHT.elements(); enumeration.hasMoreElements();) {
                    TcActivity activity = (TcActivity) enumeration.nextElement();

                    Collection<TcActivityroutetemplate> actRoutes = activity.getActivitytemplateid().getTcActivityroutetemplateCollection();
                    if (actRoutes != null) {
                        Collection<TcActivityroute> routes = new ArrayList<TcActivityroute>(actRoutes.size());
                        for (TcActivityroutetemplate activityRouteTemplate : actRoutes) {
                            TcActivity fromActivity = (TcActivity) actHT.get(activityRouteTemplate.getFromactivity().getId().toString());
                            TcActivity toActivity = (TcActivity) actHT.get(activityRouteTemplate.getToactivity().getId().toString());

                            TcActivityroute route = new TcActivityroute();
                            route.setFromactivity(fromActivity);
                            route.setToactivity(toActivity);
                            route.setRoutename(activityRouteTemplate.getRoutename());
                            route.setTally(new Long("0"));
                            route.setFromactivity(activity);
                            route.setToactivity(toActivity);
                            em.persist(route);
                            routes.add(route);
                        }
                        activity.setTcActivityrouteCollection(routes);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }

        variableFacade.createVariables(process, map);
        return process;
    }

    /**
     * To terminate a process. All running activities and workitems of the process will be terminated
     * 
     * @param process Bpmprocess To complete a process
     * @param isSingleton boolean Whether the process is the only RUNNING process for the PBO. If the flag sets, the other running processes with the
     *            PBO are terminated
     * @return return true, if the workitem is reassigned
     */
    /*public void startProcess(TcProcess process, boolean onlyAllowOneProcess) throws Exception {
    if (process == null) {
    throw new Exception("Error: BPM Engine is unable to start process, because Bpm Process is null!");
    }
    
    // To get the latest process object from DB
    
    try {
    // ==============================================
    // To terminate the other RUNNING processes for the PBO.
    // ==============================================
    if (onlyAllowOneProcess && process.getPrimaryobjectclassname() != null && process.getPrimaryobjectid() != 0) {
    Query query = em.createQuery("SELECT b FROM Bpmprocess b WHERE b.pboClassName = :pboClassName AND b.pboId=:pboId");
    query.setParameter("pboClassName", process.getPrimaryobjectclassname());
    query.setParameter("pboId", process.getPrimaryobjectid());
    List processes = query.getResultList();
    if (processes != null) {
    for (int i = 0; i < processes.size(); i++) {
    TcProcess otherProcess = (TcProcess) processes.get(i);
    if (!process.getId().equals(otherProcess.getId())
    && ExecutionStateEnum.RUNNING.equals(otherProcess.getExecutionstate())) {
    terminateBpmprocess(otherProcess);
    }
    }
    }
    }
    
    process = (TcProcess) em.merge(process);
    em.refresh(process);
    for (TcActivity activity : process.getTcActivityCollection()) {
    System.out.println("act type enum=" + activity.getActivitytemplateid().getActivitytype());
    if (ActivityTypeEnum.START.equals(activity.getActivitytemplateid().getActivitytype())) {
    process.setStarttimestamp(Calendar.getInstance().getTime());
    process.setExecutionstate(ExecutionStateEnum.RUNNING);
    // process = em.merge(process);
    System.out.println("start!!!!!");
    startBpmactivity(activity);
    break;
    }
    }
    } catch (Exception e) {
    e.printStackTrace();
    throw new Exception("Unable to start process", e);
    }
    
    return;
    }*/
    public void startProcess(TcProcess process) throws Exception {
        startProcess(process, true);
    }

    /**
     * To terminate a process. All running activities and workitems of the process will be terminated
     * 
     * @param processId String To complete a process
     * @return return true, if the workitem is reassigned
     */
    public void startProcess(TcProcess process, boolean onlyAllowOneProcess) throws Exception {

        if (process == null) {
            throw new Exception("Error: BPM Engine is unable to start process, because Bpm Process is null!");
        }

        if (onlyAllowOneProcess && process.getPrimaryobjectclassname() != null && process.getPrimaryobjectid() != 0) {
            Query query = em.createQuery("SELECT b FROM TcProcess b WHERE b.primaryobjectclassname = :primaryobjectclassname AND b.primaryobjectid=:primaryobjectid");
            query.setParameter("primaryobjectclassname", process.getPrimaryobjectclassname());
            query.setParameter("primaryobjectid", process.getPrimaryobjectid());
            List processes = query.getResultList();
            if (processes != null) {
                for (int i = 0; i < processes.size(); i++) {
                    TcProcess otherProcess = (TcProcess) processes.get(i);
                    if (!process.getId().equals(otherProcess.getId())
                            && ExecutionStateEnum.RUNNING.equals(otherProcess.getExecutionstate())) {
                        terminateBpmprocess(otherProcess);
                    }
                }
            }
        }
        // To get the latest process object from DB
        process = (TcProcess) em.merge(process);

//         process = (TcProcess)em.refresh(process);


        Collection<TcActivity> acts = process.getTcActivityCollection();
        System.out.println("activity=" + acts.size());
        for (TcActivity activity : acts) {
            // activity = (Bpmactivity) EntityManagerHelper.refresh(activity, "activityid");
            System.out.println(activity.getId() + "***" + activity.getProcessid());
            em.refresh(activity);

            if (ActivityTypeEnum.START.equals(activity.getActivitytemplateid().getActivitytype())) {

                try {
                    process.setStarttimestamp(Calendar.getInstance().getTime());
                    process.setExecutionstate(ExecutionStateEnum.RUNNING);
                    process = (TcProcess) em.merge(process);

                    em.flush();
                    // em.refresh(process);

                    startBpmactivity(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("Unable to start process", e);
                }
                break;
            }
        }
        return;
    }

    /**
     * To call back a NOT_START bpm process. The method will clean the process instance.
     * If the process is in other state, callbackProcess will raise a BPMException
     * 
     * @param processId Bpmprocess To complete a process
     * @return return true, if the workitem is reassigned
     */
    public void callbackProcess(TcProcess process) throws Exception {
        EntityTransaction et = null;
        if (process == null) {
            throw new Exception("Error: BPM Engine is unable to start process, because Bpm Process is null!");
        }
        if (!ExecutionStateEnum.NOT_START.equals(process.getExecutionstate())) {
            throw new Exception("Error: BPM Engine is unable to call back process, because Bpm Process has started!");
        }

        try {
            // ======================================================================================
            // To remove role participants
            // ======================================================================================
//            Collection<TcRole> rps = teamFacade.getRoleHolderRoles(process.getTeam());
//            for (Iterator rpit = rps.iterator(); rpit.hasNext();) {
//                em.remove(em.merge(rpit.next()));
//            }

            // ======================================================================================
            // To remove activities and routes
            // ======================================================================================
            Collection<TcActivity> acts = process.getTcActivityCollection();
            for (TcActivity act : acts) {
                Collection routes = act.getTcActivityrouteCollection();
                for (Iterator rit = routes.iterator(); rit.hasNext();) {
                    em.remove(em.merge(rit.next()));
                }
                em.remove(em.merge(act));
            }

            // ======================================================================================
            // To remove process
            // ======================================================================================
            em.remove(em.merge(process));
            em.flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return;
    }

    public void restartProcess(TcProcess process) throws Exception {
        if (process == null) {
            throw new Exception("BPM Engine is unable to add RoleParticipant in process, because process is null");
        }
        try {
            process = em.merge(process);
            for (TcActivity activity : process.getTcActivityCollection()) {
                if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
                    activity.setExecutionstate(ExecutionStateEnum.NOT_START);
                    for (TcWorkitem workitem : activity.getTcWorkitemCollection()) {
                        if (ExecutionStateEnum.RUNNING.equals(workitem.getExecutionstate())) {
                            workitem.setEndtimestamp(Calendar.getInstance().getTime());
                            workitem.setExecutionstate(ExecutionStateEnum.TERMINATED);
                            workitem = (TcWorkitem) em.merge(workitem);
                            em.flush();
                        }
                    }
                }
            }
            startProcess(process);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * To terminate a process. All running activities and workitems of the process will be terminated
     * 
     * @param process Bpmprocess To terminate a process
     * @return return true, if the process was terminated
     */
    public boolean terminateBpmprocess(TcProcess process) throws Exception {
        boolean result = false;
        try {
            if (!ExecutionStateEnum.RUNNING.equals(process.getExecutionstate())) {
                result = true;
            } else {
                process.setEndtimestamp(Calendar.getInstance().getTime());
                process.setExecutionstate(ExecutionStateEnum.TERMINATED);
                process = (TcProcess) em.merge(process);
                em.flush();
                // em.refresh(process);

                Collection<TcActivity> acts = process.getTcActivityCollection();
                if (acts != null) {
                    for (TcActivity activity : acts) {
                        if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
                            activity.setEndtimestamp(Calendar.getInstance().getTime());
                            activity.setExecutionstate(ExecutionStateEnum.TERMINATED);
                            activity = (TcActivity) em.merge(activity);
                            em.flush();
                            em.refresh(activity);

                            Collection<TcWorkitem> wis = activity.getTcWorkitemCollection();
                            if (wis != null) {
                                for (TcWorkitem workitem : wis) {
                                    if (ExecutionStateEnum.RUNNING.equals(workitem.getExecutionstate())) {
                                        workitem.setEndtimestamp(Calendar.getInstance().getTime());
                                        workitem.setExecutionstate(ExecutionStateEnum.TERMINATED);
                                        workitem = (TcWorkitem) em.merge(workitem);
                                        em.flush();
                                        em.refresh(workitem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to terminate process", e);
        }
        return result;
    }

    /**
     * To terminate a process. All running activities and workitems of the process will be terminated
     * 
     * @param processId String To complete a process
     * @return return true, if the workitem is reassigned
     */
    public void setProcessPrimaryObject(TcProcess process, String pboClassName, long pboId) throws Exception {
        if (process == null) {
            throw new Exception("Error: BPM Engine is unable to start process, because Bpm Process is null!");
        }

        try {
            process.setPrimaryobjectclassname(pboClassName);
            process.setPrimaryobjectid(pboId);
            process = (TcProcess) em.merge(process);
            em.flush();
            // em.refresh(process);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to set process PBO", e);
        }
        return;
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
        return sigature;
    }

    /**
     * To delegate a workitem to new participant
     * 
     * @param WorkItemId String To workitem that want to reassign
     * @param participant String The new participant for the workitem
     * @return return true, if the workitem is reassigned
     */
    public boolean reassign(TcWorkitem workitem, TcUser participant) throws Exception {
        return reassign(workitem, participant, null);
    }

    /**
     * To delegate a workitem to new participant
     * 
     * @param WorkItemId String To workitem that want to reassign
     * @param participant String The new participant for the workitem
     * @return return true, if the workitem is reassigned
     */
    public boolean reassign(TcWorkitem workitem, TcUser participant, String comment) throws Exception {
        boolean result = false;
        if (workitem == null) {
            throw new Exception("Error: BPM Engine is unable to delegate the workitem, because workitem id is null!");
        }
        if (participant == null) {
            throw new Exception("Error: BPM Engine is unable to delegate the workitem, because participant is null!");
        }
        try {
            workitem.setOwner(participant);
            workitem = (TcWorkitem) em.merge(workitem);

            // save signature
            saveSignature(workitem, "ReassignTask", comment);

            em.flush();
            em.refresh(workitem);

            result = true;
        } catch (Exception e) {
            throw new Exception("Unable to reassign workitem", e);
        }
        return result;
    }

    /**
     * To complete a workitem with signature
     * 
     * @param WorkItem
     * @param routeName String The route which participant seleted. If there is only one route, route name is ignored.
     * @param vars Hashtable The variables which participant want to update
     * @return return true, if the workitem is completed.
     */
    public boolean completeTask(TcWorkitem workitem, String routeName, String comments) throws Exception {
        boolean result = false;
        try {
            saveSignature(workitem, routeName, comments);
            _workComplete(workitem, routeName);
            result = true;
        } catch (Exception e) {
            throw new Exception("Unable to complete work item", e);
        }
        return result;
    }

    /**
     * To complete a work_saveSignature(em, workitem, routeName, comments);item
     * 
     * @param WorkItem
     * @param routeName String The route which participant seleted. If there is only one route, route name is ignored.
     * @param vars Hashtable The variables which participant want to update
     * @return return true, if the workitem is completed.
     */
    public boolean completeTask(TcWorkitem workitem, String routeName) throws Exception {
        return completeTask(workitem, routeName, null);
    }

    private TcWorkitem _mergeAndValidateWorkitem(TcWorkitem workitem) throws Exception {
        workitem = em.merge(workitem);
        em.refresh(workitem);

        if (ExecutionStateEnum.WAITING.equals(workitem.getExecutionstate())) {
            throw new Exception("BPM Engine is unable to complete task. The workitem state is " + workitem.getExecutionstate()
                    + ". It may have running Ad Hoc tasks.");
        } else if (!ExecutionStateEnum.RUNNING.equals(workitem.getExecutionstate())) {
            throw new Exception("BPM Engine is unable to complete task, because workitem state is " + workitem.getExecutionstate());
        }
        return workitem;
    }

    /**
     * To check whether all workitems of the activity have been completed or terminated
     * 
     * @param activity Bpmactivity The activity which is trying to be completed
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its rate of the activity
     * @return return boolean Return true, if all workitems have been completed. Return false, if there is any RUNNING workitem(s).
     */
    private boolean addTally(EntityManager em, TcWorkitem workitem, String routeName) throws Exception {
        boolean isValid = false;

        if (workitem == null) {
            throw new Exception("BPM Engine is unable to check route name, because Workitem is null");
        }
        // Not sure to call getTcActivityrouteCollection or getTcActivityrouteCollection1 -> should be getTcActivityrouteCollection
        Collection<TcActivityroute> routes = workitem.getActivityid().getTcActivityrouteCollection();
        //Collection<TcActivityroute> routes = workitem.getActivityid().getTcActivityrouteCollection1();
        if (routes != null) {
            for (TcActivityroute route : routes) {
                System.out.println(route.getRoutename() + "====" + routeName);
                if (route.getRoutename().equals(routeName)) {
                    isValid = true;
                    long tally = route.getTally() + 1;
                    route.setTally(new Long(tally));
                    em.flush();
                    em.refresh(route);
                }
            }
        }
        return isValid;
    }

    /*
    private Hashtable setupTallyForExpressionResult(TcActivity activity, Object result) {
    // To find out what are next routes by result
    // NOTE that result==null means all routes will be triggered.
    Map<String, String> resultMap = new HashMap<String, String>();
    if (result == null) {
    System.out.println("[WARNING] Result is null. All next routes are triggered.");
    } else {
    if (result instanceof String) {
    String nextRoute = (String) result;
    resultMap.put(nextRoute, nextRoute);
    } else if (result instanceof List) {
    List<String> nextRoutes = (List<String>) result;
    for (String nextRoute : nextRoutes) {
    resultMap.put(nextRoute, nextRoute);
    }
    }
    }
    
    // =================================
    // To count the routes
    // From=>To link: activity.getBpmactivityrouteCollection()
    // =================================
    Hashtable tallies = new Hashtable();
    Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();
    if (ars != null) {
    for (TcActivityroute ar:ars) {
    TcActivity toActivity = ar.getToactivity();
    // If the route match the result, set Tally and Total workitem are equals to 1
    if (resultMap.size() == 0 || resultMap.containsKey(ar.getRoutename())) {
    ActivityRouteTally art = new ActivityRouteTally(toActivity.getId(), 1, 1);
    tallies.put(ar, art);
    }
    }
    }
    
    return tallies;
    }
     */
    private boolean _workComplete(TcWorkitem workitem, String routeName) throws Exception {
        boolean status = false;
        workitem = _mergeAndValidateWorkitem(workitem);

        // ================================================
        // Add Tally to an ActivityRoute
        // ================================================
        workitem = (TcWorkitem) em.merge(workitem);
        if (!addTally(em, workitem, routeName)) {
            throw new Exception("Route Name: " + routeName + " is invalid");
        }

        // ================================================
        // Complete normal workitem(Non-Ad Hoc work items)
        // ================================================
        workitem.setEndtimestamp(Calendar.getInstance().getTime());
        workitem.setExecutionstate(ExecutionStateEnum.COMPLETED);
        workitem.setBallot(routeName);
        workitem = (TcWorkitem) em.merge(workitem);
        em.flush();
        em.refresh(workitem);

        TcActivity activity = workitem.getActivityid();
        em.refresh(activity);




        // ================================================
        // If all workitems of the activity are completed
        // ================================================
        Hashtable tallies = new Hashtable();
        // activity = (Bpmactivity)EntityManagerHelper.refresh(activity, "activityid");

        System.out.println("Workitem Id=" + workitem.getId() + ", ActivityId=" + workitem.getActivityname() + ", All Workitems="
                + activity.getTcWorkitemCollection());
        if (areAllWorkItemsCompleted(activity, tallies)) {
            // ================================================
            // Complete activity
            // ================================================
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);
            String result = executeExpression(activity);

            // start next activity
            // if there's no any result, start all next activities
            if (result == null) {
                startNextBpmactivities(activity, tallies);
            } else {
                startNextBpmactivities(activity, result);
            }

        }

        return status;
    }

    private String executeExpression(TcActivity activity) throws Exception {
        System.out.println(activity.getActivityname());
        String expression = activity.getExpression();
        System.out.println(expression);
        String result = null;
        if (expression != null) {
            // To find out next routings
            expressionFacade = new ExpressionFacade();
            System.out.println(bpmFacade.getPrimaryObject(activity.getProcessid()));
            result = (String) expressionFacade.invokeExpression(activity, bpmFacade.getPrimaryObject(activity.getProcessid()), expression);
        }
        return result;
    }

    /**
     * To check whether all workitems of the activity have been completed or terminated
     * 
     * @param activity Bpmactivity The activity which is trying to be completed
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its rate of the activity
     * @return return boolean Return true, if all workitems have been completed. Return false, if there is any RUNNING workitem(s).
     */
    private boolean areAllActivitiesCompleted(TcActivity activity) throws Exception {
        boolean allCompleted = true;

        if (activity == null) {
            throw new Exception("BPM engine is unable to count activities, because activity is null");
        }

        Collection<TcActivityroute> fromActivities = activity.getTcActivityrouteCollection1();
        if (fromActivities != null) {
            for (TcActivityroute route : fromActivities) {
                TcActivity fromActivity = route.getFromactivity();
                if (ExecutionStateEnum.RUNNING.equals(fromActivity.getExecutionstate())
                        || ExecutionStateEnum.NOT_START.equals(fromActivity.getExecutionstate())
                        || ExecutionStateEnum.HOLD.equals(fromActivity.getExecutionstate())
                        || ExecutionStateEnum.WAITING.equals(fromActivity.getExecutionstate())) {
                    allCompleted = false;
                    break;
                }

            }
        }
        return allCompleted;
    }

    /**
     * To complete a workitem
     * 
     * @param WorkItemId String To workitem that participant completes
     * @param routeName String The route which participant seleted. If there is only one route, route name is ignored.
     * @return return true, if the workitem is completed.
     */
    /**
     * To check whether all workitems of the activity have been completed or terminated
     * 
     * @param activity Bpmactivity The activity which is trying to be completed
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its rate of the activity
     * @return return boolean Return true, if all workitems have been completed. Return false, if there is any RUNNING workitem(s).
     */
    private boolean areAllWorkItemsCompleted(TcActivity activity, Hashtable tallies) throws Exception {
        boolean allCompleted = true;
        // tempararily marked below code as i don't understand it
/*                            
        long totalWorkItems = 0;
        
        if (activity == null) {
        throw new Exception("BPM engine is unable to count workitem, because activity is null");
        }
        
        // Start node is a special case
        if (ActivityTypeEnum.START.equals(activity.getActivitytemplateid().getActivitytype())) {
        return allCompleted;
        }
        
        if (tallies == null) {
        tallies = new Hashtable();
        } else if (tallies.size() > 0) {
        tallies.clear();
        }
        
        // =================================
        // To check whether there is any RUNNING workitem(s)
        // =================================
        Collection<TcWorkitem> wis = activity.getTcWorkitemCollection();
        if (wis != null) {
        for (TcWorkitem wi : wis) {
        if (ExecutionStateEnum.RUNNING.equals(wi.getState())) {
        allCompleted = false;
        }
        // Terminated workitem will be ignored
        if (!ExecutionStateEnum.TERMINATED.equals(wi.getExecutionstate()) &&
        !ExecutionStateEnum.ARCHIVED.equals(wi.getExecutionstate() )) {
        totalWorkItems++;
        }
        }
        }
        
        // =================================
        // To count the routes
        // From=>To link: activity.getBpmactivityrouteCollection()
        // =================================
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();
        if (ars != null) {
        for (TcActivityroute ar: ars) {
        TcActivity toActivity = ar.getToactivity();
        ActivityRouteTally art = new ActivityRouteTally(toActivity.getId(), ar.getTally().longValue(), totalWorkItems);
        System.out.println("toActivity=" + toActivity + ", totalWorkitems=" + totalWorkItems + ", tally=" + ar.getTally().longValue()
        + ", rate=" + art.getRate());
        tallies.put(ar, art);
        }
        }
        
        
         */
        return allCompleted;

    }

    /**
     * To start ALL next bpmactivities after START, AND GATE, OR GATE and EXPRESSION
     * 
     */
    private void startNextBpmactivities(TcActivity activity) throws Exception {
        // =================================
        // Start up next activities
        // =================================
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();

        if (ars != null) {
            for (TcActivityroute ar : ars) {
                // =================================
                // Start next activity
                // =================================
                TcActivity toActivity = ar.getToactivity();
                Long toActivityId = toActivity.getId();
                System.out.println("startNextBpmactivities(Bpmactivity activity): toActivityId=" + toActivityId + ", toActivity="
                        + toActivity.getId());
                startBpmactivity(toActivity);
            }
        }
    }

    // Start next activity by tally
    private void startNextBpmactivities(TcActivity activity, Hashtable tallies) throws Exception {
        Hashtable<TcActivity, TcActivity> nextActivities = new Hashtable<TcActivity, TcActivity>();
        //Hashtable<String, Bpmexpression> exprs = new Hashtable<String, Bpmexpression>();

        // =================================
        // Start up next activities
        // =================================
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();
        if (ars != null) {
            for (TcActivityroute ar : ars) {
                // =================================================================
                // For loop back activity, To set its next activities to NOT_START.
                // If not, it may cause GATE activity to do the wrong judgement
                // =================================================================
                if (ar.getTally() > 0) {
                    TcActivity toActivity = (TcActivity) em.merge(ar.getToactivity());

                    Long toActivityId = toActivity.getId();
                    String routeName = ar.getRoutename();
                    ActivityTypeEnum activityType = toActivity.getActivitytemplateid().getActivitytype();
                    nextActivities.put(toActivity, toActivity);
                }
            }

        }
        em.flush();


        // ==================================
        // To go to next activities
        // ==================================

        for (Enumeration<TcActivity> keys = nextActivities.keys(); keys.hasMoreElements();) {
            TcActivity nextAct = keys.nextElement();
            System.out.println("[Trigger the route] Go to " + nextAct.getActivityname() + ", toActivityId=" + nextAct.getId()
                    + ", toActivity=" + nextAct.getId());
            startBpmactivity(nextAct);
        }

    }

    // Start next activity by tally
    private void startNextBpmactivities(TcActivity activity, String result) throws Exception {
        Hashtable<TcActivity, TcActivity> nextActivities = new Hashtable<TcActivity, TcActivity>();
        // =================================
        // Start up next activities
        // =================================
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();
        if (ars != null) {
            for (TcActivityroute ar : ars) {
                if (result != null & result.equals(ar.getRoutename())) {
                    TcActivity toActivity = (TcActivity) em.merge(ar.getToactivity());
                    nextActivities.put(toActivity, toActivity);
                }
            }

        }
        em.flush();


        // ==================================
        // To go to next activities
        // ==================================

        for (Enumeration<TcActivity> keys = nextActivities.keys(); keys.hasMoreElements();) {
            TcActivity nextAct = keys.nextElement();
            System.out.println("[Trigger the route] Go to " + nextAct.getActivityname() + ", toActivityId=" + nextAct.getId()
                    + ", toActivity=" + nextAct.getId());
            startBpmactivity(nextAct);
        }

    }

    /**
     * To terminate an activity. All running activities and workitems of the process will be terminated
     * 
     * @param activityId String To complete an activity
     * @return return true, if activity is terminated
     */
    public boolean terminateBpmactivity(TcActivity activity) throws Exception {
        return _terminateBpmactivity(activity);
    }

    private boolean _terminateBpmactivity(TcActivity activity) throws Exception {
        return _closeBpmactivity(activity, ExecutionStateEnum.TERMINATED);
    }

    private boolean _completeBpmactivity(TcActivity activity) throws Exception {
        return _closeBpmactivity(activity, ExecutionStateEnum.COMPLETED);
    }

    private boolean _closeBpmactivity(TcActivity activity, ExecutionStateEnum closeState) throws Exception {
        boolean result = false;
        if (activity == null) {
            throw new Exception("Error: BPM Engine is unable to termiante activity, because activity is null");
        }

        if (!ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
            result = true;
        } else {
            if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
                em.refresh(activity);
                activity.setEndtimestamp(Calendar.getInstance().getTime());
                activity.setExecutionstate(closeState);
                activity = (TcActivity) em.merge(activity);
                em.flush();
                Collection<TcWorkitem> wis = activity.getTcWorkitemCollection();
                if (wis != null) {
                    for (TcWorkitem workitem : wis) {
                        if (ExecutionStateEnum.RUNNING.equals(workitem.getExecutionstate())) {
                            workitem.setEndtimestamp(Calendar.getInstance().getTime());
                            workitem.setExecutionstate(closeState);
                            workitem.setActivityid(activity);
                            workitem = (TcWorkitem) em.merge(workitem);
                            em.flush();
                            em.refresh(workitem);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * To send notification for a workitem. The method can be re-used for repeatly reminding email
     * 
     * @param wi Bpmworkitem The notification for the workitem
     * @return return void
     */
    public void sendWorkitemNotification(TcWorkitem wi) {
        if (wi.getActivityid().getActivitytemplateid().getNotification()) {
            try {
                TcUser tcUser = wi.getOwner();
                TcActivity tcActivity = wi.getActivityid();
                TcProcess tcProcess = tcActivity.getProcessid();
                HashMap map = variableFacade.getVariables(tcProcess);
                MailBean bean = new MailBean();
                bean.setTemplateName("approval.html");
                bean.setActorId(tcUser.getDisplayIdentifier());
                bean.setAppSubject((String) map.get("appSubject"));
                String reason = (String) map.get("reason");
                if (reason != null) {
                    reason = reason.replaceAll(System.getProperty("line.separator"), "<br>");
                }
                bean.setReason(reason);
                bean.setRequester(tcProcess.getCreator().getDisplayIdentifier());

                bean.setSubject("表單簽核");

                bean.setEmailAddress(tcUser.getEmail());
                bean.setHostId(InetAddress.getLocalHost().getHostAddress());
                bean.setOid(tcProcess.getPrimaryobjectclassname() + ":" + tcProcess.getPrimaryobjectid());
                bean.setTaskId(wi.getId());

                Multipart mp = new MimeMultipart();
                MimeBodyPart mbp = new MimeBodyPart();
                MimeMessage msg = new MimeMessage(mailSession);
                try {
                    mbp.setContent(VelocityTemplateUtils.getContent(bean, bean.getTemplateName()), "text/html;charset=utf-8");
                    mp.addBodyPart(mbp);
                    msg.setContent(mp);
                    msg.setSubject(bean.getSubject(), "UTF-8");
                    msg.setRecipient(RecipientType.TO, new javax.mail.internet.InternetAddress(bean.getEmailAddress()));
                    //  msg.setText(VelocityTemplateUtils.getContent(bean, bean.getTemplateName()));
                    Transport.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (UnknownHostException ex) {
                Logger.getLogger(BPMEngineFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private TcWorkitem newBpmworkitem(TcActivity activity, TcUser participant) {
        TcWorkitem wi = new TcWorkitem();
        wi.setActivityname(activity.getActivityname());
//        System.out.println(activity.getActivityname());
        wi.setActivityid(activity);
//        System.out.println(activity.getId());
        wi.setOwner(participant);

//        System.out.println(participant.getId());
        wi.setExecutionstate(ExecutionStateEnum.RUNNING);
        wi.setStarttimestamp(Calendar.getInstance().getTime());
        return wi;
    }

    private TcUser getAgentTcUser(TcActivity activity, TcUser participant) {
        try {
            TcProcess tcProcess = activity.getProcessid();
            HashMap map = variableFacade.getVariables(tcProcess);
            // TODO need to fix

            AgentRsClient client = AgentRsClient.build(bpmConfig.getProperty("agentRsURL"));

            //AgentListDto dto = client.listAgents((String) map.get("sysId"), (String) map.get("companyCode"), (String) map.get("deptCode"), participant.getEmpId(), null, DateUtils.getSimpleISODateTimeStr(new Date()));

            //ignore department dimension of deputy setting
            AgentListDto dto = client.listAgents((String) map.get("sysId"), null, null, participant.getEmpId(), null, DateUtils.getISODateTimeStr(new Date()));

            List<AgentDto> list = dto.getAgents();

            if (list != null && list.size() > 0) {
                AgentDto agent = list.get(0);
                return userFacade.findUserByEmpId(agent.getAgentEmpCode());
            }
        } catch (Exception ex) {
            Logger.getLogger(BPMEngineFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return participant;
    }

    private int startBpmworkitems(TcActivity activity) throws Exception {
//        if (activity.getProcessid().getTeam() == null) {
//            throw new Exception("BPM Engine is unable to check route name, because Process's Team is null");
//        }
        int workItemCount = 0;


        if (activity.getParticipant() != null) {

            TcWorkitem wi = newBpmworkitem(activity, activity.getParticipant());
            em.persist(wi);

            activity.getTcWorkitemCollection().add(wi);

            workItemCount++;
            sendWorkitemNotification(wi);

            em.flush();
        }
//        }


        return workItemCount;
    }

    /**
     * To check whether all workitems of the activity have been completed or terminated
     * 
     * @param activity Bpmactivity The activity which is trying to be completed
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its rate of the activity
     * @return return boolean Return true, if all workitems have been completed. Return false, if there is any RUNNING workitem(s).
     */
    public List getRouteNames(String workitemId) throws Exception {

        // Bpmworkitem workitem = em.find(Bpmworkitem.class, new BigDecimal(workitemId));
        TcWorkitem workitem = (TcWorkitem) em.createQuery("select b from TcWorkitem b where id=:id").setParameter("id",
                new BigDecimal(workitemId)).getSingleResult();
        if (workitem == null) {
            throw new Exception("BPM Engine is unable to get route names, because Workitem is null");
        }
        return getRouteNames(workitem);
    }

    /**
     * To check whether all workitems of the activity have been completed or terminated
     * 
     * @param workitem Bpmworkitem The workitem
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its rate of the activity
     * @return return boolean Return true, if all workitems have been completed. Return false, if there is any RUNNING workitem(s).
     */
    public List getRouteNames(TcWorkitem workitem) throws Exception {
        List list = new ArrayList();
        Hashtable ht = new Hashtable();
        if (workitem == null) {
            throw new Exception("BPM Engine is unable to get route names, because Workitem is null");
        }

        Collection<TcActivityroute> routes = workitem.getActivityid().getTcActivityrouteCollection();
        if (routes != null) {
            for (TcActivityroute route : routes) {
                String routeName = route.getRoutename();
                if (!ht.containsKey(routeName)) {
                    ht.put(routeName, routeName);
                    list.add(route.getRoutename());
                }
            }
        }
        return list;
    }

    /**
     * To check whether all workitems of the activity have been completed or terminated
     * 
     * @param activity Bpmactivity The activity which is trying to be completed
     * @param tallies Hashtable<ActivityRouteTally> Returned The tally and its rate of the activity
     * @return return boolean Return true, if all workitems have been completed. Return false, if there is any RUNNING workitem(s).
     */
    private int activityCompletedCount(TcActivity activity) throws Exception {
        int completedCount = 0;

        if (activity == null) {
            throw new Exception("BPM engine is unable to count activities, because activity is null");
        }

        Collection<TcActivityroute> fromActivities = activity.getTcActivityrouteCollection1();
        if (fromActivities != null) {
            for (TcActivityroute route : fromActivities) {
                TcActivity fromActivity = route.getFromactivity();
                if (ExecutionStateEnum.COMPLETED.equals(fromActivity.getExecutionstate())) {
                    completedCount++;
                }
            }
        }
        return completedCount;
    }

    // This is for multiple subsequent activities
    private void tagBpmactivityBreadthFirst(TcActivity activity) throws Exception {
        // =================================
        // Start up next activities
        // =================================
        Collection<TcActivityroute> ars = activity.getTcActivityrouteCollection();

        if (ars != null) {
            for (TcActivityroute ar : ars) {
                TcActivity toActivity = ar.getToactivity();
                System.out.println("Tag : toActivity=" + toActivity.getId() + " to WAITING");
                toActivity.setExecutionstate(ExecutionStateEnum.WAITING);
                toActivity = em.merge(toActivity);

                em.flush();
            }
        }
    }

    private void resetActivity(TcActivity activity) throws Exception {
        if (activity == null) {
            throw new Exception("BPM Engine is unable to reset Tally, because Activity is null");
        }
        Collection<TcActivityroute> routes = activity.getTcActivityrouteCollection();
        if (routes != null) {
            for (TcActivityroute route : routes) {
                route.setTally(new Long("0"));
                em.flush();
                em.refresh(route);
            }
        }
        Collection<TcWorkitem> workitems = activity.getTcWorkitemCollection();
        if (workitems != null) {
            for (TcWorkitem workitem : workitems) {
                if (!ExecutionStateEnum.ARCHIVED.equals(workitem.getExecutionstate())) {
                    workitem.setExecutionstate(ExecutionStateEnum.ARCHIVED);
                    em.flush();
                    em.refresh(workitem);
                }
            }
        }

        return;
    }

    private void terminateActivity(TcActivity activity) {
        if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
            activity.setExecutionstate(ExecutionStateEnum.TERMINATED);
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);

            Collection<TcWorkitem> wis = activity.getTcWorkitemCollection();
            for (TcWorkitem wi : wis) {
                if (ExecutionStateEnum.RUNNING.equals(wi.getExecutionstate())) {
                    wi.setExecutionstate(ExecutionStateEnum.TERMINATED);
                    wi.setEndtimestamp(Calendar.getInstance().getTime());
                    wi = (TcWorkitem) em.merge(wi);
                    em.flush();
                    em.refresh(wi);
                }
            }
        }
    }

    // This is for OR gate
    private void terminateUncompleteItemsInLoop(TcActivity activity, HashMap<Long, TcActivity> map) throws Exception {
        Collection<TcActivityroute> fromRoutes = activity.getTcActivityrouteCollection();

        for (TcActivityroute route : fromRoutes) {
            TcActivity fromAct = route.getFromactivity();
            // to make it stop, if the act had been reached before
            if (map.containsKey(fromAct.getId())) {
                continue;
            }

            map.put(fromAct.getId(), fromAct);
            terminateActivity(fromAct);

            // recurrsively to terminate all its upcoming activity
            terminateUncompleteItemsInLoop(fromAct, map);
        }
    }

    public void jumpToBpmactivity(TcActivity fromActivity, TcActivity toActivity, boolean isTerminateRunningAct) throws Exception {
        List<TcActivity> toActivityList = new ArrayList();
        toActivityList.add(toActivity);
        jumpToBpmactivity(fromActivity, toActivityList, isTerminateRunningAct);
    }

    public void jumpToBpmactivity(TcActivity fromActivity, List<TcActivity> toActivityList, boolean isTerminateRunningAct)
            throws Exception {
        // =====================================
        // terminate related activities
        // =====================================
        if (isTerminateRunningAct) {
            // term all
            for (TcActivity act : fromActivity.getProcessid().getTcActivityCollection()) {
                _terminateBpmactivity(act);
            }
        } else {
            // terminate itself
            _terminateBpmactivity(fromActivity);
        }

        // =====================================
        // start next bpm activities
        // =====================================
        for (TcActivity act : toActivityList) {
            startBpmactivity(act);
        }

    }

    public void triggerActivity(TcActivity activity) throws Exception {
        //TODO: some check for this method??

        startBpmactivity(activity);
    }

    private void startBpmactivity(TcActivity activity) throws Exception {
        if (activity == null) {
            throw new Exception("BPM engine is unable to start activity, because Bpm activity is null");
        }
        // ==========================================================
        // To initial process variable to activity variable
        // ==========================================================
        ActivityTypeEnum activityType = activity.getActivitytemplateid().getActivitytype();
        // AND Gate and OR Gate may be started by other branch
        if (ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())
                && !ActivityTypeEnum.AND_GATE.equals(activityType)
                && !ActivityTypeEnum.OR_GATE.equals(activityType)) {
            throw new Exception("BPM engine is unable to start activity, because Bpm activity has been started. Activity("
                    + activity.getActivityname() + ") status=" + activity.getExecutionstate());
        }


        // =========================================================
        // Update Duration and Deadline
        // If there is a pre-defined Deadline in process template, it has higher priority to use
        // =========================================================
        TcActivitytemplate activityTemplate = activity.getActivitytemplateid();
        activity.setDuration(activityTemplate.getDuration());
        if (activityTemplate.getDuration() != null) {
            Calendar deadline = Calendar.getInstance();
            deadline.setTimeInMillis(deadline.getTimeInMillis() + activityTemplate.getDuration().longValue() * 86400000);
            activity.setDeadline(deadline.getTime());
        } else {
            activity.setDeadline(null);
        }

        //activity.setVisitCount(activity.getVisitCount() == null ? 1 : (activity.getVisitCount() + 1));

        if (ActivityTypeEnum.START.equals(activityType)) {
            activity.setStarttimestamp(Calendar.getInstance().getTime());
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);


            startNextBpmactivities(activity);

        } else if (ActivityTypeEnum.TASK.equals(activityType)) {
            // Setup activity
            TcUser tcUser = getAgentTcUser(activity, activity.getParticipant());

            activity.setExecutionstate(ExecutionStateEnum.RUNNING);
            activity.setStarttimestamp(Calendar.getInstance().getTime());
            activity.setEndtimestamp(null);
            activity.setParticipant(tcUser);

            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);

            resetActivity(activity);

            // Start up workitem and activity's role
            if (startBpmworkitems(activity) == 0) {
                throw new Exception("BPM Engine is unable to start work item, because there is no role/participant for the activity");
            }


        } else if (ActivityTypeEnum.AND_GATE.equals(activityType)) {
            if (!ExecutionStateEnum.RUNNING.equals(activity.getExecutionstate())) {
                activity.setExecutionstate(ExecutionStateEnum.RUNNING);
                activity.setStarttimestamp(Calendar.getInstance().getTime());
                activity.setEndtimestamp(null);
                activity = (TcActivity) em.merge(activity);
                em.flush();
                em.refresh(activity);
            }

            if (areAllActivitiesCompleted(activity)) {
                activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
                activity.setEndtimestamp(Calendar.getInstance().getTime());
                em.flush();
                // em.refresh(activity);
                activity = (TcActivity) em.merge(activity);


                // start next activity
                // if there's no any result, start all next activities
                startNextBpmactivities(activity);
            }
        } else if (ActivityTypeEnum.OR_GATE.equals(activityType)) {
            if (ExecutionStateEnum.NOT_START.equals(activity.getExecutionstate())) {
                activity.setExecutionstate(ExecutionStateEnum.RUNNING);
                activity.setStarttimestamp(Calendar.getInstance().getTime());
                activity.setEndtimestamp(null);
                activity = (TcActivity) em.merge(activity);
                em.flush();
                em.refresh(activity);
            }
            // To make sure this OR Gate is triggered for only one time
            if (activityCompletedCount(activity) >= 1) {
                activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
                activity.setEndtimestamp(Calendar.getInstance().getTime());
                em.flush();
                em.refresh(activity);

                // to terminate all uncomplete task in the loop.
                // The HashMap is for occurence check
                terminateUncompleteItemsInLoop(activity, new HashMap());
                startNextBpmactivities(activity);
            }
        } else if (ActivityTypeEnum.END.equals(activityType)) {
            activity.setStarttimestamp(Calendar.getInstance().getTime());
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);
            TcProcess process = activity.getProcessid();
            process.setExecutionstate(ExecutionStateEnum.COMPLETED);
            process.setEndtimestamp(Calendar.getInstance().getTime());
            process = (TcProcess) em.merge(process);
            em.flush();
            em.refresh(process);
        } else if (ActivityTypeEnum.OPTION.equals(activityType)) {
            // Setup activity
            /*
            activity.setExecutionstate(ExecutionStateEnum.RUNNING);
            activity.setStarttimestamp(Calendar.getInstance().getTime());
            activity.setEndtimestamp(null);
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);
            resetActivity(activity);
            
            // Start up workitem and activity's role
            int wiCount = startBpmworkitems(activity);
            
            // All pervious actions should be committed
            if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
            }
            em.getTransaction().begin();
            
            
            if (wiCount == 0) {
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);
            
            startNextBpmactivities(activity, new Hashtable());
            }
             */
        } else if (ActivityTypeEnum.CONDITION.equals(activityType)) {
            // close activity

            activity.setStarttimestamp(Calendar.getInstance().getTime());
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);

            String result = executeExpression(activity);

            // start next activity
            startNextBpmactivities(activity, result);

        } else if (ActivityTypeEnum.EXPRESSION_ROBOT.equals(activityType)) {
            // close activity
            activity.setStarttimestamp(Calendar.getInstance().getTime());
            activity.setEndtimestamp(Calendar.getInstance().getTime());
            activity.setExecutionstate(ExecutionStateEnum.HOLD);
            try {
                executeExpression(activity);
                activity.setExecutionstate(ExecutionStateEnum.COMPLETED);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
//                activity.setExecutionstate(ExecutionStateEnum.HOLD);
            }
            activity = (TcActivity) em.merge(activity);
            em.flush();
            em.refresh(activity);


            // To find out next routings
            //executeExpression(activity);
            // start all next activity
            startNextBpmactivities(activity);
        }
        return;
    }

    private TcActivity newBpmactivity(TcProcess process, TcActivitytemplate activityTemplate, TcUser user) {
        TcActivity activity = new TcActivity();
        activity.setProcessid(process);
        activity.setActivityname(activityTemplate.getActivityname());
        activity.setActivitytemplateid(activityTemplate);
        activity.setDeadline(activityTemplate.getDeadline());
        activity.setDuration(activityTemplate.getDuration());
        activity.setActivitytype(activityTemplate.getActivitytype());
        activity.setExpression(activityTemplate.getExpression());
//        activity.setRolename(roleName);
//        if (activityTemplate.getRolename() != null) {
//            String rolename = activityTemplate.getRolename();
        activity.setRolename(activityTemplate.getRolename());
        activity.setParticipant(user);
//        }
        activity.setExecutionstate(ExecutionStateEnum.NOT_START);
        return activity;
    }
}
