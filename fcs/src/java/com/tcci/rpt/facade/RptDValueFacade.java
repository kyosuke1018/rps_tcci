/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcci.rpt.facade;

import com.tcci.fc.entity.org.TcUser;
import com.tcci.fcs.enums.CompanyGroupEnum;
import com.tcci.rpt.entity.RptDValue;
import com.tcci.rpt.entity.RptSheetUpload;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kyle.cheng
 */
@Stateless
public class RptDValueFacade {
    private static final Logger logger = LoggerFactory.getLogger(RptDValueFacade.class);
            
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    public void saveExcelValue(RptSheetUpload reportUpload, Collection<RptDValue> values, TcUser modifier, Date modifytimestamp) {
        // remove old data
        Query q = em.createQuery("DELETE FROM RptDValue v WHERE v.upload=:upload");
        q.setParameter("upload", reportUpload);
        q.executeUpdate();
        // insert new data
        for (RptDValue rv : values) {
            rv.setUpload(reportUpload);
            rv.setModifier(modifier);
            rv.setModifytimestamp(modifytimestamp);
            em.persist(rv);
        }
    }
    
    public List<RptDValue> findReportValue(RptSheetUpload upload) {
        Query q = em.createQuery("SELECT v FROM RptDValue v WHERE v.upload=:upload");
        q.setParameter("upload", upload);
        return q.getResultList();
    }
    
    public List<RptDValue> findReportValue(String yearmonth, CompanyGroupEnum group) {
        Query q = em.createQuery("SELECT v FROM RptDValue v WHERE v.upload.yearmonth=:yearmonth AND v.upload.company.group.code = :group AND v.sheet IN ('D0202', 'D0204')");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("group", group.getCode());
        return q.getResultList();
    }
}
