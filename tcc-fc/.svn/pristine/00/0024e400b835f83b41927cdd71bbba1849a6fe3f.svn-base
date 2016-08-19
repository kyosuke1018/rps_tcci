/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcActivityroutetemplate;
import com.tcci.fc.entity.bpm.TcActivitytemplate;
import com.tcci.fc.entity.bpm.TcProcesstemplate;
import com.tcci.fc.jaxb.ActivityroutetemplateConverter;
import com.tcci.fc.jaxb.ActivitytemplateConverter;
import com.tcci.fc.jaxb.ProcesstemplateConverter;
import com.tcci.fc.util.BPMException;
import java.lang.Long;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Jason.Yu
 */
@Stateless
@Named
public class BPMTemplateFacade {
    
    @EJB BPMFacade bpmFacade;
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    protected EntityManager getEntityManager(){
        return em;
    }
    public List<TcProcesstemplate> getProcessTempaltes(){
        List<TcProcesstemplate> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<TcProcesstemplate> cq = builder.createQuery(TcProcesstemplate.class);
        Root<TcProcesstemplate> root = cq.from(TcProcesstemplate.class);
        cq.select(root);
        cq.orderBy(builder.asc(root.get("processname")));
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
    
    public TcProcesstemplate getLatestProcessTempalteByName(String processName){
        TcProcesstemplate processtemplate = null;
        List<TcProcesstemplate> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<TcProcesstemplate> cq = builder.createQuery(TcProcesstemplate.class);
        Root<TcProcesstemplate> root = cq.from(TcProcesstemplate.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if( processName != null && !processName.isEmpty() ){
            Predicate p = builder.equal(root.get("processname"), processName);
            predicateList.add(p);
        }
        if( predicateList.size() > 0){
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        cq.orderBy(builder.desc(root.get("processversion")));
        list = getEntityManager().createQuery(cq).getResultList();
        
        if( list != null && !list.isEmpty()){
            processtemplate = list.get(0);
        }
        return processtemplate;
    }
    public List<TcProcesstemplate> getLatestProcessTemplates(){
        List<TcProcesstemplate> list = getProcessTempaltes();
        List<TcProcesstemplate> latestList = null;
        if( list != null && !list.isEmpty() ){
            latestList = new ArrayList<TcProcesstemplate>();
            List<String> nameList = new ArrayList<String>();
            Map<String,TcProcesstemplate> map = new HashMap<String,TcProcesstemplate>();
            for( TcProcesstemplate processtemplate : list){
                if( !nameList.contains(processtemplate.getProcessname()))
                    nameList.add(processtemplate.getProcessname());
                if( map.containsKey( processtemplate.getProcessname()) ){
                    TcProcesstemplate p = map.get(processtemplate.getProcessname());
                    if( processtemplate.getProcessversion().compareTo( p.getProcessversion() ) > 0 ) {
                        map.put(processtemplate.getProcessname(), processtemplate);
                    }
                }else{
                    map.put(processtemplate.getProcessname(), processtemplate);
                }
            }
            
            for( String name : nameList){
                TcProcesstemplate p = map.get(name);
                latestList.add(p);
            }
        }
        return latestList;
    }
    
    public void saveProcessTemplate(ProcesstemplateConverter p, TcProcesstemplate selectedProcessTemplate)throws BPMException, Exception{
        if( selectedProcessTemplate!=null && !selectedProcessTemplate.getProcessname().equalsIgnoreCase(p.getProcessname())){
            throw new BPMException("Error : The process name is different,selected process name=" + selectedProcessTemplate.getProcessname()+ ",import process name=" + p.getProcessname());
        }
        Map<String,TcActivitytemplate> activityMap = new HashMap<String,TcActivitytemplate>();
        TcProcesstemplate t = getLatestProcessTempalteByName(p.getProcessname());
        String processversion = "1.0";
        String diabled = "0";
        boolean isNew = ((selectedProcessTemplate==null) ? true : false);
        if( isNew && t != null){
            throw new BPMException("Error : The process(" + p.getProcessname()+ ") is existed,can't create as new one!");
        }else if( t != null){
            processversion= BigDecimal.valueOf( Double.valueOf(t.getProcessversion()) ).add(BigDecimal.ONE).toString();
            t.setDisabled(Short.valueOf("1"));
            getEntityManager().merge(t);
        }
        
        
        TcProcesstemplate processtemplate = wrapperProcessTemplate(p,processversion,diabled);
        List<TcActivitytemplate> activitytemplateList = new ArrayList<TcActivitytemplate>();
        if( p.getActivitytemplate() != null){
            for( ActivitytemplateConverter at : p.getActivitytemplate() ){
                TcActivitytemplate activitytemplate = wrapperActivityTemplate(at,processtemplate);
                activitytemplateList.add(activitytemplate);
                activityMap.put(activitytemplate.getActivityname(), activitytemplate);
            }
        }
        
        if( p.getActivityroutetemplate() != null ){
            for( ActivityroutetemplateConverter art : p.getActivityroutetemplate() ){
                TcActivityroutetemplate routetemplate = wrapperActivityRouteTemplate( art , activityMap);
            }
        }
        processtemplate.setTcActivitytemplateCollection(activitytemplateList);
        getEntityManager().merge(processtemplate);
    }
    
    private TcProcesstemplate wrapperProcessTemplate(ProcesstemplateConverter p,String processversion, String disabled){
        TcProcesstemplate processtemplate = new TcProcesstemplate();
        processtemplate.setProcessname(p.getProcessname());
        processtemplate.setProcessversion(processversion);
        processtemplate.setDisabled(Short.valueOf(disabled));
        getEntityManager().persist(processtemplate);
        return processtemplate;
    }
    
    private TcActivitytemplate wrapperActivityTemplate(ActivitytemplateConverter at,TcProcesstemplate processtemplate){
        TcActivitytemplate activitytemplate = new TcActivitytemplate();
        activitytemplate.setActivityname(at.getActivityname());
        activitytemplate.setActivitytype(at.getActivitytype());
        activitytemplate.setRolename(at.getRolename());
        activitytemplate.setDuration( Long.valueOf( at.getDuration() ) );
        activitytemplate.setExpression(at.getExpression());
        activitytemplate.setProcesstemplateid( processtemplate );
        getEntityManager().persist(activitytemplate);
        /*
        Collection<TcActivitytemplate> collection = processtemplate.getTcActivitytemplateCollection();
        List<TcActivitytemplate> list = new ArrayList<TcActivitytemplate>();
        if( collection == null){
            list.add(activitytemplate);
        }else{
            list.addAll(collection);
            list.add(activitytemplate);
        }
        processtemplate.setTcActivitytemplateCollection(list);
        getEntityManager().merge(processtemplate);
        */
        return activitytemplate;
    }
    
    private TcActivityroutetemplate wrapperActivityRouteTemplate(ActivityroutetemplateConverter art, Map<String,TcActivitytemplate> activityMap){
        TcActivityroutetemplate routetemplate = new TcActivityroutetemplate();
        TcActivitytemplate from = activityMap.get(art.getFromactivity());
        TcActivitytemplate to = activityMap.get(art.getToactivity());
        routetemplate.setRoutename(art.getRoutename());
        routetemplate.setFromactivity(from);
        routetemplate.setToactivity(to);
        getEntityManager().persist(routetemplate);
        
        if( from.getTcActivityroutetemplateCollection() == null ){
            List<TcActivityroutetemplate> list = new ArrayList<TcActivityroutetemplate>();
            list.add(routetemplate);
        }else{
            from.getTcActivityroutetemplateCollection().add(routetemplate);
        }
        getEntityManager().merge(from);
        
        if( to.getTcActivityroutetemplateCollection1() == null ){
            List<TcActivityroutetemplate> list = new ArrayList<TcActivityroutetemplate>();
            list.add(routetemplate);
        }else{
            to.getTcActivityroutetemplateCollection1().add(routetemplate);
        }
        getEntityManager().merge(to);
        return routetemplate;
    }
}
