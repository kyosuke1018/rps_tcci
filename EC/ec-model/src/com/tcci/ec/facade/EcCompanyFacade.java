/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcCompany;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcVendor;
import com.tcci.ec.enums.FileEnum;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcCompanyFacade extends AbstractFacade<EcCompany> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCompanyFacade() {
        super(EcCompany.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcCompany entity, EcMember operator, boolean simulated){
//        if( entity!=null ){
//            if( entity.getId()!=null && entity.getId()>0 ){
//                entity.setModifier(operator);
//                entity.setModifytime(new Date());
//                this.edit(entity, simulated);
//                logger.info("save update "+entity);
//            }else{
//                entity.setCreator(operator);
//                entity.setCreatetime(new Date());
//                this.create(entity, simulated);
//                logger.info("save new "+entity);
//            }
//        }
//    }
    public EcCompany save(EcCompany entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
        return entity;
    }
    
    public List<EcCompany> findByStore(Long storeId) {
        Query q = em.createNamedQuery("EcCompany.findByStore");
        q.setParameter("storeId", storeId);
        List<EcCompany> list = q.getResultList();
        return list;
    }
    
    public List<EcCompany> findByMainId(Long mainId, String type) {
        Query q = em.createNamedQuery("EcCompany.findByMainId");
        q.setParameter("mainId", mainId);
        q.setParameter("type", type);
        List<EcCompany> list = q.getResultList();
        return list;
    }
    
    public List<EcFile> findImageByMember(Long memberId) {
        Query q = em.createNamedQuery("EcFile.findByPrimary");
        String primaryType = FileEnum.MEMBER_PIC.getPrimaryType();
//        logger.debug("EcProduct:"+primaryType);
        q.setParameter("primaryType", primaryType);
        q.setParameter("primaryId", memberId);
        return q.getResultList();
    }
    
}
