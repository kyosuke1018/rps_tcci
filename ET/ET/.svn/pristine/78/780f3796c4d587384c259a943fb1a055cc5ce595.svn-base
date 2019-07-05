/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmCompany;
import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmFactorygroup;
import com.tcci.cm.entity.admin.CmUserFactorygroupR;
import com.tcci.cm.entity.admin.CmUsercompany;
import com.tcci.cm.entity.admin.CmUserfactory;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import com.tcci.cm.model.admin.CmFactoryVO;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.et.model.admin.MenuFunctionVO;
import com.tcci.et.model.admin.PermissionCriteriaVO;
import com.tcci.et.model.admin.PlantPermissionVO;
import com.tcci.et.model.admin.UserLoaderVO;
import com.tcci.et.model.admin.UserPermissionVO;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.org.TcUsergroupFacade;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.et.enums.AuthLevelEnum;
import com.tcci.et.facade.comparator.MenuComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author peter.pan
 */
@Stateless
@Named
public class PermissionFacade extends AbstractFacade<TcUser> {
    @EJB TcUserFacade tcUserFacade;
    @EJB TcGroupFacade tcGroupFacade;
    @EJB TcUsergroupFacade tcUsergroupFacade;
    @EJB CmFactoryFacade cmFactoryFacade;

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public PermissionFacade() {
        super(TcUser.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    //<editor-fold defaultstate="collapsed" desc="for User & Function">
    /**
     * 依 viewId 檢查 [功能] 授權
     * @param functions
     * @param viewId
     * @return 
     */
    public boolean checkAuthorizationByViewId(List<MenuFunctionVO> functions, String viewId){
        if( viewId==null || viewId.isEmpty() ){// 視為不檢查
            logger.info("checkAuthorizationByViewId viewId isEmpty !");
            return true;
        }
        
        // 特殊狀況處理
        String checkViewId = viewId;
        if( viewId.endsWith("/index.xhtml") ){
            return true;
        }else if( viewId.endsWith("/photosSelect.xhtml") 
                || viewId.endsWith("/videosSelect.xhtml")
                || viewId.endsWith("/tinymceDlg.xhtml") ){// for TinyMCE Dialogs
            checkViewId = "/publication.xhtml";
        }else if( viewId.endsWith("/formView.xhtml") 
                || viewId.endsWith("/formSign.xhtml")){//for bpm
            return true;
        }
                
        // 依授權可執行的功能之 URL 與 viewId 比對判斷
        if( functions!=null ){
            for(MenuFunctionVO vo: functions){
                if( vo.getUrl()!=null ){
                    if( vo.getUrl().indexOf(checkViewId) > 0 ){
                        logger.debug("checkAuthorizationByViewId checkViewId ="+checkViewId);
                        logger.debug("checkAuthorizationByViewId vo.getUrl() ="+vo.getUrl());
                        return true;
                    }
                }
            }
        }
        
        logger.info("checkAuthorizationByViewId fail : viewId = "+viewId);
        return false;
    }
    
    /**
     * 取的使用者功能選單
     * @param userId
     * @return 
     */
    public List<MenuFunctionVO> fetchFunctionInfoByUser(long userId) {
        return fetchFunctionInfo(0, 0, 0, userId, null, null);
    }
    /**
     * 取得所有功能資訊
     * @return 
     */
    public List<MenuFunctionVO> fetchFunctionInfoAll(){
        return fetchFunctionInfo(0, 0, 0, 0, null , null);
    }
    /**
     * 依單一 TC_GROUP 群組取得功能資訊
     * @param groupid
     * @return 
     */
    public List<MenuFunctionVO> fetchFunctionInfoByGroup(long groupid){
        return fetchFunctionInfo(0, 0, groupid, 0, null, null);
    }
    
    /**
     * 取得功能資訊
     * @param mid
     * @param sid
     * @param groupid
     * @param userId
     * @param usergroupIds
     * @param groupPrefix
     * @return 
     */
    public List<MenuFunctionVO> fetchFunctionInfo(long mid, long sid, long groupid, long userId, List<Long> usergroupIds, String groupPrefix){
        Map<String, Object> params = new HashMap<String, Object>();
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT A.ID, A.TITLE, A.URL, A.SORTNUM \n");
        sb.append(" , B.ID AS SID, B.TITLE AS STITLE, B.SORTNUM AS SSORTNUM \n");
        sb.append(" , C.ID AS MID, C.TITLE AS MTITLE, C.SORTNUM AS MSORTNUM \n");
        sb.append(" , A.TITLE AS TITLECN, B.TITLE AS STITLECN, C.TITLE AS MTITLECN \n");// 無簡體版
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            sb.append(" , R.AUTH \n");// 授權等級
        }
        
        sb.append(" FROM CM_FUNCTION A \n");
     
        sb.append(" JOIN CM_FUNCTION B ON B.CLEVEL=2 AND B.ID=A.PARENT \n");// 子選單
        sb.append(" JOIN CM_FUNCTION C ON C.CLEVEL=1 AND C.ID=B.PARENT \n");// 主選單
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            sb.append(" JOIN ( \n");
            sb.append("     SELECT R.FUNC_ID, R.AUTH \n");
            sb.append("     FROM CM_GROUP_FUNCTION_R R \n");
            
            if( groupPrefix!=null ){
                String keyword = groupPrefix+"%";
                sb.append("     JOIN TC_GROUP Q ON Q.ID=R.GROUP_ID AND Q.CODE LIKE #GROUPCODE \n");
                params.put("GROUPCODE", keyword);
            }
            
            if( userId>0 ){
                sb.append("     JOIN TC_USERGROUP G ON G.GROUP_ID=R.GROUP_ID AND G.USER_ID=#USER_ID \n");
                params.put("USER_ID", userId);
            }
            
            sb.append("     WHERE 1=1 \n");
        }
        
        if( groupid>0 ){
            sb.append(NativeSQLUtils.genEqulSQL("R.GROUP_ID", groupid, params)).append(" \n");
        }
        if( usergroupIds!=null ){// 需考慮權限
            if( !usergroupIds.isEmpty() ){// 有權限
                sb.append(NativeSQLUtils.getInSQL("R.GROUP_ID", usergroupIds, params, 2)).append(" \n");
            }else{// 無權限
                return null;
            }
        }
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            sb.append("     GROUP BY R.FUNC_ID, R.AUTH \n");// 確保出現一次即可
            sb.append(" ) R ON R.FUNC_ID=A.ID \n");
        }
        
        sb.append(" WHERE A.CLEVEL=3 \n");
        sb.append(" AND A.URL<>'#' \n"); // === 有功能頁面項目 ===
        
        if( mid>0 ){
            sb.append(NativeSQLUtils.genEqulSQL("C.ID", mid, params));
        }
        if( sid>0 ){
            sb.append(NativeSQLUtils.genEqulSQL("B.ID", sid, params));
        }
        
        sb.append(" GROUP BY A.ID, A.TITLE, A.URL, A.SORTNUM \n");// 確保出現一次即可
        sb.append(" , B.ID, B.TITLE, B.SORTNUM \n");
        sb.append(" , C.ID, C.TITLE, C.SORTNUM \n");
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            sb.append(" , R.AUTH \n");// 授權等級
        }
        
        sb.append(" ORDER BY C.SORTNUM, B.SORTNUM, A.SORTNUM \n");// 排序 // 增加 r.sortnum=Rule自訂排序
   
        logger.debug("fetchFunctionInfo sql = \n"+sb.toString());

        ResultSetHelper<MenuFunctionVO> resultSetHelper = new ResultSetHelper(MenuFunctionVO.class);
        List<MenuFunctionVO> ppFunctionVOList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            // 同一功能取出最高授權等級(level值最小)即可
            List<MenuFunctionVO> resList = new ArrayList<MenuFunctionVO>();
            List<Long> existedList = new ArrayList<Long>();
            if( ppFunctionVOList!=null ){
                AuthLevelEnum authLevel = AuthLevelEnum.ALL;
                int level = authLevel.getLevel();
                while( authLevel!=null ){
                    logger.debug("fetchFunctionInfo level = "+level);
                    for(MenuFunctionVO vo : ppFunctionVOList){
                        AuthLevelEnum thisAuth = AuthLevelEnum.getFromCode(vo.getAuth());
                        if( thisAuth!=null && thisAuth==authLevel && !existedList.contains(vo.getId()) ){
                            existedList.add(vo.getId());
                            resList.add(vo);
                        }else{
                            //logger.debug("fetchFunctionInfo existed vo.getId() = "+vo.getId());
                        }
                    }
                    // 同一功能取出最高授權等級(level值最小)即可
                    level++;
                    authLevel = AuthLevelEnum.getByLevel(level);
                }
            }
            
            logger.debug("fetchFunctionInfo resList = "+resList.size());
            //logger.debug("fetchFunctionInfo = "+resList.get(0).getMsortnum());
            Collections.sort(resList, new MenuComparator());
            //logger.debug("fetchFunctionInfo = "+resList.get(0).getMsortnum());
            return resList;
        }
        
        return ppFunctionVOList;       
    }
    //</editor-fold>
  
    //<editor-fold defaultstate="collapsed" desc="for role">
    public List<TcUser> findUsersByRole(String groupCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT A.ID, A.LOGIN_ACCOUNT, A.EMAIL, A.EMP_ID,A. DISABLED, A.CNAME, A.CREATETIMESTAMP \n");
        sb.append("FROM TC_USER A \n");
        sb.append("JOIN TC_USERGROUP B ON B.USER_ID=A.ID \n");
        sb.append("JOIN TC_GROUP C ON C.ID=B.GROUP_ID \n");
        sb.append("WHERE A.DISABLED=0 \n");
        sb.append("AND C.CODE=#groupCode \n");
        
        params.put("groupCode", groupCode);
        
        sb.append("ORDER BY A.LOGIN_ACCOUNT \n");
        
        logger.debug("findUsersByRole ...");

        ResultSetHelper<TcUser> resultSetHelper = new ResultSetHelper(TcUser.class);
        List<TcUser> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        
        return resList;
    }

    /**
     * 共用 SQL for 檢查 user group
     * @param userId
     * @param params
     * @return 
     */
    public String commonUserGroupSQL(long userId, Map<String, Object> params){
        StringBuilder sb = new StringBuilder();
        sb.append("select count(a.ID) from TC_GROUP a \n");
        sb.append("join TC_USERGROUP b on b.GROUP_ID=a.ID and b.USER_ID=#user_id \n");
        sb.append("where 1=1 \n");
        
        params.put("user_id", userId);
        
        return sb.toString();
    }
    
    /**
     * @param user
     * 是否在 特定群組 (By DB Setting)
     * @param groupCode
     * @return 
     */
    public boolean inUserGroup(TcUser user, String groupCode){
        /*if( user!=null && user.getTcUsergroupCollection()!=null ){
            for(TcUsergroup tcUsergroup : user.getTcUsergroupCollection()){
                if( groupCode!=null && groupCode.equals(tcUsergroup.getGroupId().getCode()) ){
                    return true;
                }
            }
        }*/
        String[] groups = groupCode.split(",");
        List<String> groupCodeList = Arrays.asList(groups);
        return inUserGroup(user, groupCodeList);
    }
    
    /**
     * 是否在 特定群組集
     * @param user
     * @param groupCodeList
     * @return 
     */
    public boolean inUserGroup(TcUser user, List<String> groupCodeList){
        if( user==null || groupCodeList==null || groupCodeList.isEmpty() ){
            return false;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(commonUserGroupSQL(user.getId(), params));
        sb.append(NativeSQLUtils.getInSQL("a.CODE", groupCodeList, params));
                
        return count(sb.toString(), params)>0;
    }
    
    /**
     * 是否在 特定群類別 (by 自首或字尾)
     * @param user
     * @param keyword
     * @param isPrefix
     * @return 
     */
    public boolean inUserGroup(TcUser user, String keyword, boolean isPrefix){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(commonUserGroupSQL(user.getId(), params));

        String likeStr = (isPrefix)? keyword+"%":"%"+keyword;
        sb.append("and a.CODE like #keyword \n");
        params.put("keyword", likeStr);

        return count(sb.toString(), params)>0;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="for plant">
    /**
     * 依使用者角色取得工廠資料。 (含工廠、公司、母廠 設定)  - (全部角色設定聯集)
     * @param user (需完整 entity)
     * @return
     */
    public List<CmFactoryVO> findCmFactoryByLoginUser(TcUser user) {
        // for 效能考量改用 SQL
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * \n");
        sb.append("from ( \n");// by 廠
        sb.append(genUserPlantSQL(user.getId(), params));
        sb.append(") A \n");
        sb.append("order by code \n");

        return this.selectBySql(CmFactoryVO.class, sb.toString(), params);
    }
    
    /**
     * 依使用者角色取得公司資料。 (含工廠、公司、母廠 設定)  - (全部角色設定聯集)
     * @param user (需完整 entity)
     * @return
     */
    public List<CmCompanyVO> findCmCompanyByLoginUser(TcUser user) {
        // for 效能考量改用 SQL
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select DISTINCT C.* \n");
        sb.append("from ( \n");// by 廠
        sb.append(genUserPlantSQL(user.getId(), params));
        sb.append(") A \n");
        sb.append("join CM_COMPANY C ON C.ID=A.COMPANY_ID \n");
        sb.append("order by C.ID \n");
        
        logger.debug("findCmFactoryByLoginUser sql = \n"+sb.toString());

        return this.selectBySql(CmCompanyVO.class, sb.toString(), params);
    }
    
    /**
     * 有廠權限共用SQL
     * @return 
     */
    public String genUserPlantSQL(Long userId, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("  select b.*  \n");
        sb.append("  from cm_userfactory a \n");
        sb.append("  join cm_factory b on b.id=a.factory_id \n");
        sb.append("  where user_id=#user_id \n");
        sb.append("  union \n");// by 公司(SAP CLIENT CODE)
        sb.append("  select b.* \n");
        sb.append("  from cm_usercompany a \n");
        sb.append("  join cm_factory b on b.sap_client_code=a.sap_client_code \n");
        sb.append("  where a.user_id=#user_id \n");
        sb.append("  union \n");// by 廠群組
        sb.append("  select c.* \n");
        sb.append("  from cm_user_factorygroup_r a \n");
        sb.append("  join cm_factory_group_r b on b.factorygroup_id=a.factorygroup_id \n");
        sb.append("  join cm_factory c on c.id=b.factory_id \n");
        sb.append("  where a.user_id=#user_id \n");

        params.put("user_id", userId);
        
        return sb.toString();
    }
    
    /**
     * 有廠權限共用SQL
     * @return 
     */
    public String genUserPlantSQL() {
        StringBuilder sb = new StringBuilder();       
        sb.append("select distinct B.* \n");
        sb.append("from ( \n");// by 廠
        sb.append("  select a.user_id, b.*  \n");
        sb.append("  from cm_userfactory a \n");
        sb.append("  join cm_factory b on b.id=a.factory_id \n");
        sb.append("  union \n");// by 公司(SAP CLIENT CODE)
        sb.append("  select a.user_id, b.* \n");
        sb.append("  from cm_usercompany a \n");
        sb.append("  join cm_factory b on b.sap_client_code=a.sap_client_code \n");
        sb.append("  union \n");// by 廠群組
        sb.append("  select a.user_id, c.* \n");
        sb.append("  from cm_user_factorygroup_r a \n");
        sb.append("  join cm_factory_group_r b on b.factorygroup_id=a.factorygroup_id \n");
        sb.append("  join cm_factory c on c.id=b.factory_id \n");
        sb.append(") A \n");
        sb.append("join tc_user B on B.disabled=0 and B.id=A.user_id \n");
        sb.append("where 1=1 \n");
        
        return sb.toString();
    }
    
    /**
     * 有廠權限人員 (單場)
     * @param userList
     * @param plantId
     * @return 
     */
    public List<TcUser> filterUsersByPlant(List<TcUser> userList, long plantId) {
        // for 效能考量改用 SQL
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();       
        sb.append(genUserPlantSQL());
        
        // 使用者條件
        List<Long> ids = new ArrayList<Long>();
        for(TcUser user : userList){
            ids.add(user.getId());
        }
        sb.append(NativeSQLUtils.getInSQL("A.user_id", ids, params)).append(" \n");
        // 廠別條件
        sb.append("and A.id = #plantId \n");
        params.put("plantId", plantId);
        
        sb.append("order by B.login_account");
        logger.debug("filterUsersByPlant sql = \n"+sb.toString());

        ResultSetHelper<TcUser> resultSetHelper = new ResultSetHelper(TcUser.class);
        List<TcUser> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        logger.debug("filterUsersByPlant resList = \n"+((resList!=null)?resList.size():0));
        
        return resList;
    }
    
    /**
     * 有廠權限人員 (多廠)
     * @param userList
     * @param plantCodes
     * @return 
     */
    public List<TcUser> filterUsersByPlantList(List<TcUser> userList, List<String> plantCodes) {
        if( userList==null || userList.size()<1 || plantCodes==null || plantCodes.size()<1 ){
            logger.error("filterUsersByPlantList userList==null || userList.size()<1 || plantCodes==null || plantCodes.size()<1 ...");
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();       
        sb.append(genUserPlantSQL());
        
        // 使用者條件
        List<Long> ids = new ArrayList<Long>();
        for(TcUser user : userList){
            ids.add(user.getId());
        }
        sb.append(NativeSQLUtils.getInSQL("A.user_id", ids, params)).append(" \n");
        // 廠別條件
        sb.append(NativeSQLUtils.getInSQL("A.code", plantCodes, params)).append(" \n");
        
        sb.append("order by B.login_account");
        logger.debug("filterUsersByPlantList sql = \n"+sb.toString());

        ResultSetHelper<TcUser> resultSetHelper = new ResultSetHelper(TcUser.class);
        List<TcUser> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        logger.debug("filterUsersByPlantList resList = \n"+((resList!=null)?resList.size():0));
        
        return resList;
    }
    
    /**
     * 包含廠別
     * @param factoryList
     * @param plant
     * @return 
     */
    private boolean containsFactory(List<CmFactory> factoryList, long plant){
        if( factoryList==null ){
            return false;
        }
        for(CmFactory cmFactory : factoryList){
            if( cmFactory.getId().equals(plant) ){
                return true;
            }
        }
        return false;
    }
    //</editor-fold>
    
    /**
     * 新增/更新使用者權限
     * @param user 使用者資訊
     * @param groupSelected 群組權限 (null 表不變更)
     * @param companySelected 公司權限 (null 表不變更)
     * @param factorySelected 新指定的工廠 (null 表不變更)
     * @param factoryGroupSelected
     * @param creator
     * @param specUserGroupId
     */
    public void update(TcUser user, 
            List<String> groupSelected, 
            List<String> companySelected, 
            List<String> factorySelected, 
            List<String> factoryGroupSelected, long specUserGroupId,
            TcUser creator) {              
        // === for user group ================
        if( groupSelected!=null ){
            Collection<TcUsergroup> ugColl = user.getTcUsergroupCollection();
            if (ugColl == null) {
                ugColl = new ArrayList<TcUsergroup>();
                user.setTcUsergroupCollection(ugColl);
            }
            // remove unselected group
            ArrayList<TcUsergroup> removedUG = new ArrayList<TcUsergroup>();
            if (!ugColl.isEmpty()) {
                for (TcUsergroup ug : ugColl) {
                    Long gid = ug.getGroupId().getId();
                    boolean bSelected = false;
                    for (String gidSelected : groupSelected) {                    
                        if (gid.compareTo(Long.valueOf(gidSelected))==0) {
                            bSelected = true;
                            break;
                        }
                    }
                    if (!bSelected) {
                        removedUG.add(ug);
                    }
                }
            }
            for (TcUsergroup ug : removedUG) {
                ugColl.remove(ug);
                em.remove(em.merge(ug));
            }
            // add new group
            ArrayList<TcUsergroup> addedUG = new ArrayList<TcUsergroup>();
            for (String gidSelected : groupSelected) {
                boolean bExisted = false;
                for (TcUsergroup ug : ugColl) {
                    Long gid = ug.getGroupId().getId();
                    if (gid.compareTo(Long.valueOf(gidSelected))==0) {
                        bExisted = true;
                        break;
                    }
                }
                if (!bExisted) {
                    TcUsergroup newUG = new TcUsergroup();
                    newUG.setCreator(creator);
                    newUG.setCreatetimestamp(new Date());
                    newUG.setUserId(user);
                    newUG.setGroupId(em.find(TcGroup.class, Long.valueOf(gidSelected)));
                    addedUG.add(newUG);
                }
            }
            for (TcUsergroup ug : addedUG) {
                em.persist(ug);
                ugColl.add(ug);
            }
        }     
        
        // === for user company ================
        if( companySelected!=null ){
            List<CmUsercompany> ucColl = user.getCmUsercompanyList();
            if (ucColl == null) {
                ucColl = new ArrayList<CmUsercompany>();
                user.setCmUsercompanyList(ucColl);
            }       
            // remove unselected company
            ArrayList<CmUsercompany> removedUC = new ArrayList<CmUsercompany>();
            if (!ucColl.isEmpty()) {
                for (CmUsercompany uc : ucColl) {
                    String sapClientCode = uc.getSapClientCode();
                    boolean bSelected = false;
                    for (String company : companySelected) {                    
                        if( sapClientCode.equals(company) ) {
                            em.merge(uc);//by Jackson
                            bSelected = true;
                            break;
                        }
                    }
                    if (!bSelected) {
                        removedUC.add(uc);
                    }
                }
            }
            for (CmUsercompany uc : removedUC) {
                ucColl.remove(uc);
                em.remove(em.merge(uc));
            }

            // add new company
            ArrayList<CmUsercompany> addedUC = new ArrayList<CmUsercompany>();
            for (String company : companySelected) {
                boolean bExisted = false;
                for (CmUsercompany uc : ucColl) {
                    String sapClientCode = uc.getSapClientCode();
                    if( sapClientCode.equals(company) ) {
                        bExisted = true;
                        break;
                    }
                }
                if (!bExisted) {
                    CmUsercompany newUC = new CmUsercompany();
                    newUC.setSapClientCode(company);
                    newUC.setUserId(user);
                    newUC.setCreator(creator);
                    newUC.setCreatetimestamp(new Date());
                    
                    addedUC.add(newUC);
                }
            }
            for (CmUsercompany uc : addedUC) {
                em.persist(uc);
                ucColl.add(uc);
                logger.debug("update => uc.getSapClientCode() = "+uc.getSapClientCode());
            }
        }
        
        // === for user factory ================
        if( factorySelected!=null ){
            List<CmUserfactory> ufColl = user.getCmUserfactoryList();
            if (ufColl == null) {
                ufColl = new ArrayList<CmUserfactory>();
                user.setCmUserfactoryList(ufColl);
            }       
            // remove unselected factory
            ArrayList<CmUserfactory> removedUF = new ArrayList<CmUserfactory>();
            if (!ufColl.isEmpty()) {
                for (CmUserfactory ug : ufColl) {
                    Long gid = ug.getFactoryId().getId().longValue();
                    boolean bSelected = false;
                    for (String gidSelected : factorySelected) {                    
                        if (gid.compareTo(Long.valueOf(gidSelected))==0) {
                            // ug.setRcvPriceChangeMail(rcvPriceChangeMail);//by Jackson
                            em.merge(ug);//by Jackson
                            bSelected = true;
                            break;
                        }
                    }
                    if (!bSelected) {
                        removedUF.add(ug);
                    }
                }
            }
            for (CmUserfactory ug : removedUF) {
                ufColl.remove(ug);
                em.remove(em.merge(ug));
            }

            // add new factory
            ArrayList<CmUserfactory> addedUF = new ArrayList<CmUserfactory>();
            for (String gidSelected : factorySelected) {
                boolean bExisted = false;
                for (CmUserfactory ug : ufColl) {
                    Long gid = ug.getFactoryId().getId().longValue();
                    if (gid.compareTo(Long.valueOf(gidSelected))==0) {
                        bExisted = true;
                        break;
                    }
                }
                if (!bExisted) {
                    CmUserfactory newUF = new CmUserfactory();
                    newUF.setCreator(creator.getId());
                    newUF.setCreatetimestamp(new Date());
                    newUF.setUserId(user);
                    //BigDecimal factoryId = BigDecimal.valueOf(Long.valueOf(gidSelected));
                    //newUF.setFactoryId(em.find(CmFactory.class, factoryId));
                    newUF.setFactoryId(cmFactoryFacade.find(Long.valueOf(gidSelected)));
                    // newUG.setRcvPriceChangeMail(rcvPriceChangeMail);//new
                    addedUF.add(newUF);
                }
            }
            for (CmUserfactory ug : addedUF) {
                em.persist(ug);
                ufColl.add(ug);
            }
        }

        // === for user factoryGroup ================
        if( factoryGroupSelected!=null ){
            List<CmUserFactorygroupR> ufgColl = user.getCmUserFactorygroupRList();
            if (ufgColl == null) {
                ufgColl = new ArrayList<CmUserFactorygroupR>();
                user.setCmUserFactorygroupRList(ufgColl);
            }       
            // remove unselected factory
            ArrayList<CmUserFactorygroupR> removedUFG = new ArrayList<CmUserFactorygroupR>();
            if (!ufgColl.isEmpty()) {
                for (CmUserFactorygroupR ufg : ufgColl) {
                    if( ufg.getUsergroupId().longValue() == specUserGroupId ){// 只針對目前處理的指定角色的廠別群組設定
                        Long gid = ufg.getFactorygroupId().getId().longValue();
                        boolean bSelected = false;
                        for (String gidSelected : factoryGroupSelected) {                    
                            if (gid.compareTo(Long.valueOf(gidSelected))==0) {
                                em.merge(ufg);//by Jackson
                                bSelected = true;
                                break;
                            }
                        }
                        if (!bSelected) {
                            removedUFG.add(ufg);
                        }
                    }
                }
            }
            for (CmUserFactorygroupR ufg : removedUFG) {
                ufgColl.remove(ufg);
                em.remove(em.merge(ufg));
            }

            // add new factory
            ArrayList<CmUserFactorygroupR> addedUFG = new ArrayList<CmUserFactorygroupR>();
            for (String gidSelected : factoryGroupSelected) {
                boolean bExisted = false;
                for (CmUserFactorygroupR ufg : ufgColl) {
                    if( ufg.getUsergroupId().longValue() == specUserGroupId ){// 只針對目前處理的指定角色的廠別群組設定
                        Long gid = ufg.getFactorygroupId().getId().longValue();
                        if (gid.compareTo(Long.valueOf(gidSelected))==0) {
                            bExisted = true;
                            break;
                        }
                    }
                }
                if (!bExisted) {
                    CmUserFactorygroupR newUFG = new CmUserFactorygroupR();
                    newUFG.setCreator(creator);
                    newUFG.setCreatetimestamp(new Date());
                    newUFG.setUserId(user);
                    Long factoryGroupId = Long.valueOf(gidSelected);
                    newUFG.setFactorygroupId(em.find(CmFactorygroup.class, factoryGroupId));
                    newUFG.setUsergroupId(specUserGroupId);// 只針對目前處理的指定角色的廠別群組設定
                    addedUFG.add(newUFG);
                }
            }
            for (CmUserFactorygroupR ufg : addedUFG) {
                em.persist(ufg);
                ufgColl.add(ufg);
            }
        }
               
        // == save tc_user ================
        if (user.getId() == null) {
            user.setCreator(creator);
            user.setCreatetimestamp(new Date());
            em.persist(user);
        } else {
            em.merge(user);
        }
    }
    
    /**
     * 權限彙總查詢
     * @param criteriaVO
     * @return 
     */
    public List<UserPermissionVO> findPermissionByCriteria(PermissionCriteriaVO criteriaVO){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        
        //sb.append("SELECT A.ID, A.LOGIN_ACCOUNT, A.CNAME, G.GROUPS, P.PLANTS \n");
        sb.append("SELECT A.ID, A.LOGIN_ACCOUNT, A.CNAME, G.GROUPS \n");
        sb.append("FROM TC_USER A \n");
        sb.append("LEFT OUTER JOIN ( \n");// 角色權限
        sb.append("     SELECT A.USER_ID, LISTAGG(G.CODE, ',') WITHIN GROUP (ORDER BY G.CODE) GROUPS \n");
        sb.append("     FROM TC_USERGROUP A \n");
        sb.append("     JOIN TC_GROUP G ON G.ID=A.GROUP_ID \n");
        sb.append("     GROUP BY A.USER_ID \n");
        sb.append(") G ON G.USER_ID=A.ID \n");
        /*
        sb.append("LEFT OUTER JOIN ( \n");// 廠權限
        sb.append("     SELECT S.USER_ID, LISTAGG('['||S.FACTORY_CODE||']'||S.FACTORY_NAME, ',') WITHIN GROUP (ORDER BY S.FACTORY_CODE) PLANTS \n");
        sb.append("     , LISTAGG('['||TO_CHAR(S.FACTORY_ID)||']', ',') WITHIN GROUP (ORDER BY S.FACTORY_ID) PLANTIDS \n");
        sb.append("     FROM ( \n");
        sb.append("        SELECT A.USER_ID, B.ID FACTORY_ID, B.CODE FACTORY_CODE, B.NAME FACTORY_NAME \n");
        sb.append("        FROM CM_USERFACTORY A \n");
        sb.append("        JOIN CM_FACTORY B ON B.ID=A.FACTORY_ID \n");
        sb.append("        UNION \n");
        sb.append("        SELECT A.USER_ID, B.ID FACTORY_ID, B.CODE FACTORY_CODE, B.NAME FACTORY_NAME \n");
        sb.append("        FROM CM_USERCOMPANY A \n");
        sb.append("        JOIN CM_FACTORY B ON B.SAP_CLIENT_CODE=A.SAP_CLIENT_CODE \n"); 
        sb.append("        UNION \n");
        sb.append("        SELECT A.USER_ID, C.ID FACTORY_ID, C.CODE FACTORY_CODE, C.NAME FACTORY_NAME \n");
        sb.append("        FROM CM_USER_FACTORYGROUP_R A \n");
        sb.append("        JOIN CM_FACTORY_GROUP_R B ON B.FACTORYGROUP_ID=A.FACTORYGROUP_ID \n");
        sb.append("        JOIN CM_FACTORY C ON C.ID=B.FACTORY_ID \n");  
        sb.append("     ) S \n");
        sb.append("     GROUP BY S.USER_ID \n");
        sb.append(") P ON P.USER_ID=A.ID \n");
        */
        sb.append("WHERE A.DISABLED=0 \n");
        
        // 廠別
        /*
        if( criteriaVO.getPlant()!=null ){
            String plantId = "["+criteriaVO.getPlant().toString()+"]";
            sb.append("AND P.USER_ID IS NOT NULL AND INSTR(P.PLANTIDS, #plantId) > 0 \n");
            params.put("plantId", plantId);
        }
        */
        // 有權限的資料
        /*
        if( criteriaVO.isAllowOnly() ){
            sb.append("AND (G.USER_ID IS NOT NULL AND P.USER_ID IS NOT NULL) \n");
        }
        */
        
        // 帳號、姓名
        if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().trim().isEmpty() ){
            String keyword = "%"+criteriaVO.getKeyword()+"%";
            sb.append("AND (A.LOGIN_ACCOUNT LIKE #keyword OR A.CNAME LIKE #keyword) \n");
            params.put("keyword", keyword);
        }
        
        // 不含台訊人員
        /*
        if( !criteriaVO.isIncludeTCCI() ){
            sb.append("AND NOT EXISTS( \n");
            sb.append("  SELECT X.ID \n");
            sb.append("  FROM CM_ORG X \n");
            sb.append("  JOIN CM_ORG Y ON Y.ID=X.PARENT AND Y.CTYPE='C' AND Y.NAME LIKE '%台泥資訊%' \n");
            sb.append("  WHERE X.CTYPE='D' \n");
            sb.append("  AND X.ID=A.ORG_ID \n");
            sb.append(") \n");
        }
        */
        sb.append("ORDER BY A.LOGIN_ACCOUNT \n");

        logger.debug("findPermissionByCriteria ...");

        ResultSetHelper<UserPermissionVO> resultSetHelper = new ResultSetHelper(UserPermissionVO.class);
        List<UserPermissionVO> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        
        return resList;
    }

    /**
     * 以廠權限為主
     * @param criteriaVO
     * @return 
     */
    public List<PlantPermissionVO> findPlantPermissionByCriteria(PermissionCriteriaVO criteriaVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT A.ID PLANT_ID, A.CODE PLANT_CODE, A.NAME PLANT_NAME \n"); 
        sb.append(", B.USER_ID, B.LOGIN_ACCOUNT, B.CNAME, B.GROUPS \n");
        sb.append("FROM CM_FACTORY A \n"); 
        //sb.append("JOIN AMS_COMPANY S ON S.ID=A.COMPANY_ID \n");
        sb.append("JOIN CM_COMPANY C ON C.SAP_CLIENT_CODE=A.SAP_CLIENT_CODE \n");

        sb.append("LEFT OUTER JOIN ( \n");
        sb.append("     SELECT A.USER_ID, A.LOGIN_ACCOUNT, A.CNAME, A.GROUPS, B.FACTORY_ID, B.FACTORY_CODE, B.FACTORY_NAME \n"); 
        sb.append("     FROM ( \n");// 角色權限
        sb.append("         SELECT U.ID USER_ID, U.LOGIN_ACCOUNT, U.CNAME, LISTAGG(G.CODE, ',') WITHIN GROUP (ORDER BY G.CODE) GROUPS \n"); 
        sb.append("         FROM TC_USER U \n");
        sb.append("         JOIN TC_USERGROUP A ON A.USER_ID=U.ID \n");
        sb.append("         JOIN TC_GROUP G ON G.ID=A.GROUP_ID  \n");
        sb.append("         WHERE U.DISABLED=0 \n");
        
        // 帳號、姓名
        if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().trim().isEmpty() ){
            String keyword = "%"+criteriaVO.getKeyword()+"%";
            sb.append("         AND (U.LOGIN_ACCOUNT LIKE #keyword OR U.CNAME LIKE #keyword) \n");
            params.put("keyword", keyword);
        }
        
        // 不含台訊人員
        /*if( !criteriaVO.isIncludeTCCI() ){
            sb.append("         AND NOT EXISTS( \n");
            sb.append("             SELECT X.ID \n");
            sb.append("             FROM CM_ORG X \n");
            sb.append("             JOIN CM_ORG Y ON Y.ID=X.PARENT AND Y.CTYPE='C' AND Y.NAME LIKE '%台泥資訊%' \n");
            sb.append("             WHERE X.CTYPE='D' \n");
            sb.append("             AND X.ID=U.ORG_ID \n");
            sb.append("         ) \n");
        }*/

        sb.append("         GROUP BY U.ID, U.LOGIN_ACCOUNT, U.CNAME \n");
        sb.append("     ) A \n");
        /*
        sb.append("     JOIN ( \n");// 依廠別
        sb.append("         SELECT A.USER_ID, B.ID FACTORY_ID, B.CODE FACTORY_CODE, B.NAME FACTORY_NAME \n");
        sb.append("         FROM CM_USERFACTORY A \n");
        sb.append("         JOIN CM_FACTORY B ON B.ID=A.FACTORY_ID \n");
        sb.append("         UNION \n");// 依公司
        sb.append("         SELECT A.USER_ID, B.ID FACTORY_ID, B.CODE FACTORY_CODE, B.NAME FACTORY_NAME \n");
        sb.append("         FROM CM_USERCOMPANY A \n");
        sb.append("         JOIN CM_FACTORY B ON B.SAP_CLIENT_CODE=A.SAP_CLIENT_CODE \n");
        sb.append("         UNION \n");// 依廠群組
        sb.append("         SELECT A.USER_ID, C.ID FACTORY_ID, C.CODE FACTORY_CODE, C.NAME FACTORY_NAME \n");
        sb.append("         FROM CM_USER_FACTORYGROUP_R A \n");
        sb.append("         JOIN CM_FACTORY_GROUP_R B ON B.FACTORYGROUP_ID=A.FACTORYGROUP_ID \n");
        sb.append("         JOIN CM_FACTORY C ON C.ID=B.FACTORY_ID \n");
        sb.append("     ) B ON B.USER_ID=A.USER_ID \n");
        */
        
        sb.append(") B ON B.FACTORY_ID=A.ID \n");
        sb.append("WHERE 1=1 \n"); 

        /*
        // SAP CLIENT 別
        if( criteriaVO.getSapClientCode()!=null ){
            sb.append("AND A.SAP_CLIENT_CODE=#SAP_CLIENT_CODE \n");
            params.put("SAP_CLIENT_CODE", criteriaVO.getSapClientCode());
        }   
        
        // 公司別
        if( criteriaVO.getCompany()!=null ){
            sb.append("AND A.COMPANY_ID=#comId \n");
            params.put("comId", criteriaVO.getCompany());
        }        

        // 廠別
        if( criteriaVO.getPlant()!=null ){
            sb.append("AND A.ID=#plantId \n");
            params.put("plantId", criteriaVO.getPlant());
        }        
        */
        // 有權限的資料
        if( criteriaVO.isAllowOnly() ){
            sb.append("AND B.USER_ID IS NOT NULL \n");
        }
        
        sb.append("ORDER BY C.SORT_NUM, A.CODE, B.LOGIN_ACCOUNT \n");

        logger.debug("findPlantPermissionByCriteria ...");

        ResultSetHelper<PlantPermissionVO> resultSetHelper = new ResultSetHelper(PlantPermissionVO.class);
        List<PlantPermissionVO> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        
        return resList;
    }

    /**
     * 查詢帳號資訊 (帳號載入與轉出功能)
     * @param userLoaderVO 查詢條件 
     * @param includeDisabledUser 
     * @return 
     */
    public List<UserLoaderVO> findUserLoaderVOList(UserLoaderVO userLoaderVO, boolean includeDisabledUser){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        
        sql.append("SELECT NVL(TU.EMP_ID, 'NA') AS EMPLOYEEID, NVL(TU.CNAME, 'NA') AS EMPLOYEENAME, NVL(TU.LOGIN_ACCOUNT, 'NA') AS LOGINACCOUNT, NVL(TU.EMAIL, 'NA') AS EMAILADDRESS, \n");
        sql.append(" NVL(GPS.GROUPS, 'NA') AS GROUPNAMES \n");
        sql.append(" FROM TC_USER TU \n");
//        sql.append(" LEFT OUTER JOIN ( \n");
        // 系統角色
        // for MSSQL
        /*
        sql.append("   SELECT t1.USER_ID, \n");
        sql.append("   	STUFF(( \n"); 
        sql.append("                SELECT DISTINCT ',' + t2.CODE \n");
        sql.append("                FROM (SELECT B.USER_ID, A.CODE FROM TC_GROUP A JOIN TC_USERGROUP B ON B.GROUP_ID=A.ID) t2 \n");
        sql.append("                WHERE t1.USER_ID = t2.USER_ID \n");
        sql.append("                FOR XML PATH(''), TYPE \n");
        sql.append("   	  ).value('.', 'NVARCHAR(MAX)') \n");
        sql.append("   	  ,1,1,'') GROUPS \n");
        sql.append("   FROM (SELECT B.USER_ID, A.CODE FROM TC_GROUP A JOIN TC_USERGROUP B ON B.GROUP_ID=A.ID) t1 \n");
        sql.append("   GROUP BY t1.USER_ID \n");
        
        sql.append(" ) GPS ON GPS.USER_ID=TU.ID \n");
        */
        // for Oracle
        sql.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sql.append("  SELECT B.USER_ID \n");
        sql.append("  , LISTAGG(A.CODE, ',') WITHIN GROUP (ORDER BY A.CODE) GROUPS \n");
//        sb.append("  , LISTAGG(A.NAME, ',') WITHIN GROUP (ORDER BY A.NAME) GROUPS \n");
        sql.append("  FROM TC_GROUP A \n");
        sql.append("  JOIN TC_USERGROUP B on B.GROUP_ID=A.ID \n");
        sql.append("  GROUP BY B.USER_ID \n");
        sql.append(") GPS on GPS.USER_ID=TU.ID \n");
        
        sql.append(" WHERE 1=1 ");

        // 工號 => TC_USER [EMP_ID]
        if( StringUtils.isNotBlank(userLoaderVO.getEmployeeId()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("TU.EMP_ID", userLoaderVO.getEmployeeId(), params)); 
        }
        
        // 姓名(繁簡) => TC_USER [CNAME]
        if( StringUtils.isNotBlank(userLoaderVO.getEmployeeName()) ) {
            sql.append(NativeSQLUtils.getLikeTranslateSQL("TU.CNAME", userLoaderVO.getEmployeeName(), params)); 
        }      
        
        // AD帳號 => TC_USER [LOGIN_ACCOUNT]
        if( StringUtils.isNotBlank(userLoaderVO.getLoginAccount()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("TU.LOGIN_ACCOUNT", userLoaderVO.getLoginAccount(), params)); 
        }
        
        // 是否包含已刪除使用者(預設不勾選) TC_USER [DISABLED]
        if( !includeDisabledUser ) {
            sql.append(" AND TU.DISABLED = 0 ");
        }

        ResultSetHelper<UserLoaderVO> resultSetHelper = new ResultSetHelper(UserLoaderVO.class);
        List<UserLoaderVO> userLoaderVOList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return userLoaderVOList;
    }
    
}
