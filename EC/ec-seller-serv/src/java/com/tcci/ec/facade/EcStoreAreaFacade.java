/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStoreArea;
import com.tcci.ec.enums.OptionEnum;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.StoreAreaVO;
import com.tcci.ec.model.criteria.StoreCriteriaVO;
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
 * @author peter.pan
 */
@Stateless
public class EcStoreAreaFacade extends AbstractFacade<EcStoreArea> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcStoreAreaFacade() {
        super(EcStoreArea.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcStoreArea entity, EcMember operator, boolean simulated){
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

    /**
     * @param criteriaVO
     * @return 
     */
    public List<StoreAreaVO> findByCriteria(StoreCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", P.CNAME AREA_CNAME \n");
        sql.append("FROM EC_STORE_AREA S \n");
        sql.append("JOIN EC_OPTION P ON P.ID=S.AREA_ID AND P.TYPE='").append(OptionEnum.SALES_AREA.getCode()).append("' \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND P.DISABLED=0 \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");       
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getAreaId()!=null ){
            sql.append("AND S.AREA_ID=#AREA_ID \n");       
            params.put("AREA_ID", criteriaVO.getAreaId());
        }

        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY P.SORTNUM, P.CNAME \n");
        }
        
        List<StoreAreaVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StoreAreaVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StoreAreaVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(StoreAreaVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public List<LongOptionVO> findStoreAreaOptions(Long storeId){
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<StoreAreaVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        if( list!=null ){
            for(StoreAreaVO vo : list){
                ops.add(new LongOptionVO(vo.getAreaId(), vo.getAreaCname()));
            }
        }
        return ops;
    }

    public List<EcStoreArea> findStoreAreas(Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT e FROM EcStoreArea e WHERE e.storeId = :storeId";
        params.put("storeId", storeId);
        
        return this.findByJPQLQuery(sql, params);
    }
    
    public List<StoreAreaVO> findByStore(Long storeId) {
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<StoreAreaVO> list = findByCriteria(criteriaVO);
        return list;
    }
    
    public StoreAreaVO findByKey(Long storeId, Long areaId) {
        StoreCriteriaVO criteriaVO = new StoreCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setAreaId(areaId);
        List<StoreAreaVO> list = findByCriteria(criteriaVO);
        
        StoreAreaVO vo = (list!=null && !list.isEmpty())?list.get(0):null;
        return vo;
    }

    public boolean checkInput(EcStoreArea entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
