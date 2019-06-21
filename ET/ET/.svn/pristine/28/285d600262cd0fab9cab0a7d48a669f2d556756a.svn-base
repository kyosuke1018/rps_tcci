/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package com.tcci.cm.facade.admin;

import com.tcci.cm.enums.SecurityRoleEnum;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.UsersCriteriaVO;
import com.tcci.cm.model.admin.UsersVO;
import com.tcci.cm.model.admin.WebCSEmpVO;
import com.tcci.cm.util.JsfUtils;
import com.tcci.cm.util.NativeSQLUtils;
import com.tcci.fc.entity.org.TcGroup;
import com.tcci.fc.entity.org.TcUser;
import com.tcci.fc.entity.org.TcUsergroup;
import com.tcci.fc.facade.org.TcGroupFacade;
import com.tcci.fc.util.ResultSetHelper;
import com.tcci.et.enums.ActivityLogEnum;
import com.tcci.cm.model.global.GlobalConstant;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Peter
 */
@Stateless
@Named
public class UserFacade extends AbstractFacade<TcUser> {
    @EJB TcGroupFacade tcGroupFacade;
    @EJB UserGroupFacade userGroupFacade;
    @EJB CmActivityLogFacade cmActivityLogFacade;
    
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
     * @param simulated
     */
    public void save(TcUser user, List<String> selectedGroups, boolean removePermission, TcUser operator, boolean simulated){
        if( user==null ){
            logger.error("save user = null !");
            return;
        }
        // === for user group ================
        List<TcUsergroup> removedUG = saveUserGroup(user, selectedGroups, operator);
        // === for user org ================
        //　saveUserOrg(user, selectedOrgs, operator);
        
        // save tc_user
        save(user, operator, simulated);
        
        // 同時移除廠別關聯設定
        /*if( GlobalConstant.ENABLED_FEEDBACK_RULE_PERMISSION ){
            removeUserFactoryByTcGroups(user, removedUG);
        }*/
        
        // 移除關聯權限
        if( (user.getDisabled()!=null && user.getDisabled()) && removePermission ){
            removeUserRelationByUserId(user.getId(), simulated);
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
     * 單筆儲存
     * @param entity 
     * @param operator 
     */
    public void save(TcUser entity, TcUser operator, boolean simulated){
        if( entity!=null ){
            if( entity.getId()!=null && entity.getId()>0 ){
                //entity.setModifier(operator);
                //entity.setModifytimestamp(new Date());
                this.edit(entity, simulated);
            }else{
                entity.setCreator(operator);
                entity.setCreatetimestamp(new Date());
                this.create(entity, simulated);
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
     * 　
     * Oracle LISTAGG to MSSQL:　
        SELECT t1.USER_ID,
          STUFF((
                        SELECT DISTINCT ',' + t2.CODE
                        FROM (SELECT B.USER_ID, A.CODE FROM TC_GROUP A JOIN TC_USERGROUP B ON B.GROUP_ID=A.ID) t2
                        WHERE t1.USER_ID = t2.USER_ID
                        FOR XML PATH(''), TYPE
                ).value('.', 'NVARCHAR(MAX)') 
                ,1,1,'') GROUPS
        FROM (SELECT B.USER_ID, A.CODE FROM TC_GROUP A JOIN TC_USERGROUP B ON B.GROUP_ID=A.ID) t1
        GROUP BY t1.USER_ID
     */
    public List<UsersVO> findUsersByCriteria(UsersCriteriaVO queryCriteria){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT A.ID, A.LOGIN_ACCOUNT, A.CNAME, A.EMAIL, A.EMP_ID, A.DISABLED ");
        sb.append(", R.GROUPS \n");
        // sb.append(", ORG.ORGIDS, ORG.ORGS \n");//user可存取組織
        // sb.append(", C.ID AS USERORGID, C.NAME AS USERORG \n");//user所屬組織
        sb.append(", C.NAME USERORG \n");// 直接使用 WEBCB 組織資訊
        sb.append(", B.CNAME AS CREATOR, A.CREATETIMESTAMP \n");
        
        sb.append("FROM TC_USER A \n");
        sb.append("LEFT OUTER JOIN TC_USER B ON B.ID = A.CREATOR \n");
        
        // 直接使用 WEBCB 組織資訊
        sb.append("LEFT OUTER JOIN CS_EMPLOYEE E ON E.ADACCOUNT = A.LOGIN_ACCOUNT AND E.DISABLED=A.DISABLED \n");
        sb.append("LEFT OUTER JOIN CS_COMPANY C ON C.ID = E.COMPANYID \n");
        // for MSSQL 
        /*             
        sb.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sb.append("  SELECT t1.USER_ID, \n");
        sb.append("    STUFF(( \n");
        sb.append("           SELECT DISTINCT ',' + t2.NAME \n");
        sb.append("           FROM (SELECT B.USER_ID, A.CODE, A.NAME FROM TC_GROUP A JOIN TC_USERGROUP B ON B.GROUP_ID=A.ID) t2 \n");
        sb.append("           WHERE t1.USER_ID = t2.USER_ID \n");
        sb.append("           FOR XML PATH(''), TYPE \n");
        sb.append("          ).value('.', 'NVARCHAR(MAX)') \n");
        sb.append("          , 1, 1, '' \n");
        sb.append("    ) GROUPS \n");
        sb.append("  FROM (SELECT B.USER_ID, A.CODE FROM TC_GROUP A JOIN TC_USERGROUP B ON B.GROUP_ID=A.ID) t1 \n");
        sb.append("  GROUP BY t1.USER_ID \n");
        
        sb.append(") R ON R.USER_ID=A.ID \n");
        */
        // for Oracle
        sb.append("LEFT OUTER JOIN ( \n");//系統角色 groups TC_GROUP.NAME
        sb.append("  SELECT B.USER_ID \n");
//        sb.append("  , LISTAGG(A.CODE, ',') WITHIN GROUP (ORDER BY A.CODE) GROUPS \n");
        sb.append("  , LISTAGG(A.NAME, ',') WITHIN GROUP (ORDER BY A.NAME) GROUPS \n");
        sb.append("  FROM TC_GROUP A \n");
        sb.append("  JOIN TC_USERGROUP B on B.GROUP_ID=A.ID \n");
        sb.append("  GROUP BY B.USER_ID \n");
        sb.append(") R on R.USER_ID=A.ID \n");
        /*
        sb.append("LEFT OUTER JOIN ( \n");//系統組織 orgs
        sb.append("  SELECT B.USER_ID \n");
        sb.append("  , LISTAGG(A.ID, ',') WITHIN GROUP (ORDER BY A.ID) ORGIDS \n");
        sb.append("  , LISTAGG(A.NAME, ',') WITHIN GROUP (ORDER BY A.NAME) ORGS  \n");
        sb.append("  FROM CM_ORG A \n");
        sb.append("  JOIN CM_USER_ORG B ON B.ORG_ID=A.ID \n");
        sb.append("  GROUP BY B.USER_ID \n");
        sb.append(") ORG ON ORG.USER_ID=A.ID \n");
        */
        /*
        sb.append("LEFT OUTER JOIN ( \n");
        sb.append("  SELECT A.ID, A.NAME||' ['||NVL(C.NAME,'無公司別')||']' AS NAME \n");
        sb.append("  FROM CM_ORG A \n");
        sb.append("  LEFT OUTER JOIN CS_DEPARTMENT B ON B.ID=A.SRC_ID \n");
        sb.append("  LEFT OUTER JOIN CS_COMPANY C ON C.ID=B.COMPANYID \n");
        sb.append("  WHERE A.CTYPE='D' \n");
        sb.append(") C ON C.ID = A.ORG_ID \n");
        */
        sb.append("WHERE 1=1 \n");
        
        Map<String, Object> params = new HashMap<String, Object>();
        if( queryCriteria!=null ){
            UsersCriteriaVO criteriaVO = (UsersCriteriaVO)queryCriteria;
            // 關鍵字 (帳號、姓名、EMAIL、員編)
            if( criteriaVO.getKeyword()!=null && !criteriaVO.getKeyword().isEmpty() ){
                // 姓名支援繁簡轉換
                String nameSql = NativeSQLUtils.getLikeTranslateSQL("A.CNAME", criteriaVO.getKeyword(), params, "OR");
                //sb.append(" AND (A.LOGIN_ACCOUNT LIKE #LOGIN_ACCOUNT OR A.CNAME LIKE #CNAME OR A.EMAIL LIKE #EMAIL OR A.EMP_ID LIKE #EMP_ID)");
                sb.append(" AND (A.LOGIN_ACCOUNT LIKE #LOGIN_ACCOUNT ")
                  .append(nameSql)
                  .append(" OR A.EMAIL LIKE #EMAIL OR A.EMP_ID LIKE #EMP_ID) \n");
                params.put("LOGIN_ACCOUNT", "%"+criteriaVO.getKeyword()+"%");
                //params.put("CNAME", "%"+criteriaVO.getKeyword()+"%");
                params.put("EMAIL", "%"+criteriaVO.getKeyword()+"%");
                params.put("EMP_ID", "%"+criteriaVO.getKeyword()+"%");
            }
            // 群組
            if( criteriaVO.getGroup()>0 ){
                sb.append(" AND EXISTS(SELECT ID FROM TC_USERGROUP WHERE USER_ID=A.ID AND GROUP_ID = #GROUP_ID) \n");
                params.put("GROUP_ID", criteriaVO.getGroup());
            }
            // 已顯示有效
            if( criteriaVO.isOnlyNoDisabled() ){
                sb.append(" AND A.DISABLED=0 \n");
            }
            
            // CSRC 維護人員
            if( criteriaVO.isOperator() ){
                sb.append("AND (A.OPERATOR=1 OR R.GROUPS LIKE '%")
                        .append(SecurityRoleEnum.CSRC_USER.getName()).append("%')  \n");
                //sb.append(" AND (A.OPERATOR=1 OR C.NAME=#orgname) \n");
                //params.put("orgname", criteriaVO.getOrgName());
            }
        }
        
        // 最大筆數檢核
//        BigDecimal totalCount = NativeSQLUtils.countBySQL(em, sb.toString(), params);
        
        sb.append(" ORDER BY A.LOGIN_ACCOUNT");
        logger.debug("findUsersByCriteria ...");
        
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
    public TcUser findForLogin(String loginAccount){
        List<TcUser> list = findByLoginAccount(loginAccount);
        return (list!=null && !list.isEmpty())? list.get(0):null;
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
     * disable user
     * @param user
     * @param removePermission
     * @param loginUser 
     * @param simulated 
     */
    public void delete(TcUser user, boolean removePermission, TcUser loginUser, boolean simulated){
        if(user==null || loginUser==null){
            return;
        }
        user.setDisabled(Boolean.TRUE);//設為無效
        this.edit(user, simulated);
        
        if( removePermission ){
            removeUserRelationByUserId(user.getId(), simulated);
        }        
    }
    
    /**
     * 移除使用者 系統角色及有權限的組織
     * @param userId
     * @param simulated
     */
    public void removeUserRelationByUserId(long userId, boolean simulated) {
        if( GlobalConstant.SIMULATE_DENIED_UPDATE && simulated ){ // 模擬使用者
            throw new RuntimeException("*** Simulate user can not update data! ***");
        }
        
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        
        sql.append("  DELETE \n");
        sql.append("  FROM TC_USERGROUP \n");
        sql.append("  WHERE USER_ID=#USER_ID; \n");

        sql.append("  DELETE \n");
        sql.append("  FROM CM_USER_FACTORYGROUP_R \n");
        sql.append("  WHERE USER_ID=#USER_ID; \n");

        /*sql.append("  DELETE \n");
        sql.append("  FROM CM_USER_ORG \n");
        sql.append("  WHERE USER_ID=#USER_ID2; \n");*/

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
        sb.append("SELECT ID, LOGIN_ACCOUNT, CNAME \n");
        sb.append("FROM TC_USER \n");
        sb.append("WHERE 1=1 \n");
        
        sb.append(NativeSQLUtils.getInSQL("LOGIN_ACCOUNT", acaccounts, params)).append("\n");
        
        logger.debug("findByLoginAccounts ...");
        
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
        sb.append("SELECT A.ID, A.ADACCOUNT, A.NAME, A.CODE, A.EMAIL, B.NAME AS COMPANYNAME \n");
        sb.append("FROM CS_EMPLOYEE A \n");
        sb.append("JOIN CS_COMPANY B ON B.ID=A.COMPANYID \n");
        sb.append("WHERE A.DISABLED=0 \n");
        
        sb.append(NativeSQLUtils.getInSQL("ADACCOUNT", acaccounts, params)).append("\n");
        
        logger.debug("findByWebCSAccounts ...");
        
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
        
        sb.append("SELECT ADACCOUNT AS LOGIN_ACCOUNT, NAME AS CNAME, CODE AS EMP_ID, EMAIL \n");
        sb.append(" FROM CS_EMPLOYEE \n");
        sb.append(" WHERE DISABLED=0 ");
        sb.append(" ORDER BY ADACCOUNT");
        
        logger.debug("findUserForImport ...");
        
        ResultSetHelper<UsersVO> resultSetHelper = new ResultSetHelper(UsersVO.class);
        List<UsersVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }

    /**
     * 移轉 emp data from WebCS to TcUser
     * 
     * @param selectedImportUsers
     * @param viewId
     * @param loginUser 
     * @param simulated 
     */
    public void tranWebCSToTcUser(WebCSEmpVO[] selectedImportUsers, String viewId, TcUser loginUser, boolean simulated) {
        if( selectedImportUsers==null ){
            return;
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
                
                try{
                    this.save(user, loginUser, simulated);

                    cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.A_USER, viewId, JsfUtils.getClientIP(), user.getId(), 
                            user.getLoginAccount(), user.getCname(), true, loginUser, simulated);
                }catch(Exception e){
                    logger.error("tranWebCSToTcUser Exception :\n", e);
                    cmActivityLogFacade.logActiveForSingleId(ActivityLogEnum.A_USER, viewId, JsfUtils.getClientIP(), user.getId(), 
                            user.getLoginAccount(), user.getCname(), false, loginUser, simulated);
                }
            }
        }
    }
    
    /**
     * 可複製權限來源人員
     * @param user
     * @return 
     */
    public List<UsersVO> findUserForCopy(UsersVO user) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECT ID, LOGIN_ACCOUNT, CNAME, EMP_ID, EMAIL \n");
        sb.append(" FROM TC_USER \n");
        sb.append(" WHERE DISABLED=0 \n");
        sb.append(" AND ID<>#id \n");
        sb.append(" ORDER BY LOGIN_ACCOUNT");
        
        params.put("id", user.getId());
        
        logger.debug("findUserForCopy ...");
        
        ResultSetHelper<UsersVO> resultSetHelper = new ResultSetHelper(UsersVO.class);
        List<UsersVO> usersList = resultSetHelper.queryToPOJOList(getEntityManager(), sb.toString(), params);
        
        return usersList;
    }

    /**
     * 複製權限
     * @param srcId
     * @param descId
     * @param appendOnly
     * @param loginUser 
     * @param simulated 
     */
    public void copyPermission(long srcId, long descId, boolean appendOnly, TcUser loginUser, boolean simulated) {
        // 複製功能群組權限
        copyGroupPermission(srcId, descId, appendOnly, loginUser, simulated);
    }
    
    /**
     * 複製功能群組權限
     * @param srcId
     * @param descId
     * @param appendOnly
     * @param loginUser 
     */
    public void copyGroupPermission(long srcId, long descId, boolean appendOnly, TcUser loginUser, boolean simulated) {
        if( GlobalConstant.SIMULATE_DENIED_UPDATE && simulated ){ // 模擬使用者
            throw new RuntimeException("*** Simulate user can not update data! ***");
        }
        // tc_usergroup
        StringBuilder sql = new StringBuilder();
        
        if( !appendOnly ){
            logger.debug("copyPermission sql =\n"+sql.toString());
            Query qD = em.createNativeQuery(sql.toString());
            sql.append("delete from TC_USERGROUP where USER_ID=#USER_ID;");
            
            logger.debug("copyPermission sql =\n"+sql.toString());
            Query q = em.createNativeQuery(sql.toString());

            q.setParameter("USER_ID", descId);
            q.executeUpdate();
        }
        
        sql = new StringBuilder();
        sql.append("insert into TC_USERGROUP (USER_ID, GROUP_ID, CREATOR, CREATETIMESTAMP) \n");
        sql.append("select #descId1, b.GROUP_ID, #operator1, GETDATE() \n");
        sql.append("from TC_USER a \n");
        sql.append("join TC_USERGROUP b on b.USER_ID=a.ID \n");
        sql.append("join TC_GROUP c on c.ID=b.GROUP_ID \n");
        sql.append("where a.ID=#srcId1 \n");
        sql.append("and not exists (select * from TC_USERGROUP where USER_ID=#descId11 and GROUP_ID=b.GROUP_ID); \n");

        logger.debug("copyPermission sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        
        q.setParameter("descId1", descId);
        q.setParameter("descId11", descId);
        q.setParameter("operator1", loginUser.getId());
        q.setParameter("srcId1", srcId);

        q.executeUpdate();
    }
   
    public List<TcUser> findAllActiveUser() {
        Query q = em.createQuery("SELECT u FROM TcUser u WHERE u.cname IS NOT NULL and u.disabled=0 ORDER BY u.cname");
        return q.getResultList();
    }
}