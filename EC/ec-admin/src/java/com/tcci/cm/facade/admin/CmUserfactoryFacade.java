/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmFactoryCategory;
import com.tcci.cm.entity.admin.CmUserfactory;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.UserFactoryVO;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.cm.model.admin.QueryFilter;
import com.tcci.fc.util.ResultSetHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class CmUserfactoryFacade extends AbstractFacade<CmUserfactory> {
    @EJB CmFactoryFacade cmFactoryFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmUserfactoryFacade() {
        super(CmUserfactory.class);
    }

    /**
     * 依 USER 工廠權限取出關聯SAP CLIENT CODE
     * @param tcUser
     * @return 
     */
    //public List<String> findUserSapPermission(TcUser tcUser, boolean tccOnly, boolean monthplanQueryOnly, boolean monthplanIssueOnly) {
    public List<String> findUserSapPermission(TcUser tcUser) {
        // List<CmFactory> factoryList = findUserFactoryPermission(tcUser, tccOnly, monthplanQueryOnly, monthplanIssueOnly);
        List<CmFactory> factoryList = findUserFactoryPermission(tcUser);
        logger.debug("findUserSapPermission... factoryList = "+((factoryList!=null)?factoryList.size():0));
        
        List<String> resList = new ArrayList<String>();
        if( factoryList!=null ){
            for(CmFactory cmFactory : factoryList){
                if( !resList.contains(cmFactory.getSapClientCode()) ){
                    logger.debug("findUserSapPermission... cmFactory.getSapClientCode()="+cmFactory.getSapClientCode());
                    resList.add(cmFactory.getSapClientCode());
                }
            }
        }
        
        return resList;
    }
    
    /**
     * 完整工廠權限(工廠與公司交集，不含廠群組)
     * @param tcUser
     * @return 
     */
    public List<CmFactory> findUserFactoryPermission(TcUser tcUser) {
        //參考公司權限設定
        List<CmFactory> results = cmFactoryFacade.findByCompany(tcUser.getCmUsercompanyList());
        //其它角色只能看自己所屬的工廠
        Collection<CmUserfactory> userFactoryList = tcUser.getCmUserfactoryList();
        if (CollectionUtils.isNotEmpty(userFactoryList)) {
            if (results == null) {//參考公司權限設定
                results = new ArrayList<CmFactory>();
            }
            for (CmUserfactory uf : userFactoryList) {
                if (!results.contains(uf.getFactoryId())) {//參考公司權限設定
                    results.add(uf.getFactoryId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(results)) {
            Collections.sort(results); //排序
        }

        return results;
    }
    
    /**
     * 查詢工廠權限 (不含公司權限)
     * @return List<CmUserfactory>
     */
    public List<CmUserfactory> findByUser(TcUser tcUser) {
        Query q = em.createQuery("SELECT a FROM CmUserfactory a where a.userId=:userId ORDER BY a.id");
        q.setParameter("userId", tcUser);
        
        return q.getResultList();
    }

    /**
     * 依使用者 找出 關聯特殊工廠群組包含的工廠，並剔除已有權限的工廠 (全部角色設定聯集)
     * @param tcUser
     * @param groupType
     * @param fullList
     * @return 
     */
    public List<CmFactory> findSpecFactoryByLoginUser(TcUser tcUser, String groupType, List<CmFactory> fullList) { 
        // 限制權限
        List<CmFactory> limitList = cmFactoryFacade.findSpecFactoryByGroup(tcUser.getCmUserFactorygroupRList(), groupType, null);
        logger.debug(" findSpecFactoryByLoginUser limitList.size = "+ ((limitList!=null)? limitList.size():0));
        //  完整工廠權限(工廠與公司交集，不含廠群組)
        // List<CmFactory> fullList = findUserFactoryPermission(tcUser);
        
        // 剔除包含在完整權限的項目
        List<CmFactory> keepList = new ArrayList<CmFactory>();

        if( fullList==null || fullList.isEmpty() 
                || limitList==null || limitList.isEmpty() ){
            return limitList;
        }
        
        // 剔除包含在 queryFactories 中的廠別
        for(CmFactory cmFactory : limitList){
            boolean existed = false;
            for(CmFactory cmFactory2 : fullList){
                if( !existed && cmFactory.equals(cmFactory2) ){
                    existed = true;
                    break;
                }
            }
            if( !existed ){
                keepList.add(cmFactory);
            }
        }
        
        return keepList;
    }
    // 同上(for 未知工廠與公司交集權限)
    public List<CmFactory> findSpecFactoryByLoginUser(TcUser tcUser, String groupType) { 
        //  完整工廠權限(工廠與公司交集，不含廠群組)
        List<CmFactory> fullList = findUserFactoryPermission(tcUser);
        return findSpecFactoryByLoginUser(tcUser, groupType, fullList);
    }

    /**
     * 取得工廠資訊 (依主廠區分)
     * @return 
     */
    public List<CmFactoryCategory> fetchCmFactoryCategoryList(){
        // 抓取所有工廠資訊
        Query q = em.createNamedQuery("CmFactoryCategory.findAll");
        return q.getResultList();
    }
    
    public CmFactoryCategory findCmFactoryCategoryById(BigDecimal id){
        Query q = em.createNamedQuery("CmFactoryCategory.findById");
        q.setParameter("id", id);
        List<CmFactoryCategory> resList = q.getResultList();
        return (resList==null || resList.isEmpty())? null:resList.get(0);
    }
    
    /**
     * 查詢所有群組
     * @return List<TcGroup>
     */
    public List<CmFactory> findAllFactories() {
        Query q = em.createQuery("SELECT f FROM CmFactory f ORDER BY f.code");
        return q.getResultList();
    }
    
    /**
     * find by user Id
     * @param userId
     * @return 
     */
    public UserFactoryVO findById(long userId){
        QueryFilter filter = new QueryFilter();
        filter.setUserId(userId);
        List<UserFactoryVO> list = query(filter);
        
        return (list!=null && !list.isEmpty())? list.get(0):null;
    }
    
    /**
     * 依 QueryFilter 查詢使用者
     * @param filter
     * @return List<UserFactoryVO>
     */
    public List<UserFactoryVO> query(QueryFilter filter) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
    
        // 利用 LISTAGG 指令將
        sql.append("SELECT ")
            .append(" U.ID, U.LOGIN_ACCOUNT, U.EMP_ID, U.CNAME , U.EMAIL, U.CREATETIMESTAMP, \n")
            .append(" T.FACTORY AS FACTORYS, \n")
            .append(" P.COMPANY AS COMPANIES, \n")
            .append(" G.FGROUP AS FACTORYGROUPS, \n")
            .append(" CREATUSER.LOGIN_ACCOUNT AS CREATOR \n")
            .append(" FROM TC_USER U \n")
            .append(" LEFT OUTER JOIN TC_USER CREATUSER ON U.CREATOR = CREATUSER.ID \n")
            .append(" LEFT OUTER JOIN ( \n")
            .append("   SELECT UF.USER_ID, \n")
            .append("   LISTAGG(F.CODE||'('||F.NAME||')', ',') WITHIN GROUP (ORDER BY F.CODE) FACTORY, \n")
            .append("   LISTAGG('~'||F.ID||'~', ',') WITHIN GROUP (ORDER BY F.CODE) FACTORYCODE \n")
            .append("   FROM CM_USERFACTORY UF \n")
            .append("   JOIN CM_FACTORY F ON UF.FACTORY_ID=F.ID \n")
            .append("   GROUP BY UF.USER_ID \n")
            .append(" ) T ON U.ID = T.USER_ID \n")
            .append(" LEFT OUTER JOIN ( \n")
            .append("   SELECT A.USER_ID, \n")
            .append("   LISTAGG(B.SAP_CLIENT_CODE, ',') WITHIN GROUP (ORDER BY B.SAP_CLIENT_CODE) COMPANY, \n")// for display
            .append("   LISTAGG('~'||B.SAP_CLIENT_CODE||'~', ',') WITHIN GROUP (ORDER BY B.SAP_CLIENT_CODE) COMPANYCODE \n") // for search
            .append("   FROM CM_USERCOMPANY A \n")
            .append("   JOIN CM_COMPANY B ON B.SAP_CLIENT_CODE=A.SAP_CLIENT_CODE \n")
            .append("   GROUP BY A.USER_ID \n")
            .append(" ) P ON U.ID = P.USER_ID \n")
            .append(" LEFT OUTER JOIN ( \n")
             
            /*  20141110 改多 CM_USER_FACTORYGROUP_R.USERGROUP_ID 欄位 
            .append("   SELECT FG.USER_ID, \n")
            .append("   LISTAGG(F.GROUPNAME, ',') WITHIN GROUP (ORDER BY F.GROUPNAME) FGROUP, \n")
            .append("   LISTAGG('~'||F.ID||'~', ',') WITHIN GROUP (ORDER BY F.GROUPNAME) FGROUPID \n")
            .append("   FROM CM_USER_FACTORYGROUP_R FG \n")
            .append("   JOIN CM_FACTORYGROUP F ON FG.FACTORYGROUP_ID=F.ID \n")
            .append("   GROUP BY FG.USER_ID \n")*/
            .append("   SELECT USER_ID,  \n")
            .append("   LISTAGG(GROUPNAME, ',') WITHIN GROUP (ORDER BY GROUPNAME) FGROUP, \n")
            .append("   LISTAGG('~'||TO_CHAR(ID)||'~', ',') WITHIN GROUP (ORDER BY ID) FGROUPID \n")
            .append("   FROM ( \n")
            .append("     SELECT USER_ID, GROUPNAME, ID \n")
            .append("     FROM ( \n")
            .append("       SELECT FG.USER_ID, F.GROUPNAME, F.ID \n")
            .append("       FROM CM_USER_FACTORYGROUP_R FG  \n")
            .append("       JOIN CM_FACTORYGROUP F ON F.disabled=0 and FG.FACTORYGROUP_ID=F.ID  \n");
                
        // 指定針對某回饋角色的設定
        /*if( GlobalConstant.ENABLED_FEEDBACK_RULE_PERMISSION ){
            if( filter.getUsergroupId()!=null ) {
                sql.append("       WHERE FG.USERGROUP_ID=#USERGROUP_ID \n");
                params.put("USERGROUP_ID", filter.getUsergroupId());
            }
        }*/
                
        sql.append("     ) A \n")
            .append("     GROUP BY USER_ID, GROUPNAME, ID \n")
            .append("   ) B \n")
            .append("   GROUP BY USER_ID \n")
                
            .append(" ) G ON U.ID = G.USER_ID \n")

            .append(" WHERE U.DISABLED = 0 \n"); // 有效帳號

        // USER ID
        if( filter.getUserId()>0 ) {
            sql.append(NativeSQLUtils.genEqulSQL("U.ID", filter.getUserId(), params)); 
        }
        // 登入帳號:
        if( StringUtils.isNotBlank(filter.getLoginAccount()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("U.LOGIN_ACCOUNT", filter.getLoginAccount(), params)); 
        }
        // 員工工號:  
        if( StringUtils.isNotBlank(filter.getEmpId()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("U.EMP_ID", filter.getEmpId(), params)); 
        }        
        // 電子郵件:
        if( StringUtils.isNotBlank(filter.getEmail()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("U.EMAIL", filter.getEmail(), params)); 
        }          
        // 員工姓名:  
        if( StringUtils.isNotBlank(filter.getCname()) ) {
            sql.append(NativeSQLUtils.getLikeTranslateSQL("U.CNAME", filter.getCname(), params)); 
        }
        // 工廠: 
        if( filter.getFactoryId()!=null && filter.getFactoryId()>0 ) {
            sql.append(" AND Instr(T.FACTORYCODE, #factoryId) > 0 "); 
            params.put("factoryId", "~"+filter.getFactoryId().toString()+"~");
        }else{
            if( StringUtils.isNotBlank(filter.getCompany()) ) {
                // 公司 (以整個公司設權限)
                sql.append(" AND Instr(P.COMPANYCODE, #company) > 0 "); 
                params.put("company", "~"+filter.getCompany()+"~");
            }
        }
        // 工廠群組
        if( filter.getFactoryGroupId()!=null  && filter.getFactoryGroupId()>0 ) {
            sql.append(" AND Instr(G.FGROUPID, #factoryGroupId) > 0 "); 
            params.put("factoryGroupId", "~"+filter.getFactoryGroupId().toString()+"~");
        }
        
        sql.append(" ORDER BY U.LOGIN_ACCOUNT "); // 登入帳號排序
        logger.debug("sql = "+sql.toString());
        
        Query q = em.createNativeQuery(sql.toString());
       
        // 套入查詢條件
        for (String key : params.keySet()) {
            q.setParameter(key, params.get(key));
        }

        ResultSetHelper<UserFactoryVO> resultSetHelper = new ResultSetHelper(UserFactoryVO.class);
        List<UserFactoryVO> userFactoryVOList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return userFactoryVOList;       
    }    
}
