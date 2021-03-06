/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpmtemplate;

import com.tcci.fc.entity.bpm.TcActivityroutetemplate;
import com.tcci.fc.entity.bpm.TcActivitytemplate;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.entity.bpm.enumeration.ActivityTypeEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class TcProcessTemplateFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public TcProcesstemplate findById(Long id) {
        Query q = em.createQuery("SELECT t FROM TcProcesstemplate t WHERE t.id=:id");
        q.setParameter("id", id);
        List<TcProcesstemplate> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<TcProcesstemplate> findAll() {
        return em.createQuery("SELECT t FROM TcProcesstemplate t ORDER BY t.processname, t.id").getResultList();
    }
    
    public void save(TcProcesstemplate processTemplate, List<ActivityVO> activityList, List<RouteVO> routeList) {
        if (processTemplate.getId() == null) {
            em.persist(processTemplate);
        } else {
            em.merge(processTemplate);
        }
        // 處理 activities
        Collection<TcActivitytemplate> origActs = processTemplate.getTcActivitytemplateCollection(); // 原始 activity
        Collection<TcActivitytemplate> newActs = new ArrayList<TcActivitytemplate>(); // 新增(更新)的 activity
        Collection<TcActivitytemplate> delActs = new ArrayList<TcActivitytemplate>(); // 被刪除的 activity
        processActivties(activityList, origActs, newActs, delActs, processTemplate);  // 算出 newActs, delActs
        // 處理 routes
        Collection<TcActivityroutetemplate> origRoutes = new ArrayList<TcActivityroutetemplate>(); // 原始的 route
        for (TcActivitytemplate act : origActs) {
            origRoutes.addAll(act.getTcActivityroutetemplateCollection());
        }
        Collection<TcActivityroutetemplate> newRoutes = new ArrayList<TcActivityroutetemplate>(); // 新增(更新)的 route
        Collection<TcActivityroutetemplate> delRoutes = new ArrayList<TcActivityroutetemplate>(); // 被刪除的 route
        processRoutes(routeList, origRoutes, newRoutes, delRoutes, newActs); // 算出 newRoutes, delRoutes
        
        removeRoutesActivities(delRoutes, delActs); // 刪除 delRoutes, delActs
        saveActivitiesRoutes(newActs, newRoutes); // 儲存 newActs, newRoutes
        processTemplate.setTcActivitytemplateCollection(newActs); // 更新 template
        em.merge(processTemplate);
    }

    private void processActivties(List<ActivityVO> activityList, Collection<TcActivitytemplate> origActs, 
            Collection<TcActivitytemplate> newActs, Collection<TcActivitytemplate> delActs,
            TcProcesstemplate processTemplate) {
        Map<String, ActivityVO> mapIActivityVO = new HashMap<String, ActivityVO>();
        for (ActivityVO avo : activityList) {
            mapIActivityVO.put(avo.getActivityname(), avo);
        }
        for (TcActivitytemplate origAct : origActs) {
            String key = origAct.getActivityname();
            ActivityVO avo = mapIActivityVO.get(key);
            if (null == avo) {
                delActs.add(origAct); // 刪除的act
            } else {
                updateActivity(origAct, avo);
                newActs.add(origAct); // 更新的act
                mapIActivityVO.remove(key);
            }
        }
        for (ActivityVO avo : mapIActivityVO.values()) {
            TcActivitytemplate newAct = new TcActivitytemplate();
            newAct.setProcesstemplateid(processTemplate);
            updateActivity(newAct, avo);
            newActs.add(newAct); // 新增act
        }
    }
    
    private void updateActivity(TcActivitytemplate activity, ActivityVO avo) {
        activity.setActivityname(avo.getActivityname());
        activity.setActivitytype(ActivityTypeEnum.fromValue(avo.getActivitytype()));
        activity.setOptions(avo.getOptions());
        activity.setDuration(avo.getDuration());
        activity.setExpression(avo.getExpression());
        activity.setRolename(avo.getRolename());
    }
    
    private void processRoutes(List<RouteVO> routeList, Collection<TcActivityroutetemplate> origRoutes,
            Collection<TcActivityroutetemplate> newRoutes, Collection<TcActivityroutetemplate> delRoutes,
            Collection<TcActivitytemplate> newActs) {
        Map<String, RouteVO> mapIRouteVO = new HashMap<String, RouteVO>();
        for (RouteVO rvo : routeList) {
            String key = rvo.getFromactivity() + ":" + rvo.getToactivity();
            mapIRouteVO.put(key, rvo);
        }
        Map<String, TcActivitytemplate> mapActivity = new HashMap<String, TcActivitytemplate>();
        for (TcActivitytemplate act : newActs) {
            mapActivity.put(act.getActivityname(), act);
            act.setTcActivityroutetemplateCollection(new ArrayList<TcActivityroutetemplate>());
            act.setTcActivityroutetemplateCollection1(new ArrayList<TcActivityroutetemplate>());
        }
        for (TcActivityroutetemplate origRoute : origRoutes) {
            String key = origRoute.getFromactivity().getActivityname() + ":" + origRoute.getToactivity().getActivityname();
            RouteVO rvo = mapIRouteVO.get(key);
            if (null == rvo) {
                delRoutes.add(origRoute);
            } else {
                updateRoute(origRoute, rvo, mapActivity);
                newRoutes.add(origRoute);
                mapIRouteVO.remove(key);
            }
        }
        for (RouteVO rvo : mapIRouteVO.values()) {
            TcActivityroutetemplate newRoute = new TcActivityroutetemplate();
            updateRoute(newRoute, rvo, mapActivity);
            newRoutes.add(newRoute);
        }
    }
    
    private void updateRoute(TcActivityroutetemplate route, RouteVO rvo, Map<String, TcActivitytemplate> mapActivity) {
        route.setRoutename(rvo.getRoutename());
        TcActivitytemplate fromAct = mapActivity.get(rvo.getFromactivity());
        TcActivitytemplate toAct = mapActivity.get(rvo.getToactivity());
        route.setFromactivity(fromAct);
        route.setToactivity(toAct);
        fromAct.getTcActivityroutetemplateCollection().add(route);
        toAct.getTcActivityroutetemplateCollection1().add(route);
    }
    
    private void removeRoutesActivities(Collection<TcActivityroutetemplate> delRoutes, Collection<TcActivitytemplate> delActs) {
        for (TcActivityroutetemplate delRoute : delRoutes) {
            em.remove(em.merge(delRoute));
        }
        for (TcActivitytemplate delAct : delActs) {
            // CascadeType.ALL 所以 relation 要先移除 (routes已先刪除了)
            delAct.setTcActivityroutetemplateCollection(Collections.EMPTY_LIST);
            delAct.setTcActivityroutetemplateCollection1(Collections.EMPTY_LIST);
            em.remove(em.merge(delAct));
        }
    }
    
    private void saveActivitiesRoutes(Collection<TcActivitytemplate> newActs, Collection<TcActivityroutetemplate> newRoutes) {
        for (TcActivitytemplate newAct : newActs) {
            if (newAct.getId()==null) {
                em.persist(newAct);
            } else {
                em.merge(newAct);
            }
        }
        for (TcActivityroutetemplate newRoute : newRoutes) {
            if (newRoute.getId()==null) {
                em.persist(newRoute);
            } else {
                em.merge(newRoute);
            }
        }
    }

}
