/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.datawarehouse.Zt171Cn;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author carl.lin
 */
@Named
@Singleton
public class Zt171CnFacade extends AbstractFacade {
    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    public Zt171CnFacade(Class entityClass) {
        super(entityClass);
    }

     private List<Zt171Cn> listz = new ArrayList<Zt171Cn>();
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public  Zt171CnFacade() {
        super(Zt171Cn.class);
    }
    
    @PostConstruct
    private void init() {
        reload();
    }
    
    public void reload() {
        String sql = "SELECT BZIRK,BZTXT FROM ZT171_CN";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        listz.clear();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String bzirk = (String) columns[idx++];
            String bztxt = (String) columns[idx++];
           
            Zt171Cn zt = new Zt171Cn();
            zt.setBzirk(bzirk);
            zt.setBztxt(bztxt);
            listz.add(zt);
        }
    }
    
     public  List<Zt171Cn> getAllZt171Cn() {
        return  listz;
    }
    
}
