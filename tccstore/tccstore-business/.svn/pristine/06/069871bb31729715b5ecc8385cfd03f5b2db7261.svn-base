/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.goods;

import com.tcci.tccstore.entity.EcGoods;
import com.tcci.tccstore.entity.EcGoodsBuy;
import com.tcci.tccstore.entity.EcMember;
import com.tcci.tccstore.entity.EcPartner;
import com.tcci.tccstore.entity.EcPartnerGoods;
import com.tcci.tccstore.entity.EcPartnerGoodsPK;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
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
public class EcGoodsFacade extends AbstractFacade<EcGoods> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcGoodsFacade() {
        super(EcGoods.class);
    }

    // 夥伴商品清單(主檔, active=true)
    public List<EcGoods> findAllActive() {
        Query q = em.createNamedQuery("EcGoods.findAllActive");
        return q.getResultList();
    }
    
    // 夥伴商品清單(only parter, goods are active)
    public List<EcGoods> allPartnerGoods() {
        Query q = em.createNamedQuery("EcPartnerGoods.findAllPartnerGoods");
        return q.getResultList();
    }

    // 夥伴商品購買記錄
    public void goodsBuy(EcGoods ecGoods, EcMember ecMember, int quantity, String uom) {
        EcGoodsBuy entity = new EcGoodsBuy(ecGoods, ecMember, quantity, uom);
        em.persist(entity);
    }

    public List<EcGoods> findByPartner(EcPartner ecPartner) {
        Query q = em.createNamedQuery("EcPartnerGoods.findByPartner");
        q.setParameter("ecPartner", ecPartner);
        return q.getResultList();
    }
    
    public void udpatePartnerGoods(EcPartner ecPartner, List<EcGoods> partnerGoods) {
        Query q = em.createQuery("SELECT e FROM EcPartnerGoods e WHERE e.ecPartner=:ecPartner");
        q.setParameter("ecPartner", ecPartner);
        List<EcPartnerGoods> origGoods = q.getResultList();
        List<EcGoods> insertGoods = new ArrayList<>(partnerGoods);
        // 刪除
        for (EcPartnerGoods orig : origGoods) {
            if (!partnerGoods.contains(orig.getEcGoods())) {
                em.remove(orig);
            } else {
                insertGoods.remove(orig.getEcGoods());
            }
        }
        // 新增
        for (EcGoods g : insertGoods) {
            EcPartnerGoodsPK pk = new EcPartnerGoodsPK(ecPartner.getId(), g.getId());
            EcPartnerGoods entity = new EcPartnerGoods(pk);
            entity.setEcPartner(ecPartner);
            entity.setEcGoods(g);
            em.persist(entity);
        }
    }

}
