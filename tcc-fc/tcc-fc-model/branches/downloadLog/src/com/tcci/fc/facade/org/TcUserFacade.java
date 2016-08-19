/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.facade.org;

import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
@Named
public class TcUserFacade extends AbstractFacade<TcUser> {
    
    @Resource SessionContext sessionContext;
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;


    protected EntityManager getEntityManager() {
        return em;
    }

    public TcUserFacade() {
        super(TcUser.class);
    }
    
    public TcUser getSessionUser() {
        Principal principal = sessionContext.getCallerPrincipal();
        TcUser sessionUser = findUserByLoginAccount(principal.getName());
	return sessionUser;
    }
    
    public TcUser findUserByLoginAccount(String loginAccount){
        TcUser obj = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<TcUser> root = criteriaQuery.from(TcUser.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("loginAccount"), loginAccount));
        try {
            obj = (TcUser) getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
    
    public TcUser getUserByEmail(String email){
        TcUser obj = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<TcUser> root = criteriaQuery.from(TcUser.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(criteriaBuilder.lower(root.get("email").as(String.class)), email.toLowerCase()));
        try {
            obj = (TcUser) getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
    
    public TcUser findUserByEmpId(String empId){
        TcUser obj = null;
        if(empId == null){
            return obj;
        }
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<TcUser> root = criteriaQuery.from(TcUser.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("empId"), empId));
        try {
//            obj = (TcUser) getEntityManager().createQuery(criteriaQuery).getSingleResult();
            List<TcUser> results = getEntityManager().createQuery(criteriaQuery).getResultList();
            if(!results.isEmpty()){
                // ignores multiple results
                obj = results.get(0);
            }             
        } catch (NoResultException e) {
        }
        return obj;
    }
    
    public List<TcGroup> findGroupOfUser(TcUser tcuser) {
        List<TcGroup> list = null;
        CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<TcGroup> cq = builder.createQuery(TcGroup.class);
        Root<TcGroup> root = cq.from(TcGroup.class);
        cq.select(root);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (tcuser != null) {
            Predicate p = builder.equal(root.get("tcUsergroupCollection").get("userId"), tcuser);
            predicateList.add(p);
        }
        
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = getEntityManager().createQuery(cq).getResultList();
        return list;
    }
    
    public List<TcUser> findUsersByGroupCode(String groupCode) {
        String sql = "SELECT DISTINCT ug.userId FROM TcUsergroup ug"
                + " WHERE ug.groupId.code=:groupCode"
                ;
        Query q = em.createQuery(sql);
        q.setParameter("groupCode", groupCode);
        return q.getResultList();
    }
}
