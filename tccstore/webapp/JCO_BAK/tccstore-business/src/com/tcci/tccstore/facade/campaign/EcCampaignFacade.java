/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.campaign;

import com.tcci.tccstore.entity.EcCampaign;
import com.tcci.tccstore.facade.AbstractFacade;
import com.tcci.tccstore.facade.clicklog.EcClickLogFacade;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcCampaignFacade extends AbstractFacade {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @EJB
    private EcClickLogFacade ecClickLogFacade;

    private static List<EcCampaign> activeCampaigns;
    private static List<Long> activeIds;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCampaignFacade() {
        super(EcCampaign.class);
    }

    @PostConstruct
    private void init() {
        if (null == activeCampaigns) {
            reload();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateAndReload() {
        Query update = em.createQuery("UPDATE EcCampaign e SET e.active=FALSE WHERE e.active=TRUE AND e.endDate<:today");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        update.setParameter("today", sdf.format(new Date()));
        update.executeUpdate();
        reload();
    }
    
    public void reload() {
        Query q = em.createNamedQuery("EcCampaign.findActive");
        activeCampaigns = q.getResultList();
        activeIds = new ArrayList<>();
        for (EcCampaign entity : activeCampaigns) {
            activeIds.add(entity.getId());
        }
    }

    public List<EcCampaign> getAllCampaigns() {
        return activeCampaigns;
    }

    public List<Long> findActiveVisited(String clientKey) {
        if (activeIds.isEmpty()) {
            return new ArrayList<>();
        }
        String sql = "SELECT DISTINCT e.objId FROM EcClickLog e"
                + " WHERE e.clientKey=:clientKey"
                + " AND e.objType=:objType"
                + " AND e.objId in :activeIds";
        Query q = em.createQuery(sql);
        q.setParameter("clientKey", clientKey);
        q.setParameter("objType", EcCampaign.class.getSimpleName());
        q.setParameter("activeIds", activeIds);
        return q.getResultList();
    }
    
    public EcCampaign click(String clientKey, Long id) {
        EcCampaign entity = em.find(EcCampaign.class, id);
        if (entity != null) {
            ecClickLogFacade.click(clientKey, EcCampaign.class.getSimpleName(), id);
        }
        return entity;
    }
    
    public int unreadCount(String clientKey) {
        return activeCampaigns.size() - findActiveVisited(clientKey).size();
    }

}
