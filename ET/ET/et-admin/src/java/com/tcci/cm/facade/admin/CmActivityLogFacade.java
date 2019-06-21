/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmActivityLog;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.ActivityLogCriteriaVO;
import com.tcci.cm.model.admin.ActivityLogVO;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.fc.entity.org.TcUser;
//import com.tcci.fc.util.ResultSetHelper;
import com.tcci.fc.util.StringUtils;
import com.tcci.cm.model.global.GlobalConstant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Peter
 */
@Stateless
public class CmActivityLogFacade extends AbstractFacade<CmActivityLog> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmActivityLogFacade() {
        super(CmActivityLog.class);
    }
    
    /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     * @param simulated 
     */
    public void save(CmActivityLog entity, TcUser operator, boolean simulated){
        // ActivityLog 的錯誤不要影響前端使用，故此處要 try catch
        try{
            if( entity!=null ){
                if( entity.getId()!=null && entity.getId()>0 ){
                    entity.setCreator(operator);
                    entity.setCreatetimestamp(new Date());
                    this.edit(entity, simulated);
                }else{
                    entity.setCreator(operator);
                    entity.setCreatetimestamp(new Date());
                    this.create(entity, simulated);
                }
            }
        }catch(Exception e){
            logger.error("save exception:\n", e);
        }        
    }

    /**
     * 公用 LOG 
     * @param activeEnum
     * @param viewId
     * @param clientIP
     * @param content
     * @param success
     * @param operator 
     * @param simulated 
     */
    public void logActiveCommon(ActivityLogEnum activeEnum, String viewId, String clientIP, String content, boolean success, TcUser operator, boolean simulated){
        try{// LOG 任何錯誤不應影響正常運作
            CmActivityLog cmActivityLog = new CmActivityLog();
            cmActivityLog.setCode(activeEnum.getCode());
            cmActivityLog.setViewId(viewId);
            cmActivityLog.setSuccess(success);
            
            // String clientIP = JsfUtils.getClientIP();
            clientIP = (clientIP==null)? "{無IP資訊}":"{"+clientIP+"}";
            content = (content==null)? clientIP:clientIP+content;
            // 內容長度檢核
            content = StringUtils.safeTruncat(content, GlobalConstant.ENCODING_DEF, 10, 2000);
            
            cmActivityLog.setDetail(content);

            this.save(cmActivityLog, operator, simulated);
        }catch(Exception e){
            logger.error("logActiveCommon exception:\n ", e);
        }
    }
    
    /**
     * 影響單一 ID 的異動記錄
     * @param activeEnum
     * @param viewId
     * @param clientIP
     * @param id
     * @param success
     * @param operator
     * @param simulated
     */
    public void logActiveForSingleId(ActivityLogEnum activeEnum, String viewId, String clientIP, 
            Long id, boolean success, TcUser operator, boolean simulated){
         logActiveForSingleId(activeEnum, viewId, clientIP, id, activeEnum.getCode(), activeEnum.getName(), success, operator, simulated);
    }
    public void logActiveForSingleId(ActivityLogEnum activeEnum, String viewId, 
            String clientIP, Long id, String code, String name, boolean success, TcUser operator, 
            boolean simulated){
        try{// LOG 任何錯誤不應影響正常運作
            StringBuilder detailSB = new StringBuilder();
            boolean flag = false;// ActivityLogEnum 有無定義
   
            String table = (activeEnum!=null)? activeEnum.getTable():null;
            if( table!=null ){
                flag = true;
            }

            if( flag ){
                if( StringUtils.isEmpty(table) ){
                    detailSB.append("{[");
                }else{
                    detailSB.append("{").append(table).append(":[");
                }
                
                if( id!=null && id>0 ){
                    detailSB.append("ID:").append(id).append("|");
                }
                if( name!=null && !name.isEmpty() ){
                    detailSB.append(name).append("|");
                }
                if( code!=null && !code.isEmpty() ){
                    detailSB.append(code);
                }
                detailSB.append("]}");

                logActiveCommon(activeEnum, viewId, clientIP, detailSB.toString(), success, operator, simulated);
            }else{
                logger.error("logActiveForSingleId unsupposed activeEnum = "+activeEnum);
            }
        }catch(Exception e){
            logger.error("logActiveForSingleId exception:\n ", e);
        }
    }
    
    /**
     * 依查詢條件抓取資料
     * @param criteriaVO
     * @return 
     */
    public List<ActivityLogVO> findByCriteria(ActivityLogCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT S.ID, S.CODE, S.VIEW_ID, S.DETAIL, S.CREATOR, S.CREATETIMESTAMP, S.SUCCESS, \n");
        sql.append("U.LOGIN_ACCOUNT, U.CNAME AS USERNAME \n");
        sql.append("FROM CM_ACTIVITY_LOG S \n");
        sql.append("LEFT OUTER JOIN TC_USER U ON U.ID=S.CREATOR \n");
        sql.append("WHERE 1=1 \n");
        
        if( criteriaVO!=null ){
            // 關鍵字查詢
            if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().isEmpty() ){
                String keyword = criteriaVO.getKeyword().trim();

                sql.append("AND ( \n");
                sql.append(NativeSQLUtils.getLikeTranslateSQL("U.LOGIN_ACCOUNT", keyword, params).replaceFirst(" AND ", "")).append(" \n");
                sql.append(NativeSQLUtils.getLikeTranslateSQL("U.CNAME", keyword, params).replaceFirst(" AND ", " OR ")).append(" \n");
                sql.append(NativeSQLUtils.getLikeTranslateSQL("S.DETAIL", keyword, params).replaceFirst(" AND ", " OR ")).append(" \n");
                sql.append(") \n");
            }
            // URL關鍵字查詢
            if( criteriaVO.getKeywordUrl()!=null && !criteriaVO.getKeywordUrl().isEmpty() ){
                String keyword = "%"+criteriaVO.getKeywordUrl().trim()+"%";

                sql.append("AND S.VIEW_ID LIKE #keywordUrl \n");
                params.put("keywordUrl", keyword);
            }
            
            // 日期
            if( criteriaVO.getDateStart()!=null ){
                sql.append("AND S.CREATETIMESTAMP >= #dateStart \n");
                params.put("dateStart", criteriaVO.getDateStart());
            }
            if( criteriaVO.getDateEnd()!=null ){
                sql.append("AND S.CREATETIMESTAMP-1 < #dateEnd \n");
                params.put("dateEnd", criteriaVO.getDateEnd());
            }
            
            // 操作事件
            if( criteriaVO.getCode()!=null ){
                sql.append(NativeSQLUtils.genEqualSQL("S.CODE", criteriaVO.getCode(), params)).append(" \n");
            }
            
            // 結果
            if( criteriaVO.getSuccess()!=null ){
                if( criteriaVO.getSuccess() ){
                    sql.append("AND S.SUCCESS=1 \n");
                }else{
                    sql.append("AND S.SUCCESS=0 \n");
                }
            }
        }
        
        // order by 
        if( criteriaVO!=null && criteriaVO.getOrderBy()!=null ){
            sql.append("ORDER BY ").append(criteriaVO.getOrderBy());
        }else{
            sql.append("ORDER BY S.CREATETIMESTAMP DESC \n");
        }
        
        logger.debug("findByCriteria ...");
        
//        ResultSetHelper<ActivityLogVO> resultSetHelper = new ResultSetHelper(ActivityLogVO.class);
        List<ActivityLogVO> resList;
        if( criteriaVO!=null && criteriaVO.getFirstResult()!=null && criteriaVO.getMaxResults()!=null ){
//            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
            resList = this.selectBySql(ActivityLogVO.class, sql.toString(), params, criteriaVO.getFirstResult(), criteriaVO.getMaxResults());
        }else if( criteriaVO!=null && criteriaVO.getMaxResults()!=null ){
//            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params, 0, criteriaVO.getMaxResults());
            resList = this.selectBySql(ActivityLogVO.class, sql.toString(), params, 0, criteriaVO.getMaxResults());
        }else{
//            resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
            resList = this.selectBySql(ActivityLogVO.class, sql.toString(), params);
        }
        
        return resList;
    }
    
}
