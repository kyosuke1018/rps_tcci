/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.worklist.facade.datawarehouse;

import com.tcci.fc.facade.AbstractFacade;
import com.tcci.worklist.entity.datawarehouse.RelfilenoEmp;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jackson.Lee
 */
@Stateless
public class RelfilenoEmpFacade extends AbstractFacade<RelfilenoEmp> {

    @PersistenceContext(unitName = "datawarehousePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RelfilenoEmpFacade() {
        super(RelfilenoEmp.class);
    }
    
    public RelfilenoEmp findByEmpCode(String empCode) {
        String queryStr = "SELECT object(o) from RelfilenoEmp o where o.empCode = :empCode";
        Query query = em.createQuery(queryStr);
        query.setParameter("empCode",empCode);
        List<RelfilenoEmp> resultList = query.getResultList();
        if(resultList.size()>0) {
            return resultList.get(0);
        }else {
            return null;
        }
    }
}
