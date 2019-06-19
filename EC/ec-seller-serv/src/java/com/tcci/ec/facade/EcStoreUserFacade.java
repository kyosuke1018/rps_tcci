/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStoreUser;
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
public class EcStoreUserFacade extends AbstractFacade<EcStoreUser> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcStoreUserFacade() {
        super(EcStoreUser.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcStoreUser entity, EcMember operator, boolean simulated){
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
    
    /**
     * find by unique key
     * @param storeId
     * @param memberId
     * @return 
     */
    public EcStoreUser findByKey(Long storeId, Long memberId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        params.put("memberId", memberId);
        
        List<EcStoreUser> list = this.findByNamedQuery("EcStoreUser.findByKey", params);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 找商店管理員 ID
     * @return 
     */
    public List<Long> findManagerIds(Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MEMBER_ID FROM EC_STORE_USER WHERE DISABLED=0 AND STORE_ID=#STORE_ID");
        params.put("STORE_ID", storeId);
        
        return this.findIdList(sql.toString(), params);
    }
}
