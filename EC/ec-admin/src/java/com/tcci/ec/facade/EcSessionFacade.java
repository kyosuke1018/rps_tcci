/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade;

import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.global.GlobalConstant;
import com.tcci.ec.entity.EcSession;
import com.tcci.ec.model.SessionVO;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author Peter.pan
 */
@Stateless
public class EcSessionFacade extends AbstractFacade<EcSession> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EcSessionFacade() {
        super(EcSession.class);
    }
    
    public boolean checkSession(Long memberId, String sessionKey){
        logger.info("checkSession memberId="+memberId+", sessionKey="+sessionKey);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT S.* \n");
        sql.append("FROM EC_SESSION S \n");
        sql.append("WHERE 1=1 \n");
        sql.append("AND S.MEMBER_ID=#MEMBER_ID \n");
        sql.append("AND S.SESSION_KEY=#SESSION_KEY \n");
        sql.append("AND S.EXP_TIME>#EXP_TIME \n");
        
        params.put("MEMBER_ID", memberId);
        params.put("SESSION_KEY", sessionKey);
        params.put("EXP_TIME", new Date());
        
        List<SessionVO> list = this.selectBySql(SessionVO.class, sql.toString(), params);
        return !sys.isEmpty(list);
    }
    
    public String newSession(Long memberId, Date expTime, String keyPrefix){
        logger.info("newSession memberId="+memberId);
        
        String sessionKey = UUID.randomUUID().toString();
        EcSession session = new EcSession();
        session.setMemberId(memberId);
        session.setSessionKey(keyPrefix + sessionKey);// DB 儲存加 keyPrefix
        // 過期時間同 JWT 即可
        session.setExpTime(expTime);
        session.setCreatetime(new Date());
        
        this.create(session);
        
        return sessionKey;
    }
    /*public String newSession(Long memberId){
        logger.info("newSession memberId="+memberId);
        
        String sessionKey = UUID.randomUUID().toString();
        EcSession session = new EcSession();
        session.setMemberId(memberId);
        session.setSessionKey(sessionKey);
        // 過期時間同 JWT 即可
        session.setExpTime(DateUtils.addMinutes(new Date(), GlobalConstant.JWT_EXPIRED_MINUTE));
        session.setCreatetime(new Date());
        
        this.create(session);
        
        return sessionKey;
    }*/
    
    public int deleteExpired(Long member){
        logger.info("deleteExpired ...");
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("BEGIN \n");
        sql.append("   DELETE FROM EC_SESSION WHERE MEMBER_ID=#MEMBER_ID AND EXP_TIME < #EXP_TIME; \n");
        sql.append("END; \n");
        
        params.put("MEMBER_ID", member);
        params.put("EXP_TIME", new Date());
        
        logger.debug("deleteExpired sql =\n"+sql.toString());
        Query q = em.createNativeQuery(sql.toString());
        setParamsToQuery("deleteExpired", params, q);
        
        return q.executeUpdate();
    }
}
