/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.et.facade;

import com.tcci.cm.enums.OptionTypeEnum;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.et.entity.EtTenderCategory;
import com.tcci.et.model.TenderCategoryVO;
import com.tcci.et.model.criteria.RfqCriteriaVO;
import com.tcci.fc.entity.org.TcUser;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class EtTenderCategoryFacade extends AbstractFacade<EtTenderCategory> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtTenderCategoryFacade() {
        super(EtTenderCategory.class);
    }
    
    public void removeVO(TenderCategoryVO vo, boolean simulated) {       
        EtTenderCategory entity = findByVO(vo);
        
        if( entity!=null ){
            this.remove(entity, simulated);
        }
    }

    public EtTenderCategory findByVO(TenderCategoryVO vo){
        EtTenderCategory entity = this.find(vo.getId());
        return entity;
    }
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EtTenderCategory entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            // default while null 
           
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
    public void saveVO(TenderCategoryVO vo, TcUser operator, boolean simulated){
        if( vo==null ){
            logger.error("saveVO vo==null");
            return;
        }
        EtTenderCategory entity = (vo.getId()!=null)?this.find(vo.getId()):new EtTenderCategory();
        
        ExtBeanUtils.copyProperties(entity, vo);
        save(entity, operator, simulated);
    }
    
    /**
     * 依輸入條件查詢 SQL
     * @param criteriaVO
     * @param params
     * @return 
     */
    public String findByCriteriaSQL(RfqCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM ET_TENDER_CATEGORY S \n");
        sql.append("JOIN ET_OPTION OP ON OP.TYPE='tenderCategory' AND OP.SAP_CLIENT_CODE='tcc_cn' \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.DISABLED=0 \n");

        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getTenderId()!=null ){
            sql.append("AND S.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", criteriaVO.getTenderId());
        }
        
        return sql.toString();
    }
    
    /**
     * 依輸入條件查詢筆數
     * @param criteriaVO
     * @return 
     */
    public int countByCriteria(RfqCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    
    /**
     * 依輸入條件查詢
     * @param criteriaVO
     * @return 
     */
    public List<TenderCategoryVO> findByCriteria(RfqCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S.* \n");
        sql.append(", OP.CNAME CATEGORY_NAME \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.ID");
        }
        
        List<TenderCategoryVO> list = this.selectBySql(TenderCategoryVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        return list;
    }

    /**
     * 依 ID 查詢
     * @param id
     * @param fullData
     * @return 
     */
    public TenderCategoryVO findById( Long id, boolean fullData) {
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setId(id);
        criteriaVO.setFullData(fullData);
        
        List<TenderCategoryVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     *  依標案詢價單查詢
     * @param tenderId
     * @return 
     */
    public List<TenderCategoryVO> findByTender(Long tenderId){
        RfqCriteriaVO criteriaVO = new RfqCriteriaVO();
        criteriaVO.setTenderId(tenderId);
        
        List<TenderCategoryVO> list = findByCriteria(criteriaVO);
        return list;
    }
    
    /**
     * get category Ids
     * @param list
     * @return 
     */
    public List<Long> getCategoryIds(List<TenderCategoryVO> list){
        List<Long> ids = new ArrayList<Long>();
        if( list!=null ){
            for(TenderCategoryVO vo : list){
                ids.add(vo.getCategoryId());
            }
        }
        return ids;
    }
    
    /**
     * 依標案儲存
     * @param tenderId
     * @param sapClientCode
     * @param categoryIds 
     * @param operator 
     */
    public void saveByTender(Long tenderId, String sapClientCode, List<Long> categoryIds, TcUser operator){
        List<TenderCategoryVO> oriList = findByTender(tenderId);
        List<Long> oriIds = getCategoryIds(oriList);
        // 新增
        if( categoryIds!=null ){
            for(Long id : categoryIds){
                if( oriIds==null || !oriIds.contains(id) ){
                    EtTenderCategory entity = new EtTenderCategory();
                    entity.setTenderId(tenderId);
                    entity.setCategoryId(id);
                    entity.setDisabled(Boolean.FALSE);
                    this.save(entity, operator, false);
                }
            }
        }
        // 刪除(修改)
        if( oriList!=null ){
            for(TenderCategoryVO vo : oriList){
                if( categoryIds==null || !categoryIds.contains(vo.getCategoryId()) ){
                    vo.setDisabled(Boolean.TRUE);
                    this.saveVO(vo, operator, false);
                }
            }
        }
        
        cacheTenderCategorys(sapClientCode, tenderId);
    }
    
    /**
     * cache to ET_TENDER.CATEGORYS
     * @param sapClientCode
     * @param tenderId
     * @return 
     */
    public int cacheTenderCategorys(String sapClientCode, Long tenderId){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");

        sql.append("MERGE INTO ET_TENDER D \n");
        sql.append("USING ( \n");
        sql.append("  SELECT T.TENDER_ID, LISTAGG(TO_CHAR(OP.CNAME), '、') WITHIN GROUP (ORDER BY OP.CODE) CATEGORYS \n");
        sql.append("  FROM ET_OPTION OP \n");
        sql.append("  JOIN ET_TENDER_CATEGORY T ON CATEGORY_ID=OP.ID \n");
        sql.append("  WHERE 1=1 \n");
        sql.append("  AND OP.TYPE=#TYPE \n");
        sql.append("  AND OP.SAP_CLIENT_CODE=#SAP_CLIENT_CODE \n");
        
        if( tenderId!=null ){
            sql.append("  AND T.TENDER_ID=#TENDER_ID \n");
            params.put("TENDER_ID", tenderId);
        }
        
        sql.append("  GROUP BY T.TENDER_ID \n");
        sql.append(") S ON (S.TENDER_ID=D.ID) \n");
        sql.append("WHEN MATCHED THEN \n");
        sql.append("  UPDATE SET D.CATEGORYS=S.CATEGORYS; \n");

        sql.append("END; \n");
        
        params.put("TYPE", OptionTypeEnum.TENDER_CAT.getCode());
        params.put("SAP_CLIENT_CODE", sapClientCode);
        
        logger.debug("cacheTenderCategorys sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("cacheTenderCategorys", params, q);
        
        return q.executeUpdate();
    }
}
