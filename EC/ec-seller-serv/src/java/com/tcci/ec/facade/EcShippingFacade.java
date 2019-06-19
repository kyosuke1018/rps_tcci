/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcShipping;
import com.tcci.ec.enums.ShipMethodEnum;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.rs.LongOptionVO;
import com.tcci.ec.model.ShippingVO;
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
public class EcShippingFacade extends AbstractFacade<EcShipping> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcShippingFacade() {
        super(EcShipping.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcShipping entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
            if( entity.getActive()==null ){ entity.setActive(true); }
            if( entity.getSortnum()==null ){ entity.setSortnum(0); } 

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
    public List<ShippingVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append("FROM EC_SHIPPING S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND DISABLED=0 \n");

        //if( GlobalConstant.SHARE_SHIP_METHOD ){
        //    sql.append("AND S.STORE_ID IS NULL \n");
        //}else{
            if( criteriaVO.getStoreId()!=null ){
                sql.append("AND S.STORE_ID=#STORE_ID \n");   
                params.put("STORE_ID", criteriaVO.getStoreId());
            }
        //}
        if( criteriaVO.getCode()!=null ){
            sql.append("AND S.CODE=#CODE \n");       
            params.put("CODE", criteriaVO.getCode());
        }
               
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.SORTNUM, S.TITLE");
        }
        
        List<ShippingVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(ShippingVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(ShippingVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(ShippingVO.class, sql.toString(), params);
        }
        return list;
    }
    
    public List<LongOptionVO> findShippingOptions(Long storeId, Locale locale){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<ShippingVO> list = findByCriteria(criteriaVO);
        
        List<LongOptionVO> ops = new ArrayList<LongOptionVO>();
        if( list!=null ){
            for(ShippingVO vo : list){
                if( locale!=null && vo.getCode()!=null ){
                    ShipMethodEnum theEnum = ShipMethodEnum.getFromCode(vo.getCode());
                    if( theEnum!=null ){
                        vo.setTitle(theEnum.getDisplayName(locale));
                    }
                }
                ops.add(new LongOptionVO(vo.getId(), vo.getTitle()));
            }
        }
        return ops;
    }

    public List<EcShipping> findStoreShippings(Long storeId){
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "SELECT e FROM EcShipping e WHERE e.storeId = :storeId AND e.disabled = 0";
        params.put("storeId", storeId);
        
        return this.findByJPQLQuery(sql, params);
    }

    public List<ShippingVO> findByStore(Long storeId) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        List<ShippingVO> list = findByCriteria(criteriaVO);
        return list;
    }

    public EcShipping genByEnumCode(Long storeId, String code, Integer sortnum, Locale locale){
        ShipMethodEnum shipEnum = ShipMethodEnum.getFromCode(code);
        if( shipEnum!=null ){
            return genByEnum(storeId, shipEnum, sortnum, locale);
        }else{
            logger.error("genByEnum error shipEnum = null, code = "+code);
        }
        return null;
    }
    public EcShipping genByEnum(Long storeId, ShipMethodEnum shipEnum, Integer sortnum, Locale locale){
        EcShipping entity = null;
        if( shipEnum!=null ){
            entity = new EcShipping();
            entity.setActive(Boolean.TRUE);
            entity.setCode(shipEnum.getCode());
            entity.setDisabled(Boolean.FALSE);
            entity.setSortnum(sortnum);
            entity.setStoreId(storeId);
            entity.setTitle(shipEnum.getDisplayName(locale));
            entity.setType(shipEnum.getType());
        }else{
            logger.error("genByEnum error payEnum = null");
        }
        return entity;
    }
    
    public ShippingVO findByKey(Long storeId, String code) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setCode(code);
        List<ShippingVO> list = findByCriteria(criteriaVO);
        
        ShippingVO vo = (list!=null && !list.isEmpty())?list.get(0):null;
        return vo;
    }

    public boolean checkInput(EcShipping entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }

}
