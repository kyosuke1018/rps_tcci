/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStockLog;
import com.tcci.ec.enums.StockEnum;
import com.tcci.ec.model.OrderDetailVO;
import com.tcci.ec.model.OrderVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.StockLogVO;
import com.tcci.ec.model.rs.OrderQuoteVO;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
public class EcStockLogFacade extends AbstractFacade<EcStockLog> {
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public EcStockLogFacade() {
        super(EcStockLog.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcStockLog entity, EcMember operator, boolean simulated){
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
    
    /**
     * 依條件查詢筆數
     * @param criteriaVO
     * @return
     */
    public int countByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    /**
     * 依條件查詢列表
     * @param criteriaVO
     * @param locale
     * @return
     */
    public List<StockLogVO> findByCriteria(BaseCriteriaVO criteriaVO, Locale locale){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT S.* \n");
        if( criteriaVO.isFullData() ){
            sql.append(", O.ORDER_NUMBER, OD.SNO \n");
        }
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.DATA_TIME DESC");
        }
        
        List<StockLogVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StockLogVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StockLogVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(StockLogVO.class, sql.toString(), params);
        }
        if( list!=null ){
            for(StockLogVO vo : list){
                vo.genTypeName(locale);
            }
        }
        return list;
    }
    public String findByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM EC_STOCK_LOG S \n");
        if( criteriaVO.isFullData() ){
            sql.append("LEFT OUTER JOIN EC_ORDER O ON O.ID=S.ORDER_ID \n");
            sql.append("LEFT OUTER JOIN EC_ORDER_DETAIL OD ON OD.ID=S.DETAIL_ID \n");
        }
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        
        return sql.toString();
    }
    
    public List<StockLogVO> findByPrdId(Long storeId, Long prdId, Locale locale){
        if( storeId==null || prdId==null ){
            logger.error("findByOrderId error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        List<StockLogVO> list = findByCriteria(criteriaVO, locale);
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
    public boolean checkInput(EcStockLog entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);
        
        return pass;
    }
    
    /**
     * 訂單出貨
     * @param storeId
     * @param orderId
     * @param member
     * @param simulated
     */
    public void shippingByOrder(Long storeId, Long orderId, List<OrderQuoteVO> quoteList, EcMember member, boolean simulated) {
        if( simulated ){
            logger.info("shippingByOrder simulated = "+simulated);
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        
        if( GlobalConstant.SHIP_QUANTITY_MODIFY ){// 確認出貨時可改量
            // 狀態、量 都要改
            if( quoteList!=null ){
                for(OrderQuoteVO vo : quoteList){
                    sql.append("UPDATE EC_STOCK_LOG \n");
                    sql.append("SET TYPE=#TYPE \n");
                    sql.append(", QUANTITY=#QUANTITY").append(vo.getId()).append(" \n");
                    sql.append("WHERE 1=1 \n");
                    sql.append("AND STORE_ID=#STORE_ID \n");
                    sql.append("AND ORDER_ID=#ORDER_ID \n");
                    sql.append("AND TYPE=#TYPE_ORI \n");
                    sql.append("AND PRD_ID=#PRD_ID").append(vo.getId()).append(" \n");
                    sql.append("AND DETAIL_ID=#DETAIL_ID").append(vo.getId()).append(" \n");
                    sql.append("AND CLOSED=0; \n");// 只能改未結算
                    sql.append("\n");
                    
                    params.put("QUANTITY"+vo.getId(), vo.getQuantity().negate());
                    params.put("PRD_ID"+vo.getId(), vo.getPrdId());
                    params.put("DETAIL_ID"+vo.getId(), vo.getId());
                }
                
                params.put("STORE_ID", storeId);
                params.put("ORDER_ID", orderId);
                params.put("TYPE_ORI", StockEnum.SOLD.getCode());
                params.put("TYPE", StockEnum.OUTCOME.getCode());
            }
        }else{
            // 所有項目改狀態即可
            sql.append("UPDATE EC_STOCK_LOG \n");
            sql.append("SET TYPE=#TYPE \n");
            sql.append("WHERE 1=1 \n");
            sql.append("AND STORE_ID=#STORE_ID \n");
            sql.append("AND ORDER_ID=#ORDER_ID \n");
            sql.append("AND TYPE=#TYPE_ORI \n");
            sql.append("AND CLOSED=0; \n");// 只能改未結算

            params.put("STORE_ID", storeId);
            params.put("ORDER_ID", orderId);
            params.put("TYPE_ORI", StockEnum.SOLD.getCode());
            params.put("TYPE", StockEnum.OUTCOME.getCode());
        }
        
        sql.append("END;");
        
        logger.debug("shippingByOrder sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("shippingByOrder", params, q);
        
        q.executeUpdate();
    }
    
    /**
     * 訂單確認
     * @param orderVO
     * @param member
     * @param locale
     * @param simulated 
     */
    public void minusByOrderConfirm(OrderVO orderVO, EcMember member, Locale locale, boolean simulated) {
        if( simulated ){
            logger.info("minusByOrderConfirm simulated = "+simulated);
            return;
        }
        if( orderVO.getItems()!=null ){
            StockEnum stockEnum = StockEnum.SOLD;// 已賣出
            for(OrderDetailVO detailVO : orderVO.getItems()){
                Long prdId = detailVO.getProductId();
                BigDecimal quantity = detailVO.getQuantity();
                String memo = MessageFormat.format(stockEnum.getDisplayMemo(locale), orderVO.getOrderNumber());
                
                if( prdId!=null && quantity!=null ){
                    EcStockLog entity = new EcStockLog();
                    entity.setType(stockEnum.getCode());
                    entity.setQuantity(stockEnum.isPostive()?quantity:BigDecimal.valueOf(quantity.doubleValue()*-1));
                    entity.setMemo(memo);
                    entity.setClosed(false);
                    entity.setDataTime(new Date());
                    entity.setDisabled(false);
                    entity.setPrdId(prdId);
                    entity.setStoreId(orderVO.getStoreId());
                    entity.setOrderId(orderVO.getId());
                    
                    save(entity, member, false);
                    logger.error("minusByOrderConfirm save {}", entity.getId());
                }
            }
        }
    }
    
    /**
     * 訂單取消
     * @param storeId
     * @param orderId
     * @param member
     * @param simulated
     */
    public void cancelOrder(Long storeId, Long orderId, EcMember member, boolean simulated) {
        if( simulated ){
            logger.info("plusByOrderCancel simulated = "+simulated);
            return;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        
        sql.append("UPDATE EC_STOCK_LOG \n");
        sql.append("SET DISABLED=1 \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND STORE_ID=#STORE_ID \n");
        sql.append("AND ORDER_ID=#ORDER_ID \n");
        sql.append("AND TYPE=#TYPE_ORI \n");
        sql.append("AND CLOSED=0; \n");// 只能改未結算
        
        params.put("STORE_ID", storeId);
        params.put("ORDER_ID", orderId);
        params.put("TYPE_ORI", StockEnum.SOLD.getCode());
        
        sql.append("END;");
        
        logger.debug("plusByOrderCancel sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("plusByOrderCancel", params, q);
        
        q.executeUpdate();
    }
}
