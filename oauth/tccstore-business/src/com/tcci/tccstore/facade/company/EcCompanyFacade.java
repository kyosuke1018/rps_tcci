/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.tccstore.facade.company;

import com.tcci.tccstore.entity.EcCompany;
import com.tcci.tccstore.facade.AbstractFacade;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Jimmy.Lee
 */
@Named
@Stateless
public class EcCompanyFacade extends AbstractFacade<EcCompany> {

    @PersistenceContext(unitName = "Model")
    private EntityManager em;

    public EcCompanyFacade() {
        super(EcCompany.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<EcCompany> findNoHideCredit() {
        return em.createNamedQuery("EcCompany.findNoHideCredit").getResultList();
    }
    
    public Map<String, EcCompany> findNoHideCreditToMap() {
        List<EcCompany> companys = findNoHideCredit();
        Map<String, EcCompany> result = new HashMap<>();
        for (EcCompany company : companys) {
            result.put(company.getCode(), company);
        }
        return result;
    }

}
