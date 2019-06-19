/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcAd;
import com.tcci.ec.enums.AdEnum;
import com.tcci.ec.enums.ImageSizeEnum;
import com.tcci.ec.model.AdVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcAdFacade extends AbstractFacade<EcAd> {
    
    @Resource(mappedName = "jndi/ec.config")
    protected Properties jndiConfig;
    
    @EJB EcFileFacade fileFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcAdFacade() {
        super(EcAd.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
//    public void save(EcAd entity, EcMember operator, boolean simulated){
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
    public void save(EcAd entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    public void saveVO(AdVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcAd entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcAd();
            // 需保存的系統產生欄位
            vo.setCreatetime(entity.getCreatetime());
            // 複製 UI 輸入欄位
            ExtBeanUtils.copyProperties(entity, vo);
            // DB 儲存
//            save(entity, operator, simulated);
            save(entity);
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
    public List<AdVO> findByCriteria(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", NVL(S.MODIFYTIME, S.CREATETIME) LAST_UPDATE_TIME \n");
        sql.append(", F.ID IMG_ID, F.NAME TITLE, F.SAVEDIR, F.SAVENAME, F.FILENAME, F.CONTENT_TYPE FILECONTENTTYPE \n");
        sql.append(", ST.CNAME STORE_NAME \n");
        sql.append(", P.CNAME PRD_NAME \n");
        sql.append(", A.NAME APPROVE_USER_NAME \n");
        sql.append(", C.NAME CREATOR_NAME \n");
        sql.append(", M.NAME MODIFIER_NAME \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NVL(S.MODIFYTIME, S.CREATETIME) DESC");
        }
        
        List<AdVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(AdVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(AdVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(AdVO.class, sql.toString(), params);
        }
        if( list!=null && criteriaVO.isFullData() ){
            for(AdVO vo :  list){
//                vo.setUrl(vo.genUrl("", GlobalConstant.URL_GET_IMAGE, ImageSizeEnum.SMALL.getCode()));
                String prefix = jndiConfig.getProperty("url.prefix")+"/ec-service";
                vo.setUrl(vo.genUrl("", prefix+GlobalConstant.URL_GET_IMAGE, ImageSizeEnum.SMALL.getCode()));
            }
        }
        return list;
    }
    public int countByCriteria(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        // 商品
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(ProductCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();

        sql.append("FROM EC_AD S \n");
        sql.append("LEFT OUTER JOIN EC_FILE F ON F.PRIMARY_TYPE=#PRIMARY_TYPE AND F.PRIMARY_ID=S.ID \n");
        params.put("PRIMARY_TYPE", EcAd.class.getName());
        
        sql.append("LEFT OUTER JOIN EC_STORE ST ON ST.ID=S.STORE_ID \n");
        sql.append("LEFT OUTER JOIN EC_PRODUCT P ON P.ID=S.PRD_ID \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER A ON P.ID=S.APPROVE_USER \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER C ON C.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MODIFIER \n");
        sql.append("WHERE 1=1 \n");
        
        if (StringUtils.isNotEmpty(criteriaVO.getType())) {
            sql.append("AND S.TYPE=#TYPE \n");
            params.put("TYPE", criteriaVO.getType());
        }
        
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        // O:目前生效項目, A:已核准項目, N:未核准項目
        if( criteriaVO.getStatus()!=null ){
            if( "O".equals(criteriaVO.getStatus()) ){
                sql.append("AND S.APPROVE_TIME IS NOT NULL \n");
                sql.append("AND S.STARTTIME <= TRUNC(#TODATE) \n");
                sql.append("AND S.ENDTIME >= TRUNC(#TODATE) \n");
                params.put("TODATE", new Date());
            }else if( "A".equals(criteriaVO.getStatus()) ){
                sql.append("AND S.APPROVE_TIME IS NOT NULL \n");
            }else if( "N".equals(criteriaVO.getStatus()) ){
                sql.append("AND S.APPROVE_TIME IS NULL \n");
            }
        }
        
        if( criteriaVO.getActive()!=null ){
            if( criteriaVO.getActive() ){// 目前生效項目
                sql.append("AND S.APPROVE_TIME IS NOT NULL \n");
                sql.append("AND S.STARTTIME <= TRUNC(#TODATE) \n");
                sql.append("AND S.ENDTIME >= TRUNC(#TODATE) \n");
                params.put("TODATE", new Date());
            }else{// 未核准項目
                sql.append("AND S.APPROVE_TIME IS NULL \n");
            }
        }
        
        return sql.toString();
    }
    
    public List<AdVO> findByPrdId(AdEnum type, Long storeId, Long prdId){
        if( storeId==null || prdId==null ){
            logger.error("findByPrdId error storeId="+storeId+", prdId="+prdId);
            return null;
        }

        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setType(type.getCode());
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        criteriaVO.setFullData(true);
        List<AdVO> list = findByCriteria(criteriaVO);
        return list;
    }
    public AdVO findById(AdEnum type, Long id){
        if( id==null ){
            return null;
        }
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setType(type.getCode());
        criteriaVO.setId(id);
        criteriaVO.setFullData(true);
        List<AdVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param errors
     * @return 
     */
    public boolean checkInput(EcAd entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
    
}
