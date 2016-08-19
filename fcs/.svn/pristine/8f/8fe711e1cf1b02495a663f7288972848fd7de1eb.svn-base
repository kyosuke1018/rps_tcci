/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.facade;

import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fcs.entity.FcConfig;
import com.tcci.fcs.enums.CompanyGroupEnum;
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
public class FcConfigFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;

    public String findValue(FcConfigKeyEnum key) {
        FcConfig config = findByKey(key);
        return (null == config) ? null : config.getConfigValue();
    }
    
    public String findValue(FcConfigKeyEnum key, CompanyGroupEnum group) {
        if(null == group){
            return this.findValue(key);
        }
        FcConfig config = findByKeyAndComp(key, group);
        return (null == config) ? null : config.getConfigValue();
    }
    
    public void saveValue(FcConfigKeyEnum key, String value) {
        FcConfig config = findByKey(key);
        if (null == config) {
            config = new FcConfig(key.toString(), value);
            em.persist(config);
        } else {
            config.setConfigValue(value);
            em.merge(config);
        }
    }
    
    public void saveValue(FcConfigKeyEnum key, String value, CompanyGroupEnum group) {
        if(null == group){
            this.saveValue(key, value);
            return;
        }
        FcConfig config = findByKeyAndComp(key, group);
        if (null == config) {
            config = new FcConfig(key.toString(), value);
            em.persist(config);
        } else {
            config.setConfigValue(value);
            em.merge(config);
        }
    }
    
    public boolean findValueBoolean(FcConfigKeyEnum key, boolean def) {
        String value = findValue(key);
        return (null == value) ? def : "1".equals(value);
    }
    
    public boolean findValueBoolean(FcConfigKeyEnum key, boolean def, CompanyGroupEnum group) {
        if(null == group){
            return this.findValueBoolean(key, def);
        }
        String value = findValue(key, group);
        return (null == value) ? def : "1".equals(value);
    }
    
    public void saveValueBoolean(FcConfigKeyEnum key, boolean value) {
        saveValue(key, value ? "1" : "0");
    }
    
    public void saveValueBoolean(FcConfigKeyEnum key, boolean value, CompanyGroupEnum group) {
        if(null == group){
            this.saveValueBoolean(key, value);
            return;
        }
        saveValue(key, value ? "1" : "0", group);
    }

    // helper
    public FcConfig findByKey(FcConfigKeyEnum key) {
        Query q = em.createNamedQuery("FcConfig.findByKey");
        q.setParameter("key", key.toString());
        List<FcConfig> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    //20151020 企業團獨立開關帳
    private FcConfig findByKeyAndComp(FcConfigKeyEnum key, CompanyGroupEnum group) {
        Query q = em.createNamedQuery("FcConfig.findByKeyAndComp");
        q.setParameter("key", key.toString());
        q.setParameter("group", group);
        List<FcConfig> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<FcConfig> findConigListByKey(FcConfigKeyEnum key) {
        Query q = em.createNamedQuery("FcConfig.findByKey");
        q.setParameter("key", key.toString());
        List<FcConfig> list = q.getResultList();
        return list;
    }
    
}
