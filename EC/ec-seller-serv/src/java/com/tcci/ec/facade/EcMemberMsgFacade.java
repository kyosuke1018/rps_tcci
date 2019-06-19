/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.util.ExtBeanUtils;
import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcMemberMsg;
import com.tcci.ec.model.MemberMsgVO;
import com.tcci.ec.model.criteria.MemberCriteriaVO;
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
public class EcMemberMsgFacade extends AbstractFacade<EcMemberMsg> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcMemberMsgFacade() {
        super(EcMemberMsg.class);
    }
    
    /**
     * 單筆儲存
     * @param entity
     * @param operator
     * @param simulated
     */
    public void save(EcMemberMsg entity, EcMember operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
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
    public void saveVO(MemberMsgVO vo, EcMember operator, boolean simulated){
        if( vo!=null ){
            EcMemberMsg entity = (vo.getId()!=null && vo.getId()>0)? this.find(vo.getId()):new EcMemberMsg();
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
        }
    }
    
    public int countByCriteria(MemberCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT COUNT(S.ID) COUNTS \n");
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        return this.count(sql.toString(), params);
    }
    /**
     * 依條件查詢商品
     * @param criteriaVO
     * @return 
     */
    public List<MemberMsgVO> findByCriteria(MemberCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append(", M.LOGIN_ACCOUNT, M.NAME MEMBER_NAME \n");
        sql.append(", ST.CNAME STORE_NAME, P.CNAME PRD_NAME \n");
        sql.append(", NVL(R.CC, 0) REPLY_COUNT \n");
        
        sql.append(findByCriteriaSQL(criteriaVO, params));
        
        if( criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIME DESC");
        }
        
        List<MemberMsgVO> list = null;
        if( criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberMsgVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO.getMaxResults()!=null ){
            list = this.selectBySql(MemberMsgVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
            list = this.selectBySql(MemberMsgVO.class, sql.toString(), params);
        }
        return list;
    }
    public String findByCriteriaSQL(MemberCriteriaVO criteriaVO, Map<String, Object> params){
        StringBuilder sql = new StringBuilder();

        sql.append("FROM EC_MEMBER_MSG S \n");
        sql.append("LEFT OUTER JOIN EC_MEMBER M ON M.ID=S.MEMBER_ID \n"); 
        sql.append("LEFT OUTER JOIN EC_STORE ST ON ST.ID=S.STORE_ID \n");
        sql.append("LEFT OUTER JOIN EC_PRODUCT P ON P.ID=S.PRD_ID \n");
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("     SELECT PARENT, COUNT(*) CC FROM EC_MEMBER_MSG GROUP BY PARENT \n");
        sql.append(") R ON R.PARENT=S.ID \n");
        sql.append("WHERE 1=1 \n");

        if( criteriaVO.getReply()!=null ){
            if( criteriaVO.getReply() ){
                sql.append("AND S.PARENT > 0 \n"); 
            }else{
                sql.append("AND S.PARENT IS NULL \n"); 
            }
        }
        if( criteriaVO.getStoreId()!=null ){
            sql.append("AND S.STORE_ID=#STORE_ID \n");
            params.put("STORE_ID", criteriaVO.getStoreId());
        }
        if( criteriaVO.getPrdId()!=null ){
            sql.append("AND S.PRD_ID=#PRD_ID \n");
            params.put("PRD_ID", criteriaVO.getPrdId());
        }
        if( criteriaVO.getParent()!=null ){
            sql.append("AND S.PARENT=#PARENT \n");
            params.put("PARENT", criteriaVO.getParent());
        }
        if( criteriaVO.getId()!=null ){
            sql.append("AND S.ID=#ID \n");
            params.put("ID", criteriaVO.getId());
        }
        // 待回覆留言
        if( criteriaVO.getReplyMsg()!=null && criteriaVO.getReplyMsg()
            && criteriaVO.getStoreId()!=null && criteriaVO.getMemberId()!=null ){
            sql.append("AND EXISTS ( \n");
            // 同 EcStoreFacade.countTodo() 條件
            sql.append("    SELECT T.* \n");
            sql.append("    FROM EC_MEMBER_MSG T \n");
            sql.append("    LEFT OUTER JOIN ( \n");// 最後一筆回覆
            sql.append("         SELECT A.* \n");
            sql.append("         FROM EC_MEMBER_MSG A \n");
            sql.append("         JOIN ( \n");
            sql.append("           SELECT MAX(ID) MID, PARENT FROM EC_MEMBER_MSG \n");
            sql.append("           WHERE STORE_ID=#STORE_ID AND PARENT IS NOT NULL \n");
            sql.append("           GROUP BY PARENT \n");
            sql.append("         ) L ON L.MID=A.ID \n");
            sql.append("    ) R ON R.PARENT=T.ID \n");
            sql.append("    WHERE T.STORE_ID=#STORE_ID \n");
            sql.append("    AND T.PARENT IS NULL \n");// 非回覆
            sql.append("    AND (R.ID IS NULL OR R.MEMBER_ID<>#MEMBER_ID) \n");// 無回覆 or 最後一筆非自己回覆
            sql.append("    AND T.ID=S.ID \n");
            sql.append(") \n");
            
            params.put("STORE_ID", criteriaVO.getStoreId());
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }else if( criteriaVO.getMemberId()!=null ){
            // 特定人員相關留言 (自己留言 or 該留言自己有回覆過)
            sql.append("AND ( \n");
            sql.append("    (S.MEMBER_ID=#MEMBER_ID AND S.PARENT IS NULL) \n");
            sql.append("    OR \n");
            sql.append("    EXISTS ( \n");
            sql.append("        SELECT T.* \n");
            sql.append("        FROM EC_MEMBER_MSG T \n");
            sql.append("        WHERE T.STORE_ID=#STORE_ID \n");
            sql.append("        AND T.MEMBER_ID=#MEMBER_ID \n");
            sql.append("        AND T.PARENT=S.ID \n");
            sql.append("    ) \n");
            sql.append(") \n");
            params.put("MEMBER_ID", criteriaVO.getMemberId());
        }
        
        return sql.toString();
    }
    
    public List<MemberMsgVO> findByPrdId(Long storeId, Long prdId){
        if( storeId==null || prdId==null ){
            logger.error("findByOrderId error storeId="+storeId+", prdId="+prdId);
            return null;
        }
        
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setPrdId(prdId);
        List<MemberMsgVO> list = findByCriteria(criteriaVO);
        return list;
    }
    public List<MemberMsgVO> findByParentId(Long storeId, Long parentId){
        if( storeId==null || parentId==null ){
            logger.error("findByOrderId error storeId="+storeId+", parentId="+parentId);
            return null;
        }
        
        MemberCriteriaVO criteriaVO = new MemberCriteriaVO();
        criteriaVO.setStoreId(storeId);
        criteriaVO.setParent(parentId);
        List<MemberMsgVO> list = findByCriteria(criteriaVO);
        return list;
    }

    /**
     * 輸入檢查
     * @param entity
     * @param member
     * @param errors
     * @return 
     */
    public boolean checkInput(EcMemberMsg entity, EcMember member, Locale locale, List<String> errors) {
        boolean pass = true;
        pass = inputCheckFacade.checkInput(entity, locale, errors);

        return pass;
    }
}
