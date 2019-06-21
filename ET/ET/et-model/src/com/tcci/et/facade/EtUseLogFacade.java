/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.et.entity.EtMember;
import com.tcci.et.entity.EtUseLog;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EtUseLogFacade extends AbstractFacade<EtUseLog> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtUseLogFacade() {
        super(EtUseLog.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtUseLog entity, EtMember operator, boolean simulated){
        if( entity!=null ){
            entity.setMemberId(operator.getId());
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
     * 
     * @param memberId
     * @return 
     */
    public List<EtUseLog> findByMemberId(Long memberId){
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        List<EtUseLog> list = this.findByNamedQuery("EtUseLog.findByMemberId", params);
        return list;
    }
    
    /**
     * 使用時間
     * @param memberId
     * @param type
     * @param src
     * @return 
     */
    public int calUsePeriods(Long memberId, String type, String src){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
    
        sql.append("update ec_use_log \n");
        sql.append("set period=round((sysdate-createtime)*24*60*60), modifytime=sysdate, modifier=#memberId \n");
        sql.append("where member_id=#memberId \n");
        sql.append("and type=#type \n");
        sql.append("and src=#src \n");
        sql.append("and period is null and (sysdate-createtime)<(1/8); \n");// 3小時內

        sql.append("END; \n");
        
        params.put("memberId", memberId);
        params.put("type", type);
        params.put("src", src);
        
        logger.debug("setUsePeriods sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("setUsePeriods", params, q);
        
        return q.executeUpdate();
    }
    
}
