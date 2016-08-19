package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.bpm.*;
import com.tcci.fc.entity.bpm.enumeration.*;
import com.tcci.fc.entity.essential.*;
import com.tcci.fc.facade.essential.EssentialFacade;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Named
public class BPMFacade {

    @PersistenceContext(unitName="Model")
    private EntityManager em;

    @EJB
    private EssentialFacade essentialFacade;

    public BPMFacade() {
    }

  

    /**
     * get all process template
     * 
     * @return List All process templates are returned
     */
    public List<TcProcesstemplate> getProcessTemplates() {
        List processTemplates = new ArrayList();
        String statement = "SELECT b FROM TcProcesstemplate b";
        statement += " order by b.processname";
        Query query = em.createQuery(statement);
        List list = query.getResultList();
        Hashtable ht = new Hashtable();

        for (Iterator ptit = list.iterator(); ptit.hasNext();) {
            TcProcesstemplate processTemplate = (TcProcesstemplate) ptit.next();
            String processName = processTemplate.getProcessname();
            if (!ht.containsKey(processName)) {
                ht.put(processName, processName);
                processTemplates.add(processTemplate);
            }
        }
        return processTemplates;
    }

    public List<TcProcess> getProcesses(String processName, String state) {
        return getProcesses(processName, state, null, null, null);
    }

    public List<TcProcess> getProcesses(String processName, String state, Date processCreateStart, Date processCreateEnd, Long objId) {
        boolean bstate = false;
        boolean bprocessname = false;
        boolean bprocessStart = false;
        boolean bprocessEnd = false;
        boolean bobj = false;

        String statement = "SELECT b FROM TcProcess b, TcProcesstemplate a "
                + "WHERE b.processtemplateid=a ";

        if (processName != null && !"".equals(processName)) {
            statement += " AND a.processname=:processname";
            bprocessname = true;
        }
        if (state != null && !"".equals(state)) {
            statement += " AND b.executionstate=:state";
            bstate = true;
        }
        if (processCreateStart != null) {
            statement += " AND b.starttimestamp>=:processstart";
            bprocessStart = true;
        }
        if (processCreateEnd != null) {
            statement += " AND b.starttimestamp<=:processend";
            bprocessEnd = true;
        }
        if (objId != null) {
            statement += " AND b.primaryobjectid=:objid";
            bobj = true;
        }

        statement += " order by b.starttimestamp desc";
        Query query = em.createQuery(statement);
        if (bprocessname) {
            query.setParameter("processname", processName);
        }
        if (bstate) {
            query.setParameter("state", ExecutionStateEnum.valueOf(state));
        }
        if (bprocessStart) {
            query.setParameter("processstart", processCreateStart);
        }
        if (bprocessEnd) {
            query.setParameter("processend", processCreateEnd);
        }
        if (bobj) {
            query.setParameter("objid", objId);
        }
        return query.getResultList();
    }

    /**
     * get all workitems
     * 
     * @param processName String Process name is wildcard, If process name is null,
     * @param participant String Workitem of the Participant. Participant is wildcard, if participant is null
     * @param state String NOT_START, RUNNING, COMPLETED, TERMINATED. If state is wildcard, if state is null
     * @return List <Bpmworkitem>
     */
    public List<TcProcess> getProcessesByPrimaryObject(ExecutionStateEnum state, String pboclassname, Long pboid) {
        String statement = "SELECT b FROM TcProcess b "
                + "WHERE b.primaryobjectclassname=:pboClassName "
                + "   AND b.primaryobjectid=:pboId ";
        if (state != null) {
            statement += " AND b.executionstate=:state";
        }

        statement += " order by b.processname,b.starttimestamp desc";
        Query query = em.createQuery(statement);
        if (pboclassname != null) {
            query.setParameter("primaryobjectclassname", pboclassname);
        }
        if (pboid != null) {
            query.setParameter("primaryobjectid", new Long(pboid));
        }
        if (state != null) {
            query.setParameter("executionstate", state);
        }
        return query.getResultList();
    }

    public List<TcProcess> getProcesses(TcUser participant, ExecutionStateEnum state) {
        List<TcProcess> list = new ArrayList();
        String statement = "select distinct b.activityid.processid "
                + "       from TcWorkitem b "
                + "      where b.owner = :participant"
                + "         and b.activityid.processid.executionstate = :state";

        list = em.createQuery(statement).
                setParameter("executionstate", state).
                setParameter("owner", participant).
                getResultList();

        return list;
    }

    public Persistable getPrimaryObject(TcWorkitem workItem) {
        return getPrimaryObject(workItem.getActivityid().getProcessid());
    }

    public Persistable getPrimaryObject(TcProcess process) {
        Persistable obj = null;
        try {
            if (process != null && process.getPrimaryobjectclassname() != null) {
                obj = (Persistable) essentialFacade.getObject(process.getPrimaryobjectclassname() + ":" + process.getPrimaryobjectid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * get all workitems
     * 
     * @return List <Bpmworkitem>
     */
    public List<TcWorkitem> getWorkItems() {
        return getWorkItemList(null, null, null, null, null);
    }

    /**
     * get all running workitems
     * 
     * @return List <Bpmworkitem>
     */
    public List<TcWorkitem> getUncompleteWorkItems(TcUser participant) {
        return getWorkItemList(null, participant, ExecutionStateEnum.RUNNING, null, null);
    }

    /**
     * get all running workitems
     * 
     * @return List <Bpmworkitem>
     */
    public List<TcWorkitem> getUncompleteWorkItems(TcUser participant, String pboClassName, Long pboId) {
        return getWorkItemList(null, participant, ExecutionStateEnum.RUNNING, pboClassName, pboId);
    }

    public List<TcWorkitem> getRunningWorkitems(TcUser participant) {
        String statement = "select a from TcWorkitem a "
                + " where a.executionstate = :executionstate and a.owner=:owner "
                + " order by a.starttimestamp DESC ";

        return em.createQuery(statement).
                setParameter("executionstate", ExecutionStateEnum.RUNNING).
                setParameter("owner", participant).
                getResultList();
    }

    /**
     * get all workitems by process
     * 
     * @param process Bpmprocess Workitems which the process generated
     * @return List <Bpmworkitem>
     */
    public List<TcWorkitem> getWorkItems(TcProcess process) {
        String statement = "";
        statement = "SELECT b FROM TcWorkitem b, TcActivity a "
                + "WHERE b.activityid=a "
                + "  AND NOT b.executionstate in (:state1, :state2)"
                + "   AND a.processid= :process";
        statement += " order by b.starttimestamp";
        Query query = em.createQuery(statement);
        query.setParameter("state1", ExecutionStateEnum.NOT_START);
        query.setParameter("state2", ExecutionStateEnum.TERMINATED);
        query.setParameter("process", process);
        return query.getResultList();
    }

    public List<TcWorkitem> getCompletedWorkItems(TcProcess process) {
//        String statement = "";
        String statement = "SELECT b FROM TcWorkitem b, TcActivity a "
                + "WHERE b.activityid=a "
                + "  AND NOT b.executionstate = :executionstate"
                + "   AND a.processid= :process";
        statement += " order by b.starttimestamp";
        Query query = em.createQuery(statement);
        query.setParameter("executionstate", ExecutionStateEnum.COMPLETED);
        query.setParameter("process", process);
        return query.getResultList();
    }

    /**
     * get all workitems
     * 
     * @param processName String Process name is wildcard, If process name is null,
     * @param participant String Workitem of the Participant. Participant is wildcard, if participant is null
     * @param state String NOT_START, RUNNING, COMPLETED, TERMINATED. If state is wildcard, if state is null
     * @return List <Bpmworkitem>
     */
    public List<TcWorkitem> getWorkItemList(String processname, TcUser participant, ExecutionStateEnum state, String pboclassname,
            Long pboid) {
        String statement = "";
        if (processname == null && pboclassname == null) {
            statement = "SELECT b FROM TcWorkitem b ";
        } else if (processname != null && pboclassname != null) {
            statement = "SELECT b FROM TcWorkitem b, TcActivity a, TcProcess p, TcProcesstemplate t "
                    + "WHERE b.activityid=a "
                    + "   AND a.processid=p "
                    + "   AND p.processtemplateid=t "
                    + "   AND t.processname=:processname"
                    + "   AND p.primaryobjectclassname=:pboClassName";
        } else if (pboclassname != null) {
            statement = "SELECT b FROM TcWorkitem b, TcActivity a, TcProcess p "
                    + "WHERE b.activityid=a "
                    + "   AND a.processid=p ";
        } else {
            statement = "SELECT b FROM TcWorkitem b, TcActivity a, TcProcess p, TcProcesstemplate t "
                    + "WHERE b.activityid=a "
                    + "   AND a.processid=p "
                    + "   AND p.processtemplateid=t "
                    + "   AND t.processname=:processname";
        }
        // WHERE b.participant = :participant
        if (participant != null) {
            if (statement.indexOf("WHERE") > 0) {
                statement += " AND b.owner=:participant";
            } else {
                statement += " WHERE b.owner=:participant";
            }
        }
        if (state != null) {
            if (statement.indexOf("WHERE") > 0) {
                statement += " AND b.executionstate=:state";
            } else {
                statement += " WHERE b.executionstate=:state";
            }
        }
        if (pboclassname != null) {
            if (statement.indexOf("WHERE") > 0) {
                statement += " AND p.primaryobjectclassname=:pboClassName";
            } else {
                statement += " WHERE p.primaryobjectclassname=:pboClassName";
            }
            if (pboid != null) {
                if (statement.indexOf("WHERE") > 0) {
                    statement += " AND p.primaryobjectid=:pboId";
                } else {
                    statement += " WHERE p.primaryobjectid=:pboId";
                }
            }

        }

        statement += " order by b.starttimestamp desc";
        Query query = em.createQuery(statement);
        if (processname != null) {
            query.setParameter("processname", processname);
        }
        if (participant != null) {
            query.setParameter("participant", participant);
        }
        if (state != null) {
            query.setParameter("state", state);
        }
        if (pboclassname != null) {
            query.setParameter("pboClassName", pboclassname);
            if (pboid != null) {
                query.setParameter("pboId", new Long(pboid));
            }
        }
        return query.getResultList();
    }
    
    /**
     * To get Bpmprocess by Process Id
     * 
     * @param processId BigDecimal
     * @return return Bpmprocess if found, else null.
     */
    public TcWorkitem getWorkitemById(String workItemId) throws Exception {
        if (workItemId == null) {
            throw new Exception("Error: BPM Engine is unable to find the workitem, because workitem id is null!");
        }
        TcWorkitem workitem = null;
        try {
            Query query = em.createQuery("select b from TcWorkitem b where b.id=:id"); // .setHint("refresh", "true");
            query.setParameter("id", new BigDecimal(workItemId));
            workitem = (TcWorkitem) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to get workitem by id", e);
        }
        return workitem;
    }

    /**
     * To get Bpmprocess by Process Id
     * 
     * @param processId BigDecimal
     * @return return Bpmprocess if found, else null.
     */
    public TcActivity getTcActivityById(String activityId) throws Exception {
        TcActivity activity = null;
        if (activityId == null) {
            throw new Exception("Error: BPM Engine is unable to find the activity, because activity id is null!");
        }
        try {
            Query query = em.createQuery("select b from TcActivity b where b.id=:id");
            query.setParameter("id", new Long(activityId));
            activity = (TcActivity) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Unable to get activity by Id", e);
        }

        return activity;
    }

    /**
     * To get the latest version of Bpmprocesstemplate by Process Template name.
     * 
     * @param processName Process template name
     * @return return Bpmprocesstemplate if found, else null.
     */
    public TcProcesstemplate getProcesstemplateByName(String processName) throws Exception {
        if (processName == null) {
            throw new Exception("Error: BPM Engine is unable to get process, because Process template name is null!");
        }

        Query query = em.createQuery("SELECT b FROM TcProcesstemplate b WHERE b.processname = :processname AND b.disabled<>1 order by b.id desc");
        query.setParameter("processname", processName);

        TcProcesstemplate processTemplate = null;
        List list = query.getResultList();
        if (list.size() > 0) {
            processTemplate = (TcProcesstemplate) list.get(0);
        }
        return processTemplate;
    }

    /**
     * To get Bpmprocess by Process Id
     * 
     * @param processId BigDecimal
     * @return return Bpmprocess if found, else null.
     */
    public List getProcessProgress(String processId) throws Exception {
        if (processId == null) {
            throw new Exception("Error: BPM Engine is unable to get process, because Process Id is null!");
        }
        Query query = em.createQuery("select b from TcActivity b where b.processid=:processid order by b.starttimestamp, b.state");
        query.setParameter("processid", new BigDecimal(processId));
        List list = query.getResultList();
        return list;
    }

    /**
     * To get Bpmprocess by Process Id
     * 
     * @param processId BigDecimal
     * @return return Bpmprocess if found, else null.
     */
    public TcProcess getProcessById(String processId) throws Exception {
        if (processId == null) {
            throw new Exception("Error: BPM Engine is unable to get process, because Process Id is null!");
        }
        Query query = em.createQuery("select p from TcProcess p where p.id=:id");
        query.setParameter("id", new BigDecimal(processId));
        TcProcess process = null;
        List list = query.getResultList();
        if (list.size() > 0) {
            process = (TcProcess) list.get(0);
        }
        return process;

    }

  public List<TcSignature> getSignatureList(String processId) throws Exception {
      return getSignatureList(getPrimaryObject(getProcessById(processId)));
  }

  public List<TcSignature> getSignatureList(Persistable persistable) {
        String statement = "select a from TcSignature a "
                + " where a.primaryobjectid = :pboId "
                + "   and a.primaryobjectclassname = :pboClassName";

        return em.createQuery(statement).
                setParameter("primaryobjectid", persistable.getId()).
                setParameter("pboClassName", persistable.getClass().getCanonicalName()).
                getResultList();

    }
    
    public List<TcActivity> getActivityByProcess(TcProcess process) {
        String statement = "select a from TcActivity a, TcActivitytemplate b"
                + " where a.activitytemplateid = b"
                + "       and a.processid = :processId "
                + " order by a.starttimestamp, b.id ";

        return em.createQuery(statement).
                setParameter("processId", process).
                getResultList();        
    }
}
