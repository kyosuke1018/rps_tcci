/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.tcci.cm.facade.admin;


import com.tcci.cm.entity.admin.CmOrg;
import com.tcci.cm.entity.admin.CmUserOrg;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.UsersCriteriaVO;
import com.tcci.cm.model.admin.UsersVO;
import com.tcci.cm.model.admin.WebCSEmpVO;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
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
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter
 */
@Stateless
@Named
public class UserFacade extends AbstractFacade<TcUser> {
    @EJB CmOrgFacade cmOrgFacade;
    @EJB TcGroupFacade tcGroupFacade;
    @EJB UserGroupFacade userGroupFacade;
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public UserFacade() {
        super(TcUser.class);
    }
    
    /**
     * 儲存 (新增、刪除)
     * @param user
     * @param selectedGroups
     * @param operator
     * @param removePermission
     */
    public void save(TcUser user, List<String> selectedGroups, boolean removePermission, TcUser operator){
        if( user==null ){
            logger.error("save user = null !");
            return;
        }
        // === for user group ================
        List<TcUsergroup> removedUG = saveUserGroup(user, selectedGroups, operator);
        // === for user org ================
        //　saveUserOrg(user, selectedOrgs, operator);
        
        // save tc_user
        save(user, operator);
        
        // 同時移除廠別關聯設定
        /*if( GlobalConstant.ENABLED_FEEDBACK_RULE_PERMISSION ){
            removeUserFactoryByTcGroups(user, removedUG);
        }*/
        
        // 移除關聯權限
        if( (user.getDisabled()!=null && user.getDisabled()) && removePermission ){
            removeUserRelationByUserId(user.getId());
        }
    }
    
    /**
     * 儲存 使用者與群組關聯
     * @param user
     * @param selectedGroups
     * @param operator 
     */
    private List<TcUsergroup> saveUserGroup(TcUser user, List<String> selectedGroups, TcUser operator){
        ArrayList<TcUsergroup> removedUG = null;
        
        if( selectedGroups!=null ){
            Collection<TcUsergroup> ugColl = user.getTcUsergroupCollection();
            if (ugColl == null) {
                ugColl = new ArrayList<TcUsergroup>();
                user.setTcUsergroupCollection(ugColl);
            }
            // remove unselected group
            removedUG = new ArrayList<TcUsergroup>();
            if (!ugColl.isEmpty()) {
                for (TcUsergroup ug : ugColl) {
                    Long gid = ug.getGroupId().getId();
                    boolean bSelected = false;
                    for (String gidSelected : selectedGroups) {
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
            for (String gidSelected : selectedGroups) {
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
                    newUG.setCreator(operator);
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
     
        return removedUG;
    }
    
    /**
     * 儲存 使用者與組織關聯
     * @param user
     * @param selectedOrgs
     * @param operator 
     */
    public void saveUserOrg(TcUser user, List<String> selectedOrgs, TcUser operator){
        if( selectedOrgs!=null ){
            Collection<CmUserOrg> uoColl = user.getCmUserOrgCollection();
            if (uoColl == null) {
                uoColl = new ArrayList<CmUserOrg>();
                user.setCmUserOrgCollection(uoColl);
            }
            // remove unselected group
            ArrayList<CmUserOrg> removedUO = new ArrayList<CmUserOrg>();
            if (!uoColl.isEmpty()) {
                for (CmUserOrg uo : uoColl) {
                    Long oid = uo.getOrgId().getId();
                    boolean bSelected = false;
                    for (String oidSelected : selectedOrgs) {
                        if (oid.compareTo(Long.valueOf(oidSelected))==0) {
                            bSelected = true;
                            break;
                        }
                    }
                    if (!bSelected) {
                        removedUO.add(uo);
                    }
                }
            }
            for (CmUserOrg uo : removedUO) {
                uoColl.remove(uo);
                em.remove(em.merge(uo));
            }
            // add new org
            ArrayList<CmUserOrg> addedUO = new ArrayList<CmUserOrg>();
            for (String oidSelected : selectedOrgs) {
                boolean bExisted = false;
                for (CmUserOrg uo : uoColl) {
                    Long oid = uo.getOrgId().getId();
                    if (oid.compareTo(Long.valueOf(oidSelected))==0) {
                        bExisted = true;
                        break;
                    }
                }
                if (!bExisted) {
                    CmUserOrg newUO = new CmUserOrg();
                    newUO.setCreator(operator.getId());
                    newUO.setCreatetimestamp(new Date());
                    newUO.setUserId(user);
                    newUO.setOrgId(em.find(CmOrg.class, Long.valueOf(oidSelected)));
                    addedUO.add(newUO);
                }
            }
            for (CmUserOrg uo : addedUO) {
                em.persist(uo);
                uoColl.add(uo);
            }
        }
    }
    
     /**
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(TcUser entity, TcUser operator){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                entity.setModifier(operator);
                entity.setModifytimestamp(new Date());
                this.edit(entity);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity);
            }
        }
    }
   
    /**
     * 依群組條件抓取User
     * @return 
     */
    public List<UsersVO> findByGroupCode(String groupCode){
        TcGroup tcGroup = tcGroupFacade.findGroupByCode(groupCode);
        if( tcGroup==null ){
            return null;
        }
        
        UsersCriteriaVO queryCriteria = new UsersCriteriaVO();
        queryCriteria.setGroup(tcGroup.getId());
        queryCriteria.setOnlyNoDisabled(true);
        List<UsersVO> userList = findUsersByCriteria(queryCriteria);
        
        return userList;
    }
    
    /**
     * 依查詢條件抓取資料
     * @param queryCriteria
     * @return
     */
    public List<UsersVO> findUsersByCriteria(UsersCriteriaVO queryCriteria){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT a.ID, a.LOGIN_ACCOUNT, a.CNAME, a.EMAIL, a.EMP_ID, a.DISABLED ");
        sb.append(", r.GROUPS \n");
        sb.append(", org.orgIdS, org.orgS \n");//user可存取組織
        sb.append(", c.ID as userOrgId, c.NAME as userOrg \n");//user所屬組織
        sb.append(", b.CNAME as CREATOR, a.CREATETIMESTAMP \n");
        sb.append("FROM TC_USER a \n");
        sb.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sb.append("  SELECT B.USER_ID \n");
        sb.append("  , LISTAGG(A.CODE, ',') WITHIN GROUP (ORDER BY A.CODE) GROUPS \n");
        // sb.append("  , LISTAGG(A.NAME, ',') WITHIN GROUP (ORDER BY A.NAME) GROUPS \n");//使用名稱很容易造成 [ORA:串接而成的字串過長] 錯誤
        sb.append("  FROM TC_GROUP A \n");
        sb.append("  JOIN TC_USERGROUP B on B.GROUP_ID=A.ID \n");
        sb.append("  GROUP BY B.USER_ID \n");
        sb.append(") r on r.USER_ID=a.ID \n");
        sb.append("LEFT OUTER JOIN ( \n");//系統組織 orgs
        sb.append("  SELECT B.USER_ID \n");
        sb.append("  , LISTAGG(A.ID, ',') WITHIN GROUP (ORDER BY A.ID) orgIdS \n");
        sb.append("  , LISTAGG(A.NAME, ',') WITHIN GROUP (ORDER BY A.NAME) orgS  \n");
        sb.append("  FROM CM_ORG A \n");
        sb.append("  JOIN CM_USER_ORG B on B.ORG_ID=A.ID \n");
        sb.append("  GROUP BY B.USER_ID \n");
        sb.append(") org on org.USER_ID=a.ID \n");
        sb.append("LEFT OUTER JOIN TC_USER b on b.ID = a.CREATOR \n");
        // sb.append("LEFT OUTER JOIN CM_ORG c on c.ID = a.ORG_ID \n");
        sb.append("LEFT OUTER JOIN ( \n");
        sb.append("  select a.id, a.name||' ['||NVL(c.name,'無公司別')||']' as name \n");
        sb.append("  from CM_ORG a \n");
        sb.append("  left outer join CS_DEPARTMENT b on b.id=a.src_id \n");
        sb.append("  left outer join CS_COMPANY c on c.id=b.companyid \n");
        sb.append("  where a.CTYPE='D' \n");
        sb.append(") c on c.ID = a.ORG_ID \n");
        sb.append("WHERE 1=1 \n");
        
        Map<String, Object> params = new HashMap<String, Object>();
        if( queryCriteria!=null ){
            UsersCriteriaVO criteriaVO = (UsersCriteriaVO)queryCriteria;
            // 關鍵字 (帳號、姓名、EMAIL、員編)
            if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().isEmpty() ){
                // 姓名支援繁簡轉換
                String nameSql = NativeSQLUtils.getLikeTranslateSQL("a.CNAME", criteriaVO.getKeyword(), params, "OR");
                //sb.append(" AND (a.LOGIN_ACCOUNT LIKE #LOGIN_ACCOUNT OR a.CNAME LIKE #CNAME OR a.EMAIL LIKE #EMAIL OR a.EMP_ID LIKE #EMP_ID)");
                sb.append(" AND (a.LOGIN_ACCOUNT LIKE #LOGIN_ACCOUNT ")
                  .append(nameSql)
                  .append(" OR a.EMAIL LIKE #EMAIL OR a.EMP_ID LIKE #EMP_ID)");
                params.put("LOGIN_ACCOUNT", "%"+criteriaVO.getKeyword()+"%");
                //params.put("CNAME", "%"+criteriaVO.getKeyword()+"%");
                params.put("EMAIL", "%"+criteriaVO.getKeyword()+"%");
                params.put("EMP_ID", "%"+criteriaVO.getKeyword()+"%");
            }
            // 群組
            if( criteriaVO.getGroup()>0 ){
                sb.append(" AND EXISTS(SELECT ID FROM TC_USERGROUP WHERE USER_ID=a.ID AND GROUP_ID = #GROUP_ID) ");
                params.put("GROUP_ID", criteriaVO.getGroup());
            }
            
            // 所屬組織  指定組織及其child的組織
            if (criteriaVO.getOrgId() > 0) {
                List<Long> orgIdList = cmOrgFacade.findOrgWithChild(criteriaVO.getOrgId());
                if (CollectionUtils.isNotEmpty(orgIdList)) {
                    sb.append(NativeSQLUtils.getInSQL("a.ORG_ID", orgIdList, params));
                    sb.append(" \n");
                }
//                sb.append(" AND a.ORG_ID = #ORG_ID ");
//                params.put("ORG_ID", criteriaVO.getOrgId());
            }
            if (CollectionUtils.isNotEmpty(criteriaVO.getOrgIds())) {
                List<Long> orgIdList = new ArrayList<Long>();
                for(Long orgId: criteriaVO.getOrgIds()){
                    orgIdList.addAll(cmOrgFacade.findOrgWithChild(orgId));
                }
                if (CollectionUtils.isNotEmpty(orgIdList)) {
                    sb.append(NativeSQLUtils.getInSQL("a.ORG_ID", orgIdList, params));
                    sb.append(" \n");
                }
            }
            // 已顯示有效
            if( criteriaVO.isOnlyNoDisabled() ){
                sb.append(" AND a.DISABLED=0 \n");
            }
        }
        
        // 最大筆數檢核
//        BigDecimal totalCount = NativeSQLUtils.countBySQL(em, sb.toString(), params);
        
        sb.append(" ORDER BY a.LOGIN_ACCOUNT");
        logger.debug("findUsersByCriteria sql = \n"+sb.toString());
        
        ResultSetHelper<UsersVO> resultSetHelper = new ResultSetHelper(UsersVO.class);
        List<UsersVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }
    
    /**
     * 關鍵字查詢 cname、loginAccount
     * @param keyword
     * @param includeDisabled
     * @return
     */
    public List<TcUser> findByKeyword(String keyword,boolean includeDisabled) {
        List<TcUser> list;
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TcUser> cq = cb.createQuery(TcUser.class);
        Root<TcUser> root = cq.from(TcUser.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        if (!includeDisabled) {
            Predicate p = cb.equal(root.get("disabled"), Boolean.FALSE);
            predicateList.add(p);
        }
        if (!StringUtils.isEmpty(keyword)) {
            Predicate p1 = cb.like(cb.lower(root.get("cname").as(String.class)), "%" + keyword.toLowerCase() + "%");
            Predicate p2 = cb.like(cb.lower(root.get("loginAccount").as(String.class)), "%" + keyword.toLowerCase() + "%");
            Predicate p3 = cb.or(p1, p2);
            predicateList.add(p3);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = em.createQuery(cq).getResultList();
        return (list == null ? new ArrayList<TcUser>() : list);
    }
    
    /**
     * 依 loginAccount 查詢使用者
     * @param loginAccount
     * @return TcUser, null if not found.
     */
    public TcUser findUserByLoginAccount(String loginAccount){
        String sql = "SELECT u FROM TcUser u WHERE u.loginAccount = :loginAccount";
        Query q = em.createQuery(sql);
        q.setParameter("loginAccount", loginAccount);
        List<TcUser> tcUserList = q.getResultList();

        return (tcUserList!=null && !tcUserList.isEmpty())? tcUserList.get(0):null;
    }
    
    /**
     * 依登入帳號查詢
     * @param loginAccount
     * @return
     */
    public List<TcUser> findByLoginAccount(String loginAccount) {
        List<TcUser> list;
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<TcUser> cq = cb.createQuery(TcUser.class);
        Root<TcUser> root = cq.from(TcUser.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate p = cb.equal(root.get("disabled"), Boolean.FALSE);
        predicateList.add(p);
        
        if (!StringUtils.isEmpty(loginAccount)) {
            Predicate p1 = cb.equal(root.get("loginAccount").as(String.class), loginAccount);
            predicateList.add(p1);
        }
        if (predicateList.size() > 0) {
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicateList.toArray(predicates);
            cq.where(predicates);
        }
        list = em.createQuery(cq).getResultList();
        return (list == null ? new ArrayList<TcUser>() : list);
    }
    
    /**
     * 依 autoComplete 輸入字串取得 USER
     * @param allUserList
     * @param txt
     * @return 
     */
    public TcUser findUserByTxt(List<UsersVO> allUserList, String txt){
        int s = txt.lastIndexOf("(");
        int e = txt.lastIndexOf(")");

        String loginAccount;
        if( s<0 || e<=0 ){
            loginAccount = txt;
        }else{
            loginAccount = txt.substring(s+1, e);
        }
        
        if( allUserList!=null ){
            //  先試完全相等
            for(UsersVO user : allUserList){
                if( user.getLogin_account()!=null && user.getCname()!=null ){
                    if( user.getLogin_account().toUpperCase().equals(loginAccount.toUpperCase()) 
                            || user.getCname().toUpperCase().equals(loginAccount.toUpperCase()) ){
                        return find(user.getId());
                    }
                }
            }
            // 沒有再取包含
            for(UsersVO user : allUserList){
                if( user.getLogin_account()!=null && user.getCname()!=null ){
                    if( user.getLogin_account().toUpperCase().indexOf(loginAccount.toUpperCase())>=0 
                            || user.getCname().toUpperCase().indexOf(loginAccount.toUpperCase())>=0 ){
                        return find(user.getId());
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 抓取使用者歸屬"公司"
     * @param user
     * @param allOrgList 系統所有組織列表
     * @return 
     */
    public CmOrg findUserCompany(TcUser user, List<CmOrg> allOrgList){
        if( user.getOrgId()==null ){
            logger.error("findUserCompany user.getOrgId() = null");
            return null;
        }
        return cmOrgFacade.findOrgCompany(user.getOrgId(), allOrgList);
    }
    /**
     * 抓取使用者歸屬"公司"
     * @param user
     * @return 
     */
    public CmOrg findUserCompany(TcUser user){
        List<CmOrg> cmOrgAll = cmOrgFacade.findAll();// 所有組織
        return findUserCompany(user, cmOrgAll);
    }
    
    /**
     * disable user
     * @param user
     * @param removePermission
     * @param loginUser 
     */
    public void delete(TcUser user, boolean removePermission, TcUser loginUser){
        if(user==null || loginUser==null){
            return;
        }
        user.setDisabled(Boolean.TRUE);//設為無效
        this.edit(user);
        
        if( removePermission ){
            removeUserRelationByUserId(user.getId());
        }        
    }
    
    /**
     * 移除使用者 系統角色及有權限的組織
     * @param userId
     */
    public void removeUserRelationByUserId(long userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        
        sql.append("  DELETE \n");
        sql.append("  FROM TC_USERGROUP \n");
        sql.append("  WHERE USER_ID=#USER_ID; \n");

        sql.append("  DELETE \n");
        sql.append("  FROM CM_USER_FACTORYGROUP_R \n");
        sql.append("  WHERE USER_ID=#USER_ID; \n");

        sql.append("  DELETE \n");
        sql.append("  FROM CM_USER_ORG \n");
        sql.append("  WHERE USER_ID=#USER_ID2; \n");

        sql.append("END;");

        Query q = em.createNativeQuery(sql.toString());
        q.setParameter("USER_ID", userId);
        q.setParameter("USER_ID2", userId);
        q.executeUpdate();
    }

    /**
     * ACCOUNT 已存在列表
     * @param acaccounts
     * @return 
     */
    public List<UsersVO> findByLoginAccounts(List<String> acaccounts) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select id, login_account, cname \n");
        sb.append("from tc_user \n");
        sb.append("where 1=1 \n");
        
        sb.append(NativeSQLUtils.getInSQL("login_account", acaccounts, params)).append("\n");
        
        logger.debug("findByLoginAccounts sql = \n"+sb.toString());
        
        ResultSetHelper<UsersVO> resultSetHelper = new ResultSetHelper(UsersVO.class);
        List<UsersVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }

    /**
     * 依 AD account 找 WebCS 資訊
     * @param acaccounts
     * @return 
     */
    public List<WebCSEmpVO> findByWebCSAccounts(List<String> acaccounts) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select a.id, a.adaccount, a.name, a.code, a.email, b.name as companyName \n");
        sb.append("from cs_employee a \n");
        sb.append("join cs_company b on b.id=a.companyid \n");
        sb.append("where a.disabled=0 \n");
        
        sb.append(NativeSQLUtils.getInSQL("adaccount", acaccounts, params)).append("\n");
        
        logger.debug("findByWebCSAccounts sql = \n"+sb.toString());
        
        ResultSetHelper<WebCSEmpVO> resultSetHelper = new ResultSetHelper(WebCSEmpVO.class);
        List<WebCSEmpVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }

    /**
     * 可匯入使用者
     * @return 
     */
    public List<UsersVO> findUserForImport() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        
        sb.append("select ADACCOUNT as login_account, NAME as cname, CODE as emp_id, EMAIL \n");
        sb.append(" from cs_employee \n");
        sb.append(" where disabled=0 ");
        sb.append(" ORDER BY ADACCOUNT");
        
        logger.debug("findUserForImport sql = \n"+sb.toString());
        
        ResultSetHelper<UsersVO> resultSetHelper = new ResultSetHelper(UsersVO.class);
        List<UsersVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }

    /**
     * 移轉 emp data from WebCS to TcUser
     * 
     * @param selectedImportUsers
     * @param loginUser 
     */
    public int tranWebCSToTcUser(WebCSEmpVO[] selectedImportUsers, TcUser loginUser) {
        int count = 0;
        if( selectedImportUsers==null ){
            return count;
        }
        
        for(WebCSEmpVO webCSEmpVO:selectedImportUsers){
            List<TcUser> users = this.findByLoginAccount(webCSEmpVO.getAdaccount());
            TcUser user;
            if( users==null || users.isEmpty() ){
                user = new TcUser();
            }else{
                user = users.get(0);
            }
            
            if( user.getDisabled()!=null && user.getDisabled() ){
                logger.error("tranWebCSToTcUser user "+user.getLoginAccount()+" disabled !");
            }else{
                // 更新資訊
                user.setLoginAccount(webCSEmpVO.getAdaccount());
                user.setName(webCSEmpVO.getName());
                user.setCname(webCSEmpVO.getName());
                user.setEmail(webCSEmpVO.getEmail());
                user.setEmpId(webCSEmpVO.getCode());
                user.setDisabled(Boolean.FALSE);
                
                this.save(user, loginUser);
                count++;
                // 新增 user 預設為 PLANT_USER
                userGroupFacade.defPlantUser(user, loginUser);
            }
        }
        return count;
    }
    
    /**
     * 刪除 Usergroup 關聯廠群組
     * @param tcUser
     * @param removedUG 
     */
    public void removeUserFactoryByTcGroups(TcUser tcUser, List<TcUsergroup> removedUG) {
        if( tcUser==null || removedUG==null || removedUG.isEmpty() ){
            return;
        }
     
        List<Long> ids = new ArrayList<Long>();
        for(TcUsergroup tcUsergroup : removedUG){
            if( tcUsergroup.getId()!=null && tcUsergroup.getId().longValue()>0 ){
                ids.add(tcUsergroup.getGroupId().getId());
                logger.debug("removeUserFactoryByTcGroups ids ="+tcUsergroup.getGroupId().getId());
            }
        }
        
        if( ids.isEmpty() ){
            return;
        }
        
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        sql.append("  DELETE FROM CM_USER_FACTORYGROUP_R \n");
        sql.append("  WHERE 1=1 \n");
        
        sql.append("  AND USER_ID=#USER_ID  \n");
        params.put("USER_ID", tcUser.getId());
        
        sql.append(NativeSQLUtils.getInSQL("USERGROUP_ID", ids, params)).append("; \n");
        
        // sql.append("  COMMIT; \n");
        sql.append("END;");
        
        logger.debug("removeUserFactoryByTcGroups sql =\n"+sql.toString());
        
        Query q = em.createNativeQuery(sql.toString());
        
        for(String key : params.keySet()){
            q.setParameter(key, params.get(key));
        }
        
        q.executeUpdate();
    }

    /**
     * 可複製權限來源人員
     * @param user
     * @return 
     */
    public List<UsersVO> findUserForCopy(UsersVO user) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        
        sb.append("select id, login_account, cname, emp_id, email \n");
        sb.append(" from tc_user \n");
        sb.append(" where disabled=0 \n");
        sb.append(" and id<>#id \n");
        sb.append(" ORDER BY login_account");
        
        params.put("id", user.getId());
        
        logger.debug("findUserForCopy sql = \n"+sb.toString());
        
        ResultSetHelper<UsersVO> resultSetHelper = new ResultSetHelper(UsersVO.class);
        List<UsersVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }

    /**
     * 複製權限
     * @param srcId
     * @param descId
     * @param loginUser 
     */
    public void copyPermission(long srcId, long descId, TcUser loginUser) {
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        // tc_usergroup
        sql.append("    insert into tc_usergroup (id, user_id, group_id, creator, createtimestamp) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, #descId1, b.group_id, #operator1, sysdate \n");
        sql.append("    from tc_user a \n");
        sql.append("    join tc_usergroup b on b.user_id=a.id \n");
        sql.append("    join tc_group c on c.id=b.group_id \n");
        sql.append("    where a.id=#srcId1 \n");
        sql.append("    and not exists (select * from tc_usergroup where user_id=#descId11 and group_id=b.group_id); \n");
        
        // cm_userfactory
        sql.append("    \n");
        sql.append("    insert into cm_userfactory (id, user_id, factory_id, creator, createtimestamp) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, #descId1, b.factory_id, #operator1, sysdate \n");
        sql.append("    from tc_user a \n");
        sql.append("    join cm_userfactory b on b.user_id=a.id \n");
        sql.append("    join cm_factory c on c.id=b.factory_id \n");
        sql.append("    where a.id=#srcId1 \n");
        sql.append("    and not exists (select * from cm_userfactory where user_id=#descId1 and factory_id=b.factory_id); \n");
        
        // cm_usercompany
        sql.append("    \n");
        sql.append("    insert into cm_usercompany (id, user_id, sap_client_code, creator, createtimestamp) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, #descId1, b.sap_client_code, #operator1, sysdate \n");
        sql.append("    from tc_user a \n");
        sql.append("    join cm_usercompany b on b.user_id=a.id \n");
        sql.append("    join cm_company c on c.sap_client_code=b.sap_client_code \n");
        sql.append("    where a.id=#srcId1 \n");
        sql.append("    and not exists (select * from cm_usercompany where user_id=#descId1 and sap_client_code=b.sap_client_code); \n");
        
        // cm_user_factorygroup_r
        sql.append("    \n");
        sql.append("    insert into cm_user_factorygroup_r (id, user_id, factorygroup_id, creator, createtimestamp, usergroup_id) \n");
        sql.append("    select SEQ_TCC.NEXTVAL, #descId1, b.factorygroup_id, #operator1, sysdate, b.usergroup_id \n");
        sql.append("    from tc_user a \n");
        sql.append("    join cm_user_factorygroup_r b on b.user_id=a.id \n");
        sql.append("    join cm_factorygroup c on c.disabled=0 and c.id=b.factorygroup_id \n");
        sql.append("    where a.id=#srcId1 \n");
        sql.append("    and not exists (select * from cm_user_factorygroup_r where user_id=#descId1 and factorygroup_id=b.factorygroup_id and usergroup_id=b.usergroup_id); \n");
        sql.append("END;");
        
        logger.debug("copyPermission sql =\n"+sql.toString());
        
        Query q = em.createNativeQuery(sql.toString());
        
        q.setParameter("descId1", descId);
        q.setParameter("descId11", descId);
        q.setParameter("operator1", loginUser.getId());
        q.setParameter("srcId1", srcId);
        
        /*q.setParameter("descId2", descId);
        q.setParameter("descId22", descId);
        q.setParameter("operator2", loginUser.getId());
        q.setParameter("srcId2", srcId);*/
        
        q.executeUpdate();
    }
}
