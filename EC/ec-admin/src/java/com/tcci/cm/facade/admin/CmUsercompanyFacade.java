/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmUsercompany;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.facade.AbstractFacade;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class CmUsercompanyFacade extends AbstractFacade<CmUsercompany> { 
    @PersistenceContext (unitName="Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public CmUsercompanyFacade() {
        super(CmUsercompany.class);
    }
    
    /**
     * 查詢公司權限
     * @return List<CmUsercompany>
     */
    public List<CmUsercompany> findByUser(TcUser tcUser) {
        Query q = em.createQuery("SELECT a FROM CmUsercompany a where a.userId=:userId ORDER BY a.sapClientCode");
        q.setParameter("userId", tcUser);
        
        return q.getResultList();
    }    
}
