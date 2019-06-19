/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcFile;
import com.tcci.ec.enums.FileEnum;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.model.criteria.BaseCriteriaVO;
import com.tcci.ec.model.FileVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcFileFacade extends AbstractFacade<EcFile> {
    @Resource(mappedName = "jndi/ec.config")
    protected Properties jndiConfig;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcFileFacade() {
        super(EcFile.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcFile entity, EcMember operator, boolean simulated){
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
    
    public void save(EcFile entity, EcMember operator) {
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
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<FileVO> findByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT S.* \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
               
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIME DESC");
        }
        
        List<FileVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(FileVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(FileVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(FileVO.class, sql.toString(), params);
        }
        
        if( list!=null ){
            for(FileVO vo :  list){
                FileEnum fileEnum = FileEnum.getFromPrimaryType(vo.getPrimaryType());
                String imgSrc = fileEnum!=null? fileEnum.getCode():null;
//                vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, imgSrc, ImageSizeEnum.SMALL.getCode()));
                String prefix = jndiConfig.getProperty("url.prefix")+"/ec-service";
                vo.setUrl(vo.genUrl("", prefix+GlobalConstant.URL_GET_IMAGE, imgSrc, ImageSizeEnum.SMALL.getCode()));
            }
        }
        return list;
    }
    public int countByCriteria(BaseCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        // 商品
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(BaseCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM EC_FILE S \n");
        sql.append("WHERE 1=1 \n");

        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }else if( criteriaVO.getIdList()!=null && !criteriaVO.getIdList().isEmpty() ){
            sql.append(NativeSQLUtils.getInSQL("S.ID", criteriaVO.getIdList(), params)).append(" \n");
        }
        
        if( criteriaVO.getPrimaryType()!=null ){
            sql.append("AND S.PRIMARY_TYPE=#PRIMARY_TYPE \n");
            params.put("PRIMARY_TYPE", criteriaVO.getPrimaryType());
        }
        if( criteriaVO.getPrimaryId()!=null ){
            sql.append("AND S.PRIMARY_ID=#PRIMARY_ID \n");
            params.put("PRIMARY_ID", criteriaVO.getPrimaryId());
        }
        
        return sql.toString();
    }

    public List<FileVO> findByPrimary(Long storeId, String primaryType, Long primaryId){
        if( storeId==null || primaryType==null || primaryId==null ){
            logger.error("findByPrimary error storeId==null || primaryType==null || primaryId==null");
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrimaryType(primaryType);
        criteriaVO.setPrimaryId(primaryId);
        criteriaVO.setFullData(true);
        List<FileVO> list = findByCriteria(criteriaVO);
        
        return list;
    }
    
   public List<FileVO> findByIds(Long storeId, String primaryType, Long primaryId, List<Long> idList){
        if( storeId==null || primaryType==null || primaryId==null || idList==null || idList.isEmpty() ){
            logger.error("findByIds error input null !");
            return null;
        }
        BaseCriteriaVO criteriaVO = new BaseCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrimaryType(primaryType);
        criteriaVO.setPrimaryId(primaryId);
        criteriaVO.setIdList(idList);
        criteriaVO.setFullData(true);
        List<FileVO> list = findByCriteria(criteriaVO);
        
        return list;
    }
  
    public FileVO findSingleByPrimary(Long storeId, String primaryType, Long primaryId){
        List<FileVO> list = findByPrimary(storeId, primaryType, primaryId);
        
        logger.debug("findSingleByPrimary list = "+(list!=null? list.size():0));
//        return (list!=null && !list.isEmpty())?list.get(0):null;
        FileVO vo = (list!=null && !list.isEmpty())?list.get(0):null;
        if( vo!=null ){
            FileEnum fileEnum = FileEnum.getFromPrimaryType(vo.getPrimaryType());
            String imgSrc = fileEnum!=null? fileEnum.getCode():null;
//            vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, imgSrc, ImageSizeEnum.SMALL.getCode()));
            String prefix = jndiConfig.getProperty("url.prefix")+"/ec-service";
            vo.setUrl(vo.genUrl("", prefix+GlobalConstant.URL_GET_IMAGE, imgSrc, ImageSizeEnum.SMALL.getCode()));
        }
        return vo;
    }
    public boolean checkInput(EcFile entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
}
