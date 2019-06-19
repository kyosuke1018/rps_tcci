/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrderCarInfo;
import com.tcci.ec.model.OrderCarInfoVO;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
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
public class EcOrderCarInfoFacade extends AbstractFacade<EcOrderCarInfo> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderCarInfoFacade() {
        super(EcOrderCarInfo.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcOrderCarInfo entity, EcMember operator, boolean simulated){
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
    
    public void saveVO(OrderCarInfoVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcOrderCarInfo entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcOrderCarInfo();
            // 需保存的系統產生欄位
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
            save(entity, operator, simulated);
            // 回傳 VO 欄位
            vo.setId(entity.getId());
            vo.setCreatorId(entity.getCreator()!=null? entity.getCreator().getId():null);
            vo.setCreatetime(entity.getCreatetime());
            vo.setModifierId(entity.getModifier()!=null? entity.getModifier().getId():null);
            vo.setModifytime(entity.getModifytime());
        }
    }
    
    /**
     * 刪除 EC_ORDER_CAR_INFO 不存在 carNoList 的資料
     * @param storeId
     * @param orderId
     * @param carNoList
     * @param operator
     * @param simulated 
     */
    public void deleteByCarNo(Long storeId, Long orderId, List<String> carNoList, EcMember operator, boolean simulated){
        if( simulated ){
            logger.info("deleteByCarNo simulated = "+simulated);
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        
        sql.append("    DELETE FROM EC_ORDER_CAR_INFO WHERE 1=1 \n");
        sql.append("    AND STORE_ID=#STORE_ID \n");
        sql.append("    AND ORDER_ID=#ORDER_ID \n");
        sql.append("    ").append(NativeSQLUtils.getNotInSQL("UPPER(CAR_NO)", carNoList, params)).append("; \n");

        params.put("STORE_ID", storeId);
        params.put("ORDER_ID", orderId);

        sql.append("END; \n");
        
        logger.debug("deleteByCarNo sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("deleteByCarNo", params, q);
        
        q.executeUpdate();
    }
    
    /**
     * 依條件查詢
     * @param criteriaVO
     * @return 
     */
    public List<OrderCarInfoVO> findByCriteria(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM EC_ORDER_CAR_INFO S \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID = #STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        
        if( criteriaVO.getOrderId()!=null ){
            sql.append("AND S.ORDER_ID = #ORDER_ID \n");
            params.put("ORDER_ID", criteriaVO.getOrderId());
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NVL(S.MODIFYTIME, S.CREATETIME) DESC");
        }
        
        List<OrderCarInfoVO> list = this.selectBySql(OrderCarInfoVO.class, sql.toString(), params);

        return list;
    }

    public List<OrderCarInfoVO> findByOrderId(Long storeId, Long orderId){
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setOrderId(storeId);
        criteriaVO.setOrderId(orderId);
        
        List<OrderCarInfoVO> list = findByCriteria(criteriaVO);
        return list;
    }

    public String getCarListStr(List<OrderCarInfoVO> list){
        StringBuilder sb = new StringBuilder();
        
        if( list!=null ){
            for(OrderCarInfoVO vo : list){
                if( sb.toString().isEmpty() ){
                    sb.append(vo.getCarNo());
                }else{
                    sb.append("、").append(vo.getCarNo());
                }
            }
        }
        
        return sb.toString();
    }
    
}
