/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fc.util.time.DateUtils;
import com.tcci.fcs.entity.FcCompany;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.rpt.entity.RptSheetUpload;
import com.tcci.rpt.entity.ZtfiAfcsCsbu;
import java.util.List;
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
public class ZtfiAfcsCsbuFacade {
    private static final Logger logger = LoggerFactory.getLogger(ZtfiAfcsCsbuFacade.class);
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public ZtfiAfcsCsbu findByCompany(FcCompany company) {
        Query q = em.createNamedQuery("ZtfiAfcsCsbu.findByCompany");
        q.setParameter("compCode", company.getCode());
        List<ZtfiAfcsCsbu> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public List<ZtfiAfcsCsbu> findAll(){
        Query q = em.createNamedQuery("ZtfiAfcsCsbu.findAll");
        return q.getResultList();
    }
}
