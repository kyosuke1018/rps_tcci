/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ecdemo.facade.banner;

import com.tcci.ecdemo.entity.EcBanner;
import com.tcci.ecdemo.model.banner.Banner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class EcBannerFacade {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public List<EcBanner> home() {
        Query q = em.createNamedQuery("EcBanner.findByCategory");
        q.setParameter("category", Banner.CAT_HOME);
        return q.getResultList();
    }
    
    public List<EcBanner> product(Long product_id) {
        // TODO: category=3(CAT_PROMOTION) 要與 product_id 關聯?
        String sql = "SELECT e FROM EcBanner e WHERE e.active=TRUE AND (e.category=2 OR e.category=3)";
        Query q = em.createQuery(sql);
        return q.getResultList();
    }
    
    public List<EcBanner> bonus() {
        Query q = em.createNamedQuery("EcBanner.findByCategory");
        q.setParameter("category", Banner.CAT_BONUS);
        return q.getResultList();
    }

    public List<EcBanner> reward() {
        Query q = em.createNamedQuery("EcBanner.findByCategory");
        q.setParameter("category", Banner.CAT_REWARD);
        return q.getResultList();
    }
    
}
