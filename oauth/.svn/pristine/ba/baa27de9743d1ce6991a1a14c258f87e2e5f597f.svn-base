/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.datawarehouse.ZmaraCn;
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
public class ZmaraCnFacade extends AbstractFacade {
    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    public ZmaraCnFacade(Class entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public  ZmaraCnFacade() {
        super(ZmaraCn.class);
    }
    
    private List<ZmaraCn> listz = new ArrayList<ZmaraCn>();
    
    @PostConstruct
    private void init() {
        reload();
    }
    
    public void reload() {
        String sql = "SELECT MATNR,MAKTX FROM ZMARA_CN";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        listz.clear();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String matnr = (String) columns[idx++];
            String maktx = (String) columns[idx++];
           
            ZmaraCn zt = new ZmaraCn();
            zt.setMatnr(matnr);
            zt.setMaktx(maktx);
            listz.add(zt);
        }
    }
    
    
    public  List<ZmaraCn> getAllZmaraCn() {
        return  listz;
    }
}
