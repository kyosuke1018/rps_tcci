/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.cm.facade.admin;

import com.tcci.cm.entity.admin.CmCompany;
import com.tcci.cm.facade.AbstractFacade;
import java.util.List;
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
        CmCompany cmCompany = getBySapClientCode(sapClientCode);
        String result = null;
        if (cmCompany != null) {
            result = cmCompany.getCompanyName();
        }
        return result;
    }
    
}
