/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fc.facade.org;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Gilbert.Lin
 */
@Stateless
public class TcUserFacade extends AbstractFacade<TcUser> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    @Resource
    private javax.transaction.UserTransaction utx;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TcUserFacade() {
        super(TcUser.class);
    }

    
    public TcUser getTcUserListByLoginAccount(String loginAccount) {
      Query q = null;
      StringBuilder jpql_sb = new StringBuilder();
      jpql_sb.append("SELECT t FROM TcUser t ");
      jpql_sb.append("WHERE t.isValid = 'Y' and t.loginAccount = :loginAccount");
      
//      System.out.println("jpql_sb="+jpql_sb.toString());
      q = em.createQuery(jpql_sb.toString());  
      
      q.setParameter ("loginAccount", loginAccount);
        return (TcUser)q.getSingleResult();
    }

    public TcUser findUserByEmpId(String empId){
        TcUser obj = null;
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        Root<TcUser> root = criteriaQuery.from(TcUser.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("empId"), empId));
        try {
            obj = (TcUser) getEntityManager().createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
        }
        return obj;
    }
}
