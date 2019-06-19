/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.product;

import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdVariant;
import com.tcci.ec.enums.ProductVariantEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.PrdVariantVO;
import com.tcci.ec.model.rs.SubmitVO;
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
public class EcPrdVariantFacade extends AbstractFacade<EcPrdVariant> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdVariantFacade() {
        super(EcPrdVariant.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcPrdVariant entity, EcMember operator, boolean simulated){
//        if( entity!=null ){
//            if( entity.getId()!=null && entity.getId()>0 ){
//                entity.setModifier(operator);
//                entity.setModifytime(new Date());
//                this.edit(entity, simulated);
//                logger.info("save update "+entity);
//            }else{
//                entity.setCreator(operator);
//                entity.setCreatetime(new Date());
//                this.create(entity, simulated);
//                logger.info("save new "+entity);
//            }
//        }
//    }
    public void save(EcPrdVariant entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    // 自動產生名稱 CNAME
    public void genVariantName(EcPrdVariant entity, SubmitVO formVO){
        StringBuilder sb = new StringBuilder();
        if( formVO!=null ){
            if( formVO.getColorId()!=null && formVO.getColorId()>0 ){
                sb.append(ProductVariantEnum.COLOR.getDisplayName()).append(":").append(formVO.getColorName());
            }
            if( !sb.toString().isEmpty() ){
                sb.append(",");
            }
            if( formVO.getSizeId()!=null && formVO.getSizeId()>0 ){
                sb.append(ProductVariantEnum.SIZE.getDisplayName()).append(":").append(formVO.getSizeName());
            }
        }
        
        if( !sb.toString().isEmpty() ){
            entity.setCname(sb.toString());
        }else{
            entity.setCname("單一型別");
        }
    }
    
    /**
     * 依商品查詢
     * @param criteriaVO
     * @return 
     */
    public List<PrdVariantVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.*, C.CNAME COLORNAME, Z.CNAME SIZENAME \n");
        sql.append("FROM EC_PRD_VARIANT S \n");
        sql.append("LEFT OUTER JOIN EC_PRD_VAR_OPTION C ON C.ID=S.COLOR_ID \n");
        sql.append("LEFT OUTER JOIN EC_PRD_VAR_OPTION Z ON Z.ID=S.SIZE_ID \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }else if( criteriaVO.getPrdList()!=null && !criteriaVO.getPrdList().isEmpty() ){
            sql.append(NativeSQLUtils.getInSQL("S.PRD_ID", criteriaVO.getPrdList(), params)).append(" \n");
        }
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.PRD_ID, S.CREATETIME");
        }
        
        List<PrdVariantVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdVariantVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdVariantVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(PrdVariantVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public List<PrdVariantVO> findByPrd(Long storeId, Long prdId){
        if( storeId==null || prdId==null ){
            logger.error("findByPrd error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        
        return findByCriteria(criteriaVO);
    }

    /**
     * 輸入檢查
     * @param vo
     * @param member
     * @param errors
     * @return 
     */
    public boolean checkInput(EcPrdVariant vo, EcMember member, Locale locale, List<String> errors) {
        vo.setDisabled(vo.getDisabled()==null?false:vo.getDisabled());
        vo.setColorId((vo.getColorId()!=null && vo.getColorId()==0)? null:vo.getColorId());
        vo.setSizeId((vo.getSizeId()!=null && vo.getSizeId()==0)? null:vo.getSizeId());
        vo.setWeightUnit((vo.getWeightUnit()!=null && vo.getWeightUnit()==0)? null:vo.getWeightUnit());
        
        boolean pass = true;
        pass = inputCheckFacade.checkInput(vo, locale, errors);

        return pass;
    }

}
