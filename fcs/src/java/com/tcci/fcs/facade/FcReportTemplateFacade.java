/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.fcs.facade;

import com.tcci.fcs.enums.FcConfigKeyEnum;
import com.tcci.fc.facade.content.ContentFacade;
import com.tcci.fc.vo.AttachmentVO;
import com.tcci.fcs.entity.FcReportTemplate;
import com.tcci.fcs.enums.CompanyGroupEnum;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jimmy.Lee
 */
@Stateless
public class FcReportTemplateFacade {
    
    @PersistenceContext(unitName="Model")
    private EntityManager em;
    
    @EJB
    private FcConfigFacade configFacade;
    @EJB
    private ContentFacade contentFacade;
            
    public FcReportTemplate find(Long id) {
        return em.find(FcReportTemplate.class, id);
    }
    
    public FcReportTemplate findByYearmonth(String yearmonth, CompanyGroupEnum group) {
        Query q = em.createNamedQuery("FcReportTemplate.findByYearmonth");
        q.setParameter("yearmonth", yearmonth);
        q.setParameter("group", group);
        List<FcReportTemplate> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public FcReportTemplate findLast(CompanyGroupEnum group) {
        Query q = em.createNamedQuery("FcReportTemplate.findLast").setMaxResults(1);
        q.setParameter("group", group);
        List<FcReportTemplate> list = q.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void save(FcReportTemplate reportTemplate, List<AttachmentVO> attachments) throws Exception {
        if (reportTemplate.getId() == null) {
            em.persist(reportTemplate);
        } else {
            em.merge(reportTemplate);
        }
        contentFacade.saveContent(reportTemplate, attachments, reportTemplate.getModifier());
        configFacade.saveValue(FcConfigKeyEnum.YEARMONTH, reportTemplate.getYearmonth(), reportTemplate.getGroup());
        //20151020 企業團獨立開關帳
        configFacade.saveValueBoolean(FcConfigKeyEnum.STOPUPLOAD, false, reportTemplate.getGroup());
    }
    
}
