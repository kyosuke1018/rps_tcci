/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.ec.facade.member;

import com.tcci.ec.entity.EcMember;
import com.tcci.ec.entity.EcMemberMsg;
import com.tcci.ec.entity.EcProduct;
import com.tcci.ec.entity.EcStore;
import com.tcci.ec.enums.MsgTypeEnum;
import com.tcci.ec.facade.AbstractFacade;
import com.tcci.fc.util.ResultSetHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class EcMemberMsgFacade extends AbstractFacade<EcMemberMsg> {
    private final static Logger logger = LoggerFactory.getLogger(EcMemberMsgFacade.class);
    
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcMemberMsgFacade() {
        super(EcMemberMsg.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void save(EcMemberMsg entity) {
        if (entity.getId() == null) {
            entity.setCreatetime(new Date());
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public List<EcMemberMsg> findByMember(EcMember member) {
        Query q = em.createNamedQuery("EcMemberMsg.findByMember");
        q.setParameter("member", member);
        List<EcMemberMsg> list = q.getResultList();
        return list;
    }
    
    public List<EcMemberMsg> findByPrd(EcProduct product) {
        return this.findByPrd(product, MsgTypeEnum.P);
    }
    public List<EcMemberMsg> findByPrd(EcProduct product, MsgTypeEnum type) {
        Query q = em.createNamedQuery("EcMemberMsg.findByPrd");
        q.setParameter("product", product);
        if(type!=null){
            q.setParameter("type", type);
        }
        List<EcMemberMsg> list = q.getResultList();
        return list;
    }
    
    public List<EcMemberMsg> findByStore(EcStore store) {
        return this.findByStore(store, MsgTypeEnum.S);
    }
    public List<EcMemberMsg> findByStore(EcStore store, MsgTypeEnum type) {
        Query q = em.createNamedQuery("EcMemberMsg.findByStore");
        q.setParameter("store", store);
        if(type!=null){
            q.setParameter("type", type);
        }
        List<EcMemberMsg> list = q.getResultList();
        return list;
    }
    
    public List<EcMemberMsg> findRootByPrd(EcProduct product) {
        Query q = em.createNamedQuery("EcMemberMsg.findRootByPrd");
        q.setParameter("product", product);
        q.setParameter("type", MsgTypeEnum.P);
        List<EcMemberMsg> list = q.getResultList();
        return list;
    }
    
    public List<EcMemberMsg> findRootByStore(EcStore store) {
        Query q = em.createNamedQuery("EcMemberMsg.findRootByStore");
        q.setParameter("store", store);
        q.setParameter("type", MsgTypeEnum.S);
        List<EcMemberMsg> list = q.getResultList();
        return list;
    }
    
    public List<EcMemberMsg> findByParent(Long parent) {
        Query q = em.createNamedQuery("EcMemberMsg.findByParent");
        q.setParameter("parent", parent);
        List<EcMemberMsg> list = q.getResultList();
        return list;
    }
    
    public List<Long> findParentByMember(EcMember member){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT distinct NVL(parent, id) as id \n");
        sql.append("from (select PARENT, ID, CREATETIME from EC_MEMBER_MSG where MEMBER_ID=#MEMBER_ID \n");
        sql.append("order by CREATETIME desc)  \n");
        
        params.put("MEMBER_ID", member.getId());
//        List<Long> resList;
//        ResultSetHelper resultSetHelper = new ResultSetHelper(Long.class);
//        resList = resultSetHelper.queryToPOJOList(getEntityManager(), sql.toString(), params, 0, 20, false);
        
        Query query = em.createNativeQuery(sql.toString());
        query.setMaxResults(20);
        if( params!=null ){
            for (String key : params.keySet()) { // 條件參數
                query.setParameter(key, params.get(key));
            }
        }
        List list = query.getResultList();
        List<Long> resList = new ArrayList<>();
        for (Object row : list) {
            BigDecimal bd = (BigDecimal)row;
            resList.add(bd.longValue());
        }

//        List<Long> resList = query.getResultList();
        return resList;
    }
    
    public List<Long> findParentByStore(EcStore store){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT distinct NVL(parent, id) as id \n");
        sql.append("from (select PARENT, ID, CREATETIME from EC_MEMBER_MSG where STORE_ID=#STORE_ID \n");
        sql.append("order by CREATETIME desc)  \n");
        
        params.put("STORE_ID", store.getId());
        
        Query query = em.createNativeQuery(sql.toString());
        query.setMaxResults(20);
        if( params!=null ){
            for (String key : params.keySet()) { // 條件參數
                query.setParameter(key, params.get(key));
            }
        }
        List list = query.getResultList();
        List<Long> resList = new ArrayList<>();
        for (Object row : list) {
            BigDecimal bd = (BigDecimal)row;
            resList.add(bd.longValue());
        }

        return resList;
    }
    
    
}
