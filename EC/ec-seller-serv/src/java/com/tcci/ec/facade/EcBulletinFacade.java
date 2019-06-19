/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcBulletin;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.model.BulletinVO;
import com.tcci.ec.model.criteria.ProductCriteriaVO;
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
public class EcBulletinFacade extends AbstractFacade<EcBulletin> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcBulletinFacade() {
        super(EcBulletin.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcBulletin entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            // default while null 
            if( entity.getDisabled()==null ){ entity.setDisabled(false); }
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
    public void saveVO(BulletinVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcBulletin entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcBulletin();
            // 需保存的系統產生欄位
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
    public List<BulletinVO> findByCriteria(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", NVL(S.MODIFYTIME, S.CREATETIME) LAST_UPDATE_TIME \n");
        sql.append(", C.NAME||'('||C.LOGIN_ACCOUNT||')' CREATOR_LABEL \n");
        sql.append(", M.NAME||'('||M.LOGIN_ACCOUNT||')' MODIFIER_LABEL \n");
        sql.append(", CASE WHEN S.DISABLED=0 \n");
        sql.append("    AND TRUNC(S.STARTTIME)<=TRUNC(SYSDATE) \n");
        sql.append("    AND TRUNC(S.ENDTIME)>=TRUNC(SYSDATE) \n");
        sql.append("  THEN 1 ELSE 0 END ONLINED \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NVL(S.MODIFYTIME, S.CREATETIME) DESC");
        }
        
        List<BulletinVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(BulletinVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(BulletinVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(BulletinVO.class, sql.toString(), params);
        }
        return list;
    }
    public int countByCriteria(ProductCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(ProductCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();

        sql.append("FROM EC_BULLETIN S \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER C ON C.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MODIFIER \n");
        
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        
        if( criteriaVO.getActive()!=null ){
            if( criteriaVO.getActive() ){// 只顯示目前生效公告
                sql.append("AND S.DISABLED=0 \n");
                sql.append("AND TRUNC(S.STARTTIME) <= TRUNC(#TODATE) \n");
                sql.append("AND TRUNC(S.ENDTIME) >= TRUNC(#TODATE) \n");
                params.put("TODATE", new Date());
            }/*else{// 未核准項目
                sql.append("AND (S.DISABLED=1 \n");
                sql.append("OR S.STARTTIME > TRUNC(#TODATE) \n");
                sql.append("OR S.ENDTIME < TRUNC(#TODATE)) \n");
                params.put("TODATE", new Date());
            }*/
        }
        
        return sql.toString();
    }
    
    public BulletinVO findById(Long id){
        if( id==null ){
            return null;
        }
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        criteriaVO.setId(id);
        List<BulletinVO> list = findByCriteria(criteriaVO);
        return (list!=null && !list.isEmpty())?list.get(0):null;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(EcBulletin entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
}

