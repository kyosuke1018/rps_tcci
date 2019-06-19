/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcTccDealerDs;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcTccDealerDsFacade extends AbstractFacade<EcTccDealerDs> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcTccDealerDsFacade() {
        super(EcTccDealerDs.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcTccDealerDs entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }
    
    public void saveByKey(Long dealerId, Long dsId, EcMember operator, boolean simulated){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealerId", dealerId);
        params.put("dsId", dsId);
        
        List<EcTccDealerDs> list = this.findByNamedQuery("EcTccDealerDs.findByKey", params);
        if( list==null || list.isEmpty() ){
            EcTccDealerDs entitty = new EcTccDealerDs(dealerId, dsId);
            save(entitty, operator, simulated);
            logger.info("saveByKey save dealerId="+dealerId+", storeId="+dsId);
        }else{
            logger.warn("saveByKey exists dealerId="+dealerId+", storeId="+dsId);
        }
    }
    
    public List<EcTccDealerDs> findByDs(Long dsId){
        logger.debug("findByDs dsId = "+dsId);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dsId", dsId);
        
        List<EcTccDealerDs> list = this.findByNamedQuery("EcTccDealerDs.findByDs", params);
        
        return list;
    }
}
