/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcUseLog;
import com.tcci.ec.model.OrderLogVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
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
public class EcUseLogFacade extends AbstractFacade<EcUseLog> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcUseLogFacade() {
        super(EcUseLog.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcUseLog entity, EcMember operator, boolean simulated){
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
     * 報價歷史記錄
     * @param criteriaVO
     * @return 
     */
    public List<OrderLogVO> findForQuotation(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", M.NAME||'('||M.LOGIN_ACCOUNT||')' MEMBER_LABEL \n");
        sql.append("FROM EC_USE_LOG S \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND S.MEMBER_ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if( criteriaVO.getType()!=null ){
            sql.append("AND S.TYPE=#TYPE \n");
            params.put("TYPE", criteriaVO.getType());
        }
        
        sql.append("ORDER BY S.CREATETIME DESC");
        
        List<OrderLogVO> list = this.selectBySql(OrderLogVO.class, sql.toString(), params);

        return list;
    }
    
    /**
     * 
     * @param memberId
     * @return 
     */
    public List<EcUseLog> findByMemberId(Long memberId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("memberId", memberId);
        List<EcUseLog> list = this.findByNamedQuery("EcUseLog.findByMemberId", params);
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
