/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcPrdDetail;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.PrdDetailVO;
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
public class EcPrdDetailFacade extends AbstractFacade<EcPrdDetail> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcPrdDetailFacade() {
        super(EcPrdDetail.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcPrdDetail entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
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
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<PrdDetailVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", F.NAME TITLE, F.SAVEDIR, F.SAVENAME, F.FILENAME, F.CONTENT_TYPE FILECONTENTTYPE \n");
        sql.append("FROM EC_PRD_DETAIL S \n");
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
            sql.append("ORDER BY S.SORTNUM");
        }
        
        List<PrdDetailVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdDetailVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(PrdDetailVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(PrdDetailVO.class, sql.toString(), params);
        }
        
        if( list!=null && criteriaVO.isFullData() ){
            for(PrdDetailVO vo :  list){
                vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, ImageSizeEnum.SMALL.getCode()));
            }
        }
        return list;
    }
    
    /**
     * 依商品類別查詢
     * @param storeId
     * @param prdId
     * @return 
     */
    public List<PrdDetailVO> findByPrd(Long storeId, Long prdId){
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setFullData(true);
        List<PrdDetailVO> list = findByCriteria(criteriaVO);
        
        return list;
    } 

    /**
     *  find by id
     * @param storeId
     * @param prdId
     * @param id
     * @return 
     */
    public PrdDetailVO findById(Long storeId, Long prdId, Long id) {
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setFullData(true);
        List<PrdDetailVO> list = findByCriteria(criteriaVO);
        PrdDetailVO vo =  (list!=null && !list.isEmpty())? list.get(0):null;

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

        sql.append("UPDATE EC_PRD_DETAIL \n");
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

    public boolean checkInput(EcPrdDetail entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
