/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.datawarehouse;

import com.tcci.tccstore.entity.datawarehouse.ZtvkoCn;
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
public class ZtvkoCnFacade {
    @PersistenceContext(unitName = "DatawarehousePU")
    private EntityManager em;

    private List<ZtvkoCn> listz = new ArrayList<ZtvkoCn>();
    
    @PostConstruct
    private void init() {
        reload();
    }
    
    public void reload() {
        String sql = "SELECT VKORG, VTEXT FROM ZTVKO_CN";
        Query q = em.createNativeQuery(sql);
        List list = q.getResultList();
        listz.clear();
        for (Object row : list) {
            Object[] columns = (Object[]) row;
            int idx = 0;
            String vkorg = (String) columns[idx++];
            String vtext = (String) columns[idx++];
            
            ZtvkoCn zt = new ZtvkoCn();
            zt.setVkorg(vkorg);
            zt.setVtext(vtext);
            listz.add(zt);
        }
    }
    
    public List<ZtvkoCn> getAllZtvkoCn(){
        return listz;
    }
    
}
