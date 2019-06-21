/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmCompany;
import com.tcci.cm.facade.AbstractFacade;
import com.tcci.cm.model.admin.CmCompanyVO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Jackson.Lee
 */
@Stateless
public class CmCompanyFacade extends AbstractFacade<CmCompany> {
    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CmCompanyFacade() {
        super(CmCompany.class);
    }
    
    public List<CmCompany> findAllAndSort() {
        Query q = em.createNamedQuery("CmCompany.findAllAndSort");
        return q.getResultList();
    }
    public List<CmCompanyVO> findAllCompanies(){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select a.* \n");
        sb.append("from cm_company a \n");
        sb.append("order by sort_num \n");
        
        return this.selectBySql(CmCompanyVO.class, sb.toString(), params);
    }
    
    /**
     * 依code取得Sapclient
     * @param code
     * @return 
     */
    public CmCompany getBySapClientCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        Query q = em.createNamedQuery("CmCompany.findBySapClientCode");
        q.setParameter("sapClientCode", code);
        List<CmCompany> list = q.getResultList();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }        	    
    
    /**
     * 依SapClient Code取得所屬公司名稱
     * @param sapClientCode
     * @return 
     */
    public String getCompanyName(String sapClientCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        CmCompany cmCompany = getBySapClientCode(sapClientCode);
        String result = null;
        if (cmCompany != null) {
            result = cmCompany.getCompanyName();
        }
        return result;
    }
    
    /**
     * 特定廠群組包含公司別
     * 
     * @param groupCode
     * @return 
     */
    public List<String> getByFactorygroup(String groupCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append("select a.sap_client_code \n");
        sb.append("from cm_company a \n");
        sb.append("join cm_factory b on b.sap_client_code=a.sap_client_code \n");
        sb.append("join cm_factory_group_r c on c.factory_id=b.id \n");
        sb.append("join cm_factorygroup d on d.id=c.factorygroup_id \n");
        sb.append("where 1=1 \n");
        sb.append("and d.code = #groupCode \n");
        params.put("groupCode", groupCode);
        
        sb.append("group by a.sap_client_code \n");
        sb.append("order by a.sap_client_code");
        
        return this.findStringList(sb.toString(), params);
    }
}
