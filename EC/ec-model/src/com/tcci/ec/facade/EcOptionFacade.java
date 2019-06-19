/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcOption;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.LongOptionVO;
import com.tcci.ec.model.OptionVO;
import java.util.ArrayList;
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
public class EcOptionFacade extends AbstractFacade<EcOption> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcOptionFacade() {
        super(EcOption.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcOption entity, EcMember operator, boolean simulated){
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
    
    /**
     * 依商品查詢
     * @param criteriaVO
     * @return 
     */
    public List<OptionVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_OPTION S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND DISABLED=0 \n");

        // 系統共用，STROE_ID=0
        sql.append("AND S.STORE_ID=#STORE_ID \n");
        params.put("STORE_ID", criteriaVO.getStoreId());
        
        sql.append("AND S.TYPE=#TYPE \n");       
        params.put("TYPE", criteriaVO.getType());
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.CNAME \n");
        }
        
        List<OptionVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OptionVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(OptionVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(OptionVO.class, sql.toString(), params);
        }

        return list;
    }
    
    public List<OptionVO> findByType(Long storeId, String type){
        if( storeId==null || type==null ){
            logger.error("findByType error storeId=" +storeId+", type="+type);
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setType(type);
        
        return findByCriteria(criteriaVO);
    }

    /**
     * 選單選項
     * @param storeId
     * @param type
     * @return 
     */
    public List<LongOptionVO> findByTypeOptions(Long storeId, String type) {
        List<OptionVO> list = findByType(storeId, type);
        List<LongOptionVO> ops = new ArrayList<>();
        if( list!=null ){
            for(OptionVO vo : list){
                LongOptionVO op = new LongOptionVO(vo.getId(), vo.getCname());
                ops.add(op);
            }
        }
        return ops;
    }

    public boolean checkInput(EcOption entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }

    /**
     *  for import check
     * @param storeId
     * @param type
     * @return 
     */
    public Map<String, Long> findForNameMap(Long storeId, String type) {
        List<OptionVO> list = findByType(storeId, type);
        Map<String, Long> map = new HashMap<String, Long>();
        if( list!=null ){
            for(OptionVO vo : list){
                if( vo.getCname()!=null ){
                    map.put(vo.getCname().trim().toUpperCase(), vo.getId());
                }
            }
        }
        return map;
    }
}
