/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrderProcess;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.OrderProcessVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcOrderProcessFacade extends AbstractFacade<EcOrderProcess> {
    @EJB EcMemberFacade memberFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderProcessFacade() {
        super(EcOrderProcess.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcOrderProcess entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }

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
    public void saveVO(OrderProcessVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcOrderProcess entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcOrderProcess();
            // 需保存的系統產生欄位
            //vo.setCreator(entity.getCreator()!=null? entity.getCreator().getId():null);
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
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<OrderProcessVO> findByCriteria(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", NVL(S.MODIFYTIME, S.CREATETIME) RECORD_TIME \n");
        sql.append(", P.NAME PROCESS_USER_NAME \n");
        sql.append(", C.NAME CREATOR_NAME \n");
        sql.append(", M.NAME MODIFIER_NAME \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.PROCESS_TIME DESC");
        }
        
        List<OrderProcessVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OrderProcessVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OrderProcessVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(OrderProcessVO.class, sql.toString(), params);
        }
        return list;
    }
    public int countByCriteria(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(OrderCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();

        sql.append("FROM EC_ORDER_PROCESS S \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER P ON P.ID=S.PROCESS_USER \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER C ON C.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MODIFIER \n");

        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        
        if( criteriaVO.getOrderId()!=null ){
            sql.append("AND S.ORDER_ID=#ORDER_ID \n");
            params.put("ORDER_ID", criteriaVO.getOrderId());
        }
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        
        return sql.toString();
    }
    
    public List<OrderProcessVO> findByOrderId(Long storeId, Long orderId){
        if( storeId==null || orderId==null ){
            logger.error("findByOrderId error storeId="+storeId+", orderId="+orderId);
            return null;
        }
        
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setOrderId(orderId);
        List<OrderProcessVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(EcOrderProcess entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}