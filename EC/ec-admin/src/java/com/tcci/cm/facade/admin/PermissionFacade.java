/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmFactory;
import com.tcci.cm.entity.admin.CmFactorygroup;
import com.tcci.cm.entity.admin.CmUserFactorygroupR;
import com.tcci.cm.entity.admin.CmUsercompany;
import com.tcci.cm.entity.admin.CmUserfactory;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.cm.model.admin.MenuFunctionVO;
import com.tcci.cm.model.admin.PermissionCriteriaVO;
import com.tcci.cm.model.admin.PlantPermissionVO;
import com.tcci.cm.model.admin.UserLoaderVO;
import com.tcci.cm.model.admin.UserPermissionVO;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.facade.org.TcUserFacade;
import com.tcci.fc.facade.org.TcUsergroupFacade;
import com.tcci.fc.util.ResultSetHelper;
import java.util.ArrayList;
import java.util.Collection;
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
    @EJB CmUserfactoryFacade cmUserfactoryFacade;
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
        if( viewId.endsWith("/index.xhtml") ){
            return true;
        }

        // 依授權可執行的功能之 URL 與 viewId 比對判斷
        if( functions!=null ){
            for(MenuFunctionVO vo: functions){
                if( vo.getUrl()!=null ){
                    if( vo.getUrl().indexOf(viewId) > 0 ){
                        logger.debug("checkAuthorizationByViewId viewId ="+viewId);
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
        sb.append("select a.id, a.title, a.title_cn as titleCN, a.url, a.sortnum \n");
        sb.append(" , b.id as sid, b.title as stitle, b.title_cn as stitleCN, b.sortnum as ssortnum \n");
        sb.append(" , c.id as mid, c.title as mtitle, c.title_cn as mtitleCN, c.sortnum as msortnum \n");
        sb.append(" from CM_FUNCTION a \n");
     
        sb.append(" join CM_FUNCTION b on b.clevel=2 and b.id=a.parent \n");// 子選單
        sb.append(" join CM_FUNCTION c on c.clevel=1 and c.id=b.parent \n");// 主選單
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            sb.append(" join ( \n");
            sb.append("     select R.func_id \n");
            sb.append("     from CM_GROUP_FUNCTION_R R \n");
            
            if( groupPrefix!=null ){
                String keyword = groupPrefix+"%";
                sb.append("     join TC_GROUP Q on Q.ID=R.GROUP_ID and Q.CODE LIKE #GROUPCODE \n");
                params.put("GROUPCODE", keyword);
            }
            
            if( userId>0 ){
                sb.append("     join TC_USERGROUP G on G.GROUP_ID=R.GROUP_ID AND G.USER_ID=#USER_ID \n");
                params.put("USER_ID", userId);
            }
            
            sb.append("     where 1=1 \n");
        }
        
        if( groupid>0 ){
            sb.append(NativeSQLUtils.genEqulSQL("group_id", groupid, params)).append(" \n");
        }
        
        if( usergroupIds!=null ){// 需考慮權限
            if( !usergroupIds.isEmpty() ){// 有權限
                sb.append(NativeSQLUtils.getInSQL("group_id", usergroupIds, params)).append(" \n");
            }else{// 無權限
                return null;
            }
        }
        
        if( groupid>0 || userId>0 || (usergroupIds!=null && !usergroupIds.isEmpty()) || groupPrefix!=null ){
            sb.append("     group by R.func_id \n");// 確保出現一次即可
            sb.append(" ) r on r.func_id=a.id \n");
        }
        
        sb.append(" where a.clevel=3 \n");
        sb.append(" and a.url<>'#' \n"); // === 有功能頁面項目 ===
        
        if( mid>0 ){
            sb.append(NativeSQLUtils.genEqulSQL("c.id", mid, params));
        }
        if( sid>0 ){
            sb.append(NativeSQLUtils.genEqulSQL("b.id", sid, params));
        }
        
        sb.append(" group by a.id, a.title, a.title_cn, a.url, a.sortnum \n");// 確保出現一次即可
        sb.append(" , b.id, b.title, b.title_cn, b.sortnum \n");
        sb.append(" , c.id, c.title, c.title_cn, c.sortnum \n");
        sb.append(" order by c.sortnum, b.sortnum, a.sortnum \n");// 排序 // 增加 r.sortnum=Rule自訂排序
   
        logger.debug("fetchFunctionInfo sql = \n"+sb.toString());

        ResultSetHelper<MenuFunctionVO> resultSetHelper = new ResultSetHelper(MenuFunctionVO.class);
        List<MenuFunctionVO> ppFunctionVOList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        
        return ppFunctionVOList;       
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="for plant">
    /**
     * 依使用者角色取得工廠資料。 (含工廠、公司、母廠 設定)  - (全部角色設定聯集)
     * @param user (需完整 entity)
     * @return
     */
    public List<CmFactory> findCmFactoryByLoginUser(TcUser user) {
        /*List<CmFactory> factoryAll = new ArrayList<CmFactory>();
        // 工廠與公司交集，不含廠群組
        List<CmFactory> factorys = cmUserfactoryFacade.findUserFactoryPermission(user);
        if( factorys!=null && !factorys.isEmpty() ){
            factoryAll.addAll(factorys);
        }
        // 需聯集的廠別 (By廠別群組設定)
        List<CmFactory> factoryByGroups = cmUserfactoryFacade.findSpecFactoryByLoginUser(user, FactoryGroupTypeEnum.COMMON.getCode(), factorys);
        if( factoryByGroups!=null && !factoryByGroups.isEmpty() ){
            factoryAll.addAll(factoryByGroups);
        }
        return factoryAll;
        */
        // for 效能考量改用 SQL
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select * \n");
        sb.append("from ( \n");// by 廠
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
        sb.append(") A \n");
        sb.append("order by code \n");
        
        params.put("user_id", user.getId());
        
        logger.debug("findCmFactoryByLoginUser sql = \n"+sb.toString());

        ResultSetHelper<CmFactory> resultSetHelper = new ResultSetHelper(CmFactory.class);
        List<CmFactory> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        logger.debug("findCmFactoryByLoginUser resList = \n"+((resList!=null)?resList.size():0));
        
        return resList;
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
    
    //<editor-fold defaultstate="collapsed" desc="for role">
    public List<TcUser> findUsersByRole(String groupCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();

        sb.append("select a.id, a.login_account, a.email, a.emp_id,a. disabled, a.cname, a.createtimestamp,a.send_email \n");
        sb.append("from tc_user a \n");
        sb.append("join tc_usergroup b on b.user_id=a.id \n");
        sb.append("join tc_group c on c.id=b.group_id \n");
        sb.append("where a.disabled=0 \n");
        sb.append("and c.code=#groupCode \n");
        
        params.put("groupCode", groupCode);
        
        sb.append("order by a.login_account \n");
        
        logger.debug("findUsersByRole sql = \n"+sb.toString());

        ResultSetHelper<TcUser> resultSetHelper = new ResultSetHelper(TcUser.class);
        List<TcUser> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        logger.debug("findUsersByRole resList = "+((resList!=null)?resList.size():0));
        
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
        sb.append("select count(a.id) from TC_GROUP a \n");
        sb.append("join TC_USERGROUP b on b.group_id=a.id and b.user_id=#user_id \n");
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
        //logger.debug("Before inUserGroup ... groupCode = "+groupCode);
        if( user!=null && user.getTcUsergroupCollection()!=null ){
            for(TcUsergroup tcUsergroup : user.getTcUsergroupCollection()){
                if( groupCode!=null && groupCode.equals(tcUsergroup.getGroupId().getCode()) ){
                    return true;
                }
            }
        }
        //logger.debug("After inUserGroup ... groupCode = "+groupCode);
        return false;
    }
    
    /**
     * 是否在 特定群組集
     * @param user
     * @param groupCodeList
     * @return 
     */
    public boolean inUserGroup(TcUser user, List<String> groupCodeList){
//        logger.debug("inUserGroup(TcUser user, List<String> groupCodeList) ...");
        /*速度慢改直接查
        if( user!=null && user.getTcUsergroupCollection()!=null ){
            for(TcUsergroup tcUsergroup : user.getTcUsergroupCollection()){
                if( groupCodeList!=null && groupCodeList.contains(tcUsergroup.getGroupId().getCode().toUpperCase()) ){
                    return true;
                }
            }
        }
        return false;*/
        
        if( user==null || groupCodeList==null || groupCodeList.isEmpty() ){
            return false;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(commonUserGroupSQL(user.getId(), params));
        sb.append(NativeSQLUtils.getInSQL("a.code", groupCodeList, params));
                
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
//        logger.debug("inUserGroup(TcUser user, String keyword, boolean isPrefix) ...");
        /*if( user!=null && user.getTcUsergroupCollection()!=null ){
            for(TcUsergroup tcUsergroup : user.getTcUsergroupCollection()){
                if( isPrefix && tcUsergroup.getGroupId().getCode().toUpperCase().startsWith(keyword.toUpperCase()) ){
                    return true;
                }else if( !isPrefix && tcUsergroup.getGroupId().getCode().toUpperCase().endsWith(keyword.toUpperCase()) ){
                    return true;
                }
            }
        }
        return false;*/
        
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(commonUserGroupSQL(user.getId(), params));

        String likeStr = (isPrefix)? keyword+"%":"%"+keyword;
        sb.append("and a.code like #keyword \n");
        params.put("keyword", likeStr);

        //logger.debug("inUserGroup sql = \n"+sb.toString());

        return count(sb.toString(), params)>0;
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
        
        // save tc_user
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
        
        sb.append("select a.id, a.login_account, a.cname, g.groups, p.plants \n");
        sb.append("from tc_user a \n");
        sb.append("left outer join ( \n");// 角色權限
        sb.append("     select a.user_id, LISTAGG(g.code, ',') WITHIN GROUP (ORDER BY g.code) groups \n");
        sb.append("     from tc_usergroup a \n");
        sb.append("     join tc_group g on g.id=a.group_id \n");
        sb.append("     group by a.user_id \n");
        sb.append(") g on g.user_id=a.id \n");
        sb.append("left outer join ( \n");// 廠權限
        sb.append("     select s.user_id, LISTAGG('['||s.factory_code||']'||s.factory_name, ',') WITHIN GROUP (ORDER BY s.factory_code) plants \n");
        sb.append("     , LISTAGG('['||to_char(s.factory_id)||']', ',') WITHIN GROUP (ORDER BY s.factory_id) plantIds \n");
        sb.append("     from ( \n");
        sb.append("        select a.user_id, b.id factory_id, b.code factory_code, b.name factory_name \n");
        sb.append("        from cm_userfactory a \n");
        sb.append("        join cm_factory b on b.id=a.factory_id \n");
        sb.append("        union \n");
        sb.append("        select a.user_id, b.id factory_id, b.code factory_code, b.name factory_name \n");
        sb.append("        from cm_usercompany a \n");
        sb.append("        join cm_factory b on b.sap_client_code=a.sap_client_code \n"); 
        sb.append("        union \n");
        sb.append("        select a.user_id, c.id factory_id, c.code factory_code, c.name factory_name \n");
        sb.append("        from cm_user_factorygroup_r a \n");
        sb.append("        join cm_factory_group_r b on b.factorygroup_id=a.factorygroup_id \n");
        sb.append("        join cm_factory c on c.id=b.factory_id \n");  
        sb.append("     ) s \n");
        sb.append("     group by s.user_id \n");
        sb.append(") p on p.user_id=a.id \n");
        sb.append("where a.disabled=0 \n");
        
        // 廠別
        if( criteriaVO.getPlant()!=null ){
            String plantId = "["+criteriaVO.getPlant().toString()+"]";
            sb.append("and p.user_id is not null and Instr(p.plantIds, #plantId) > 0 \n");
            params.put("plantId", plantId);
        }
        
        // 有權限的資料
        if( criteriaVO.isAllowOnly() ){
            sb.append("and (g.user_id is not null and p.user_id is not null) \n");
        }
        
        // 帳號、姓名
        if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().trim().isEmpty() ){
            String keyword = "%"+criteriaVO.getKeyword()+"%";
            sb.append("and (a.login_account like #keyword or a.cname like #keyword) \n");
            params.put("keyword", keyword);
        }
        
        // 不含台訊人員
        if( !criteriaVO.isIncludeTCCI() ){
            sb.append("and not exists( \n");
            sb.append("  select x.id \n");
            sb.append("  from cm_org x \n");
            sb.append("  join cm_org y on y.id=x.parent and y.ctype='C' and y.name like '%台泥資訊%' \n");
            sb.append("  where x.ctype='D' \n");
            sb.append("  and x.id=a.org_id \n");
            sb.append(") \n");
        }
        
        sb.append("order by a.login_account \n");

        logger.debug("findPermissionByCriteria sql = \n"+sb.toString());

        ResultSetHelper<UserPermissionVO> resultSetHelper = new ResultSetHelper(UserPermissionVO.class);
        List<UserPermissionVO> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        logger.debug("findPermissionByCriteria resList = \n"+((resList!=null)?resList.size():0));
        
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
        
        sb.append("select A.id plant_id, A.code plant_code, A.name plant_name \n"); 
        sb.append(", B.user_id, B.login_account, B.cname, B.groups \n");
        sb.append("from cm_factory A \n"); 
        //sb.append("join ams_company S on S.ID=A.COMPANY_ID \n");
        sb.append("join cm_company C on C.SAP_CLIENT_CODE=A.SAP_CLIENT_CODE \n");

        sb.append("left outer join ( \n");
        sb.append("     select a.user_id, a.login_account, a.cname, a.groups, b.factory_id, b.factory_code, b.factory_name \n"); 
        sb.append("     from ( \n");// 角色權限
        sb.append("         select u.id user_id, u.login_account, u.cname, LISTAGG(g.code, ',') WITHIN GROUP (ORDER BY g.code) groups \n"); 
        sb.append("         from tc_user u \n");
        sb.append("         join tc_usergroup a on a.user_id=u.id \n");
        sb.append("         join tc_group g on g.id=a.group_id  \n");
        sb.append("         where u.disabled=0 \n");
        
        // 帳號、姓名
        if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().trim().isEmpty() ){
            String keyword = "%"+criteriaVO.getKeyword()+"%";
            sb.append("         and (u.login_account like #keyword or u.cname like #keyword) \n");
            params.put("keyword", keyword);
        }
        
        // 不含台訊人員
        if( !criteriaVO.isIncludeTCCI() ){
            sb.append("         and not exists( \n");
            sb.append("             select x.id \n");
            sb.append("             from cm_org x \n");
            sb.append("             join cm_org y on y.id=x.parent and y.ctype='C' and y.name like '%台泥資訊%' \n");
            sb.append("             where x.ctype='D' \n");
            sb.append("             and x.id=u.org_id \n");
            sb.append("         ) \n");
        }

        sb.append("         group by u.id, u.login_account, u.cname \n");
        sb.append("     ) a \n");
        sb.append("     join ( \n");// 依廠別
        sb.append("         select a.user_id, b.id factory_id, b.code factory_code, b.name factory_name \n");
        sb.append("         from cm_userfactory a \n");
        sb.append("         join cm_factory b on b.id=a.factory_id \n");
        sb.append("         union \n");// 依公司
        sb.append("         select a.user_id, b.id factory_id, b.code factory_code, b.name factory_name \n");
        sb.append("         from cm_usercompany a \n");
        sb.append("         join cm_factory b on b.sap_client_code=a.sap_client_code \n");
        sb.append("         union \n");// 依廠群組
        sb.append("         select a.user_id, c.id factory_id, c.code factory_code, c.name factory_name \n");
        sb.append("         from cm_user_factorygroup_r a \n");
        sb.append("         join cm_factory_group_r b on b.factorygroup_id=a.factorygroup_id \n");
        sb.append("         join cm_factory c on c.id=b.factory_id \n");
        sb.append("     ) b on b.user_id=a.user_id \n");
        sb.append(") B on B.factory_id=A.id \n");
        sb.append("where 1=1 \n"); 

        // SAP CLIENT 別
        if( criteriaVO.getSapClientCode()!=null ){
            sb.append("and A.SAP_CLIENT_CODE=#SAP_CLIENT_CODE \n");
            params.put("SAP_CLIENT_CODE", criteriaVO.getSapClientCode());
        }   
        
        // 公司別
        if( criteriaVO.getCompany()!=null ){
            sb.append("and A.COMPANY_ID=#comId \n");
            params.put("comId", criteriaVO.getCompany());
        }        

        // 廠別
        if( criteriaVO.getPlant()!=null ){
            sb.append("and A.id=#plantId \n");
            params.put("plantId", criteriaVO.getPlant());
        }        
        
        // 有權限的資料
        if( criteriaVO.isAllowOnly() ){
            sb.append("and B.user_id is not null \n");
        }
        
        sb.append("order by C.SORT_NUM, A.code, B.login_account \n");

        logger.debug("findPlantPermissionByCriteria sql = \n"+sb.toString());

        ResultSetHelper<PlantPermissionVO> resultSetHelper = new ResultSetHelper(PlantPermissionVO.class);
        List<PlantPermissionVO> resList = resultSetHelper.queryToPOJOList(em, sb.toString(), params);
        logger.debug("findPlantPermissionByCriteria resList = \n"+((resList!=null)?resList.size():0));
        
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
        
        sql.append("select NVL(tu.emp_id, 'NA') as employeeId, NVL(tu.cname, 'NA') as employeeName, NVL(tu.login_account, 'NA') as loginAccount, NVL(tu.email, 'NA') as emailAddress, \n");
        sql.append(" NVL(gps.GROUPS, 'NA') as groupNames, \n");
        sql.append(" NVL(fs.FACTORYS, 'NA') as factorycodes, \n");
        sql.append(" NVL(P.COMPANY, 'NA') as companies, \n");
        sql.append(" NVL(G.FACTORYGROUP, 'NA') as factorygroups \n");
        sql.append(" from TC_USER tu \n");
        sql.append(" left outer join ( \n");// 系統角色
        sql.append("   select B.USER_ID, LISTAGG(A.CODE, ',') WITHIN GROUP (ORDER BY A.CODE) GROUPS \n");
        sql.append("   from TC_GROUP A \n");
        sql.append("   join TC_USERGROUP B ON B.GROUP_ID=A.ID \n");
        sql.append("   GROUP BY B.USER_ID \n");
        sql.append(" ) gps on gps.USER_ID=tu.ID \n");
        sql.append(" left outer join ( \n");// 廠權限
        sql.append("   select B.USER_ID, LISTAGG(A.CODE, ',') WITHIN GROUP (ORDER BY A.CODE) FACTORYS \n");
        sql.append("   from CM_FACTORY A \n");
        sql.append("   join CM_USERFACTORY B ON B.FACTORY_ID=A.ID \n");
        sql.append("   GROUP BY B.USER_ID \n");
        sql.append(" ) fs on fs.USER_ID=tu.ID \n");
        //sql.append(" left outer join TC_USER_INFO tui on tui.user_id = tu.id \n");
        sql.append(" LEFT OUTER JOIN ( \n");// SAP CLIENT 權限
        sql.append("   SELECT A.USER_ID, \n");
        sql.append("   LISTAGG(B.SAP_CLIENT_CODE, ',') WITHIN GROUP (ORDER BY B.SAP_CLIENT_CODE) COMPANY \n");
        sql.append("   FROM CM_USERCOMPANY A \n");
        sql.append("   JOIN CM_COMPANY B ON B.SAP_CLIENT_CODE=A.SAP_CLIENT_CODE \n");
        sql.append("   GROUP BY A.USER_ID \n");
        sql.append(" ) P ON tu.ID = P.USER_ID \n");
        sql.append(" LEFT OUTER JOIN ( \n");// 廠群組權限
        sql.append("   SELECT A.USER_ID, \n");
        sql.append("   LISTAGG('('||B.ID||')'||B.GROUPNAME, ',') WITHIN GROUP (ORDER BY B.ID) FACTORYGROUP \n");
        sql.append("   FROM CM_USER_FACTORYGROUP_R A \n");
        sql.append("   JOIN CM_FACTORYGROUP B ON B.disabled=0 and B.ID=A.FACTORYGROUP_ID \n");
        sql.append("   GROUP BY A.USER_ID \n");
        sql.append(" ) G ON tu.ID = G.USER_ID \n");
        sql.append(" where 1=1 ");
        
        // 廠別 
        if( StringUtils.isNotBlank(userLoaderVO.getFactoryFullName()) ) {
            sql.append(" and (Instr(NVL(fs.FACTORYS, 'NA'), #plant)>0 or Instr(NVL(G.FACTORYGROUP, 'NA'), #plant)>0) \n");
            params.put("plant", userLoaderVO.getFactoryFullName());
        }
        
        // 工號 => TC_USER [EMP_ID]
        if( StringUtils.isNotBlank(userLoaderVO.getEmployeeId()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("tu.emp_id", userLoaderVO.getEmployeeId(), params)); 
        }
        
        // 姓名(繁簡) => TC_USER [CNAME]
        if( StringUtils.isNotBlank(userLoaderVO.getEmployeeName()) ) {
            sql.append(NativeSQLUtils.getLikeTranslateSQL("tu.cname", userLoaderVO.getEmployeeName(), params)); 
        }      
        
        // AD帳號 => TC_USER [LOGIN_ACCOUNT]
        if( StringUtils.isNotBlank(userLoaderVO.getLoginAccount()) ) {
            sql.append(NativeSQLUtils.getLikeSQL("tu.login_account", userLoaderVO.getLoginAccount(), params)); 
        }
        
        // 是否包含已刪除使用者(預設不勾選) TC_USER [DISABLED]
        if( !includeDisabledUser ) {
            sql.append(" AND tu.disabled = 0 ");
        }

        ResultSetHelper<UserLoaderVO> resultSetHelper = new ResultSetHelper(UserLoaderVO.class);
        List<UserLoaderVO> userLoaderVOList = resultSetHelper.queryToPOJOList(em, sql.toString(), params);
        
        return userLoaderVOList;
    }
    
}
