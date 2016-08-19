/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.datawarehouse.Zt001wCn;
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
public class Zt001wCnFacade extends AbstractFacade {
    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public  Zt001wCnFacade() {
        super(Zt001wCn.class);
    }
    
    private List<Zt001wCn> listz = new ArrayList<Zt001wCn>();
    
    @PostConstruct
    private void init() {
        reload();
    }
    
    public void reload() {
        String sql = "SELECT WERKS,NAME1 FROM ZT001W_CN ";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        listz.clear();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String werks = (String) columns[idx++];
            String name1 = (String) columns[idx++];
           
            Zt001wCn zt = new Zt001wCn();
            zt.setWerks(werks);
            zt.setName1(name1);
            listz.add(zt);
        }
    }
    
    public  List<Zt001wCn> getAllZt001wCn() {
        return  listz;
    }
}
