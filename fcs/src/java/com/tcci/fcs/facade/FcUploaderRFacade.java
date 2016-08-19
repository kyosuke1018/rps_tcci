/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.entity.FcUploaderR;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class FcUploaderRFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public FcUploaderR find(Long id) {
        return em.find(FcUploaderR.class, id);
    }
    
    public List<FcUploaderR> findByCompanyCode(String companyCode) {
        String sql = "SELECT u FROM FcUploaderR u"
                + " WHERE u.fcCompany.code=:code";
        Query q = em.createQuery(sql);
        q.setParameter("code", companyCode);
        return q.getResultList();
    }
    
    public void save(FcUploaderR fcUploaderR) {
        if (fcUploaderR.getId()==null) {
            fcUploaderR.setCreatetimestamp(new Date());
            em.persist(fcUploaderR);
        } else {
            em.merge(fcUploaderR);
        }
    }
    
    public void remove(Long id){
        if (id!=null) {
            em.remove(this.find(id));
        }
    }
    
}
