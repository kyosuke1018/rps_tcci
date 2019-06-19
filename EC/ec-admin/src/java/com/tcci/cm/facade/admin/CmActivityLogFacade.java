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
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.cm.enums.ActivityLogEnum;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.fc.util.StringUtils;
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
     */
    public void save(CmActivityLog entity, TcUser operator){
        // ActivityLog 的錯誤不要影響前端使用，故此處要 try catch
        try{
            if( entity!=null ){
                if( entity.getId()!=null && entity.getId()>0 ){
                    entity.setCreator(operator);
                    entity.setCreatetimestamp(new Date());
                    this.edit(entity);
                }else{
                    entity.setCreator(operator);
                    entity.setCreatetimestamp(new Date());
                    this.create(entity);
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
     * @param content
     * @param success
     * @param operator 
     */
    public void logActiveCommon(ActivityLogEnum activeEnum, String viewId, String content, boolean success, TcUser operator){
        try{// LOG 任何錯誤不應影響正常運作
            CmActivityLog cmActivityLog = new CmActivityLog();
            cmActivityLog.setCode(activeEnum.getCode());
            cmActivityLog.setViewId(viewId);
            cmActivityLog.setSuccess(success);
            
            //String clientIP = JsfUtils.getClientIP();
            String clientIP = JsfUtils.getRequestServerName();
            clientIP = (clientIP==null)? "{無IP資訊}":"{"+clientIP+"}";
            content = (content==null)? clientIP:clientIP+content;
            // 內容長度檢核
            content = StringUtils.safeTruncat(content, "UTF-8", 10, 2000);
            
            cmActivityLog.setDetail(content);

            this.save(cmActivityLog, operator);
        }catch(Exception e){
            logger.error("logActiveCommon exception:\n ", e);
        }
    }
    
    /**
     * 影響單一 ID 的異動記錄
     * @param activeEnum
     * @param viewId
     * @param id
     * @param code
     * @param success
     * @param name
     * @param operator
     */
    public void logActiveForSingleId(ActivityLogEnum activeEnum, String viewId, 
            long id, boolean success, TcUser operator){
         logActiveForSingleId(activeEnum, viewId, id, activeEnum.getCode(), activeEnum.getName(), success, operator);
    }
    public void logActiveForSingleId(ActivityLogEnum activeEnum, String viewId, 
            long id, String code, String name, boolean success, TcUser operator){
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
                
                if( id>0 ){
                    detailSB.append("ID:").append(id).append("|");
                }
                if( name!=null && !name.isEmpty() ){
                    detailSB.append(name).append("|");
                }
                if( code!=null && !code.isEmpty() ){
                    detailSB.append(code);
                }
                detailSB.append("]}");

                logActiveCommon(activeEnum, viewId, detailSB.toString(), success, operator);
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
        
        sql.append("select s.id, s.code, s.view_id, s.detail, s.creator, s.createtimestamp, s.success, \n");
        sql.append("u.login_account, u.cname as userName, o.id as deptId, o.name as deptName \n");
        sql.append("from CM_ACTIVITY_LOG s \n");
        sql.append("left outer join TC_USER u on u.id=s.creator \n");
        //sql.append("left outer join CM_ORG o on o.id=u.org_id \n");
        sql.append("LEFT OUTER JOIN ( \n");
        sql.append("  select a.id, a.name||' ['||NVL(c.name,'無公司別')||']' as name \n");
        sql.append("  from CM_ORG a \n");
        sql.append("  left outer join CS_DEPARTMENT b on b.id=a.src_id \n");
        sql.append("  left outer join CS_COMPANY c on c.id=b.companyid \n");
        sql.append("  where a.CTYPE='D' \n"); 
        sql.append(") o on o.id=u.org_id \n");
        sql.append("where 1=1 \n");
        
        if( criteriaVO!=null ){
            // 關鍵字查詢
            if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().isEmpty() ){
                String keyword = criteriaVO.getKeyword().trim();

                sql.append("and ( \n");
                sql.append(NativeSQLUtils.getLikeTranslateSQL("u.login_account", keyword, params).replaceFirst(" AND ", "")).append(" \n");
                sql.append(NativeSQLUtils.getLikeTranslateSQL("u.cname", keyword, params).replaceFirst(" AND ", " OR ")).append(" \n");
                sql.append(NativeSQLUtils.getLikeTranslateSQL("s.detail", keyword, params).replaceFirst(" AND ", " OR ")).append(" \n");
                sql.append(") \n");
            }
            
            // 日期
            if( criteriaVO.getDateStart()!=null ){
                sql.append("and s.createtimestamp >= #dateStart \n");
                params.put("dateStart", criteriaVO.getDateStart());
            }
            if( criteriaVO.getDateEnd()!=null ){
                sql.append("and s.createtimestamp-1 < #dateEnd \n");
                params.put("dateEnd", criteriaVO.getDateEnd());
            }
            
            // 操作事件
            if( criteriaVO.getCode()!=null ){
                sql.append(NativeSQLUtils.genEqualSQL("s.code", criteriaVO.getCode(), params)).append(" \n");
            }
            
            // 結果
            if( criteriaVO.getSuccess()!=null ){
                if( criteriaVO.getSuccess() ){
                    sql.append("and s.success=1 \n");
                }else{
                    sql.append("and s.success=0 \n");
                }
            }
        }
        
        sql.append("order by s.createtimestamp desc \n");
        
        logger.debug("findByCriteria sql = \n"+sql.toString());
        
        ResultSetHelper<ActivityLogVO> resultSetHelper = new ResultSetHelper(ActivityLogVO.class);
        List<ActivityLogVO> resList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return resList;
    }

}
