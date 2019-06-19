/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcStock;
import com.tcci.ec.model.StockVO;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
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
public class EcStockFacade extends AbstractFacade<EcStock> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcStockFacade() {
        super(EcStock.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcStock entity, EcMember operator, boolean simulated){
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
    public void save(EcStock entity, EcMember operator) {
        if (entity.getId() == null) {
            entity.setModifier(operator);
            entity.setModifytime(new Date());
            em.persist(entity);
        } else {
            entity.setCreator(operator);
            entity.setCreatetime(new Date());
            em.merge(entity);
        }
    }
    
    /**
     * 依條件查詢
     * @param criteriaVO
     * @return 
     */
    public List<StockVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", F.NAME TITLE, F.SAVEDIR, F.SAVENAME, F.FILENAME, F.CONTENT_TYPE FILECONTENTTYPE \n");
        sql.append("FROM EC_STOCK S \n");
        sql.append("LEFT OUTER JOIN EC_FILE F ON F.ID=S.CONTENT_IMG \n");
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
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIME DESC");
        }
        
        List<StockVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StockVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(StockVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(StockVO.class, sql.toString(), params);
        }
        
        if( list!=null && criteriaVO.isFullData() ){
            for(StockVO vo :  list){
            }
        }
        return list;
    }
    
    /**
     * 依商品查詢
     * @param storeId
     * @param prdId
     * @return 
     */
    public List<StockVO> findByPrd(Long storeId, Long prdId){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setFullData(true);
        List<StockVO> list = findByCriteria(criteriaVO);
        
        return list;
    } 

    /**
     *  find by id
     * @param storeId
     * @param prdId
     * @param id
     * @return 
     */
    public StockVO findById(Long storeId, Long prdId, Long id) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setFullData(true);
        List<StockVO> list = findByCriteria(criteriaVO);
        StockVO vo =  (list!=null && !list.isEmpty())? list.get(0):null;

        return vo;
    }
    
    /**
     * 變更順序
     * @param storeId
     * @param prdId
     * @param sortnum
     * @param plus
     * @param operator
     * @param simulated 
     */
    public void updateSortnum(Long storeId, Long prdId, int sortnum, boolean plus, EcMember operator, boolean simulated){
        if( simulated ){
            logger.info("updateSortnum simulated = "+simulated);
            return;
        } 
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        sql.append("UPDATE EC_STOCK \n");
        if( plus ){
            sql.append("SET SORTNUM=SORTNUM+1 \n");
        }else{
            sql.append("SET SORTNUM=SORTNUM-1 \n");
        }
        sql.append("WHERE 1=1 \n");
        sql.append("AND STORE_ID=#STORE_ID \n");
        sql.append("AND PRD_ID=#PRD_ID \n");
        sql.append("AND SORTNUM >= #SORTNUM; \n");

        params.put("STORE_ID", storeId);
        params.put("PRD_ID", prdId);
        params.put("SORTNUM", sortnum);

        sql.append("END; \n");
        
        logger.debug("updateSortnum sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("updateSortnum", params, q);
        
        q.executeUpdate();
    }

    public boolean checkInput(EcStock entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
