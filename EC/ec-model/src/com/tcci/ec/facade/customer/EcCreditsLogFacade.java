/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.customer;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcCreditsLog;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.ec.model.CreditsLogVO;
import com.tcci.ec.model.criteria.CustomerCriteriaVO;
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
public class EcCreditsLogFacade extends AbstractFacade<EcCreditsLog> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcCreditsLogFacade() {
        super(EcCreditsLog.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcCreditsLog entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytime(new Date());
                this.edit(entity);
                logger.info("save update "+entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetime(new Date());
                this.create(entity);
                logger.info("save new "+entity);
            }
        }
    }

    public void saveVO(CreditsLogVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcCreditsLog entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcCreditsLog();
            // 需保存的系統產生欄位
            //vo.setCreator(entity.getCreator()!=null? entity.getCreator().getId():null);
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
    public List<CreditsLogVO> findByCriteria(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
                
        sql.append("SELECT S.* \n");
        sql.append(", M.LOGIN_ACCOUNT, M.NAME \n");
        sql.append(", CR.LOGIN_ACCOUNT CREATOR_ACCOUNT, CR.NAME CREATOR_NAME \n");
        sql.append(", MD.LOGIN_ACCOUNT MODIFIER_ACCOUNT, MD.NAME MODIFIER_NAME \n");

        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY NVL(S.MODIFYTIME, S.CREATETIME) DESC");
        }
        
        List<CreditsLogVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CreditsLogVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(CreditsLogVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(CreditsLogVO.class, sql.toString(), params);
        }
        return list;
    }
    public int countByCriteria(CustomerCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    public String findByCriteriaSQL(CustomerCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();
        
        sql.append("FROM EC_CREDITS_LOG S \n");
        sql.append("JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER CR ON CR.ID=S.CREATOR \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER MD ON MD.ID=S.MODIFIER \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getMemberId()!=null ){
            sql.append("AND S.MEMBER_ID=#MEMBER_ID \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        if( criteriaVO.getLoginAccount()!=null ){
            sql.append("AND UPPER(M.LOGIN_ACCOUNT)=UPPER(#LOGIN_ACCOUNT) \n");
            params.put("LOGIN_ACCOUNT", criteriaVO.getLoginAccount());
        }
        if( criteriaVO.getStartAt()!=null ){
            sql.append("AND NVL(S.MODIFYTIME, S.CREATETIME)>=#START_AT \n");
            params.put("START_AT", criteriaVO.getStartAt());
        }

        return sql.toString();
    }
    
    /**
     * 
     * @param storeId
     * @param id
     * @param fullInfo
     * @return 
     */
    public CreditsLogVO findById(Long storeId, Long id, boolean fullInfo){
        if( storeId==null || id==null ){
            logger.error("findById error storeId="+storeId+", id="+id);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setId(id);
        List<CreditsLogVO> list = findByCriteria(criteriaVO);
        
        CreditsLogVO prdVO = (list!=null && !list.isEmpty())? list.get(0):null;
        return prdVO;
    }
    
    /**
     * 依會員
     * @param storeId
     * @param loginAccount
     * @return 
     */
    public CreditsLogVO findByMember(Long storeId, String loginAccount){
        if( storeId==null || loginAccount==null ){
            logger.error("findByMember error storeId="+storeId+", loginAccount="+loginAccount);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setLoginAccount(loginAccount);
        List<CreditsLogVO> list = findByCriteria(criteriaVO);
        
        CreditsLogVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        return vo;
    }
    public CreditsLogVO findByMemberId(Long storeId, Long memberId){
        if( storeId==null || memberId==null ){
            logger.error("findByMemberId error storeId="+storeId+", memberId="+memberId);
            return null;
        }
        
        CustomerCriteriaVO criteriaVO = new CustomerCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setMemberId(memberId);
        List<CreditsLogVO> list = findByCriteria(criteriaVO);
        
        CreditsLogVO vo = (list!=null && !list.isEmpty())? list.get(0):null;
        return vo;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param locale
     * @param errors
     * @return 
     */
    public boolean checkInput(EcCreditsLog entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
    
}
