/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fc.facade.bpm;

import com.tcci.fc.entity.bpm.TcReassignConf;
import com.tcci.fc.entity.org.TcUser;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
@Named
public class TcReassignConfFacade {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public TcReassignConf find(Long id) {
        return em.find(TcReassignConf.class, id);
    }
    
    public List<TcReassignConf> findAll() {
        return em.createNamedQuery("TcReassignConf.findAll").getResultList();
    }
    
    public List<TcReassignConf> findByOwner(TcUser owner) {
        return em.createNamedQuery("TcReassignConf.findByOwner")
                .setParameter("owner", owner)
                .getResultList();
    }
    
    public List<TcReassignConf> findByNewowner(TcUser newowner) {
        return em.createNamedQuery("TcReassignConf.findByNewowner")
                .setParameter("newowner", newowner)
                .getResultList();
    }
    
    public void save(TcReassignConf entity) {
        save(entity, false);
    }
    
    public void save(TcReassignConf entity, boolean allowOverlap) {
        if (!allowOverlap) {
            List<TcReassignConf> existConfs = findByOwner(entity.getOwner());
            for (TcReassignConf conf : existConfs) {
                // 略過自己
                if (entity.equals(conf)) {
                    continue;
                }
                // 同processname或空白processname才要比較
                if (null == entity.getProcessname()
                        || null == conf.getProcessname()
                        || entity.getProcessname().equals(conf.getProcessname())) {
                    boolean check1 = (entity.getStarttime() == null
                            || conf.getEndtime() == null
                            || entity.getStarttime().compareTo(conf.getEndtime()) <= 0);
                    boolean check2 = (entity.getEndtime() == null
                            || conf.getStarttime() == null
                            || entity.getEndtime().compareTo(conf.getStarttime()) >= 0);
                    if (check1 && check2) {
                        throw new EJBException("duration overlap!");
                    }
                }
            }
        }
        entity.setModifytime(new Date());
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public void remove(TcReassignConf entity) {
        em.remove(em.merge(entity));
    }

    public TcReassignConf findCurrentConf(TcUser owner, String processname) {
        Query q = em.createNamedQuery("TcReassignConf.findCurrent");
        q.setParameter("owner", owner);
        q.setParameter("now", new Date());
        List <TcReassignConf> list = q.getResultList();
        for (TcReassignConf conf : list) {
            if (conf.getNewowner() != null && conf.getNewowner().getDisabled()) {
                continue;
            }
            if (conf.getProcessname()==null 
                    || processname==null 
                    || conf.getProcessname().equals(processname)) {
                return conf;
            }
        }
        return null;
    }

}
