/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.irs.facade;

import com.tcci.fcs.enums.AccountTypeEnum;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.irs.controller.sheetdata.DetailTabVO;
import com.tcci.irs.entity.AccountNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class IrsAccountNodeFacade {
    private static final Logger logger = LoggerFactory.getLogger(IrsAccountNodeFacade.class);
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public AccountNode find(Long id) {
        return em.find(AccountNode.class, id);
    }
    
    public List<AccountNode> findByGroupAndRole(CompanyGroupEnum group, AccountTypeEnum accountType) {
        if(accountType == null){
            this.findByGroup(group);
        }
        Query q = em.createQuery("SELECT a FROM AccountNode a WHERE a.group=:group and a.reclRole=:accountType ");
        q.setParameter("group", group);
        q.setParameter("accountType", accountType);
        return q.getResultList();
    }
    public List<AccountNode> findByGroup(CompanyGroupEnum group) {
        Query q = em.createQuery("SELECT a FROM AccountNode a WHERE a.group=:group ");
        q.setParameter("group", group);
        return q.getResultList();
    }
    
    public List<DetailTabVO> findCodeListByParent(AccountNode parent){
        Collection<AccountNode> anList = parent.getChildren();
        List<DetailTabVO> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(anList)) {
            for(AccountNode accountNode : anList){
                DetailTabVO vo = new DetailTabVO();
                vo.setCode(accountNode.getCode());
                vo.setName(accountNode.getName());
                resultList.add(vo);
            }
        }
        return resultList;
    }
    /**
    public List<DetailTabVO> findCodeListByParent(String groupCode, String parentCode, AccountTypeEnum accountTypeEnum){
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT n1.id ");
        sql.append(",n1.CODE, n1.NAME ");
        sql.append("from IRS_ACCOUNT_NODE n1 \n");
        sql.append("join IRS_ACCOUNT_NODE n2 on n1.PARENT_ID = n2.id \n");
        sql.append("where 1=1 ");
        sql.append("and n1.COMP_GROUP = #groupCode \n");
        params.put("groupCode", groupCode);
        sql.append("and n2.CODE = #parentCode \n");
        params.put("parentCode", parentCode);
        if(accountTypeEnum != null){
            sql.append("and n2.RECL_ROLE = #accType \n");
            params.put("accType", accountTypeEnum.getCode());
        }
        logger.debug("sql: "+sql);
        
        Query query = em.createNativeQuery(sql.toString());
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        List list = query.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            logger.debug("list.size()= 0 ");
            return list;
        }
        
        List<DetailTabVO> resultList = new ArrayList<>();
        for (Object row : list) {
            DetailTabVO vo = new DetailTabVO();
            Object[] columns = (Object[]) row;
            vo.setCode((String)columns[1]);
            vo.setName((String)columns[2]);
            resultList.add(vo);
        }
        return resultList;
    }
    * */
}
