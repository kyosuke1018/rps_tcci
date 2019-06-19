package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOrderDetail;
import com.tcci.ec.model.criteria.OrderCriteriaVO;
import com.tcci.ec.model.OrderDetailVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class EcOrderDetailFacade extends AbstractFacade<EcOrderDetail> {
    @EJB EcMemberFacade memberFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOrderDetailFacade() {
        super(EcOrderDetail.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcOrderDetail entity, EcMember operator, boolean simulated){
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
    public void saveVO(OrderDetailVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcOrderDetail entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcOrderDetail();
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
    public List<OrderDetailVO> findByCriteria(OrderCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", P.CNAME, P.ENAME, P.PRICE_UNIT, P.PRICE_AMT, P.ITEM_UNIT, P.ITEM_AMT \n");
        sql.append(", P.CODE, P.STATUS, P.DISABLED, P.PUBLISH_TIME \n");
        sql.append(", NVL(V.COMPARE_AT_PRICE, P.COMPARE_AT_PRICE) COMPARE_AT_PRICE \n");
        sql.append(", NVL(V.PRICE, P.PRICE) PRICE_NOW \n");
        sql.append(", NVL(V.BARCODE, P.BARCODE) BARCODE \n");
        sql.append(", NVL(V.SKU, P.SKU) SKU \n");
        sql.append(", CO.CNAME COLOR_NAME, SI.CNAME SIZE_NAME \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SNO");
        }
        
        List<OrderDetailVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OrderDetailVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OrderDetailVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(OrderDetailVO.class, sql.toString(), params);
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

        sql.append("FROM EC_ORDER_DETAIL S \n");
        sql.append("LEFT OUTER JOIN EC_PRODUCT P ON P.ID=S.PRODUCT_ID \n");
        sql.append("LEFT OUTER JOIN EC_PRD_VARIANT V ON V.ID=S.VARIANT_ID \n");
        sql.append("LEFT OUTER JOIN EC_PRD_VAR_OPTION CO ON CO.ID = V.COLOR_ID \n");
        sql.append("LEFT OUTER JOIN EC_PRD_VAR_OPTION SI ON SI.ID = V.SIZE_ID \n");
        sql.append("WHERE 1=1 \n");
        
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
    
    public List<OrderDetailVO> findByOrderId(Long storeId, Long orderId){
        if( storeId==null || orderId==null ){
            logger.error("findByOrderId error storeId="+storeId+", orderId="+orderId);
            return null;
        }
        
        OrderCriteriaVO criteriaVO = new OrderCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setOrderId(orderId);
        List<OrderDetailVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param errors
     * @return 
     */
    public boolean checkInput(OrderDetailVO vo, EcMember member, List<String> errors) {
        // TODO check input
        return true;
    }
    
}
