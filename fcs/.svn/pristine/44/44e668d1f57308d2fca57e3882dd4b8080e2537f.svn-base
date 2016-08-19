/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.fcs.facade;

import com.tcci.fcs.entity.FcRptConfig;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.fcs.enums.ReportSheetEnum;
import com.tcci.fcs.enums.RptConfigCategoryEnum;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Kyle.Cheng
 */
@Stateless
public class FcRptConfigFacade {
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public List<FcRptConfig> findByCompAndCategory(CompanyGroupEnum group, RptConfigCategoryEnum category) {
        Query q = em.createNamedQuery("FcRptConfig.findByCompAndCategory");
        q.setParameter("group", group);
        q.setParameter("category", category);
        return q.getResultList();
    }
    
    public List<FcRptConfig> findByCompAndCategoryAndSheet(CompanyGroupEnum group, RptConfigCategoryEnum category, ReportSheetEnum sheet) {
        Query q = em.createNamedQuery("FcRptConfig.findByCompAndCategoryAndSheet");
        q.setParameter("group", group);
        q.setParameter("category", category);
        q.setParameter("sheet", sheet);
        return q.getResultList();
    }

}
