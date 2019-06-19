/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcCusFeedback;
import com.tcci.ec.model.criteria.CustomerCriteriaVO;
import com.tcci.ec.model.CusFeedbackVO;
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
public class EcCusFeedbackFacade extends AbstractFacade<EcCusFeedback> {
    @EJB EcMemberFacade memberFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCusFeedbackFacade() {
        super(EcCusFeedback.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcCusFeedback entity, EcMember operator, boolean simulated){
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
    public void saveVO(CusFeedbackVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcCusFeedback entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcCusFeedback();
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
    public List<CusFeedbackVO> findByCriteria(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", T.CNAME TYPE_NAME \n");
        sql.append(", P.NAME PROCESS_USER_NAME \n");
        sql.append(", L.NAME CLOSE_USER_NAME \n");
        sql.append(", M.NAME MEMBER_NAME \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO!=null && criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIME DESC");
        }
        
        List<CusFeedbackVO> list = null;
        if( criteriaVO!=null && criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CusFeedbackVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CusFeedbackVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(CusFeedbackVO.class, sql.toString(), params);
        }
        return list;
    }
    public int countByCriteria(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(CustomerCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();

        sql.append("FROM EC_CUS_FEEDBACK S \n");
        sql.append("LEFT OUTER JOIN EC_OPTION T ON T.ID=S.TYPE_ID \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER P ON P.ID=S.PROCESS_USER \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER L ON L.ID=S.CLOSE_USER \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n");

        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND S.MEMBER_ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }
        if( criteriaVO.getOrderId()!=null ){
            sql.append("AND S.ORDER_ID=#ORDER_ID \n");
            params.put("ORDER_ID", criteriaVO.getOrderId());
        }
        logger.info("findByCriteriaSQL criteriaVO.getId() = ", criteriaVO.getId());
        if( criteriaVO.getId()!=null ){
            logger.info("findByCriteriaSQL criteriaVO.getId()!=null = ", criteriaVO.getId());
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        
        return sql.toString();
    }
    
    public List<CusFeedbackVO> findByMemberId(Long storeId, Long memberId){
        if( storeId==null || memberId==null ){
            logger.error("findByOrderId error storeId="+storeId+", memberId="+memberId);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setMemberId(memberId);
        List<CusFeedbackVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param errors
     * @return 
     */
    public boolean checkInput(EcCusFeedback entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
