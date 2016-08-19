package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcVariable;
import com.tcci.fc.entity.essential.Persistable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Named
public class VariableFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
//    @EJB
//    private EssentialFacade essentialFacade;

    public void updateVariables(Persistable container, HashMap map) {
        Set set = map.keySet();        
        Iterator i = set.iterator();
        
        while (i.hasNext()) {
            String key = (String) i.next();
            String value = map.get(key).toString();
            
            TcVariable variable = getTcVariable(container, key);
            if (variable == null) {
                variable = new TcVariable();
                variable.setName(key);
                variable.setValue(value);
                variable.setContainerclassname(container.getClass().getName());
                variable.setContainerid(container.getId());                
                em.persist(variable);
            }else {
                variable.setValue(value);
                em.merge(variable);
            }
        } 
    }
    
    public void createVariables(Persistable container, HashMap map) {
        Set set = map.keySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            TcVariable variable = new TcVariable();
            variable.setName(key);
            variable.setValue(map.get(key).toString());
            variable.setContainerclassname(container.getClass().getName());
            variable.setContainerid(container.getId());
            em.persist(variable);
        }     //}
    }

    public HashMap getVariables(Persistable container) {
        String statement = "SELECT b FROM TcVariable b "
                + "WHERE b.containerclassname=:containerclassname "
                + "   AND b.containerid=:containerid ";
        System.out.println("container=" + container);
        Query query = em.createQuery(statement);
        System.out.println("classCanName=" + container.getClass().getCanonicalName());
        System.out.println("className=" + container.getClass().getName());
        query.setParameter("containerclassname", container.getClass().getCanonicalName());
        query.setParameter("containerid", container.getId());
        List<TcVariable> result = query.getResultList();
        HashMap resultMap = new HashMap();
        for (TcVariable var : result) {
            resultMap.put(var.getName(), var.getValue());
        }
        return resultMap;
    }

    public HashMap getVariables(Persistable container, boolean visible) {
        String statement = "SELECT b FROM TcVariable b "
                + "WHERE b.containerclassname=:containerclassname "
                + "   AND b.containerid=:containerid and b.visible=:visible ";
        System.out.println("container=" + container);
        Query query = em.createQuery(statement);
        System.out.println("classCanName=" + container.getClass().getCanonicalName());
        System.out.println("className=" + container.getClass().getName());
        query.setParameter("containerclassname", container.getClass().getCanonicalName());
        query.setParameter("containerid", container.getId());
        query.setParameter("visible", visible);
        List<TcVariable> result = query.getResultList();
        HashMap resultMap = new HashMap();
        for (TcVariable var : result) {
            resultMap.put(var.getName(), var.getValue());
        }
        return resultMap;
    }

    public String getVariable(Persistable container, String name) {
        return (String) getVariables(container).get(name);
    }
    
    public TcVariable getTcVariable(Persistable container, String name) {
        String statement = "SELECT b FROM TcVariable b "
                + "WHERE b.containerclassname=:containerclassname "
                + "   AND b.containerid=:containerid "
                + "   AND b.name=:name ";
        System.out.println("container=" + container);
        Query query = em.createQuery(statement);
        System.out.println("classCanName=" + container.getClass().getCanonicalName());
        System.out.println("className=" + container.getClass().getName());
        query.setParameter("containerclassname", container.getClass().getCanonicalName());
        query.setParameter("containerid", container.getId());
        query.setParameter("name", name);
        List<TcVariable> result = query.getResultList();        
        
        if (result.isEmpty()) {
            return null;
        }else {
            return result.get(0);
        }
    }
    
    public void saveTcVariable(TcVariable variable) {
        if (variable == null)
            return;
        if (variable.getId() == null) {
            em.persist(variable);
        } else {
            em.merge(variable);
        }                
    }
    
}
