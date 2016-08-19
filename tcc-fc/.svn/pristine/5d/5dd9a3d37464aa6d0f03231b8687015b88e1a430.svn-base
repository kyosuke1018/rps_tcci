/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.PrimaryBpmObject;
import com.tcci.fc.entity.bpm.TcActivity;
import com.tcci.fc.entity.bpm.TcProcess;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.entity.bpm.TcWorkitem;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import com.tcci.fc.entity.bpm.enumeration.ExecutionStateEnum;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.role.TcRole;
import com.tcci.fc.entity.role.TcRoleholderrolemap;
import com.tcci.fc.entity.role.TcRoleprincipallink;
import com.tcci.fc.facade.role.RoleFacade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
public class TcProcessFacade {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @EJB
    private TcActivityFacade tcActivityFacade;
    @EJB
    private TcWorkitemFacade workitemFacade;
    @EJB
    private RoleFacade RoleFacade;

    /*
     * 建立 process
     */
    public TcProcess createProcess(String processTemplateName, String processName, TcUser creator, PrimaryBpmObject pbo) {
        Query query = em.createNamedQuery("TcProcesstemplate.findByName");
        query.setParameter("processname", processTemplateName);
        List<TcProcesstemplate> procTempList = query.getResultList();
        if (procTempList.isEmpty()) {
            throw new EJBException("processTemplateName:" + processTemplateName + " not found!");
        } else if (procTempList.size() > 1) {
            throw new EJBException("processTemplateName:" + processTemplateName + " duplicated!");    
        }
        TcProcesstemplate processTemplate = (TcProcesstemplate) procTempList.get(0);
        TcProcess process = new TcProcess();
        process.setProcesstemplateid(processTemplate);
        process.setProcessname(processName);
        process.setStarttimestamp(Calendar.getInstance().getTime());
        process.setCreator(creator);
        process.setPrimaryobjectclassname(pbo.getClass().getCanonicalName());
        process.setPrimaryobjectid(pbo.getId());
        process.setExecutionstate(ExecutionStateEnum.NOT_START);
        em.persist(process);

        tcActivityFacade.createProcessActivities(process); // 包含 activities, routes
        
        // TODO: process roles: 如需共用，由client 端提供?
        createRoles(process);
        
        return process;
    }
    
    /*
     * 取得 process roles
     */
    public Collection<TcRole> getProcessRoles(TcProcess process) {
        return RoleFacade.getRoleHolderRoles(process);
    }
    
    /*
     * 啟動 process
     */
    public void startProcess(TcProcess process) {
//        System.out.println("********* startProcess");
        if (ExecutionStateEnum.NOT_START != process.getExecutionstate()) {
            throw new EJBException("process(" + process.getId() +") is already running!");
        }
        // 強制停止其它執行中的process(相同pbo)
        terminateOtherProcess(process);
        
        // 從 START activity 啟動
        Collection<TcActivity> acts = process.getTcActivityCollection();
        for (TcActivity activity : acts) {
            if (ActivityTypeEnum.START.equals(activity.getActivitytemplateid().getActivitytype())) {
                process.setStarttimestamp(new Date());
                process.setExecutionstate(ExecutionStateEnum.RUNNING);
                em.merge(process);
                tcActivityFacade.startActivity(activity);
                break;
            }
        }
    }
    
    /*
     * 停止 process
     */
    public void terminateProcess(Long processid) {
        TcProcess process = findByPK(processid);
        if (process != null) {
            terminateProcess(process);
        }
    }

    public void terminateProcess(TcProcess process) {
        // TODO: 應該加上reason欄位
        if (ExecutionStateEnum.RUNNING != process.getExecutionstate()) {
            throw new EJBException("process(" + process.getId() +") is not running!");
        }
        process.setExecutionstate(ExecutionStateEnum.TERMINATED);
        process.setEndtimestamp(new Date());
        Collection<TcActivity> acts = process.getTcActivityCollection();
        if (acts != null && !acts.isEmpty()) {
            for (TcActivity activity : acts) {
                if (activity.getExecutionstate().equals(ExecutionStateEnum.RUNNING)) {
                    tcActivityFacade.terminateActivity(activity);
                }
            }
        }
        em.merge(process);
    }
    
    // private helper
    private void terminateOtherProcess(TcProcess process) {
        //TODO: implement terminateOtherProcess
    }

    // 建立 roleholderrolemap 及 roles
    private void createRoles(TcProcess process) {
        Set<String> roleSet = new HashSet<String>();
        List<TcRole> roleList = new ArrayList<TcRole>();
        for (TcActivity activity : process.getTcActivityCollection()) {
            String roleName = activity.getRolename();
            if (roleName != null && roleSet.add(roleName)) {
                TcRole role = new TcRole();
                // 在這邊的應用是 OneToOne mapping
                role.setName(roleName);
                TcRoleholderrolemap holder = new TcRoleholderrolemap();
                holder.setHolderclassname(process.getClass().getCanonicalName());
                holder.setHolderid(process.getId());
                ArrayList<TcRoleholderrolemap> holders = new ArrayList<TcRoleholderrolemap>();
                holders.add(holder);
                role.setTcRoleholderrolemapCollection(holders);
                role.setTcRoleprincipallinkCollection(new ArrayList<TcRoleprincipallink>());
                roleList.add(role);
            }
        }
        RoleFacade.createRoles(roleList); // 自動建立相關的 TcRoleholderrolemap ??
    }
    
//    public Collection<TcActivitytemplate> getActivityTempletByProcessName(String processName){
//        Collection<TcActivitytemplate> activitytemplate = new ArrayList<TcActivitytemplate>();
//        
//        String sql = "SELECT p FROM TcProcesstemplate p "
//                + " WHERE p.processname = :processname "
//                + "   AND p.disabled = 0";
//        try {
//                Query query = em.createQuery(sql);
//                query.setParameter("processname", processName);
//                TcProcesstemplate processtemplate = (TcProcesstemplate)query.getSingleResult();
//                activitytemplate = processtemplate.getTcActivitytemplateCollection();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        return activitytemplate;
//    }
    
    public TcProcess findByPK(Long processid){
        TcProcess process = new TcProcess();
        String sql = " select a from TcProcess a where a.id = :processid ";
        try{
            Query query = em.createQuery(sql);
            query.setParameter("processid", processid);
            process = (TcProcess)query.getSingleResult();
        }catch(Exception a){
            a.printStackTrace();
        }
        return process;
    }
}
