/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.banner;

import com.tcci.tccstore.entity.EcBanner;
import com.tcci.tccstore.entity.EcProduct;
import com.tcci.tccstore.enums.BannerCategoryEnum;
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
        return findByCategory(BannerCategoryEnum.HOME);
    }
    
    public List<EcBanner> bonus() {
        return findByCategory(BannerCategoryEnum.BONUS);
    }

    public List<EcBanner> gold() {
        return findByCategory(BannerCategoryEnum.GOLD);
    }
    
    public List<EcBanner> productUsage() {
        return findByCategory(BannerCategoryEnum.PRODUCT_USAGE);
    }
    
    public List<EcBanner> findByCategory(BannerCategoryEnum category) {
        Query q = em.createNamedQuery("EcBanner.findByCategory");
        q.setParameter("category", category.getValue());
        return q.getResultList();
    }
    
    public List<EcBanner> findByCategoryAndProduct(BannerCategoryEnum category, EcProduct ecProduct) {
        String sql = "SELECT e.ecBanner"
                + " FROM EcBannerProduct e"
                + " WHERE e.ecBanner.active=TRUE"
                + " AND e.ecBanner.category=:category"
                + " AND e.ecProduct=:ecProduct";
        Query q = em.createQuery(sql);
        q.setParameter("category", category.getValue());
        q.setParameter("ecProduct", ecProduct);
        return q.getResultList();
    }

}
