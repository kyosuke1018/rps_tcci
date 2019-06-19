/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade;

import com.tcci.tccstore.facade.vo.TccMemberVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class TccMemberFacade {
    private static final Logger logger = LoggerFactory.getLogger(TccMemberFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;
    
    public TccMemberVO findActiveByLoginAccount(String loginAccount) {
        List<TccMemberVO> result;
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
//        select * from tccstore_user.ec_member;
        sb.append("select LOGIN_ACCOUNT,NAME,EMAIL,PHONE,PASSWORD ");
        sb.append(" FROM tccstore_user.ec_member ");
        sb.append(" WHERE 1=1 ");
        sb.append(" AND ACTIVE = 1 ");
        if (StringUtils.isNotBlank(loginAccount)) {
            sb.append(" and LOGIN_ACCOUNT = :loginAccount \n");
            params.put("loginAccount", loginAccount);
        }
        Query q = em.createNativeQuery(sb.toString()).setHint("eclipselink.jdbc.parameter-delimiter", ":");
        for (String key : params.keySet()) {
            q.setParameter(key, params.get(key));
        }
        
        
        List list = q.getResultList();
        if(list.isEmpty()){
            return null;
        }else{
            TccMemberVO vo = new TccMemberVO();
            Object[] columns = (Object[]) list.get(0);
            vo.setLoginAccount((String) columns[0]);
            vo.setName((String) columns[1]);
            vo.setEmail((String) columns[2]);
            vo.setPhone((String) columns[3]);
            vo.setPassword((String) columns[4]);
            
            return vo;
        }
    }
    
        /**
     * 同步 EC1.0 密碼 & Active
     * @param loginAccount
     * @return 
     */
    public int syncPasswordFromEc10(String loginAccount) {
        // 不要因同步問題影響登入，使用 try catch
        try {
            Map<String, Object> params = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append("BEGIN \n");

            sql.append("MERGE INTO EC_MEMBER D \n");
            sql.append("USING ( \n");
            sql.append("     SELECT E10.ID, E10.LOGIN_ACCOUNT, E10.NAME, E10.EMAIL, E10.PHONE, E10.PASSWORD, E10.ACTIVE \n");
            sql.append("     FROM TCCSTORE_USER.EC_MEMBER E10 \n");
            // 針對經銷商
            sql.append("     JOIN EC_MEMBER M ON UPPER(M.LOGIN_ACCOUNT)=UPPER(E10.LOGIN_ACCOUNT) AND M.TCC_DEALER=1 \n");
            //sql.append("     -- WHERE M.VERIFY_CODE>1 \n");
            sql.append("     WHERE UPPER(E10.LOGIN_ACCOUNT) = UPPER(#LOGIN_ACCOUNT) \n");
            sql.append(") S ON (UPPER(S.LOGIN_ACCOUNT)=UPPER(D.LOGIN_ACCOUNT)) \n");
            sql.append("WHEN MATCHED THEN \n");
            sql.append("UPDATE SET D.PASSWORD=S.PASSWORD, D.ACTIVE=S.ACTIVE, D.VERIFY_CODE=TO_CHAR(SYSDATE, 'yyyyMMddHH24MISS'); \n");

            sql.append("END; \n");

            params.put("LOGIN_ACCOUNT", loginAccount);

            logger.debug("syncPasswordFromEc10 sql =\n"+sql.toString());
            Query q = em.createNativeQuery(sql.toString());
            setParamsToQuery("syncPasswordFromEc10", params, q);

            return q.executeUpdate();
        } catch (Exception e) {
            logger.error("syncPasswordFromEc10"+e);
//            sys.processUnknowException(null, "syncPasswordFromEc10", e);
        }
        logger.error("syncPasswordFromEc10 error loginAccount = " + loginAccount);
        return -1;
    }
    
    public void setParamsToQuery(String method, Map<String, Object> params, Query q){
        if( params!=null ){
            for(String key : params.keySet()){
                q.setParameter(key, params.get(key));
                logger.debug(method+" params "+key+" = "+params.get(key));
            }
        }
    }
        
//        if (CollectionUtils.isEmpty(list)) {
////            logger.debug("list.size()= 0 ");
//            return list;
//        }
//        result = new ArrayList<>();
//        for (Object row : list) {
//            TccMemberVO vo = new TccMemberVO();
//            Object[] columns = (Object[]) row;
//            vo.setLoginAccount((String) columns[0]);
//            vo.setName((String) columns[1]);
//            vo.setEmail((String) columns[2]);
//            vo.setPhone((String) columns[3]);
//            vo.setPassword((String) columns[4]);
//            
//            result.add(vo);
//        }
//        return result;
    
    /*@PersistenceContext(unitName = "tccstorePU")
    private EntityManager em;
    
    public TccMember findActiveByLoginAccount(String loginAccount) {
        Query q = em.createNamedQuery("TccMember.findActiveByLoginAccount");
        q.setParameter("loginAccount", loginAccount);
        List<TccMember> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    public TccMember findByLoginAccount(String loginAccount) {
        Query q = em.createNamedQuery("findByLoginAccount");
        q.setParameter("loginAccount", loginAccount);
        List<TccMember> result = q.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
    
    public List<TccMember> findAll() {
        Query q = em.createNamedQuery("TccMember.findAll");
        return q.getResultList();
    }*/
    
}
