/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrder;
import com.tcci.ec.entity.EcOrderLog;
import com.tcci.ec.enums.OrderLogEnum;
import com.tcci.ec.enums.OrderStatusEnum;
import com.tcci.ec.enums.RfqStatusEnum;
import com.tcci.ec.model.OrderLogVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcOrderLogFacade extends AbstractFacade<EcOrderLog> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderLogFacade() {
        super(EcOrderLog.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcOrderLog entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            entity.setMemberId(operator.getId());
            entity.setCreatetime(new Date());
            if( entity.getId()!=null && entity.getId()>0 ){
                this.edit(entity, simulated);
                logger.info("save update "+entity);
            }else{
                this.create(entity, simulated);
                logger.info("save new "+entity);
            }
        }
    }

    public Long save(Long storeId, Long orderId, String message, Boolean buyer, 
            String type, String eventType, EcMember operator, boolean simulated){
        return save(storeId, orderId, message, buyer, 
            type, eventType, null, null, operator, simulated);
    }
    
    public Long save(Long storeId, Long orderId, String message, Boolean buyer, 
            String type, String eventType, BigDecimal shippingTotal, BigDecimal total,
            EcMember operator, boolean simulated){
        EcOrderLog orderLog = new EcOrderLog();
        orderLog.setStoreId(storeId);
        orderLog.setOrderId(orderId);
        orderLog.setMessage(message);
        orderLog.setBuyer(buyer);
        orderLog.setType(type);
        orderLog.setEventType(eventType);
        
        orderLog.setShippingTotal(shippingTotal);
        orderLog.setTotal(total);
        
        save(orderLog, operator, simulated);
        return orderLog.getId();
    }
    
    /**
     * save order log  
     * @param entity
     * @param buyer
     * @param locale
     * @param operator 
     */
    public void saveByOrderStatus(EcOrder entity, boolean buyer, Locale locale, EcMember operator){
        OrderStatusEnum orderStatus = sys.isFalse(entity.getBuyerCheck())?
                OrderStatusEnum.Waiting:OrderStatusEnum.getFromCode(entity.getStatus());
        if( orderStatus!=null ){
            // EC_ORDER_LOG
            saveByOrder(entity, OrderLogEnum.ORDER.getCode(), orderStatus.getCode(), orderStatus.getDisplayName(locale), operator);
        }else{
            logger.error("saveByOrder error entity = "+entity);
        }
    }
    public void saveByOrder(EcOrder entity, String statusType, String eventType, String message, EcMember operator){
        EcOrderLog orderLog = new EcOrderLog();
        orderLog.setShippingTotal(entity.getShippingTotal());
        orderLog.setTotal(entity.getTotal());
        orderLog.setStoreId(entity.getStoreId());
        orderLog.setOrderId(entity.getId());
        orderLog.setMessage(message);
        orderLog.setBuyer(Boolean.FALSE);// 賣家後台
        orderLog.setType(statusType);
        orderLog.setEventType(eventType);
        save(orderLog, operator, false);
        logger.info("saveByOrder save orderLog = "+orderLog);
    }
    
    /**
     * 報價歷史記錄
     * @param storeId
     * @param orderId
     * @return 
     */
    public List<OrderLogVO> findForQuotation(Long storeId, Long orderId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.TOTAL, S.SHIPPING_TOTAL, S.CREATETIME \n");
        sql.append(", M.NAME||'('||M.LOGIN_ACCOUNT||')' CREATOR \n");
        sql.append("FROM EC_ORDER_LOG S \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n");
        sql.append("WHERE 1=1 \n");
        
        if( storeId!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", storeId);
        }
        if( orderId!=null ){
            sql.append("AND S.ORDER_ID=#ORDER_ID \n");
            params.put("ORDER_ID", orderId);
        }
        sql.append("AND S.EVENT_TYPE='").append(RfqStatusEnum.Quotation.getCode()).append("' \n");
        
        sql.append("ORDER BY S.CREATETIME DESC");
        
        List<OrderLogVO> list = this.selectBySql(OrderLogVO.class, sql.toString(), params);

        return list;
    }
    
    /**
     * 
     * @param storeId
     * @param orderId
     * @return 
     */
    public List<EcOrderLog> findByOrderId(Long storeId, Long orderId){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", storeId);
        params.put("orderId", orderId);
        List<EcOrderLog> list = this.findByNamedQuery("EcOrderLog.findByOrderId", params);
        return list;
    }
}
